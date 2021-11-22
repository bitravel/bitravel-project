var initQuill = document.querySelectorAll("[data-quill]");
initQuill.forEach((qe) => {
  const qt = {
    ...(qe.dataset.quill ? JSON.parse(qe.dataset.quill) : {}),
    modules: {
      toolbar: [
        ["bold", "italic"],
        ["link", "blockquote", "code", "image"],
        [{ list: "ordered" }, { list: "bullet" }]
      ]
    },
    theme: "snow"
  };
  new Quill(qe, qt);
});