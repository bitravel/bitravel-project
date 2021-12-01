import gsap from 'gsap';


let vw=window.innerWidth||document.documentElement.clientWidth,maxVw=320;
vw>maxVw&&document.querySelectorAll(".js-hover-img-item").forEach(function(e){
  let t=e,r=(t.getBoundingClientRect(),
e.querySelector("img")),
    a=r.offsetHeight,
    l=r.offsetWidth;
 e.addEventListener("mouseenter",
s=>{e.classList.contains("is-hover")||e.classList.add("is-hover");
let o=s.clientX-t.offsetLeft-l/2.7,u=s.offsetY-a/2.5;gsap.set(r,{x:o,y:u,rotate:-4})}),
e.addEventListener("mousemove",
e=>{let s=e.clientX-t.offsetLeft-l/2,
o=e.offsetY-r.offsetTop-a*.5;
gsap.to(r,{x:s,y:o,rotate:-4})}),
     e.addEventListener("mouseleave",()=>{e.classList.contains("is-hover")&&e.classList.remove("is-hover")})
 });

//cursor
window.addEventListener('DOMContentLoaded', (event) => {
    let isCursor = document.querySelectorAll('.is-cursor');
    isCursor.forEach(function (el) {
        gsap.set(".v-cursor", { xPercent: -50, yPercent: -50 });
        const e = document.querySelector(".v-cursor"),
            t = { x: window.innerWidth / 2, y: window.innerHeight / 2 },
            o = { x: t.x, y: t.y },
            r = .15, n = gsap.quickSetter(e, "x", "px"),
            a = gsap.quickSetter(e, "y", "px");
        window.addEventListener("mousemove", e => { o.x = e.x, o.y = e.y }),
            gsap.ticker.add(() => {
                const e = 1 - Math.pow(1 - r,
                    gsap.ticker.deltaRatio());
                t.x += (o.x - t.x) * e,
                    t.y += (o.y - t.y) * e,
                    n(t.x),
                    a(t.y)
            })
    })
})

//Js circle rotation using gsap animation
let rotation = document.querySelectorAll(".js-circle-rotation");
rotation.forEach(function (e) {
  const o = gsap.to(e, {
      duration: 10,
      rotation: -360,
      repeat: -1,
      ease: "none"
    })
});

