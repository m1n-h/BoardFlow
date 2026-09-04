export interface UserInfo {
    id: string;
    name: string;
}

declare function openLoginModal(): void;

export async function handleLogin(id: string, pw: string): Promise<boolean> {
    const response = await fetch("/login", {
        method: "POST",
        headers: {"Content-Type": "application/x-www-form-urlencoded"},
        body: `user-id=${encodeURIComponent(id)}&user-pw=${encodeURIComponent(pw)}`
    });

    return response.ok;
}

export async function handleLogout(): Promise<void> {
    const response = await fetch("/logout", {
        method: "POST"
    });

    if (response.ok) {
        alert("로그아웃 되었습니다.");
        window.location.href = "/";
    }
}

export function goMyPage(): void {
    if (document.getElementById("loginBtn")) {
        alert("로그인이 필요한 서비스 입니다.");
        openLoginModal();
        return;
    }

    window.location.href = "/user/mypage.html";
}

export async function loadUserList(): Promise<void> {
    try {
        const response: Response = await fetch("/user/list");

        if (response.status === 401) {
            alert("로그인이 필요한 서비스 입니다.");
            openLoginModal();
            return;
        }

        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);

        const html: string = await response.text();
        const contentContainer = document.querySelector<HTMLElement>("#main .container");

        if (contentContainer) {
            contentContainer.innerHTML = html;
        } else {
            console.error("Not Found main in DOM");
        }
    } catch (error) {
        console.error("Failed to load user list: ", error);
    }
}