import router from './router.js';

export default {
  user: {
    authenticated: window.localStorage.getItem('id_token') ? true : false
  },

  login(context, creds, redirect) {
    return new Promise((resolve, reject) => {
      context.$http.post('login', creds).then(response => {
        var user = response.body;
        window.localStorage.setItem('id_token', user.token)

        context.$store.commit('setUser', { user });

        this.user.authenticated = true
        resolve(response.body.token);

        if (redirect)
          router.push({ path: redirect })
      }, response => {
        reject();
        // context.fieldErrors = response.responseJSON.fieldErrors;
      })
    });
  },

  currentUser(context) {
    context.$http.get('api/user/me', { headers: this.getAuthHeader() }).then(resp => {
      context.user = resp.body
    }, error => {
      console.log(error)
    })
  },

  signup(context, creds, redirect) {
    return new Promise((resolve, reject) => {
      context.$http.post('register', creds).then(response => {
        var user = response.body;
        window.localStorage.setItem('id_token', user.token)

        context.$store.commit('setUser', { user });
        this.user.authenticated = true;

        resolve(response.body.token);
        if (redirect) {
          router.push({ path: redirect })
        }
      }, response => {
        context.fieldErrors = response.body.fieldErrors;
        reject(response);
      })
    });
  },

  logout(context, redirect) {
    window.localStorage.removeItem('id_token')
    this.user.authenticated = false
    router.push({ path: redirect })
  },

  checkAuth() {
    const jwt = window.localStorage.getItem('id_token')
    this.user.authenticated = jwt ? true : false
  },

  getAuthHeader() {
    return window.localStorage.getItem('id_token');
  }
}