export default {
  bind: (el, binding, vNode) => {
    var tmp = document.createElement('DIV');
    tmp.innerHTML = binding.value;
    el.innerHTML = tmp.textContent || tmp.innerText;
  }
};
