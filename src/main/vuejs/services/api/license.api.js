import Vue from 'vue';

const LICENSE_API_URL = 'api/license';

export default {

  /**
   * Get license information by license id.
   * @param {*} id - license id
   * @param {*} success - success callback function
   */
  getLicense (id, success) {
    Vue.http.get(`${LICENSE_API_URL}/${id}`).then(response => {
      success(response.body);
    });
  },

  /**
   * Make payment by credit card.
   * @param {*} id - license id
   * @param {*} card_details - user card payment details
   * @param {*} success - success callback function
   * @param {*} error - error callback function
   */
  postPayment (id, card_details, success, error) {
    Vue.http.post(`${LICENSE_API_URL}/${id}/payment`, card_details).then(response => {
      if (response.body.successful === true) {
        success(response.body);
      }
    }, response => {
      error(response.bodyText);
    });
  }
};
