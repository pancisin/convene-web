<template>
  <div>
    <div class="row m-b-20">
      <form class="form" @submit.prevent="submit">
        <div class="col-md-4 col-md-offset-2">
          <image-upload v-model="galleryItem.data"></image-upload>

        </div>
        <div class="col-md-4">
          <div class="form-group">
            <label>Title</label>
            <input type="text" class="form-control" v-model="galleryItem.title" />
          </div>
          <div class="form-group">
            <label>Description</label>
            <textarea class="form-control" v-model="galleryItem.description" />
          </div>
          <button type="submit" class="btn btn-success btn-block" @click="submit">Submit</button>
        </div>
      </form>
    </div>

    <div class="gallery-masonry">
      <div class="gallery-item" v-for="item in gallery" :key="item.id">
        <img :src="item.path" class="img img-thumbnail">
        <h5 v-show="item.title">{{ item.title }}</h5>
        <p v-show="item.description">{{ item.description }}</p>
      </div>
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
      galleryItem: {}
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

<style lang="less">
.gallery-masonry {
  padding: 0;
  -moz-column-gap: 1.5em;
  -webkit-column-gap: 1.5em;
  column-gap: 1.5em;

  .gallery-item {
    display: inline-block;
    margin: 0 0 1.5em;
    width: 100%;
    box-sizing: border-box;
    -moz-box-sizing: border-box;
    -webkit-box-sizing: border-box;
    font-size: 12px;

    .img-thumbnail {
      box-shadow: 3px 3px 10px 0px rgba(111, 110, 110, 0.3);
    }
  }
}

@media only screen and (min-width: 700px) {
  .gallery-masonry {
    -moz-column-count: 2;
    -webkit-column-count: 2;
    column-count: 2;
  }
}

@media only screen and (min-width: 900px) {
  .gallery-masonry {
    -moz-column-count: 3;
    -webkit-column-count: 3;
    column-count: 3;
  }
}

@media only screen and (min-width: 1100px) {
  .gallery-masonry {
    -moz-column-count: 3;
    -webkit-column-count: 3;
    column-count: 3;
  }
}
</style>
