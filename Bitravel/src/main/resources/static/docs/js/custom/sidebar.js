//sidebar layout style
var t = document.getElementsByClassName("page-sidebar")[0];
if (t) {
  var e = document.getElementsByClassName("sidebar-toggler")[0];
  e.addEventListener("click", function () {
    this.classList.toggle("sidebar-toggled");
    document.getElementsByTagName("body")[0].classList.toggle("page-collapsed")
  });
}
