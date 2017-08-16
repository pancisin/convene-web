import Vue from 'vue';
const PAGE_API_URL = 'api/page';
export default {
  getPage (id, auth, success) {
    if (id == null || id === '') throw new Error('missing entity id');

    var url = `${auth ? 'api' : 'public'}/page/${id}`;
    Vue.http.get(url).then(response => {
      success(response.body);
    });
  },
  putPage (page, success) {
    Vue.http.put(`${PAGE_API_URL}/${page.id}`, page).then(response => {
      success(response.body);
    });
  },
  deletePage (id, success) {
    if (id == null || id === '') throw new Error('missing entity id');

    Vue.http.delete(`${PAGE_API_URL}/${id}`).then(response => {
      success(response.body);
    });
  },
  publishPage (id, success) {
    Vue.http.patch(`${PAGE_API_URL}/${id}/publish`).then(response => {
      success(response.body);
    });
  },
  deactivatePage (id, success) {
    Vue.http.patch(`${PAGE_API_URL}/${id}/deactivate`).then(response => {
      success(response.body);
    });
  },
  getEvents (id, page, size, success) {
    if (id == null || id === '') throw new Error('missing entity id');

    Vue.http.get(`${PAGE_API_URL}/${id}/event/${page}/${size}`).then(response => {
      success(response.body);
    });
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
    });
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
    });
  },
  postService (id, service, success) {
    Vue.http.post(`${PAGE_API_URL}/${id}/service`, service).then(response => {
      success(response.body);
    });
  },
  getFollowStatus (id, success) {
    if (id == null || id === '') throw new Error('missing entity id');

    Vue.http.get(`${PAGE_API_URL}/${id}/follow-status`).then(response => {
      success(response.body);
    });
  },
  toggleFollowStatus (id, success) {
    if (id == null || id === '') throw new Error('missing entity id');

    Vue.http.patch(`${PAGE_API_URL}/${id}/toggle-follow`).then(response => {
      success(response.body);
    });
  },
  getPlaces (id, success) {
    Vue.http.get(`${PAGE_API_URL}/${id}/place`).then(response => {
      success(response.body);
    });
  },
  getRequests (id, success) {
    Vue.http.get(`${PAGE_API_URL}/${id}/requests`).then(response => {
      success(response.body);
    });
  },
  getAdministrators (id, success) {
    Vue.http.get(`${PAGE_API_URL}/${id}/administrator`).then(response => {
      success(response.body);
    });
  },
  postAdministrator (id, administrator, success) {
    Vue.http.post(`${PAGE_API_URL}/${id}/administrator`, { id: administrator.id }).then(response => {
      success(response.body);
    });
  },
};
