import Vue from 'vue';
const PROGRAMME_API_URL = 'api/programme';
export default {
  getProgramme (id, success) {
    Vue.http.get(`${PROGRAMME_API_URL}/${id}`).then(response => {
      success(response.body);
    });
  },
  deleteProgramme (id, success) {
    Vue.http.delete(`${PROGRAMME_API_URL}/${id}`).then(response => {
      success(response.body);
    });
  },
  putProgramme (id, programme, success) {
    Vue.http.put(`${PROGRAMME_API_URL}/${id}`, programme).then(response => {
      success(response.body);
    });
  }
};
