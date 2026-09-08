import { handleLogout, goMyPage, loadUserList } from "./user.js";
import { fetchArticles, loadArticleDetail, loadArticleForm, createArticle, loadArticleUpdateForm, updateArticle, deleteArticle } from "./board.js";
import { loadSchedule } from "./schedule.js";
import { AuthModal } from "./authModal.js";
document.addEventListener("DOMContentLoaded", () => {
    const logoutBtn = document.getElementById("logoutBtn");
    logoutBtn?.addEventListener("click", () => {
        handleLogout();
    });
    const returnHomeBtn = document.getElementById("return-main");
    returnHomeBtn?.addEventListener("click", () => {
        window.location.href = "/";
    });
    const myPageBtn = document.getElementById("myPageBtn");
    myPageBtn?.addEventListener("click", () => {
        goMyPage();
    });
    const userListBtn = document.getElementById("userListBtn");
    userListBtn?.addEventListener("click", () => {
        loadUserList();
    });
    if (document.getElementById("boardList")) {
        fetchArticles().then(articles => {
            console.log(articles);
        });
    }
    if (document.getElementById("calendar")) {
        loadSchedule("2026-08");
    }
    new AuthModal();
});
document.addEventListener("click", (e) => {
    const target = e.target;
    if (target.closest("#writeBtn")) {
        e.preventDefault();
        loadArticleForm();
        return;
    }
    const articleLink = target.closest(".article-detail-link");
    if (articleLink) {
        e.preventDefault();
        const articleId = articleLink.dataset.id;
        if (articleId)
            loadArticleDetail(Number(articleId));
        return;
    }
    const updateBtn = target.closest(".article-update-btn");
    if (updateBtn) {
        e.preventDefault();
        const articleId = updateBtn.dataset.id;
        if (articleId)
            loadArticleUpdateForm(Number(articleId));
        return;
    }
    const deleteBtn = target.closest(".article-delete-btn");
    if (deleteBtn) {
        e.preventDefault();
        const articleId = deleteBtn.dataset.id;
        if (articleId)
            deleteArticle(Number(articleId));
        return;
    }
});
document.addEventListener("submit", (e) => {
    const form = e.target;
    if (form.matches("#articleCreateForm")) {
        e.preventDefault();
        createArticle(form);
    }
    else if (form.matches("#articleUpdateForm")) {
        e.preventDefault();
        updateArticle(form);
    }
});
