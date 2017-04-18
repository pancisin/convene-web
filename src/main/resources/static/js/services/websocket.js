import SockJS from "sockjs-client"
import Stomp from "webstomp-client"
import Auth from './auth.js'

export default {
  install(Vue) {
    const keyPrefix = "_";
    Vue.prototype.keyPrefix = keyPrefix;

    let connectWM = function (serverEndPoint) {
      return new Promise((resolve, reject) => {
        if (this.$stompClient && this.$stompClient.connected) resolve();

        let socket = new SockJS(serverEndPoint);

        let stompClient = Stomp.over(socket);
        Vue.prototype.$stompClient = stompClient;

        this.$stompClient.connect({
          'Authorization': 'Bearer ' + Auth.getAuthHeader(),
        }, resolve, reject);
      })
    }

    Vue.prototype.connectWM = connectWM;

    let disconnectWM = function () {
      if (this.$stompClient && this.$stompClient.connected) {
        this.$stompClient.disconnect();
      }
    };

    Vue.prototype.disconnectWM = disconnectWM;

    let sendWM = function (target, data) {
      return new Promise((resolve, reject) => {
        if (typeof data != 'string')
          data = JSON.stringify(data);

        this.$stompClient.send(target, data, {
          // 'Authorization': 'Bearer ' + Auth.getAuthHeader(),
        });
        resolve();
      })
    }

    Vue.prototype.sendWM = sendWM;
  }
}