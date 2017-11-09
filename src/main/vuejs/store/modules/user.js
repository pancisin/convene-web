import UserApi from 'api/user.api';
import AuthApi from 'api/auth.api';
import PageApi from 'api/page.api';
import EventApi from 'api/event.api';
import * as types from 'store/mutation-types';

const state = {
  user: null,
  loadingUser: false,
  notifications: [],
  authenticated: false,
  contacts: [],
  followedPages: [],
  attendingEvents: [],
  conversations: []
};

const getters = {
  user: state => {
    if (state.user && !state.user.address) {
      state.user.address = {};
    }

    return state.user;
  },
  locale: state => state.user ? state.user.locale : null,
  isAdmin: state => state.user != null && state.user.role != null && state.user.role.level >= 40,
  license: state => state.user ? state.user.license : null,
  notifications: state => state.notifications,
  authenticated: state => {
    return state.authenticated || window.localStorage.getItem('id_token') != null;
  },
  loadingUser: state => state.loadingUser,
  contacts: state => state.contacts,
  followedPages: state => state.followedPages,
  pageFollowStatus: state => page_id => {
    return !state.followedPages.every(p => p.id !== page_id);
  },
  attendingEvents: state => state.attendingEvents,
  eventAttendingStatus: state => event_id => {
    return !state.attendingEvents.every(e => e.id !== event_id);
  },
  conversations: state => state.conversations
};

const watchers = [
  // [
  //   state => {
  //     return state.user.user.locale;
  //   },
  //   (newVal, oldVal) => {
  //     moment.locale(newVal.name);
  //   }
  // ]
];

const actions = {
  initializeUser ({ commit }) {
    return new Promise((resolve, reject) => {
      commit(types.LOADING_USER, true);

      UserApi.getUser((user) => {
        commit(types.SET_USER, { user });
        commit(types.LOADING_USER, false);
        resolve(user);
      });
    });
  },
  updateUser ({ commit }, user) {
    return new Promise((resolve) => {
      commit(types.LOADING_USER, true);

      UserApi.putUser(user, result => {
        commit(types.SET_USER, { result });
        commit(types.LOADING_USER, false);
        resolve(user);
      });
    });
  },
  login ({ commit }, credentials) {
    return new Promise((resolve, reject) => {
      commit(types.LOADING_USER, true);

      AuthApi.login(credentials, user => {
        window.localStorage.setItem('id_token', user.token);
        commit(types.SET_USER, { user });
        commit(types.LOADING_USER, false);
        resolve(user);
      }, response => {
        reject(response.fieldErrors);
      });
    });
  },
  register ({ commit }, user_data) {
    return new Promise((resolve, reject) => {
      commit(types.LOADING_USER, true);

      AuthApi.register(user_data, user => {
        window.localStorage.setItem('id_token', user.token);
        commit(types.SET_USER, { user });
        commit(types.LOADING_USER, false);
        resolve(user);
      }, response => {
        reject(response.fieldErrors);
      });
    });
  },
  logout ({ commit }) {
    commit(types.LOADING_USER, true);

    return new Promise((resolve) => {
      window.localStorage.removeItem('id_token');
      commit(types.SET_USER, { user: null });
      commit(types.LOADING_USER, false);
      commit(types.SET_PAGES, { pages: [] });
      commit(types.SET_CONFERENCES, { conferences: [] });
      commit(types.SET_CONTACTS, { contacts: [] });
      commit(types.SET_ATTENDING_EVENTS, { events: [] });
      commit(types.SET_FOLLOWED_PAGES, { pages: [] });
      resolve();
    });
  },
  initializeNotifications ({ commit }) {
    UserApi.getNotifications((notifications) => {
      commit(types.SET_NOTIFICATIONS, { notifications });
    });
  },
  addNotification ({ commit }, notification) {
    commit(types.ADD_NOTIFICATION, { notification });
  },
  removeNotification ({ commit }, notification) {
    commit(types.REMOVE_NOTIFICATION, { notification });
  },
  initializeContacts ({ commit }) {
    return new Promise((resolve) => {
      UserApi.getContacts((contacts) => {
        commit(types.SET_CONTACTS, { contacts });
        resolve();
      });
    });
  },
  initializeConversations ({ commit }) {
    return new Promise(resolve => {
      UserApi.getConversations(conversations => {
        commit(types.SET_CONVERSATIONS, { conversations });
        resolve();
      });
    });
  },
  updateContactsActivityState ({ commit, state }, contactsActivityList) {
    const contacts = [ ...state.contacts ].map(c => {
      let active = false;
      if (contactsActivityList.includes(c.email)) {
        active = true;
      };

      return {
        ...c,
        active
      };
    });

    commit(types.SET_CONTACTS, { contacts });
  },
  initializeFollowedPages ({ commit }) {
    return new Promise((resolve) => {
      UserApi.getFollowedPages(pages => {
        commit(types.SET_FOLLOWED_PAGES, { pages });
        resolve(pages);
      });
    });
  },
  togglePageFollow ({ commit, state }, page) {
    PageApi.toggleFollowStatus(page.id, status => {
      if (status) {
        let pages = [...state.followedPages];
        pages.push(page);
        commit(types.SET_FOLLOWED_PAGES, { pages });
      } else {
        commit(types.SET_FOLLOWED_PAGES, {
          pages: state.followedPages.filter(p => p.id !== page.id)
        });
      }
    });
  },
  initializeAttendingEvents ({ commit }) {
    return new Promise(resolve => {
      UserApi.getAttendingEvents(events => {
        commit(types.SET_ATTENDING_EVENTS, { events });
        resolve(events);
      });
    });
  },
  toggleEventAttending ({ commit, state }, event) {
    EventApi.toggleAttendanceStatus(event.id, status => {
      event.attendeesCount += status ? 1 : -1;

      if (status) {
        let events = [...state.attendingEvents];
        events.push(event);
        commit(types.SET_ATTENDING_EVENTS, { events });
      } else {
        commit(types.SET_ATTENDING_EVENTS, {
          events: state.attendingEvents.filter(e => e.id !== event.id)
        });
      }
    });
  }
};

const mutations = {
  [types.SET_USER] (state, { user }) {
    state.user = user;
    state.authenticated = user != null;
  },

  [types.SET_NOTIFICATIONS] (state, { notifications }) {
    state.notifications = notifications;
  },

  [types.REMOVE_NOTIFICATION] (state, { notification }) {
    state.notifications = state.notifications.filter(n => {
      return n.id !== notification.id;
    });
  },

  [types.ADD_NOTIFICATION] (state, { notification }) {
    state.notifications.push(notification);
  },

  [types.LOADING_USER] (state, loading_state) {
    state.loadingUser = loading_state;
  },

  [types.SET_CONTACTS] (state, { contacts }) {
    state.contacts = contacts;
  },

  [types.SET_CONVERSATIONS] (state, { conversations }) {
    state.conversations = conversations;
  },

  [types.SET_FOLLOWED_PAGES] (state, { pages}) {
    state.followedPages = pages;
  },

  [types.SET_ATTENDING_EVENTS] (state, { events }) {
    state.attendingEvents = events;
  }
};

export default {
  state,
  getters,
  actions,
  mutations,
  watchers
};
