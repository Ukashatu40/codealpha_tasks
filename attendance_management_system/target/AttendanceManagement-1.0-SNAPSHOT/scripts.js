// JavaScript for Sidebar Toggle
document.addEventListener("DOMContentLoaded", function () {
    const toggleButton = document.getElementById("sidebar-toggle");
    const sidebar = document.getElementById("sidebar");
    const content = document.getElementById("content");

    toggleButton.addEventListener("click", () => {
        sidebar.classList.toggle("collapsed");
        content.classList.toggle("collapsed");

        console.log("Sidebar classes:", sidebar.classList);
        console.log("Content classes:", content.classList);
    });
});
