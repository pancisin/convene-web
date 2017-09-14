<template>
  <div class="row">
    <div class="col-md-4" v-for="item in gallery" :key="item.id">
      <img :src="item.path" class="img img-thumbnail">
    </div>

    <div class="col-md-4">
      <image-upload v-model="galleryItem"></image-upload>
        <button class="btn btn-default btn-block" @click="submit">Submit</button>
    </div>
  </div>
</template>

<script>
import PlaceApi from 'api/place.api';
import { ImageUpload } from 'elements';

export default {
  name: 'event-gallery',
  props: ['place'],
  data () {
    return {
      gallery: [],
      galleryItem: null
    }
  },
  components: {
    ImageUpload
  },
  created () {
    if (this.place != null) {
      this.getGallery();
    }
  },
  watch: {
    'place': 'getGallery'
  },
  methods: {
    getGallery () {
      PlaceApi.getGallery(this.place.id, gallery => {
        this.gallery = gallery;
      })
    },
    submit () {
      PlaceApi.postGalleryItem(this.place.id, this.galleryItem, gallery => {
        this.gallery = gallery;
      })
    }
  }
};
</script>

<style>

</style>
