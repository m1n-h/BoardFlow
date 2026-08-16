export interface UserInfo {
    id: string;
    name: string;
}

export async function handleLogin(id: string, pw: string): Promise<boolean> {
    const response = await fetch("/login", {
        method: "POST",
        headers: {"Content-Type": "application/x-www-form-urlencoded"},
        body: `userId=${encodeURIComponent(id)}&userPw=${encodeURIComponent(pw)}`
    });

    return response.ok;
}

export async function handleLogout(): Promise<void> {
    const response = await fetch("/logout", {
        method: "POST"
    });

    if (response.ok) {
        alert("로그아웃 되었습니다.");
        window.location.href = "/index.html";
    }
}