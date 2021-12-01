import Splitting from 'splitting';

Splitting();
  document.querySelectorAll("[data-split-image]").forEach(e=>{
    setTimeout(function(){
        Splitting({ 
          target: '[data-rows], [data-columns], [data-image]',
          by: 'cells',
          image: true
        });
      
    })});

