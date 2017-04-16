import SockJS from "sockjs-client"
import Stomp from "webstomp-client"

export default {
  install(Vue) {
    const keyPrefix = "_";
    Vue.prototype.keyPrefix = keyPrefix;

    let connectWM = function (serverEndPoint, ...args) {
      if (this.$stompClient && this.$stompClient.connected) {
        return;
      }

      let socket = new SockJS(serverEndPoint);

      let stompClient = Stomp.over(socket);
      Vue.prototype.$stompClient = stompClient;

      this.$stompClient.connect(...args);
    }

    Vue.prototype.connectWM = connectWM;

    let disconnectWM = function () {
      if (this.$stompClient && this.$stompClient.connected) {
        this.$stompClient.disconnect();
      }
    };

    Vue.prototype.disconnectWM = disconnectWM;
  }
}