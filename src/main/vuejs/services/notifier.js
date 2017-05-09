export default {
  install(Vue) {
    Vue.prototype.keyPrefix = "_";

    Vue.prototype.$success = function (title, message) {
      this.$store.commit('addNotification', {
        title, message,
        type: 'success'
      })
    }

    Vue.prototype.$error = function (title, message) {
      this.$store.commit('addNotification', {
        title, message,
        type: 'danger'
      })
    }

    Vue.prototype.$info = function (title, message) {
      this.$store.commit('addNotification', {
        title, message,
        type: 'info'
      })
    }

    Vue.prototype.$warn = function (title, message) {
      this.$store.commit('addNotification', {
        title, message,
        type: 'warning'
      })
    }
  }
}