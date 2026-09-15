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
import { fetchSchedules,
    loadScheduleList,
    loadScheduleDetail,
    loadScheduleForm,
    createSchedule,
    loadScheduleUpdateForm,
    updateSchedule,
    deleteSchedule
} from "./schedule.js";
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

    new AuthModal();
});

document.addEventListener("click", (e: MouseEvent) => {
    const target = e.target as HTMLElement;

    // board
    const articleLink = target.closest<HTMLAnchorElement>(".article-detail-link");
    if (articleLink) {
        e.preventDefault();
        const articleId = articleLink.dataset.id;
        if (articleId) loadArticleDetail(Number(articleId));
        return;
    }

    const articleUpdateBtn = target.closest<HTMLAnchorElement>(".article-update-btn");
    if (articleUpdateBtn) {
        e.preventDefault();
        const articleId = articleUpdateBtn.dataset.id;
        if (articleId) loadArticleUpdateForm(Number(articleId));
        return;
    }

    const articleDeleteBtn = target.closest<HTMLAnchorElement>(".article-delete-btn");
    if (articleDeleteBtn) {
        e.preventDefault();
        const articleId = articleDeleteBtn.dataset.id;
        if (articleId) deleteArticle(Number(articleId));
        return;
    }

    // schedule
    const scheduleLink = target.closest<HTMLAnchorElement>(".schedule-detail-link");
    if (scheduleLink) {
        e.preventDefault();
        const scheduleId = scheduleLink.dataset.id;
        if (scheduleId) loadScheduleDetail(Number(scheduleId));
        return;
    }

    const scheduleUpdateBtn = target.closest<HTMLAnchorElement>(".schedule-update-btn");
    if (scheduleUpdateBtn) {
        e.preventDefault();
        const scheduleId = scheduleUpdateBtn.dataset.id;
        if (scheduleId) loadScheduleUpdateForm(Number(scheduleId));
        return;
    }

    const scheduleDeleteBtn = target.closest<HTMLElement>(".schedule-delete-btn");
    if (scheduleDeleteBtn) {
        e.preventDefault();
        const scheduleId = scheduleDeleteBtn.dataset.id;
        if (scheduleId) deleteSchedule(Number(scheduleId));
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
    } else if (form.matches("#scheduleCreateForm")) {
        e.preventDefault();
        createSchedule(form);
    } else if (form.matches("#scheduleUpdateForm")) {
        e.preventDefault();
        updateSchedule(form);
    }
});