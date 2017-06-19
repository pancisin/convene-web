import Vue from 'vue'

export default {

  deletePlace(id, success) {
    Vue.http.delete('api/place/' + id).then(response => {
      success(response.body);
    })
  }
}