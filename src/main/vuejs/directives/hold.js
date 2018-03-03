
const events = ['mousedown', 'mouseup', 'mouseleave'];
export default {
  bind: function (el, binding, vNode) {
    if (typeof binding.value !== 'function') {
      const compName = vNode.context.name;
      let warn = `[Vue-click-outside:] provided expression '${binding.expression}' is not a function, but has to be`;
      if (compName) { warn += `Found in component '${compName}'`; };
      console.warn(warn);
    }

    var timeout_id;
    var holdingStart = false;

    const handler = event => {
      switch (event.type) {
        case 'mousedown':
          timeout_id = setTimeout(() => {
            holdingStart = true;
            binding.value.call(null, {
              holding: true
            });
          }, 1000);
          break;
        case 'mouserleave':
        case 'mouseup':
          if (holdingStart) {
            binding.value.call(null, {
              holding: false
            });
          }

          clearTimeout(timeout_id);
          break;
      }

    };

    el.__vueHold__ = handler;

    events.forEach(event => el.addEventListener(event, handler));
  },

  unbind: function (el, binding) {
    events.forEach(event => removeEventListener(event, el.__vueHold__));
    el.__vueHold__ = null;
  }
};
