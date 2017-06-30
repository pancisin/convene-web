import Vue from 'vue';

export default {
  getConferences (page, size, auth, success) {
    var url = `${auth ? 'api' : 'public'}/conferences/${page}/${size}`;

    Vue.http.get(url).then(response => {
      success(response.body);
    });
  }
};
