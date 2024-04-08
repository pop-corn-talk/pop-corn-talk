function signup(){
  document.getElementById('signup-form').addEventListener('submit', function (event) {
    event.preventDefault(); // Prevent the form from submitting normally
    // fetch로 현재 Url로 페이지를 먼저 가져오고 post를 진행했으면 좋겠어
    const username = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    const signupData = {
      email: username,
      password: password
    };
    // Perform AJAX request to submit form data
    fetch('/users/signup', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(signupData),
    })
    .then(function (response) {
      if (response.ok) {
        // If signup successful, redirect to testLogin page
        window.location.href = '/users/testLogin';
      } else {
        // If signup failed, redirect back to signup page
        window.location.href = '/users/signup';
      }
    })
    .catch(function (error) {
      console.error('Error:', error);
    });
  })}
