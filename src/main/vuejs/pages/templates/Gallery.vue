<template>
  <div>
    <div class="row">
      <div class="col-md-8 col-md-offset-2">
        <panel type="default" v-loading="uploading">
          <span slot="title">Upload image</span>
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
                <button type="submit" class="btn btn-primary btn-block">
                  <i class="fa fa-paper-plane-o m-r-5"></i> Submit</button>
              </div>
            </form>
          </div>

          <b>~ {{ usedStorage | bytes }} / {{ limit | bytes }} used</b>
          <div class="progress progress-striped">
            <div class="bar progress-bar progress-bar-primary" :style="progressBarStyle"></div>
          </div>
        </panel>
      </div>
    </div>

    <masonry v-loading="loading" :columns="columns">
      <masonry-item v-for="item in gallery" :key="item.id" class="gallery-item" @contextmenu.prevent="$refs.menu.open($event, item)">
        <img :src="item.path" class="img img-thumbnail">
        <h5 v-show="item.title">{{ item.title }}
          <sup class="pull-right label label-primary">({{ item.size | bytes }})</sup>
        </h5>

        <p v-show="item.description">{{ item.description }}</p>

        <div class="controls-wrapper">
          <div class="controls">
            <a class="btn btn-link">
              <i class="fa fa-pencil"></i>
            </a>
            <a class="btn btn-outline pull-right" @click="deleteImage(item.id)">
              <i class="fa fa-trash-o text-danger"></i>
            </a>
          </div>
        </div>
      </masonry-item>
    </masonry>

    <context-menu ref="menu">
      <template scope="props">
        <ul>
          <li :class="{ 'disabled' : !editable }">
            <a>
              Edit
            </a>
          </li>
          <li class="separator"></li>
          <li :class="{ 'disabled' : !editable }">
            <a @click="deleteImage(props.data.id)">
              Delete
            </a>
          </li>
        </ul>
      </template>
    </context-menu>

  </div>
</template>

<script>
import { ImageUpload, Masonry, MasonryItem } from 'elements';
import MediaApi from 'api/media.api';
import { bytes } from 'filters';

export default {
  name: 'event-gallery',
  inject: ['provider'],
  props: {
    columns: {
      type: Number,
      default () {
        return 3;
      }
    },
    editable: {
      type: Boolean,
      default () {
        return true;
      }
    },
    limit: {
      type: Number,
      default () {
        return 10000000;
      }
    }
  },
  data () {
    return {
      gallery: [],
      galleryItem: {},
      loading: false,
      uploading: false
    };
  },
  components: {
    ImageUpload, Masonry, MasonryItem
  },
  created () {
    this.getGallery();
  },
  computed: {
    api () {
      return this.provider.api;
    },
    usedStorage () {
      if (this.gallery !== null && this.gallery.length > 0) {
        return this.gallery.reduce((ac, current) => {
          return ac + current.size;
        }, 0);
      } else return 0;
    },
    progressBarStyle () {
      return {
        width: `${this.usedStorage / this.limit * 100}%`
      };
    }
  },
  watch: {
    'api': 'getGallery'
  },
  filters: {
    bytes
  },
  methods: {
    getGallery () {
      this.loading = true;
      this.api.getGallery(gallery => {
        this.gallery = gallery;
        this.sortGallery();
        this.loading = false;
      });
    },
    submit () {
      this.$validator.validateAll().then(valid => {
        if (valid) {
          this.uploading = true;
          this.api.postGalleryItem(this.galleryItem, gallery_item => {
            this.gallery.push(gallery_item);
            this.sortGallery();
            this.galleryItem = {};
            this.uploading = false;
          }, percentage => {
            this.uploading = percentage;
          });
        }
      });
    },
    deleteImage (uuid) {
      this.$prompt('notification.media.delete_prompt', () => {
        MediaApi.deleteMedia(uuid, gallery_item => {
          this.gallery = this.gallery.filter(g => {
            return g.id !== uuid;
          });
        });
      });
    },
    sortGallery () {
      this.gallery.sort((a, b) => {
        if (a.created === b.created) {
          return 0;
        }

        return a.created < b.created ? 1 : -1;
      });
    }
  }
};
</script>

<style lang="less">
.gallery-item {
  &:hover .controls-wrapper .controls {
    opacity: 1;
    transform: translateY(0);
  }

  .controls-wrapper {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    overflow: hidden;
    bottom: 0;
    z-index: 1;

    border-top-left-radius: 4px;
    border-top-right-radius: 4px;

    .controls {
      transform: translateY(-50%);
      background-color: #fff;
      opacity: 0;
      transition: all .3s ease;
      border-bottom: 1px solid #eee;
      box-shadow: 3px 3px 10px 0px rgba(111, 110, 110, 0.3);

      a {
        &.btn {
          border-radius: 0;
        }

        &:hover {
          background-color: #eee;
        }
      }
    }
  }
}
</style>
