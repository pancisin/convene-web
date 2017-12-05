import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';

export default Vue => {
  let connectPromise = null;
  let stompClient = null;

  const connectWM = serverEndpoint => {
    if (!connectPromise) {
      connectPromise = new Promise((resolve, reject) => {
        if (stompClient && stompClient.connected) {
          resolve(stompClient);
        } else if (stompClient && !stompClient.connected) {
          return connectPromise;
        } else {
          let socket = new SockJS(serverEndpoint);
          stompClient = Stomp.over(socket);
          Vue.prototype.$stompClient = stompClient;

          stompClient.connect({
            'Authorization': 'Bearer ' + window.localStorage.getItem('id_token') || window.sessionStorage.getItem('id_token')
          }, resolve, reject);
        }
      });
    }

    return connectPromise;
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
