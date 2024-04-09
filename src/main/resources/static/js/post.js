function uploadImage(event) {
  // 이벤트에서 입력 요소 가져오기
  const input = event.target;

  // 파일이 선택되었는지 확인
  if (input.files && input.files[0]) {
    // FormData 객체 생성
    const formData = new FormData();

    // FormData에 이미지 파일 추가
    formData.append('Image', input.files[0]);

    // 이미지를 서버로 업로드하기 위해 fetch 요청 보내기
    fetch('/image', {
      method: 'POST',
      body: formData
    })
    .then(response => {
      // 응답이 성공적으로 받아지는지 확인
      if (!response.ok) {
        throw new Error('HTTP error, status = ' + response.status);
      }
      // JSON 형식으로 응답 데이터 가져오기
      return response.json();
    })
    .then(data => {
      // 응답 데이터에서 이미지 URL 가져오기
      const imageUrl = data.data.imageUrl;

      // 이미지 미리보기 엘리먼트 가져오기
      const imgPreview = document.getElementById('post_imgPreview');

      // 이미지 URL 설정 및 데이터 전달 후에 이미지 미리보기 기능 block
      imgPreview.src = imageUrl;
      imgPreview.style.display = 'block';

      console.log('이미지 업로드 성공:', imageUrl);
    })
    .catch(error => {
      // 에러 발생 시 처리
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

// 이전에 표시된 게시물을 지우고 새로운 게시물을 표시하는 함수
function displayNewPost(postData) {
  const postsContainer = document.getElementById('posts');
  postData.forEach((post) => {
    const article = document.createElement('article');
    article.classList.add('post-card');
    article.dataset.postId = post.id;
    article.innerHTML = `
      <h2>${post.name}</h2>
      <p>${post.content}</p>
      <img src="${post.image}" alt="Post Image">
    `;
    postsContainer.appendChild(article);
  });
}
//todo : slicing 방식 size가 10이 아니면 url에서 에러가 나는 현상 수정해야함.
let loadingPosts = false;
let page = 0;
async function loadMorePosts() {
  try {
    if (loadingPosts) return;

    loadingPosts = true;

    page++; // Increase the page number when loading more posts

    const size = 10;
    const url = `/posts?type=0&keyword=&page=${page}&size=${size}`;

    const token = sessionStorage.getItem('jwt');

    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    })

    console.log('Response status:', response.status);

    if (!response.ok) {
      throw new Error('Failed to view post.');
    }

    const responseData = await response.json();

    if (responseData && responseData.data && responseData.data.content) {
      const newPosts = responseData.data.content;
      if (newPosts.length > 0) {
        displayNewPost(newPosts);
      } else {
        console.log('No more posts available.');
        // Notify the user that there are no more posts available
      }
    } else {
      console.log('No more posts available.');
      // Notify the user that there are no more posts available
    }
  } catch (error) {
    console.error('An error occurred:', error);
  } finally {
    loadingPosts = false;
  }
}







// 클릭한 게시물의 id를 사용하여 데이터 조회
async function getPost(postId) {
  try {
    const url = `/posts/${postId}`;
    const token = sessionStorage.getItem('jwt');

    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });

    if (!response.ok) {
      throw new Error('Failed to fetch post by id.');
    }

    const responseData = await response.json();
    console.log('Fetched post by id:', responseData);
    // Additional data processing logic
  } catch (error) {
    console.error('An error occurred while fetching post by id:', error);
  }

}
// Click event handler
//todo : 아무곳이나 클릭해도 이벤트가 발생하는 문제 해결해야함
document.addEventListener('click', async function(event) {
  const clickedPostCard = event.target.closest('.post-card');
  if (!clickedPostCard) {
    return;
  }

  // 모달 요소 가져오기
  const modal = document.querySelector('.modal');
  const modalContent = document.querySelector('.modal-content');
  const closeBtn = document.querySelector('.close');

  // 모달에 게시글 내용 채우기
  const title = clickedPostCard.querySelector('h2').textContent;
  const content = clickedPostCard.querySelector('p').textContent;
  const imageSrc = clickedPostCard.querySelector('img').src;

  modalContent.querySelector('.post-title').textContent = title;
  modalContent.querySelector('.post-content').textContent = content;
  modalContent.querySelector('.post-image').src = imageSrc;

  // 모달 열기
  modal.style.display = 'block';

  // 닫기 버튼 클릭 이벤트 처리
  closeBtn.onclick = function() {
    modal.style.display = 'none';
  }

  // 모달 외부를 클릭하여 모달 닫기
  window.onclick = function(event) {
    if (event.target == modal) {
      modal.style.display = 'none';
    }
  }
});





// Event listener for the scroll event
window.addEventListener('scroll', async () => {
  const scrollY = window.scrollY;
  const windowHeight = window.innerHeight;
  const documentHeight = document.documentElement.scrollHeight;

  // Check if the user has scrolled to the bottom of the page
  if (scrollY + windowHeight >= documentHeight) {
    await loadMorePosts();
  }
});

// Call the function to load initial posts when the page loads
window.addEventListener('load', async function () {
  await loadMorePosts();
});






















