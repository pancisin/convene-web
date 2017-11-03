import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';

export default Vue => {
  var connecting = null;

  const connectWM = function (serverEndPoint) {
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

  const disconnectWM = function () {
    if (this.$stompClient && this.$stompClient.connected) {
      this.$stompClient.disconnect();
    }
  };

  const sendWM = function (target, data) {
    return new Promise((resolve, reject) => {
      if (typeof data !== 'string') {
        data = JSON.stringify(data);
      }

      this.$stompClient.send(target, data, {
        // 'Authorization': 'Bearer ' + window.localStorage.getItem('id_token')
      });
      resolve();
    });
  };
  
  Vue.connectWM = connectWM;

  Object.defineProperties(Vue.prototype, {
    connectWM: {
      get () {
        return connectWM;
      }
    },
    disconnectWM: {
      get () {
        return disconnectWM;
      }
    },
    sendWM: {
      get () {
        return sendWM;
      }
    }
  });
};
