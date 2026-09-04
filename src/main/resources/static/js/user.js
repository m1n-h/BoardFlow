export async function handleLogin(id, pw) {
    const response = await fetch("/login", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: `user-id=${encodeURIComponent(id)}&user-pw=${encodeURIComponent(pw)}`
    });
    return response.ok;
}
export async function handleLogout() {
    const response = await fetch("/logout", {
        method: "POST"
    });
    if (response.ok) {
        alert("로그아웃 되었습니다.");
        window.location.href = "/";
    }
}
export function goMyPage() {
    if (document.getElementById("loginBtn")) {
        alert("로그인이 필요한 서비스 입니다.");
        openLoginModal();
        return;
    }
    window.location.href = "/user/mypage.html";
}
export async function loadUserList() {
    try {
        const response = await fetch("/user/list");
        if (response.status === 401) {
            alert("로그인이 필요한 서비스 입니다.");
            openLoginModal();
            return;
        }
        if (!response.ok)
            throw new Error(`HTTP error! status: ${response.status}`);
        const html = await response.text();
        const contentContainer = document.querySelector("#main .container");
        if (contentContainer) {
            contentContainer.innerHTML = html;
        }
        else {
            console.error("Not Found main in DOM");
        }
    }
    catch (error) {
        console.error("Failed to load user list: ", error);
    }
}
