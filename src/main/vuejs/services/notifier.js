export default {
  install (Vue) {
    Vue.prototype.keyPrefix = '_';

    Vue.prototype.$success = function (code, subject) {
      var notification = {
        code,
        subject,
        type: 'success'
      };

      this.$store.dispatch('addToast', notification);
    };

    Vue.prototype.$error = function (code, subject) {
      var notification = {
        code,
        subject,
        type: 'danger'
      };

      this.$store.dispatch('addToast', notification);
    };

    Vue.prototype.$info = function (code, subject) {
      var notification = {
        code,
        subject,
        type: 'info'
      };

      this.$store.dispatch('addToast', notification);
    };

    Vue.prototype.$warn = function (code, subject) {
      var notification = {
        code,
        subject,
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
