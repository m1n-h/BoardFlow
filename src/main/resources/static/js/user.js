export async function handleLogin(id, pw) {
    const response = await fetch("/login", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: `userId=${encodeURIComponent(id)}&userPw=${encodeURIComponent(pw)}`
    });
    return response.ok;
}
export async function handleLogout() {
    const response = await fetch("/logout", {
        method: "POST"
    });
    if (response.ok) {
        alert("로그아웃 되었습니다.");
        window.location.href = "/index.html";
    }
}
