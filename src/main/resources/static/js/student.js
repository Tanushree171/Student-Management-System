// =============================================
// STUDENT DASHBOARD — JavaScript
// =============================================

// Sidebar Toggle
function toggleSidebar() {
    const sidebar = document.getElementById('sidebar');
    sidebar.classList.toggle('collapsed');
    sidebar.classList.toggle('show');
}

// Sub-menu Toggle
function toggleSubMenu(element) {
    element.classList.toggle('open');
    const subMenu = element.nextElementSibling;
    if (subMenu) {
        subMenu.classList.toggle('show');
    }
}

// User Dropdown
function toggleUserDropdown() {
    const dropdown = document.getElementById('userDropdown');
    dropdown.classList.toggle('show');
}

// Close dropdown on outside click
document.addEventListener('click', function(e) {
    const userMenu = document.getElementById('userMenu');
    const dropdown = document.getElementById('userDropdown');
    if (userMenu && !userMenu.contains(e.target)) {
        dropdown?.classList.remove('show');
    }
});

// Announcement Modal
function showAnnouncement(element) {
    event.preventDefault();
    const title = element.getAttribute('data-title');
    const body = element.getAttribute('data-body');
    document.getElementById('modalTitle').textContent = title;
    document.getElementById('modalBody').innerHTML = '<p>' + body + '</p>';
    document.getElementById('announcementModal').classList.add('show');
}

function closeModal() {
    document.getElementById('announcementModal').classList.remove('show');
}

// Close modal on overlay click
document.addEventListener('click', function(e) {
    const modal = document.getElementById('announcementModal');
    if (e.target === modal) {
        closeModal();
    }
});

// Logout
async function logout() {
    try {
        await fetch('/api/auth/logout', { method: 'POST' });
    } catch(e) {}
    window.location.href = '/login';
}

// Star Rating
function initStarRating() {
    document.querySelectorAll('.star-rating').forEach(container => {
        const stars = container.querySelectorAll('i');
        const input = container.querySelector('input[type="hidden"]');
        
        stars.forEach((star, index) => {
            star.addEventListener('click', () => {
                const value = index + 1;
                if (input) input.value = value;
                stars.forEach((s, i) => {
                    s.classList.toggle('active', i < value);
                });
            });

            star.addEventListener('mouseenter', () => {
                stars.forEach((s, i) => {
                    s.style.color = i <= index ? '#f59e0b' : '#d1d5db';
                });
            });
        });

        container.addEventListener('mouseleave', () => {
            const val = input ? parseInt(input.value) || 0 : 0;
            stars.forEach((s, i) => {
                s.style.color = i < val ? '#f59e0b' : '#d1d5db';
            });
        });
    });
}

// Initialize on page load
document.addEventListener('DOMContentLoaded', function() {
    initStarRating();
    
    // Set active nav item based on URL
    const currentPath = window.location.pathname;
    document.querySelectorAll('.nav-item, .sub-menu a').forEach(item => {
        if (item.getAttribute('href') === currentPath) {
            item.classList.add('active');
            // Expand parent submenu
            const parent = item.closest('.nav-group');
            if (parent) {
                parent.querySelector('.nav-toggle')?.classList.add('open');
                parent.querySelector('.sub-menu')?.classList.add('show');
            }
        }
    });
});
