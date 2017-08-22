import router from './router.js';

import AuthApi from 'api/auth.api';

export default {
  user: {
    authenticated: window.localStorage.getItem('id_token')
  },

  login (context, creds, redirect) {
    return new Promise((resolve, reject) => {
      AuthApi.login(creds, user => {
        window.localStorage.setItem('id_token', user.token);

        context.$store.commit('SET_USER', { user });

        this.user.authenticated = true;
        resolve(user.token);

        if (redirect) {
          router.push({ path: redirect });
        }
      }, response => {
        reject(response.fieldErrors);
      });
    });
  },

  signup (context, creds, redirect) {
    return new Promise((resolve, reject) => {
      AuthApi.register(creds, user => {
        window.localStorage.setItem('id_token', user.token);

        context.$store.commit('SET_USER', { user });
        this.user.authenticated = true;

        resolve(user.token);
        if (redirect) {
          router.push({ path: redirect });
        }
      }, response => {
        context.fieldErrors = response.fieldErrors;
        reject(response.fieldErrors);
      });
    });
  },

  logout (context, redirect) {
    window.localStorage.removeItem('id_token');
    this.user.authenticated = false;
    context.$store.dispatch('clearUser');
    router.push({ path: redirect });
  },

  checkAuth () {
    const jwt = window.localStorage.getItem('id_token');
    this.user.authenticated = jwt;
  },

  getAuthHeader () {
    return window.localStorage.getItem('id_token');
  }
};
