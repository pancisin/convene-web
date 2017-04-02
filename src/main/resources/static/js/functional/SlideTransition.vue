<script>
export default {
  functional: true,
  render: function (createElement, context) {
    var data = {
      props: {
        name: 'slide-transition',
        mode: 'out-in'
      },
      on: {
        enter: function (el, insert, timeout) {
          $(el).hide();
          insert();
          $(el).slideDown(500, 'easeOutExpo');
        },
        leave: function (el, remove, timeout) {
          $(el).slideUp(500, 'easeOutExpo', function () {
            remove();
          });
        }
      }
    }
    return createElement('transition', data, context.children)
  }
}
</script>