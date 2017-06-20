import PageApi from '../api/page.api.js'

export default class PageInjector {
  constructor(page_id) {
    this.page_id = page_id;
  }

  getPage(auth, success) {
    if (this.page_id == null)
      return new Object();

    return PageApi.getPage(this.page_id, auth, success);
  }

  postPage(page, success) {
    PageApi.postPage(page, success);
  }

  putPage(page, success) {
    PageApi.putPage(page, success);
  }

  getEvents(page, size, success) {
    return PageApi.getEvents(this.page_id, page, size, success);
  }

  getAdministrators(page, size, success) {
    return PageApi.getAdministrators(this.page_id, page, size, success);
  }

  getPlaces(success) {
    return PageApi.getPlaces(this.page_id, success);
  }

  publishPage(success) {
    return PageApi.publishPage(this.page_id, success);
  }

  deactivatePage(success) {
    return PageApi.deactivatePage(this.page_id, success);
  }
}