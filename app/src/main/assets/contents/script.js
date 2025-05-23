function onWindowClick(e) {
    const tocTitle = document.querySelector(".toc .title");
    const tocList = document.getElementById("toc-ul");

    if (tocTitle && (e.target === tocTitle || tocTitle.contains(e.target))) {
        if (tocList) {
            tocList.style.display = tocList.style.display === "none" ? "block" : "none";
        }
    }
}

// All DOMContentLoaded logic in one place:
document.addEventListener("DOMContentLoaded", function() {
    // Theme toggle
    var toggle = document.getElementById('themeToggle');
    if (toggle) {
        toggle.onclick = function() {
            document.documentElement.classList.toggle('dark');
        };
    }

    // Go to top button
    var btn = document.getElementById("goTopBtn");
    if (btn) {
        window.onscroll = function() {
            if (document.body.scrollTop > 200 || document.documentElement.scrollTop > 200) {
                btn.style.display = "block";
            } else {
                btn.style.display = "none";
            }
        };
        btn.onclick = function() {
            window.scrollTo({top: 0, behavior: "smooth"});
        };
        btn.style.display = "none"; // Hide by default
    }
});

window.addEventListener("click", onWindowClick);