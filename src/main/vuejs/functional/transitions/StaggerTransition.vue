<script>
import velocity from 'velocity-animate';
export default {
  functional: true,
  name: 'stagger-transition',
  props: {
    delay: {
      type: Number,
      default () {
        return 100;
      }
    },
    tag: {
      type: String,
      default () {
        return 'div';
      }
    }
  },
  render (createElement, context) {
    const options = {
      props: {
        tag: context.props.tag,
        css: false,
        appear: true
      },
      on: {
        beforeEnter (el) {
          el.style.opacity = 0;
        },
        enter (el, done) {
          const children = Array.from(el.parentElement.children);
          const index = children.findIndex(e => e === el);

          velocity(el, {
            opacity: 1
          }, {
            queue: false,
            duration: 150,
            delay: index * context.props.delay,
            easing: 'ease-in',
            complete: done
          });
        },
        leave (el, done) {
          const children = Array.from(el.parentElement.children);

          const index = children.findIndex(e => e === el);
          const order = (children.length - 1) - index;

          velocity(el, {
            opacity: 0
          }, {
            queue: false,
            duration: 150,
            delay: order * context.props.delay,
            easing: 'ease-in',
            complete: done
          });
        }
      }
    };

    return createElement('transition-group', options, context.children);
  }
};
</script>