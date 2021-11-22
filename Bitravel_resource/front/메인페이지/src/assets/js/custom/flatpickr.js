let el = document.querySelectorAll("[data-flatpickr]");
el.forEach(e =>{
    const t = {
        ...e.dataset.flatpickr ? JSON.parse(e.dataset.flatpickr) : {},
    }
    new flatpickr(e,t)}
    );