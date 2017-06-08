export default {
  bind(el, binding, node) {
    el.style.position = 'relative';

    var loader = document.createElement('div');
    var spinner = document.createElement('div');

    spinner.className = 'spinner';
    loader.appendChild(spinner);
    loader.className = 'loader';

    el.appendChild(loader);
  },
  inserted(el, binding) {
    el.classList.toggle('_loading', binding.value);
  },
  update(el, binding, node) {
    el.classList.toggle('_loading', binding.value);
  },
}