export class ScheduleManager {
    constructor() {
        this.mainContentArea = document.getElementById('main-content');
        this.initEventListeners();
        window.deleteSchedule = this.deleteSchedule.bind(this);
    }
    initEventListeners() {
        document.addEventListener('click', (event) => {
            const target = event.target;
            const listLink = target.closest('.schedule-list-link');
            if (listLink) {
                event.preventDefault();
                this.navigate('/schedule/list');
                return;
            }
            const writeLink = target.closest('.schedule-write-link');
            if (writeLink) {
                event.preventDefault();
                this.navigate('/schedule/write');
                return;
            }
            const detailLink = target.closest('.schedule-detail-link');
            if (detailLink) {
                event.preventDefault();
                const scheduleId = detailLink.dataset.id || detailLink.getAttribute('href')?.split('id=')[1];
                if (scheduleId) {
                    this.navigate(`/schedule/detail?id=${scheduleId}`);
                }
                return;
            }
            const modifyLink = target.closest('.schedule-modify-link');
            if (modifyLink) {
                event.preventDefault();
                const scheduleId = modifyLink.dataset.id || modifyLink.getAttribute('href')?.split('id=')[1];
                if (scheduleId) {
                    this.navigate(`/schedule/modify?id=${scheduleId}`);
                }
                return;
            }
            const deleteBtn = target.closest('.schedule-delete-btn');
            if (deleteBtn) {
                event.preventDefault();
                const scheduleId = deleteBtn.dataset.id;
                if (scheduleId && confirm('정말 이 일정을 삭제하시겠습니까?')) {
                    this.deleteSchedule(scheduleId);
                }
                return;
            }
        });
        document.addEventListener('submit', (event) => {
            const form = event.target;
            if (form.id === 'schedule-write-form') {
                event.preventDefault();
                this.handleWriteSubmit(form);
            }
            if (form.id === 'schedule-modify-form') {
                event.preventDefault();
                this.handleModifySubmit(form);
            }
        });
    }
    async navigate(url) {
        try {
            const response = await fetch(url, {
                headers: {
                    'X-Requested-With': 'XMLHttpRequest'
                }
            });
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const html = await response.text();
            if (this.mainContentArea) {
                this.mainContentArea.innerHTML = html;
                window.history.pushState({}, '', url);
            }
        }
        catch (error) {
            console.error('Schedule Navigation Error:', error);
            alert('페이지를 불러오는 중 오류가 발생했습니다.');
        }
    }
    async handleWriteSubmit(form) {
        const formData = new FormData(form);
        const params = new URLSearchParams();
        formData.forEach((value, key) => {
            params.append(key, value.toString());
        });
        try {
            const response = await fetch('/schedule/write', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'X-Requested-With': 'XMLHttpRequest'
                },
                body: params.toString()
            });
            if (response.ok) {
                alert('일정이 성공적으로 등록되었습니다.');
                this.navigate('/schedule/list');
            }
            else {
                const errorText = await response.text();
                alert(`일정 등록 실패: ${errorText || '오류가 발생했습니다.'}`);
            }
        }
        catch (error) {
            console.error('Schedule Write Error:', error);
            alert('일정 등록 요청 중 네트워크 오류가 발생했습니다.');
        }
    }
    async handleModifySubmit(form) {
        const formData = new FormData(form);
        const scheduleId = formData.get('id');
        const params = new URLSearchParams();
        formData.forEach((value, key) => {
            params.append(key, value.toString());
        });
        try {
            const response = await fetch(`/schedule/modify?id=${scheduleId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'X-Requested-With': 'XMLHttpRequest'
                },
                body: params.toString()
            });
            if (response.ok) {
                alert('일정이 수정되었습니다.');
                this.navigate(`/schedule/detail?id=${scheduleId}`);
            }
            else {
                alert('일정 수정에 실패했습니다.');
            }
        }
        catch (error) {
            console.error('Schedule Modify Error:', error);
            alert('일정 수정 요청 중 오류가 발생했습니다.');
        }
    }
    async deleteSchedule(scheduleId) {
        if (!confirm("정말 이 일정을 삭제하시겠습니까?"))
            return;
        try {
            const response = await fetch(`/schedule/delete`, {
                method: 'POST',
                headers: {
                    'X-Requested-With': 'XMLHttpRequest'
                },
                body: `id=${scheduleId}`
            });
            if (response.ok) {
                alert('일정이 삭제되었습니다.');
                window.location.href = '/schedule/list';
            }
            else {
                alert('일정 삭제 권한이 없거나 오류가 발생했습니다.');
            }
        }
        catch (error) {
            console.error('Schedule Delete Error:', error);
            alert('일정 삭제 요청 중 오류가 발생했습니다.');
        }
    }
}
document.addEventListener('DOMContentLoaded', () => {
    new ScheduleManager();
});
