// import Swiper JS
import Swiper from 'swiper/swiper-bundle';

//Classic
var swiperClassic = new Swiper(".swiper-classic", {
  slidesPerView: 1, spaceBetween: 0,
  loop: true, autoplay: { delay: 2500 },
  pagination: {
    el: ".swiperClassic-pagination", type: "fraction"
  },
  navigation: { nextEl: ".swiperClassic-button-next", prevEl: ".swiperClassic-button-prev" }
});
//Swiper for text/headings
var swiperText = new Swiper(".swiper-text", {
  autoplay: true, direction: "vertical", loop: true, grabCursor: false, speed: 600
});
//parallax-bg
var swiperParallax = new Swiper(".swiper-parallax-bg", {
  speed: 900, parallax: true,
  pagination: { el: ".swiperParallax-pagination", clickable: true },
  navigation: {
    nextEl: ".swiperParallax-button-next", prevEl: ".swiperParallax-button-prev"
  }
});
//swiper-auto
var swiperAuto = new Swiper(".swiper-auto", {
  slidesPerView: "auto",loop:true, centeredSlides: true, spaceBetween: 16,
  pagination: {
    el: ".swiperAuto-pagination", clickable: true,
  },
  navigation: {
    nextEl: ".swiperAuto-button-next", prevEl: ".swiperAuto-button-prev",
  }
});
//swiper-2
var swiper2 = new Swiper(".swiper-2", {
  autoHeight: true,
  spaceBetween: 8,
  loop:true,
  breakpoints: {
    640: { slidesPerView: 1, spaceBetween: 16 },
    768: { slidesPerView: 2, spaceBetween: 16 },
    1024: { slidesPerView: 2, spaceBetween: 16 }
  },
  pagination: { el: ".swiper2-pagination", clickable: true },
  navigation: { nextEl: ".swiper2-button-next", prevEl: ".swiper2-button-prev" }
});

//swiper-3
var swiper3 = new Swiper(".swiper-3", {
  autoHeight: true,
  spaceBetween: 16,
  breakpoints: {
    640: { slidesPerView: 1, spaceBetween: 16 },
    768: { slidesPerView: 2, spaceBetween: 16 },
    1024: { slidesPerView: 3, spaceBetween: 30 }
  },
  pagination: {
    el: ".swiper3-pagination", clickable: true
  },
  navigation: {
    nextEl: ".swiper3-button-next", prevEl: ".swiper3-button-prev"
  }
});

//swiper-4
var swiper4 = new Swiper(".swiper-4", {
  autoHeight: true,
  spaceBetween: 8,
  breakpoints: {
    640: { slidesPerView: 1, spaceBetween: 16 },
    768: { slidesPerView: 3, spaceBetween: 16 },
    1024: {
      slidesPerView: 4, spaceBetween: 16
    }
  },
  pagination: {
    el: ".swiper4-pagination", clickable: true
  },
  navigation: {
    nextEl: ".swiper4-button-next", prevEl: ".swiper4-button-prev"
  }
});
//swiper-4 -partners
var swiperPartners4 = new Swiper(".swiper-partners-4", {
  slidesPerView: 3,loop: true,
  spaceBetween: 12,
  breakpoints: {
    768: {
      slidesPerView: 4
    }
  },
  pagination: {
    el: ".swiper-partners-4-pagination",
    clickable: true
  },
  navigation: {
    nextEl: ".swiper-partners-4-button-next",
    prevEl: ".swiper-partners-4-button-prev"
  }
});
//swiper-5
var swiper5 = new Swiper(".swiper-5", {
  slidesPerView: 1,
  spaceBetween: 8,
  breakpoints: {
    1024: {
      slidesPerView: 5
    }
  },
  pagination: {
    el: ".swiper5-pagination", clickable: true
  },
  navigation: { nextEl: ".swiper5-button-next", prevEl: ".swiper5-button-prev" }
});
//swiper-4 -partners
var swiperPartners5 = new Swiper(".swiper-partners-5", {
  slidesPerView: 2,loop: true, spaceBetween: 16, autoplay: true,
  breakpoints: {
    768: { slidesPerView: 4 },
    1024: { slidesPerView: 5 }
  },
  pagination: {
    el: ".swiper-partners-5-pagination", clickable: true
  },
  navigation: {
    nextEl: ".swiper-partners-5-button-next", prevEl: ".swiper-partners-5-button-prev"
  }
});
//swiperScale
var swiperScale = new Swiper(".swiper-scale", {
  slidesPerView: "auto",autoHeight:true,loop: true, centeredSlides: true, spaceBetween: 0,
  breakpoints: {
    1024: { slidesPerView: 2 }
  },
  pagination: {
    el: ".swiperScale-pagination", clickable: true
  },
  navigation: { nextEl: ".swiperScale-button-next", prevEl: ".swiperScale-button-prev" }
});

//Progress bar pagination
var swiperProgress = new Swiper(".swiper-progress", {
  pagination: { el: ".swiperProgress-pagination", type: "progressbar" },
  navigation: { nextEl: ".swiperProgress-button-next", prevEl: ".swiperProgress-button-prev" }
});

//swiper-vertical
var swiperVertical = new Swiper(".swiper-vertical", {
  loop: true, direction: "vertical",
  pagination: {
    el: ".swiperVertical-pagination",
    clickable: true
  }
});

//Swiper thumbnail demo
var swiperThumbnails = new Swiper(".swiper-thumbnails", {
  spaceBetween: 30, slidesPerView: "auto", freeMode: true, watchSlidesVisibility: true, watchSlidesProgress: true
});
var swiperThumbnailsMain = new Swiper(".swiper-thumbnails-main", {
  spaceBetween: 0,
  navigation: { nextEl: ".swiperThumb-next", prevEl: ".swiperThumb-prev" },
  thumbs: { swiper: swiperThumbnails }
});