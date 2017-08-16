import PageApi from 'api/page.api';

export default class PageInjector {
  constructor (page_id) {
    this.page_id = page_id;
  }

  getPage (auth, success) {
    if (this.page_id == null) {
      return {};
    }

    return PageApi.getPage(this.page_id, auth, success);
  }

  postPage (page, success) {
    return PageApi.postPage(page, success);
  }

  putPage (page, success) {
    return PageApi.putPage(page, success);
  }

  getEvents (page, size, success) {
    return PageApi.getEvents(this.page_id, page, size, success);
  }

  getAdministrators (page, size, success) {
    return PageApi.getAdministrators(this.page_id, page, size, success);
  }

  getPlaces (success) {
    return PageApi.getPlaces(this.page_id, success);
  }

  publishPage (success) {
    return PageApi.publishPage(this.page_id, success);
  }

  deactivatePage (success) {
    return PageApi.deactivatePage(this.page_id, success);
  }

  getRequests (success) {
    return PageApi.getRequests(this.page_id, success);
  }

  getServices (auth, success) {
    return PageApi.getServices(this.page_id, auth, success);
  }

  postService (service, success) {
    return PageApi.postService(this.page_id, service, success);
  }

  getFollowStatus (success) {
    return PageApi.getFollowStatus(this.page_id, success);
  }

  getAdministrators (success) {
    return PageApi.getAdministrators(this.page_id, success);
  }

  postAdministrator (administrator, success) {
    return PageApi.postAdministrator(this.page_id, administrator, success);
  }
}
