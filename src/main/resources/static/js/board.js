export async function fetchArticles() {
    const response = await fetch("/api/articles");
    if (!response.ok)
        return [];
    return await response.json();
}
export async function loadArticleList() {
    try {
        const response = await fetch("/board/list");
        if (!response.ok)
            throw new Error(`HTTP error! status: ${response.status}`);
        const html = await response.text();
        renderMainContent(html);
    }
    catch (error) {
        console.error("Failed to load article list: ", error);
    }
}
export async function createArticle(form) {
    const formData = new FormData(form);
    const bodyParams = new URLSearchParams(formData);
    try {
        const response = await fetch("/board/create", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: bodyParams.toString()
        });
        if (response.status === 401) {
            alert("로그인이 필요합니다.");
            if (typeof openLoginModal() === "function")
                openLoginModal();
            return;
        }
        if (response.status === 400) {
            alert("제목과 내용을 모두 입력해주세요.");
            return;
        }
        if (response.ok) {
            alert("게시글이 등록되었습니다.");
            loadArticleList();
        }
    }
    catch (error) {
        console.error("Failed to create article: ", error);
    }
}
export async function loadArticleDetail(articleId) {
    try {
        const response = await fetch(`/board/detail?id=${articleId}`);
        if (response.status === 404) {
            alert("존재하지 않은 게시글 입니다.");
            loadArticleList();
            return;
        }
        if (!response.ok)
            throw new Error(`HTTP error! status: ${response.status}`);
        const html = await response.text();
        renderMainContent(html);
    }
    catch (error) {
        console.error("Failed to load article detail: ", error);
    }
}
export async function loadArticleForm() {
    try {
        const response = await fetch("/board/form");
        if (response.status === 401) {
            alert("게시물을 작성하려면 로그인이 필요합니다.");
            if (typeof openLoginModal === "function") {
                openLoginModal();
            }
            return;
        }
        if (!response.ok)
            throw new Error(`HTTP error! status: ${response.status}`);
        const html = await response.text();
        renderMainContent(html);
    }
    catch (error) {
        console.error("Failed to load article form: ", error);
    }
}
export async function loadArticleUpdateForm(articleId) {
    try {
        const response = await fetch(`/board/update?id=${articleId}`);
        if (response.status === 401) {
            alert("로그인이 필요합니다.");
            if (typeof openLoginModal === "function")
                openLoginModal();
            return;
        }
        if (response.status === 403) {
            alert("수정 권한이 없습니다.");
            return;
        }
        if (!response.ok)
            throw new Error(`HTTP error! status: ${response.status}`);
        const html = await response.text();
        renderMainContent(html);
    }
    catch (error) {
        console.error("Failed to load article update: ", error);
    }
}
export async function updateArticle(form) {
    const formData = new FormData(form);
    const bodyParams = new URLSearchParams(formData);
    try {
        const response = await fetch("/board/update", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: bodyParams.toString()
        });
        if (response.status === 401) {
            alert("로그인이 필요합니다.");
            if (typeof openLoginModal === "function")
                openLoginModal();
            return;
        }
        if (response.status === 403) {
            alert("수정 권한이 없습니다.");
            return;
        }
        if (response.ok) {
            alert("게시글이 수정 되었습니다.");
            const articleId = formData.get("id");
            if (articleId) {
                loadArticleDetail(Number(articleId));
            }
            else {
                loadArticleList();
            }
        }
    }
    catch (error) {
        console.error("Failed to update article: ", error);
    }
}
export async function deleteArticle(articleId) {
    if (!confirm("정말 이 게시글을 삭제하시겠습니까?"))
        return;
    try {
        const response = await fetch("/board/delete", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: `id=${articleId}`
        });
        if (response.status === 401) {
            alert("로그인이 필요합니다.");
            if (typeof openLoginModal === "function")
                openLoginModal();
            return;
        }
        if (response.status === 403) {
            alert("삭제 권한이 없습니다.");
            return;
        }
        if (response.ok) {
            alert("삭제되었습니다.");
            loadArticleList();
        }
    }
    catch (error) {
        console.error("Failed to delete article: ", error);
    }
}
function renderMainContent(html) {
    const contentContainer = document.querySelector("#main .container");
    if (contentContainer) {
        contentContainer.innerHTML = html;
    }
    else {
        console.error("Main container element not found in DOM");
    }
}
