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
        this.loginForm?.addEventListener("submit", (e) => {
            e.preventDefault();
            const targetForm = e.currentTarget;
            this.handleSubmit(targetForm, "/login");
        });
        this.joinForm?.addEventListener("submit", (e) => {
            e.preventDefault();
            const targetForm = e.currentTarget;
            this.handleSubmit(targetForm, "/join");
        });
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
        this.joinForm?.reset();
        this.loginForm?.reset();
    }
    async handleSubmit(form, url) {
        if (!form)
            return;
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
                body: bodyParams.toString(),
            });
            if (response.ok) {
                if (url.includes("join")) {
                    alert("회원가입이 정상적으로 처리 되었습니다!");
                    this.resetForm(form);
                    this.switchToLoginTab();
                }
                else if (url.includes("login")) {
                    alert("로그인 성공!");
                    this.closeModal();
                    window.location.reload();
                }
            }
            else {
                const errorText = await response.text();
                console.error("Server Error Response:", response.status, errorText);
                alert("처리 중 오류가 발생했습니다.");
            }
        }
        catch (error) {
            console.error("Fetch Error: " + error);
            alert("서버 통신 실패");
        }
    }
    switchToLoginTab() {
        this.joinForm?.classList.add("d-none");
        this.loginForm?.classList.remove("d-none");
    }
    resetForm(form) {
        form.reset();
    }
}
