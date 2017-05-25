<script>
// import Velocity from 'velocityjs'
export default {
  functional: true,
  render: function (createElement, context) {
    var data = {
      props: {
        name: 'staggered-fade',
        tag: 'div',
        css: false
      },
      on: {
        beforeEnter: function (el) {
          el.style.opacity = 0
          el.style.height = 0
          el.style.display = "block"
        },
        enter: function (el, done) {
          var delay = el.dataset.index * 150
          setTimeout(function () {
            Velocity(el, {
              opacity: 1,
              height: '100%'
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
              height: 0
            }, {
                complete: done
              }
            )
          }, delay)
        }
      }
    }
    return createElement('transition-group', data, context.children)
  }
}
</script>