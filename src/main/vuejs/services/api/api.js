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
  },

  /**
   * Get all users for system maintanance
   * @param {*} page - paginator page attribute
   * @param {*} size - paginator size attribute
   * @param {*} success - success callback function
   */
  getUsers (page, size, success) {
    Vue.http.get(`api/user`, {
      params: {
        page, size
      }
    }).then(response => {
      success(response.body);
    })
  }
};
