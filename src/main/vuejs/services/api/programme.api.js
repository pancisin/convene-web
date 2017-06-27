import Vue from 'vue'
export default {
  getProgramme(id, success) {
    Vue.http.get(`api/programme/${id}`).then(response => {
      success(response.body);
    })
  },
  deleteProgramme(id, success) {
    Vue.http.delete(`api/programme/${id}`).then(response => {
      success(response.body);
    })
  },
  putProgramme(id, programme, success) {
    Vue.http.put(`api/programme/${id}`, programme).then(response => {
      success(response.body);
    })
  }
}