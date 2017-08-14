import router from './router.js';

export default {
  user: {
    authenticated: window.localStorage.getItem('id_token')
  },

  login (context, creds, redirect) {
    return new Promise((resolve, reject) => {
      context.$http.post('login', creds).then(response => {
        var user = response.body;
        window.localStorage.setItem('id_token', user.token);

        context.$store.commit('SET_USER', { user });

        this.user.authenticated = true;
        resolve(response.body.token);

        if (redirect) {
          router.push({ path: redirect });
        }
      }, response => {
        reject();
        // context.fieldErrors = response.responseJSON.fieldErrors;
      });
    });
  },

  signup (context, creds, redirect) {
    return new Promise((resolve, reject) => {
      context.$http.post('register', creds).then(response => {
        var user = response.body;
        window.localStorage.setItem('id_token', user.token);

        context.$store.commit('SET_USER', { user });
        this.user.authenticated = true;

        resolve(response.body.token);
        if (redirect) {
          router.push({ path: redirect });
        }
      }, response => {
        context.fieldErrors = response.body.fieldErrors;
        reject(response);
      });
    });
  },

  logout (context, redirect) {
    window.localStorage.removeItem('id_token');
    this.user.authenticated = false;
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
