import PageApi from 'api/page.api';

export default class PageInjector {
  constructor (page_id) {
    this.page_id = page_id;
  }

  getPage (success) {
    if (this.page_id == null) {
      return {};
    }

    PageApi.getPage(this.page_id, success);
  }

  postPage (page, success) {
    PageApi.postPage(page, success);
  }

  putPage (page, success) {
    PageApi.putPage(page, success);
  }

  getEvents (page, size, success) {
    PageApi.getEvents(this.page_id, page, size, success);
  }

  getAdministrators (page, size, success) {
    PageApi.getAdministrators(this.page_id, page, size, success);
  }

  getPlaces (success) {
    PageApi.getPlaces(this.page_id, success);
  }

  publishPage (success) {
    PageApi.publishPage(this.page_id, success);
  }

  deactivatePage (success) {
    PageApi.deactivatePage(this.page_id, success);
  }

  getRequests (success) {
    PageApi.getRequests(this.page_id, success);
  }

  getServices (success) {
    PageApi.getServices(this.page_id, success);
  }

  postService (service, success) {
    PageApi.postService(this.page_id, service, success);
  }

  getFollowStatus (success) {
    PageApi.getFollowStatus(this.page_id, success);
  }

  getAdministrators (success) {
    PageApi.getAdministrators(this.page_id, success);
  }

  postAdministrator (administrator, success) {
    PageApi.postAdministrator(this.page_id, administrator, success);
  }

  putAdministrator (administrator_id, administrator, success) {
    PageApi.putAdministrator(administrator_id, administrator, success);
  }

  deleteAdministrator (administrator_id, success) {
    PageApi.deleteAdministrator(administrator_id, success);
  }
}
