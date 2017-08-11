<script>
export default {
  functional: true,
  render (createElement, context) {
    context.data.on = {
      beforeEnter (el) {
      },
      enter (el, done) {
        Velocity(el, 'slideDown', {
          duration: 250,
          easing: 'ease-in-out',
          complete: done
        });
      },
      leave (el, done) {
        Velocity(el, 'slideUp', {
          duration: 250,
          easing: 'ease-in-out',
          complete: done
        });
      }
    };

    context.data.css = false;
    return createElement('transition', context.data, context.children);
  }
};
</script>