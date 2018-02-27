<script>
import velocity from 'velocity-animate';
export default {
  functional: true,
  props: {
    delay: {
      type: Number,
      default: 150
    },
    tag: {
      type: String,
      default: 'div'
    }
  },
  render (createElement, context) {
    var data = {
      props: {
        name: 'staggered-fade',
        tag: context.props.tag,
        css: false
      },
      on: {
        beforeEnter (el) {
          el.style.opacity = 0;
          el.style.height = 0;
          el.style.display = 'block';
        },
        enter (el, done) {
          velocity(el, {
            opacity: 1,
            height: '100%'
          }, {
            delay: el.dataset.index * context.props.delay,
            complete: done
          }
          );
        },
        leave (el, done) {
          velocity(el, {
            opacity: 0,
            height: 0
          }, {
            delay: el.dataset.index * context.props.delay,
            complete: done
          }
          );
        }
      }
    };
    return createElement('transition-group', data, context.children);
  }
};
</script>