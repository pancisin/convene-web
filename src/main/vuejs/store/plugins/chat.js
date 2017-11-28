import Vue from 'vue';
import * as types from '../mutation-types';
import moment from 'moment';

export default store => {
  // store.subscribe(mutation => {
  //   if (mutation.type === types.SET_USER) {
  //     const user = mutation.payload;
  //     moment.locale(user.locale.name);
  //   }
  // })

  Vue.connectWM('/stomp').then(frame => {
    Vue.$stompClient.subscribe('/user/queue/chat.message', response => {
      let message = JSON.parse(response.body);
      console.warn(message);
    });

    Vue.$stompClient.subscribe('/user/queue/chat.activeUsers', response => {
      Vue.sendWM('/app/activeUsers', {});
      let activeContacts = JSON.parse(response.body);

      console.warn(activeContacts);
    });

    Vue.sendWM('/app/activeUsers', {});
  }, frame => {
    // console.log(frame);
  });
};
