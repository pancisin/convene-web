import Vue from 'vue';

const PAGE_MEMBER_API_URL = '/api/v1/page-member/';

export default {

  getPageMember (member_id, success) {
    Vue.http.get(`${PAGE_MEMBER_API_URL}/${member_id}`).then(response => {
      success(response.body);
    });
  },

  deletePageMember (member_id, success) {
    Vue.http.delete(`${PAGE_MEMBER_API_URL}/${member_id}`).then(response => {
      success(response.body);
    });
  }
};
