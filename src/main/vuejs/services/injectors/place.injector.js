import PlaceApi from 'api/place.api';

export default class PlaceInjector {

  constructor(place_id) {
    this.place_id = place_id;
  }

  getPlace(success) {
    PlaceApi.getPlace(this.place_id, success);
  }

  deletePlace(success) {
    PlaceApi.deletePlace(this.place_id, success);
  }

  putPlace(place, success) {
    PlaceApi.putPlace(this.place_id, place, success);
  }

  patchVenue(venue_data, success) {
    PlaceApi.patchVenue(this.place_id, venue_data, success);
  }

  getVenue(success) {
    PlaceApi.getVenue(this.place_id, success);
  }

  getGallery(success) {
    PlaceApi.getGallery(this.place_id, success);
  }

  postGalleryItem(gallery_item, success) {
    PlaceApi.postGalleryItem(this.place_id, gallery_item, success);
  }
}