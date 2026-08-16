import { handleLogin } from "./user.js";
import { handleLogout } from "./user.js";
import { fetchArticles } from "./board.js";
import { createArticle } from "./board.js";
import { loadSchedule } from "./schedule.js";

document.addEventListener("DOMContentLoaded", () => {
    const logoutBtn = document.getElementById("logoutBtn") as HTMLButtonElement | null;
    logoutBtn?.addEventListener("click", () => {
        handleLogout();
    });

    if (document.getElementById("boardList")) {
        fetchArticles().then(articles => {
            console.log(articles);
        });
    }

    if (document.getElementById("calendar")) {
        loadSchedule("2026-08");
    }
});