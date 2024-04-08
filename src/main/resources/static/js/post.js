
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
    const postImgInput = document.getElementById('post_imgInput');
    const imgPreview = document.getElementById('post_imgPreview');

    const postImgFile = imgPreview.src;
    const postName = postNameInput.value.trim();
    const postContent = postContentInput.value.trim();

    // 이미지 파일이 선택되었는지 확인
    if (!postImgFile) {
      console.error("이미지를 선택하세요.");
      return;
    }

    // 게시글 제목과 내용이 입력되었는지 확인
    if (!postName || !postContent) {
      console.error("게시글 제목 또는 내용을 입력하세요.");
      return;
    }

    try {
      const postData = {
        postName: postName,
        postContent: postContent,
        postImage: postImgFile
      };

      console.log('보낼 데이터:', postData); // 데이터 확인용 로그

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
        throw new Error('게시글 등록에 실패했습니다.');
      }
      console.log('게시글 등록에 성공했습니다.');

      // 게시글 카드를 동적으로 생성하여 postsContainer에 추가
      const postsContainer = document.getElementById('posts');
      const article = document.createElement('article');
      article.classList.add('post-card');
      article.innerHTML = `
      <h2>${postName}</h2>
      <p>${postContent}</p>
      <img src="${postImgFile}" alt="Post Image">
    `;
      postsContainer.prepend(article);

      // 입력 필드 초기화
      postNameInput.value = '';
      postContentInput.value = '';
      imgPreview.src = '#';
      imgPreview.style.display = 'none';
      postImgInput.value = ''; // Clear file input field
    } catch (error) {
      console.error('오류 발생:', error);
    }
  }



  function displayNewPost(postData) {
    const postsContainer = document.getElementById('posts');

    postData.data.content.forEach((post) => {
      const article = document.createElement('article');
      article.classList.add('post-card');
      article.innerHTML = `
      <h2>${post.name}</h2>
      <p>${post.content}</p>
      <img src="${post.image}" alt="Post Image">
    `;
      postsContainer.prepend(article);
    });
  }





  // 코드 수정 예시
  async function getPosts(type, keyword) {
    try {
      const page = 0;
      const size = 10;
      const url = `/posts?type=${type}&keyword=${keyword}&page=${page}&size=${size}`;
      console.log('Fetching data from:', url); // url 확인

      const token = sessionStorage.getItem('jwt');
      console.log('JWT token:', token); // 토큰 확인

      const response = await fetch(url, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        }
      });

      console.log('Response status:', response.status); // 응답 상태 코드 확인

      if (!response.ok) {
        throw new Error('Failed to view post.');
      }

      const res = await response.text();
      const responseData = JSON.parse(res);
      console.log('Viewed post data:', responseData);

      displayNewPost(responseData);
    } catch (error) {
      console.error('An error occurred:', error);
    }

// JavaScript

// // Function to fetch post by postId
//     async function fetchPostById(postId) {
//       try {
//         const url = `/posts/${postId}`;
//         const token = sessionStorage.getItem('jwt');
//
//         const response = await fetch(url, {
//           method: 'GET',
//           headers: {
//             'Content-Type': 'application/json',
//             'Authorization': `Bearer ${token}`
//           }
//         });
//
//         if (!response.ok) {
//           throw new Error('Failed to fetch post by id.');
//         }
//
//         const responseData = await response.json();
//         console.log('Fetched post by id:', responseData);
//         // Add logic to handle the fetched post data
//       } catch (error) {
//         console.error('An error occurred while fetching post by id:', error);
//       }
//     }
//
// // Attach click event listener to post-card elements
//     document.addEventListener('click', function(event) {
//       const postCardElement = event.target.closest('.post-card');
//       if (postCardElement) {
//         const postId = postCardElement.getAttribute('data-post-id');
//         fetchPostById(postId);
//       }
//     });




}






