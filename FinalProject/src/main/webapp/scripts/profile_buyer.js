/**
 * Script for rendering buyer profile page
 */

let user = JSON.parse(sessionStorage.user);

function getPurchasedProducts() {

	// request list of user's purchased products from the server
	$.ajax({
		url: 'buyer',
		method: 'GET',
		data: {
			user_id: user.user_id
		},
		success: function(response) {
			console.log("received user's purchased items: ", JSON.parse(response));
			displayPurchasedProducts(JSON.parse(response));
		},
		error: function(xhr, error, status) {
			{
				console.log('error: ', xhr.responseText);
			}
		}
	});
}

function displayPurchasedProducts(products) {
	products.forEach(product => {
		createCard(product);
	})
}

function createCard(product) {
	const card = document.createElement('div')
	const imgContainer = document.createElement('div')
	const img = document.createElement('img')
	const txtContainer = document.createElement('div')
	const title = document.createElement('h4')
	const titleBold = document.createElement('b')
	const price = document.createElement('p')
	const quantity = document.createElement('p')

	card.classList.add('card')
	imgContainer.classList.add('img-container')
	txtContainer.classList.add('container')

	img.src = product.image_url;
	img.alt = product.name + 'Product Image'
	img.classList.add('overlay-image')

	titleBold.innerHTML = product.name
	price.innerHTML = 'Price: <b>$' + product.price + '</b>';
	quantity.innerHTML = 'Total Quantity Bought: <b>' + product.quantity + '</b>';

	title.appendChild(titleBold)

	imgContainer.appendChild(img)

	txtContainer.appendChild(title)
	txtContainer.appendChild(price)
	txtContainer.appendChild(quantity)

	card.appendChild(imgContainer)
	card.appendChild(txtContainer)

	document.querySelector(".card-container").appendChild(card)
} // createCard

function displayUserInfo() {
	// display username heading
	document.querySelector('.header h1').innerHTML = `${user.username}'s Profile`;

	// display user's balance
	document.querySelector('#balance').innerHTML = user.current_balance;

	// display user's num products purchased
	document.querySelector('#products-purchased').innerHTML = user.products_purchased;
}

function bindButtons() {
	document.querySelector('#add-funds').onclick = displayAddFundsForm;
	document.querySelector('#add-funds-submit').onclick = addFundsSubmit;
	document.querySelector('#add-funds-cancel').onclick = hideAddFundsForm;
}

function displayAddFundsForm() {
	document.querySelector('.popup-background').style.display = 'block';
	document.querySelector('.popup').style.display = 'block';
}

function hideAddFundsForm() {
	document.querySelector('.popup-background').style.display = 'none';
	document.querySelector('.popup').style.display = 'none';
}

function addFundsSubmit() {
	if (validateAddFundsForm()) {
		$.ajax({
			url: 'buyer',
			method: 'POST',
			data: {
				user_id: user.user_id,
				amount_added: document.querySelector('#amount').value
			},
			success: function(response) {
				hideAddFundsForm();
				resetUserInfo();
			},
			error: function(xhr, error, status) {
				console.log('error when adding funds: ', xhr.responseText);
			}
		})
	}
}

function resetUserInfo() {
	const data = {
		username: user.username,
		password: user.password
	};

	// Make the AJAX request to get new user data
	$.ajax({
		url: "login",
		method: "POST",
		data: data,
		success: function(response) {
			// If login is successful, store user in sessionStorage and redirect to homepage
			sessionStorage.setItem('user', response);
			user = JSON.parse(response);
			displayUserInfo();
		},
		error: function(xhr, status, error) {
			// Handle unsuccessful login (e.g., show an error message)
			$("#username-error").html(xhr.responseText);
			console.error("Login failed.");
			event.preventDefault();
		}
	});
}

function validateAddFundsForm() {
	const amount = document.querySelector('#amount').value;
	const ccNumber = document.querySelector('#cc-number').value;
	const expDate = document.querySelector('#exp-date').value;
	const cvv = document.querySelector('#cvv').value;

	const errorElement = document.querySelector('#add-funds-err');

	if (amount == '' || amount == null || amount == undefined) {
		errorElement.innerHTML = 'Please enter a valid amount';
		return false;
	}

	if (ccNumber.length != 16) {
		errorElement.innerHTML = 'Please enter a valid credit card number.';
		return false;
	}

	if (expDate == '') {
		errorElement.innerHTML = 'Please enter a valid expiration date.';
		return false;
	}


	const inputDate = new Date(expDate + "-01"); // Assuming day is always the 1st of the month
	const currentDate = new Date();

	if (inputDate < currentDate) {
		errorElement.innerHTML = 'Please enter a valid expiration date.';
		return false;
	}

	if (cvv.length != 3) {
		errorElement.innerHTML = 'Please enter a valid CVV.';
		return false;
	}

	return true;
}

resetUserInfo();
getPurchasedProducts();
hideAddFundsForm();
bindButtons();










