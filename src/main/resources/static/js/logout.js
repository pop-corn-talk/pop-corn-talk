function logout() {
  const jwt = sessionStorage.getItem('jwt'); // localStorage에서 'jwt' 토큰 가져오기
  console.log("jwt : {}", jwt);
  if (!jwt) {
    window.location.href = "/users/signup"; // 회원가입 페이지로 리다이렉트
  }
  var logoutBtn = document.querySelector("#logout-btn");
  if (jwt && logoutBtn) {
    logoutBtn.addEventListener('click', function () {
      sessionStorage.clear();
      window.location.href = "/users/testLogin";
    })
  }
}

function moveSignup(){
  window.location.href="users/signup";
}
