import { handleLogin,
    handleLogout,
    goMyPage,
    loadUserList } from "./user.js";
import { fetchArticles,
    loadArticleList,
    loadArticleDetail,
    loadArticleForm,
    createArticle,
    loadArticleUpdateForm,
    updateArticle,
    deleteArticle } from "./board.js";
//import {  } from "./schedule.js";
import { AuthModal } from "./authModal.js";

document.addEventListener("DOMContentLoaded", () => {
    const logoutBtn = document.getElementById("logoutBtn") as HTMLButtonElement | null;
    logoutBtn?.addEventListener("click", () => {
        handleLogout();
    });

    const returnHomeBtn = document.getElementById("return-main") as HTMLButtonElement | null;
    returnHomeBtn?.addEventListener("click", () => {
        window.location.href = "/";
    });

    const myPageBtn = document.getElementById("myPageBtn") as HTMLButtonElement | null;
    myPageBtn?.addEventListener("click", () => {
        goMyPage();
    });

    const userListBtn = document.getElementById("userListBtn") as HTMLButtonElement | null;
    userListBtn?.addEventListener("click", () => {
        loadUserList();
    });

    if (document.getElementById("boardList")) {
        fetchArticles().then(articles => {
            console.log(articles);
        });
    }

    new AuthModal();
});

document.addEventListener("click", (e: MouseEvent) => {
    const target = e.target as HTMLElement;

    if (target.closest("#writeBtn")) {
        e.preventDefault();
        loadArticleForm();
        return;
    }

    const articleLink = target.closest<HTMLAnchorElement>(".article-detail-link");
    if (articleLink) {
        e.preventDefault();
        const articleId = articleLink.dataset.id;
        if (articleId) loadArticleDetail(Number(articleId));
        return;
    }

    const updateBtn = target.closest<HTMLAnchorElement>(".article-update-btn");
    if (updateBtn) {
        e.preventDefault();
        const articleId = updateBtn.dataset.id;
        if (articleId) loadArticleUpdateForm(Number(articleId));
        return;
    }

    const deleteBtn = target.closest<HTMLAnchorElement>(".article-delete-btn");
    if (deleteBtn) {
        e.preventDefault();
        const articleId = deleteBtn.dataset.id;
        if (articleId) deleteArticle(Number(articleId));
        return;
    }
});

document.addEventListener("submit", (e: SubmitEvent) => {
    const form = e.target as HTMLFormElement;
    if (form.matches("#articleCreateForm")) {
        e.preventDefault();
        createArticle(form);
    } else if (form.matches("#articleUpdateForm")) {
        e.preventDefault();
        updateArticle(form);
    }
});