export default {
  install (Vue) {
    Vue.prototype.keyPrefix = '_';

    Vue.prototype.$success = function (title, message) {
      var notification = {
        title, message,
        type: 'success'
      };

      this.$store.dispatch('addToast', notification);
    };

    Vue.prototype.$error = function (title, message) {
      var notification = {
        title, message,
        type: 'danger'
      };

      this.$store.dispatch('addToast', notification);
    };

    Vue.prototype.$info = function (title, message) {
      var notification = {
        title, message,
        type: 'info'
      };

      this.$store.dispatch('addToast', notification);
    };

    Vue.prototype.$warn = function (title, message) {
      var notification = {
        title, message,
        type: 'warning'
      };

      this.$store.dispatch('addToast', notification);
    };
  }
};
