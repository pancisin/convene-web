import Vue from 'vue'

export default {

  getConferences(page, size, auth, success) {

    var url = '';

    if (auth)
      url = ['api/conferences', page, size].join('/');
    else
      url = ['public/conferences', page, size].join('/');

    Vue.http.get(url).then(response => {
      success(response.body);
    })
  }
}