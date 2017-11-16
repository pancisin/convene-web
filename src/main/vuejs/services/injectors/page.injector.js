import PageApi from 'api/page.api';

export default class PageInjector {

  get base_route () {
    return {
      name: 'page',
      props: {
        id: this.page_id
      }
    };
  }

  get parent_type () {
    return 'page';
  }

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

  getEvents (page, size, success, params) {
    PageApi.getEvents(this.page_id, page, size, success, params);
  }

  getAdministrators (page, size, success) {
    PageApi.getAdministrators(this.page_id, page, size, success);
  }

  getPlaces (success) {
    PageApi.getPlaces(this.page_id, success);
  }

  postPlace (place, success) {
    PageApi.postPlace(this.page_id, place, success);
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

  getActivities (success) {
    PageApi.getActivities(this.page_id, success);
  }

  getWidgets (success) {
    PageApi.getWidgets(this.page_id, success);
  }

  putWidgets (widgets, success) {
    PageApi.putWidgets(this.page_id, widgets, success);
  }

  getGallery (success) {
    PageApi.getGallery(this.page_id, success);
  }

  postGalleryItem (gallery_item, success, progress_func) {
    PageApi.postGalleryItem(this.page_id, gallery_item, success, progress_func);
  }

  getBots (success) {
    PageApi.getBots(this.page_id, success);
  }

  postBot (bot, success) {
    PageApi.postBot(this.page_id, bot, success);
  }
}
