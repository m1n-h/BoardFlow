/// <reference lib="dom" />
/// <reference lib="es2015" />

export class AuthModal {
    private modal: HTMLElement | null;
    private loginForm: HTMLFormElement | null;
    private joinForm: HTMLFormElement | null;

    constructor() {
        this.modal = document.getElementById("authModal");
        this.loginForm = document.getElementById("loginForm") as HTMLFormElement | null;
        this.joinForm = document.getElementById("joinForm") as HTMLFormElement | null;

        this.initEvents();
    }

    private initEvents(): void {
        const loginBtn = document.getElementById("loginBtn");
        const joinBtn = document.getElementById("joinBtn");
        const closeBtn = document.getElementById("closeModalBtn");

        loginBtn?.addEventListener("click", () => this.openModal("login"));
        joinBtn?.addEventListener("click", () => this.openModal("join"));
        closeBtn?.addEventListener("click", () => this.closeModal());

        this.modal?.addEventListener("click", (e: MouseEvent) => {
            if (e.target === this.modal) this.closeModal();
        });

        this.loginForm?.addEventListener("submit", (e: Event) => {
            e.preventDefault();
            const targetForm = e.currentTarget as HTMLFormElement;
            this.handleSubmit(targetForm, "/login")
        });

        this.joinForm?.addEventListener("submit", (e: Event) => {
            e.preventDefault();
            const targetForm = e.currentTarget as HTMLFormElement;
            this.handleSubmit(targetForm, "/join");
        });
    }

    public openModal(type: "login" | "join"): void {
        if (!this.modal) return;

        this.modal.classList.remove("d-none");

        if (type === "login") {
            this.loginForm?.classList.remove("d-none");
            this.joinForm?.classList.add("d-none");
        } else {
            this.joinForm?.classList.remove("d-none");
            this.loginForm?.classList.add("d-none");
        }
    }

    public closeModal(): void {
        this.modal?.classList.add("d-none");
        this.joinForm?.reset();
        this.loginForm?.reset();
    }

    private async handleSubmit(form: HTMLFormElement | null, url: string): Promise<void> {
        if (!form) return;

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
                const text = await response.text();
                if (url.includes("join")) {
                    alert("회원가입이 정상적으로 처리 되었습니다!");
                    this.resetForm(form);
                    this.switchToLoginTab();
                } else if (url.includes("login")) {
                    if (text.includes("SUCCESS")) {
                        alert("로그인 성공!");
                        this.closeModal();
                        window.location.reload();
                    } else {
                        alert("아이디 또는 비밀번호가 일치하지 않습니다.");
                    }
                }

            } else {
                const errorText = await response.text();
                console.error("Server Error Response:", response.status, errorText);
                alert("처리 중 오류가 발생했습니다.");
            }
        } catch (error) {
            console.error("Fetch Error: " + error);
            alert("서버 통신 실패");
        }
    }

    private switchToLoginTab(): void {
        this.joinForm?.classList.add("d-none");
        this.loginForm?.classList.remove("d-none");
    }

    private resetForm(form: HTMLFormElement): void {
        form.reset();
    }
}