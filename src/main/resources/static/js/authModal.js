/// <reference lib="dom" />
/// <reference lib="es2015" />
export class AuthModal {
    constructor() {
        this.modal = document.getElementById("authModal");
        this.loginForm = document.getElementById("loginForm");
        this.joinForm = document.getElementById("joinForm");
        this.initEvents();
    }
    initEvents() {
        const loginBtn = document.getElementById("loginBtn");
        const joinBtn = document.getElementById("joinBtn");
        const closeBtn = document.getElementById("closeModalBtn");
        loginBtn?.addEventListener("click", () => this.openModal("login"));
        joinBtn?.addEventListener("click", () => this.openModal("join"));
        closeBtn?.addEventListener("click", () => this.closeModal());
        this.modal?.addEventListener("click", (e) => {
            if (e.target === this.modal)
                this.closeModal();
        });
        this.loginForm?.addEventListener("submit", (e) => this.handleSubmit(e, "/login"));
        this.joinForm?.addEventListener("submit", (e) => this.handleSubmit(e, "/join"));
    }
    openModal(type) {
        if (!this.modal)
            return;
        this.modal.classList.remove("d-none");
        if (type === "login") {
            this.loginForm?.classList.remove("d-none");
            this.joinForm?.classList.add("d-none");
        }
        else {
            this.joinForm?.classList.remove("d-none");
            this.loginForm?.classList.add("d-none");
        }
    }
    closeModal() {
        this.modal?.classList.add("d-none");
    }
    async handleSubmit(e, url) {
        e.preventDefault();
        const form = e.target;
        const formData = new FormData(form);
        const bodyParams = new URLSearchParams();
        formData.forEach((value, key) => {
            bodyParams.append(key, value.toString());
        });
        try {
            const response = await fetch(url, {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                body: bodyParams,
            });
            if (response.ok) {
                this.closeModal();
                window.location.reload();
            }
            else {
                alert("처리 중 오류가 발생했습니다.");
            }
        }
        catch (error) {
            console.error("Fetch Error: " + error);
            alert("서버 통신 실패");
        }
    }
}
