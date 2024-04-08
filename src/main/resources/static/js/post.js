
  function uploadImage(event) {
  const input = event.target;
  if (input.files && input.files[0]) {
  const formData = new FormData();
  formData.append('Image', input.files[0]);

  fetch('/image', {
  method: 'POST',
  body: formData
})
  .then(response => {
  if (!response.ok) {
  throw new Error('HTTP error, status = ' + response.status);
}
  return response.json();
})
  .then(data => {
  // 응답 데이터에서 이미지 URL 가져오기
  const imageUrl = data.data.imageUrl;

  // 이미지 미리보기 엘리먼트 가져오기
  const imgPreview = document.getElementById('post_imgPreview');

  // 이미지 URL 설정 및 보이도록 설정
  imgPreview.src = imageUrl;
  imgPreview.style.display = 'block';

  console.log('이미지 업로드 성공:', imageUrl);
})
  .catch(error => {
  console.error('이미지 업로드 실패:', error);
});
}
}

  async function deleteImage(imageUrl) {
  // 현재 posts에 저장된 게시글 목록중에서 클릭된 이미ㅈ
  try {
  const response = await fetch('/image', {
  method: 'DELETE',
  headers: {
  'Content-Type': 'application/json'
},
  body: JSON.stringify({imageUrl: imageUrl})
});

  if (!response.ok) {
  throw new Error('이미지 삭제에 실패했습니다.');
}

  console.log('이미지 삭제에 성공했습니다.');
} catch (error) {
  console.error('오류 발생:', error);
}
}

  async function createPostData() {
  const postNameInput = document.querySelector("#post_nameInput");
  const postContentInput = document.querySelector("#post_contentInput");
  const imgPreview = document.getElementById('post_imgPreview');

  const postName = postNameInput.value.trim();
  const postContent = postContentInput.value.trim();
  const postImageSrc = imgPreview.src;

  // Check if all necessary fields are filled
  if (!postName || !postContent || !postImageSrc || postImageSrc === '#') {
  console.error("Please fill in all fields: post title, content, and image.");
  return;
}

  try {
  // Gather elements into an array
  const postDataArray = [
  postNameInput,
  postContentInput,
  imgPreview
  ];

  // Clear input fields
  postDataArray.forEach(element => {
  if (element.tagName.toLowerCase() === 'input' || element.tagName.toLowerCase() === 'textarea') {
  element.value = '';
} else if (element.tagName.toLowerCase() === 'img') {
  element.src = '#';
  element.style.display = 'none';
}
});

  // Send post data to the server
  const postData = {
  postName: postName,
  postContent: postContent,
  postImage: postImageSrc
};
  // Clear image input field
  const postImgInput = document.getElementById('post_imgInput');
  postImgInput.value = '';  // Clear the input value

  console.log('Data to be sent:', postData); // Log for data verification

  const token = sessionStorage.getItem('jwt');

  const response = await fetch('/posts', {
  method: 'POST',
  headers: {
  'Content-Type': 'application/json',
  'Authorization': `Bearer ${token}`
},
  body: JSON.stringify(postData),
});

  if (!response.ok) {
  throw new Error('Failed to post the article.');
}

  console.log('Successfully posted the article.');
} catch (error) {
  console.error('An error occurred:', error);
}
}




  // 코드 수정 예시
  async function getPosts(type, keyword) {
  try {
  const page = 0;
  const size = 10;
  const url = `/posts?type=${type}&keyword=${keyword}&page=${page}&size=${size}`;
  const token = sessionStorage.getItem('jwt');

  const response = await fetch(url, {
  method: 'GET',
  headers: {
  'Content-Type': 'application/json',
  'Authorization': `Bearer ${token}`
}
});

  if (!response.ok) {
  throw new Error('Failed to view post.');
}

  const responseData = await response.json(); // 이부분에서 문제발생
  console.log('Viewed post data:', responseData);
  displayPosts(responseData);
} catch (error) {
  console.error('An error occurred:', error);
}
}


  function displayPosts(postsData) {
  const postsContainer = document.getElementById('posts');
  postsContainer.innerHTML = ''; // Clear previous posts

  if (!Array.isArray(postsData)) {
  console.error('postsData is not an array:', postsData);
  return;
}

  postsData.forEach(post => {
  const article = document.createElement('article');
  article.classList.add('post-card');

  const postTitle = document.createElement('h2');
  postTitle.textContent = post.title;

  const postContent = document.createElement('p');
  postContent.textContent = post.content;

  const postImage = document.createElement('img');
  postImage.src = post.image;
  postImage.alt = 'Post Image';

  article.appendChild(postTitle);
  article.appendChild(postContent);
  article.appendChild(postImage);

  postsContainer.appendChild(article);

  if (postsContainer.hasChildNodes()) {
  console.log('Successfully fetched and displayed posts:', article);
}
});
}






