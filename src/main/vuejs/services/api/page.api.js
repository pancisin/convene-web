import Vue from 'vue';
const PAGE_API_URL = 'api/page';

function errorHandler(response) {
  console.error(response);
}

export default {
  getPage (id, auth, success) {
    if (id == null || id === '') throw new Error('missing entity id');

    var url = `${auth ? 'api' : 'public'}/page/${id}`;
    Vue.http.get(url).then(response => {
      success(response.body);
    }, errorHandler);
  },
  putPage (page, success) {
    Vue.http.put(`${PAGE_API_URL}/${page.id}`, page).then(response => {
      success(response.body);
    }, errorHandler);
  },
  deletePage (id, success) {
    if (id == null || id === '') throw new Error('missing entity id');

    Vue.http.delete(`${PAGE_API_URL}/${id}`).then(response => {
      success(response.body);
    }, errorHandler);
  },
  publishPage (id, success) {
    Vue.http.patch(`${PAGE_API_URL}/${id}/publish`).then(response => {
      success(response.body);
    }, errorHandler);
  },
  deactivatePage (id, success) {
    Vue.http.patch(`${PAGE_API_URL}/${id}/deactivate`).then(response => {
      success(response.body);
    }, errorHandler);
  },
  getEvents (id, page, size, success) {
    if (id == null || id === '') throw new Error('missing entity id');

    Vue.http.get(`${PAGE_API_URL}/${id}/event/${page}/${size}`).then(response => {
      success(response.body);
    }, errorHandler);
  },
  getAllEvents (id, auth, success) {
    if (id == null || id === '') throw new Error('missing entity id');

    var url = '';

    if (auth) {
      url = ['api/page', id, 'event'].join('/');
    } else {
      url = ['public/page', id, 'event'].join('/');
    }

    Vue.http.get(url).then(response => {
      success(response.body);
    }, errorHandler);
  },
  getServices (id, auth, success) {
    if (id == null || id === '') throw new Error('missing entity id');

    var url = '';
    if (auth) {
      url = ['api/page', id, 'service'].join('/');
    } else {
      url = ['public/page', id, 'service'].join('/');
    }

    Vue.http.get(url).then(response => {
      success(response.body);
    }, errorHandler);
  },
  postService (id, service, success) {
    Vue.http.post(`${PAGE_API_URL}/${id}/service`, service).then(response => {
      success(response.body);
    }, errorHandler);
  },
  getFollowStatus (id, success) {
    if (id == null || id === '') throw new Error('missing entity id');

    Vue.http.get(`${PAGE_API_URL}/${id}/follow-status`).then(response => {
      success(response.body);
    }, errorHandler);
  },
  toggleFollowStatus (id, success) {
    if (id == null || id === '') throw new Error('missing entity id');

    Vue.http.patch(`${PAGE_API_URL}/${id}/toggle-follow`).then(response => {
      success(response.body);
    }, errorHandler);
  },
  getPlaces (id, success) {
    Vue.http.get(`${PAGE_API_URL}/${id}/place`).then(response => {
      success(response.body);
    }, errorHandler);
  },
  getRequests (id, success) {
    Vue.http.get(`${PAGE_API_URL}/${id}/requests`).then(response => {
      success(response.body);
    }, errorHandler);
  },
  getAdministrators (id, success) {
    Vue.http.get(`${PAGE_API_URL}/${id}/administrator`).then(response => {
      success(response.body);
    }, errorHandler);
  },
  postAdministrator (id, administrator, success) {
    Vue.http.post(`${PAGE_API_URL}/${id}/administrator`, { id: administrator.id }).then(response => {
      success(response.body);
    }, errorHandler);
  },
};
