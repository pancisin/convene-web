import Vue from 'vue';

export default {

  /**
   * Get conferences
   * @param {*} page - paginator page property
   * @param {*} size - paginator size property
   * @param {*} auth - authenticated user
   * @param {*} success - success callback function
   */
  getConferences (page, size, auth, success) {
    var url = `${auth ? 'api' : 'public'}/conferences/${page}/${size}`;

    Vue.http.get(url).then(response => {
      success(response.body);
    });
  }
};
