export default {
  install (Vue) {
    Vue.prototype.keyPrefix = '_';

    Vue.prototype.$success = function (code, target) {
      var notification = {
        code,
        target,
        type: 'success'
      };

      this.$store.dispatch('addToast', notification);
    };

    Vue.prototype.$error = function (code, target) {
      var notification = {
        code,
        target,
        type: 'danger'
      };

      this.$store.dispatch('addToast', notification);
    };

    Vue.prototype.$info = function (code, data) {
      var notification = {
        code,
        type: 'info',
        ...data
      };

      this.$store.dispatch('addToast', notification);
    };

    Vue.prototype.$warn = function (code, target) {
      var notification = {
        code,
        target,
        type: 'warning'
      };

      this.$store.dispatch('addToast', notification);
    };

    Vue.prototype.$prompt = function (code, target, agree, disagree) {
      var notification = {
        code,
        target,
        type: 'prompt', agree, disagree
      };

      this.$store.dispatch('addToast', notification);
    };
  }
};
