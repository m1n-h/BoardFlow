import { handleLogin } from "./user.js";
import { handleLogout } from "./user.js";
import { goMyPage } from "./user.js";
import { fetchArticles } from "./board.js";
import { createArticle } from "./board.js";
import { loadSchedule } from "./schedule.js";
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