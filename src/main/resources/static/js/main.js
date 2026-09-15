import { handleLogout, goMyPage, loadUserList } from "./user.js";
import { loadArticleDetail, createArticle, loadArticleUpdateForm, updateArticle, deleteArticle } from "./board.js";
import { loadScheduleDetail, createSchedule, loadScheduleUpdateForm, updateSchedule, deleteSchedule } from "./schedule.js";
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
    new AuthModal();
});
document.addEventListener("click", (e) => {
    const target = e.target;
    // board
    const articleLink = target.closest(".article-detail-link");
    if (articleLink) {
        e.preventDefault();
        const articleId = articleLink.dataset.id;
        if (articleId)
            loadArticleDetail(Number(articleId));
        return;
    }
    const articleUpdateBtn = target.closest(".article-update-btn");
    if (articleUpdateBtn) {
        e.preventDefault();
        const articleId = articleUpdateBtn.dataset.id;
        if (articleId)
            loadArticleUpdateForm(Number(articleId));
        return;
    }
    const articleDeleteBtn = target.closest(".article-delete-btn");
    if (articleDeleteBtn) {
        e.preventDefault();
        const articleId = articleDeleteBtn.dataset.id;
        if (articleId)
            deleteArticle(Number(articleId));
        return;
    }
    // schedule
    const scheduleLink = target.closest(".schedule-detail-link");
    if (scheduleLink) {
        e.preventDefault();
        const scheduleId = scheduleLink.dataset.id;
        if (scheduleId)
            loadScheduleDetail(Number(scheduleId));
        return;
    }
    const scheduleUpdateBtn = target.closest(".schedule-update-btn");
    if (scheduleUpdateBtn) {
        e.preventDefault();
        const scheduleId = scheduleUpdateBtn.dataset.id;
        if (scheduleId)
            loadScheduleUpdateForm(Number(scheduleId));
        return;
    }
    const scheduleDeleteBtn = target.closest(".schedule-delete-btn");
    if (scheduleDeleteBtn) {
        e.preventDefault();
        const scheduleId = scheduleDeleteBtn.dataset.id;
        if (scheduleId)
            deleteSchedule(Number(scheduleId));
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
    else if (form.matches("#scheduleCreateForm")) {
        e.preventDefault();
        createSchedule(form);
    }
    else if (form.matches("#scheduleUpdateForm")) {
        e.preventDefault();
        updateSchedule(form);
    }
});
