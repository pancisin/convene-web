export default {
  install (Vue) {
    Vue.prototype.keyPrefix = '_';

    Vue.prototype.$success = function (code) {
      var notification = {
        code,
        type: 'success'
      };

      this.$store.dispatch('addToast', notification);
    };

    Vue.prototype.$error = function (code) {
      var notification = {
        code,
        type: 'danger'
      };

      this.$store.dispatch('addToast', notification);
    };

    Vue.prototype.$info = function (code) {
      var notification = {
        code,
        type: 'info'
      };

      this.$store.dispatch('addToast', notification);
    };

    Vue.prototype.$warn = function (code) {
      var notification = {
        code,
        type: 'warning'
      };

      this.$store.dispatch('addToast', notification);
    };

    Vue.prototype.$prompt = function (code, agree, disagree) {
      var notification = {
        code,
        type: 'prompt', agree, disagree
      };

      this.$store.dispatch('addToast', notification);
    };
  }
};
