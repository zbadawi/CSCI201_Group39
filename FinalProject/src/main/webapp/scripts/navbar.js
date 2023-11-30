/**
 * 
 */

// no user logged in, show "log out" button

function userLoggedIn() {
	return sessionStorage.user != undefined && sessionStorage.user != '';
}

if (userLoggedIn()) {
	
	document.querySelector('#login-register').innerHTML = `
			<a href="farm_login.html" id="logout">Logout</a>`;
			
	document.getElementById('logout').onclick = function () {
		sessionStorage.user = '';
		window.location.href = 'farm_login.html';
	}
}	 