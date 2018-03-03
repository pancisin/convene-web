<script>
import velocity from 'velocity-animate';
export default {
  functional: true,
  render (createElement, context) {
    context.data.on = {
      enter (el, done) {
        const curHeight = el.clientHeight;
        el.style.height = 'auto';
        const autoHeight = el.clientHeight;
        el.style.height = `${curHeight}px`;

        velocity(el, {
          height: autoHeight,
          opacity: 1
        }, {
          duration: 300,
          easing: 'ease-in-out',
          queue: false,
          complete: done
        });
      },
      leave (el, done) {
        velocity(el, {
          height: 0,
          opacity: 0.5
        }, {
          duration: 300,
          easing: 'ease-in-out',
          queue: false,
          complete: done
        });
      },
      afterEnter (el) {
        el.style.height = 'auto';
      }
    };

    context.data.css = false;
    return createElement('transition', context.data, context.children);
  }
};
</script>