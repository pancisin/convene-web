<script>
export default {
  functional: true,
  render(createElement, context) {
    context.data.on = {
      beforeEnter(el) {
        el.style.opacity = 0
      },
      enter(el, done) {
        Velocity(el, {
          opacity: 1,
        }, {
            delay: el.dataset.index * 150,
            complete: done
          }
        )
      },
      leave(el, done) {
        Velocity(el, {
          opacity: 0,
        }, {
            delay: el.dataset.index * 150,
            complete: done
          }
        )
      }
    }

    context.data.name = "staggered-fade";
    context.data.css = false;
    return createElement('transition-group', context.data, context.children)
  }
}
</script>