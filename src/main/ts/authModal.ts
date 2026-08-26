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

        this.loginForm?.addEventListener("submit", (e: Event) => this.handleSubmit(e, "/login"));
        this.joinForm?.addEventListener("submit", (e: Event) => {
            e.preventDefault();
            this.handleSubmit(e, "/join");
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
    }

    private async handleSubmit(e: Event, url: string): Promise<void> {
        e.preventDefault();

        const form = (e.currentTarget || e.target) as HTMLFormElement;
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
                alert("성공적으로 처리 되었습니다.");
                this.closeModal();
                window.location.reload();
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
}