import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';

export default {
  install (Vue) {
    const keyPrefix = '_';
    Vue.prototype.keyPrefix = keyPrefix;
    var connecting = null;

    let connectWM = function (serverEndPoint) {
      var promise = new Promise((resolve, reject) => {
        if (this.$stompClient != null && this.$stompClient.connected) {
          connecting = null;
          resolve();
        } else if (this.$stompClient != null && !this.$stompClient.connected && connecting != null) {
          connecting.then(resolve);
        } else {
          connecting = promise;
          let socket = new SockJS(serverEndPoint);

          let stompClient = Stomp.over(socket);
          Vue.prototype.$stompClient = stompClient;

          this.$stompClient.connect({
            'Authorization': 'Bearer ' + window.localStorage.getItem('id_token')
          }, resolve, reject);
        }
      });

      return promise;
    };

    Vue.prototype.connectWM = connectWM;

    let disconnectWM = function () {
      if (this.$stompClient && this.$stompClient.connected) {
        this.$stompClient.disconnect();
      }
    };

    Vue.prototype.disconnectWM = disconnectWM;

    let sendWM = function (target, data) {
      return new Promise((resolve, reject) => {
        if (typeof data !== 'string') {
          data = JSON.stringify(data);
        }

        this.$stompClient.send(target, data, {
          // 'Authorization': 'Bearer ' + Auth.getAuthHeader(),
        });
        resolve();
      });
    };

    Vue.prototype.sendWM = sendWM;
  }
};
