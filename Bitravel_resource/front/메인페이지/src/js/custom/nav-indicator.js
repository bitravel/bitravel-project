//Nav Indicator Resizing script

function indicatorResize() {
    var linkAct = document.querySelectorAll(".nav-indicator .nav-link.active");
    linkAct.forEach(function (el) {
        const navBg = document.querySelector(".nav-indicator-bg");
        const rect = el.getBoundingClientRect();
        navBg.style.width = el.clientWidth + "px";
        navBg.style.height = el.clientHeight + "px";
        navBg.style.left = el.offsetLeft + "px";
        navBg.style.top = el.offsetTop + "px";
    })
    var link = document.querySelectorAll(".nav-indicator .nav-link");
    link.forEach(function (e) {
        const navBg = document.querySelector(".nav-indicator-bg");
        e.addEventListener('click', event => {
            const rect = e.getBoundingClientRect();
            navBg.style.width = e.clientWidth + "px";
            navBg.style.height = e.clientHeight + "px";
            navBg.style.left = e.offsetLeft + "px";
            navBg.style.top = e.offsetTop + "px";
        })
    })
};
indicatorResize();
window.addEventListener("resize", indicatorResize);
window.addEventListener("scroll", indicatorResize);