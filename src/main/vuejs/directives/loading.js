const updateLoader = (el, value) => {
  let percentage = el.getElementsByClassName('percentage')[0];
  percentage.style = {
    display: value && value !== true ? 'inline-block' : 'none'
  };

  percentage.innerText = value !== true && value !== false ? `${value}%` : '';
};

export default {
  bind (el, binding, node) {
    el.style.position = 'relative';

    var loader = document.createElement('div');
    var spinner = document.createElement('div');
    var percentage = document.createElement('span');

    spinner.className = 'spinner';
    loader.appendChild(spinner);
    loader.className = 'loader';
    percentage.className = 'percentage';
    loader.appendChild(percentage);

    el.appendChild(loader);
  },
  inserted (el, binding) {
    el.classList.toggle('_loading', binding.value);
    updateLoader(el, binding.value);
  },
  update (el, binding, node) {
    el.classList.toggle('_loading', binding.value);
    updateLoader(el, binding.value);
  }
};
