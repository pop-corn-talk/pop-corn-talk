function login(){
  document.getElementById('login-btn').addEventListener('click', function () {
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    const loginData = {
      email: username,
      password: password
    };

    // ajax vs fetch -> ajax 무거움 fetch가 빠름
    // jquery 임포트한다는게 엄청난손해

    fetch('/users/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(loginData),
    })
    .then(response => {
      const jwt = response.headers.get('Authorization');
      if (jwt) {
        sessionStorage.setItem("jwt", jwt.split(' ')[1]);
        window.location.href = "/";
      } else {
        throw Error("리다이렉팅에러");
      }
    })
    .catch((error) => {
      console.error('Error:', error);
      document.getElementById('error-msg').style.display = 'block';
      document.getElementById('error-msg').textContent = 'Login failed. Please try again.';
    });
  })}
