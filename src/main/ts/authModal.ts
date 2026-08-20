export class AuthModal {
    private modal: HTMLElement | null;
    private loginForm: HTMLElement | null;
    private joinForm: HTMLElement | null;

    constructor() {
        this.modal = document.getElementById("authModal");
        this.loginForm = document.getElementById("loginForm");
        this.joinForm = document.getElementById("joinForm");

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
    }

    public openModal(type: "login" | "join"): void {
        if (!this.modal) return;

        this.modal.classList.remove("d-none");

        if (type === "login") {
            this.loginForm?.classList.remove("d-none");
            this.joinForm?.classList.add("d-none");
        } else {
            this.joinForm?.classList.remove("d-none");
            this.loginForm?.classList.remove("d-none");
        }
    }

    public closeModal(): void {
        this.modal?.classList.add("d-none");
    }
}