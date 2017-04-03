import router from './router.js';

export default {
  user: {
    authenticated: window.localStorage.getItem('id_token') ? true : false
  },

  login(context, creds, redirect) {
    context.$http.post('login', creds).then(response => {
      window.localStorage.setItem('id_token', response.body)

      this.user.authenticated = true

      if (redirect) {
        router.push({ path: redirect })
      }
    }, response => {
      context.fieldErrors = response.responseJSON.fieldErrors;
    })
  },

  currentUser(context) {
    context.$http.get(CURRENT_USER_URL, { headers: this.getAuthHeader() }).then(resp => {
      context.user = resp.body.user
    }, error => {
      console.log(error)
    })
  },

  signup(context, creds, redirect) {
    context.$http.post(REGISTRATION_URL, creds).then(resp => {
      window.localStorage.setItem('id_token', resp.body.jwt)

      this.user.authenticated = true;

      if (redirect) {
        router.push({ path: redirect })
      }
    }, resp => {
      console.log(resp.body.errors)
      context.errors = resp.body.errors
    })
  },

  logout(context, options) {
    context.$http.delete(SESSION_URL, options).then(data => {
      window.localStorage.removeItem('id_token')
      this.user.authenticated = false
      router.push({ path: '/login' })
    }, error => {
      console.log(error.message)
    })
  },

  checkAuth() {
    const jwt = window.localStorage.getItem('id_token')
    this.user.authenticated = jwt ? true : false
  },

  getAuthHeader() {
    return {
      'Authorization': window.localStorage.getItem('id_token')
    }
  }
}