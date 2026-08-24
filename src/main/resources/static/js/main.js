import { handleLogout } from "./user.js";
import { fetchArticles } from "./board.js";
import { loadSchedule } from "./schedule.js";
import { AuthModal } from "./authModal.js";
document.addEventListener("DOMContentLoaded", () => {
    const logoutBtn = document.getElementById("logoutBtn");
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
    new AuthModal();
});
