import router from './router.js';

export default {
  user: {
    authenticated: window.localStorage.getItem('id_token') ? true : false
  },

  login(context, creds, redirect) {
    return new Promise((resolve, reject) => {
      context.$http.post('login', creds).then(response => {
        window.localStorage.setItem('id_token', response.body.token)

        this.user.authenticated = true
        resolve();

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
      context.$http.post('register', creds).then(resp => {
        window.localStorage.setItem('id_token', resp.body.token)

        this.user.authenticated = true;

        resolve();
        if (redirect) {
          router.push({ path: redirect })
        }
      }, response => {
        context.fieldErrors = response.responseJSON.fieldErrors;
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
    return 'Bearer ' + window.localStorage.getItem('id_token');
  }
}