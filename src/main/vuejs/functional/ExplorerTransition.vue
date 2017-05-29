<script>
export default {
  functional: true,
  render(createElement, context) {
    context.data.on = {
      beforeEnter(el) {
        el.style.opacity = 0
      },
      enter(el, done) {
        var delay = el.dataset.index * 150
        setTimeout(() => {
          Velocity(el, {
            opacity: 1,
          }, {
              complete: done
            }
          )
        }, delay)
      },
      leave(el, done) {
        var delay = el.dataset.index * 150
        setTimeout(() => {
          Velocity(el, {
            opacity: 0,
          }, {
              complete: done
            }
          )
        }, delay)
      }
    }

    context.data.name = "staggered-fade";
    context.data.css = false;
    return createElement('transition-group', context.data, context.children)
  }
}
</script>