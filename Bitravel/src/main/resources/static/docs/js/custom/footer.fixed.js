//footer fixed for min width 992px
//Css available for this component - scss/custom/utilities/_position.scss
let footerFixed = document.querySelectorAll('.js-footer-fixed');
footerFixed.forEach(function(el){

  let rect = el.getBoundingClientRect();
  let h = rect.height;
  let lg = window.matchMedia("(min-width: 992px)");
lg.addEventListener('change',footerFixedResize);

function footerFixedResize(e) {
  if (e.matches) {
    document.body.classList.add("footer-fixed");
  document.body.style.marginBottom=h + 'px';
  }
  else {
 document.body.classList.remove("footer-fixed");document.body.style.marginBottom=('0px');
  }
}
  footerFixedResize(lg);
  window.addEventListener('resize',footerFixedResize(lg));
});


