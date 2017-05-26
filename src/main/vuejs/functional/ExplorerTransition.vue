<script>
// import Velocity from 'velocityjs'
export default {
  functional: true,
  mounted() {
    console.log(this);
  },
  render(createElement, context) {
    context.data.on = {
      beforeEnter: function (el) {
        el.style.opacity = 0
      },
      enter: function (el, done) {
        var delay = el.dataset.index * 150
        setTimeout(function () {
          Velocity(el, {
            opacity: 1,
          }, {
              complete: done
            }
          )
        }, delay)
      },
      leave: function (el, done) {
        var delay = el.dataset.index * 150
        setTimeout(function () {
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