//Header sticky top
window.addEventListener('scroll', function () {
   const headerSticky = document.querySelector(".header-sticky");
   // var headerStickyFixed = document.querySelector(".header-sticky-fixed");
   const coords = document.documentElement.clientHeight;
   if (headerSticky) {
      headerSticky.classList.toggle("is-sticky", window.scrollY > coords)
   }
});