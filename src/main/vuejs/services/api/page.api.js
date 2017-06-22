import Vue from 'vue';

export default {
  getPage(id, auth, success) {
    if (id == null || id == '') throw 'missing entity id';

    var url = `${auth ? 'api' : 'public'}/page/${id}`
    Vue.http.get(url).then(response => {
      success(response.body);
    })
  },
  putPage(page, success) {
    Vue.http.put('api/page/' + page.id, page).then(response => {
      success(response.body);
    });
  },
  deletePage(id, success) {
    if (id == null || id == '') throw 'missing entity id';

    Vue.http.delete('api/page/' + this.page.id).then(response => {
      success(response.body);
    })
  },
  publishPage(id, success) {
    Vue.http.patch('api/page/' + id + '/publish').then(response => {
      success(response.body);
    })
  },
  deactivatePage(id, success) {
    Vue.http.patch('api/page/' + id + '/deactivate').then(response => {
      success(response.body);
    })
  },
  getEvents(id, page, size, success) {
    if (id == null || id == '') throw 'missing entity id';

    var url = ['api/page', id, 'event', page, size].join('/');
    Vue.http.get(url).then(response => {
      success(response.body);
    })
  },
  getAllEvents(id, auth, success) {
    if (id == null || id == '') throw 'missing entity id';

    var url = "";

    if (auth)
      url = ['api/page', id, 'event'].join('/');
    else
      url = ['public/page', id, 'event'].join('/');

    Vue.http.get(url).then(response => {
      success(response.body);
    })
  },
  getServices(id, auth, success) {
    if (id == null || id == '') throw 'missing entity id';

    var url = "";
    if (auth)
      url = ['api/page', id, 'service'].join('/');
    else
      url = ['public/page', id, 'service'].join('/');

    Vue.http.get(url).then(response => {
      success(response.body);
    });
  },
  postService(id, service, success) {
    Vue.http.post('api/page/' + id + '/service', service).then(response => {
      success(response.body);
    });
  },
  getFollowStatus(id, success) {
    if (id == null || id == '') throw 'missing entity id';

    var url = ['api/page', id, 'follow-status'].join('/');
    Vue.http.get(url).then(response => {
      success(response.body);
    })
  },
  toggleFollowStatus(id, success) {
    if (id == null || id == '') throw 'missing entity id';

    var url = ['api/page', id, 'toggle-follow'].join('/');
    Vue.http.patch(url).then(response => {
      success(response.body);
    })
  },
  getPlaces(id, success) {
    var url = ['api/page', id, 'place'].join('/');
    Vue.http.get(url).then(response => {
      success(response.body);
    })
  },
  getRequests(id, success) {
    var url = ['api/page', id, 'requests'].join('/');
    Vue.http.get(url).then(response => {
      success(response.body);
    })
  },
}