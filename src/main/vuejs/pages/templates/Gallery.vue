<template>
  <div>
    <div class="row">
      <div class="col-md-8 col-md-offset-2">
        <panel type="default">
          <span slot="title">Add image</span>
          <div class="row m-b-20">
            <form class="form" @submit.prevent="submit">
              <div class="col-md-6">
                <image-upload v-model="galleryItem.data"></image-upload>

              </div>
              <div class="col-md-6">
                <div class="form-group" :class="{ 'has-error' : errors.has('title') }">
                  <label>Title</label>
                  <input type="text" class="form-control" v-model="galleryItem.title" name="title" v-validate data-vv-rules="required" />
                  <span class="text-danger" v-if="errors.has('title')">{{ errors.first('title') }}</span>
                </div>
                <div class="form-group" :class="{ 'has-error' : errors.has('description') }">
                  <label>Description</label>
                  <textarea rows="3" class="form-control" v-model="galleryItem.description" name="description" v-validate data-vv-rules="min:3|required" />
                  <span class="text-danger" v-if="errors.has('description')">{{ errors.first('description') }}</span>
                </div>
                <button type="submit" class="btn btn-primary btn-block" @click="submit">
                  <i class="fa fa-paper-plane-o m-r-5"></i> Submit</button>
              </div>
            </form>
          </div>
        </panel>
      </div>
    </div>

    <div class="gallery-masonry" v-loading="loading">
      <div class="gallery-item" v-for="item in gallery" :key="item.id">
        <img :src="item.path" class="img img-thumbnail">
        <h5 v-show="item.title">{{ item.title }}</h5>
        <p v-show="item.description">{{ item.description }}</p>
      </div>
    </div>

  </div>
</template>

<script>
import { ImageUpload } from 'elements';

export default {
  name: 'event-gallery',
  props: ['editable'],
  inject: ['provider'],
  data () {
    return {
      gallery: [],
      galleryItem: {},
      loading: false
    }
  },
  components: {
    ImageUpload
  },
  created () {
    this.getGallery();
  },
  computed: {
    api () {
      return this.provider.api;
    }
  },
  watch: {
    'api': 'getGallery'
  },
  methods: {
    getGallery () {
      this.loading = true;
      this.api.getGallery(gallery => {
        this.gallery = gallery;
        this.gallery.sort((a, b) => {
          if (a.created === b.created)
            return 0;

          return a.created < b.created ? 1 : -1;
        })
        this.loading = false;
      });
    },
    submit () {
      this.$validator.validateAll().then(valid => {
        if (valid) {
          this.api.postGalleryItem(this.galleryItem, gallery => {
            this.gallery = gallery;
            this.galleryItem = {};
          });
        }
      });
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
