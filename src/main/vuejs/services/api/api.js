import Vue from 'vue';

export default {

  /**
   * Get conferences
   * @param {*} page - paginator page property
   * @param {*} size - paginator size property
   * @param {*} success - success callback function
   */
  getConferences (page, size, success) {
    Vue.http.get(`api/conferences/${page}/${size}`).then(response => {
      success(response.body);
    });
  }
};
