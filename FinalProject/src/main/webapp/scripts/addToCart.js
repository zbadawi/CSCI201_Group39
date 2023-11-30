// document.addEventListener('DOMContentLoaded', function() {
  const cart = localStorage.cart //taking cart string from storage
  let cartArray = [] //instatiating empty array

  const cartList = document.querySelector('#cart-list');

  if(cart) { //if cart already exists
      cartArray = JSON.parse(cart) //resetting array to be array from storag
      //loop through array and create DOM elements to display stored cartArray
      for (item of cartArray) {
          addItem(item)
      }
      bindRemoveBtns() //allows us to remove items
      updateTotal(); // Update the total price
  }
      
  document.querySelector('#cart-delete-all').onclick = function(){
    document.querySelector('#cart-list').innerHTML = '';
    //remove from localStorage
    localStorage.cart = ''
  }

  function addItem(item){
    // Create HTML elements.
    const itemContainer = document.createElement('div')
    const li = document.createElement('li');
    const img = document.createElement('img');
    const spanRemove = document.createElement('span');
    const spanItem = document.createElement('span');
    const spanPrice = document.createElement('span');
    const priceBold = document.createElement('b');

    // Add classes for new elements.
    itemContainer.classList.add('product-container')
    li.classList.add('list-group-item', 'cart-item');
    img.classList.add('cart-item-image');
    spanPrice.classList.add('price');
    spanRemove.classList.add('cart-remove', 'oi', 'oi-circle-x');

    // Set attributes and properties.
    spanRemove.title = 'Remove';
    spanItem.innerHTML = item.name;
    img.src = 'img/' + item.image_url;
    img.alt = item.name + ' Product Image';
    priceBold.innerHTML = item.price;

    // Add elements to the list item.
    li.appendChild(spanRemove);
    li.appendChild(img);
    li.appendChild(spanItem);
    li.appendChild(spanPrice);

    //Add list item to the container
    itemContainer.appendChild(li);

    // Add itemContainer to the cart list.
    document.querySelector('#cart-list').appendChild(itemContainer);
  }

  //adds remove buttons to everything in cart
  function bindRemoveBtns() {
    const removeBtns = document.querySelectorAll('.cart-remove');
    for ( let i = 0; i < removeBtns.length; i++ ) {
      removeBtns[i].onclick = function(){

        const item = this.nextElementSibling.innerHTML
        const index = cartArray.indexOf(item) //finding index of item in cartArray
        cartArray.splice(index, 1) //removing from array, slice(startIndex, numElementsRemove)

        localStorage.cart = JSON.stringify(cartArray)

        document.querySelector('#cart-list').removeChild( this.parentElement );
      } // END removeBtns[i].onclick
    } // END for loop
  } // END bindRemoveBtns()

  //updates total price
  function updateTotal() {
    const cart = localStorage.cart;
    let cartArray = [];

    if (cart) {
      cartArray = JSON.parse(cart);

      let total = 0;

      // Iterate through the items in the cart
      for (item of cartArray) {
        // Assuming each item has a price property
        const price = parseFloat(item.price);
        total += price;
      }

      // Update the total in the HTML
      const totalElement = document.querySelector('#total-amount');
      totalElement.innerHTML = '$' + total.toFixed(2); // Assuming two decimal places
    }
  }
// });