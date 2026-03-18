// =============================================
// ADMIN DASHBOARD — JavaScript
// =============================================

function toggleAdminSidebar() {
    const sidebar = document.getElementById('adminSidebar');
    sidebar.classList.toggle('collapsed');
    sidebar.classList.toggle('show');
}

function toggleAdminSubMenu(element) {
    element.classList.toggle('open');
    const subMenu = element.nextElementSibling;
    if (subMenu) subMenu.classList.toggle('show');
}

function toggleAdminUserDropdown() {
    const dd = document.getElementById('adminUserDropdown');
    dd.classList.toggle('show');
}

document.addEventListener('click', function(e) {
    const pill = document.querySelector('.user-pill');
    const dd = document.getElementById('adminUserDropdown');
    if (pill && !pill.contains(e.target)) dd?.classList.remove('show');
});

async function logout() {
    try { await fetch('/api/auth/logout', { method: 'POST' }); } catch(e) {}
    window.location.href = '/login';
}

// Set active nav
document.addEventListener('DOMContentLoaded', function() {
    const path = window.location.pathname;
    document.querySelectorAll('.admin-nav-item, .admin-sub-menu a').forEach(item => {
        if (item.getAttribute('href') === path) {
            item.classList.add('active');
            const parent = item.closest('.admin-sub-menu');
            if (parent) {
                parent.classList.add('show');
                parent.previousElementSibling?.classList.add('open');
            }
        }
    });
});
