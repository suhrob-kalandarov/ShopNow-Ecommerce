document.addEventListener('DOMContentLoaded', function() {
    // Add to cart animation
    const addToCartButtons = document.querySelectorAll('.btn-add-cart');

    addToCartButtons.forEach(button => {
        button.addEventListener('click', function() {
            // Add animation class
            this.classList.add('btn-add-cart-clicked');

            // Remove animation class after animation completes
            setTimeout(() => {
                this.classList.remove('btn-add-cart-clicked');
            }, 300);
        });
    });

    // Mobile sidebar toggle
    const sidebarToggle = document.querySelector('.nav-action-btn[data-bs-toggle="collapse"]');
    const sidebar = document.querySelector('.sidebar');

    if (sidebarToggle && sidebar) {
        sidebarToggle.addEventListener('click', function() {
            sidebar.classList.toggle('d-none');
        });
    }
});