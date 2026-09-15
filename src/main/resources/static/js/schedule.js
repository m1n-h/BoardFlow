export async function fetchSchedules() {
    const response = await fetch("/api/schedules");
    if (!response.ok)
        return [];
    return await response.json();
}
export async function loadScheduleList() {
    try {
        const response = await fetch("/schedule/list");
        if (!response.ok)
            throw new Error(`HTTP error! status: ${response.status}`);
        const html = await response.text();
        renderMainContent(html);
    }
    catch (error) {
        console.error("Failed to load schedule list: ", error);
    }
}
export async function createSchedule(form) {
    const formData = new FormData(form);
    const bodyParams = new URLSearchParams(formData);
    try {
        const response = await fetch("/schedule/write", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: bodyParams.toString()
        });
        if (response.status === 401) {
            alert("로그인이 필요합니다.");
            if (typeof openLoginModal === "function")
                openLoginModal();
            return;
        }
        if (response.status === 400) {
            alert("필수 정보를 모두 입력해주세요.");
            return;
        }
        if (response.ok) {
            alert("일정이 등록되었습니다.");
            window.location.href = "/schedule/list";
        }
    }
    catch (error) {
        console.error("Failed to create schedule: ", error);
    }
}
export async function loadScheduleDetail(scheduleId) {
    try {
        const response = await fetch(`/schedule/detail?id=${scheduleId}`);
        if (response.status === 404) {
            alert("존재하지 않는 일정입니다.");
            window.location.href = "/schedule/list";
            return;
        }
        if (!response.ok)
            throw new Error(`HTTP error! status: ${response.status}`);
        const html = await response.text();
        renderMainContent(html);
    }
    catch (error) {
        console.error("Failed to load schedule detail: ", error);
    }
}
export async function loadScheduleForm() {
    try {
        const response = await fetch("/schedule/form");
        if (response.status === 401) {
            alert("일정을 작성하려면 로그인이 필요합니다.");
            if (typeof openLoginModal === "function") {
                openLoginModal();
            }
            return;
        }
        if (!response.ok)
            throw new Error(`HTTP error! status: ${response.status}`);
        const html = await response.text();
        renderMainContent(html);
    }
    catch (error) {
        console.error("Failed to load schedule form: ", error);
    }
}
export async function loadScheduleUpdateForm(scheduleId) {
    try {
        const response = await fetch(`/schedule/modify?id=${scheduleId}`);
        if (response.status === 401) {
            alert("로그인이 필요합니다.");
            if (typeof openLoginModal === "function")
                openLoginModal();
            return;
        }
        if (response.status === 403) {
            alert("수정 권한이 없습니다.");
            return;
        }
        if (!response.ok)
            throw new Error(`HTTP error! status: ${response.status}`);
        const html = await response.text();
        renderMainContent(html);
    }
    catch (error) {
        console.error("Failed to load schedule update: ", error);
    }
}
export async function updateSchedule(form) {
    const formData = new FormData(form);
    const bodyParams = new URLSearchParams(formData);
    try {
        const response = await fetch("/schedule/modify", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: bodyParams.toString()
        });
        if (response.status === 401) {
            alert("로그인이 필요합니다.");
            if (typeof openLoginModal === "function")
                openLoginModal();
            return;
        }
        if (response.status === 403) {
            alert("수정 권한이 없습니다.");
            return;
        }
        if (response.ok) {
            alert("일정이 수정되었습니다.");
            const scheduleId = formData.get("id");
            if (scheduleId) {
                loadScheduleDetail(Number(scheduleId));
            }
            else {
                window.location.href = "/schedule/list";
            }
        }
    }
    catch (error) {
        console.error("Failed to update schedule: ", error);
    }
}
export async function deleteSchedule(scheduleId) {
    if (!confirm("정말 이 일정을 삭제하시겠습니까?"))
        return;
    try {
        const response = await fetch(`/schedule/delete`, {
            method: "POST",
            headers: {
                "X-Requested-With": "XMLHttpRequest",
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: `id=${scheduleId}`
        });
        if (response.status === 401) {
            alert("로그인이 필요합니다.");
            if (typeof openLoginModal === "function")
                openLoginModal();
            return;
        }
        if (response.status === 403) {
            alert("삭제 권한이 없습니다.");
            return;
        }
        if (response.ok) {
            alert("삭제되었습니다.");
            window.location.href = "/schedule/list";
        }
    }
    catch (error) {
        console.error("Failed to delete schedule: ", error);
    }
}
function renderMainContent(html) {
    const contentContainer = document.getElementById("main-content");
    if (contentContainer) {
        contentContainer.innerHTML = html;
    }
    else {
        console.error("Main container element not found in DOM");
    }
}
window.deleteSchedule = deleteSchedule;
