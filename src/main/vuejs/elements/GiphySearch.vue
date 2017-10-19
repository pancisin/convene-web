<template>
  <div class="" v-loading="loading">
    <image-upload v-if="upload" v-model="selected" class="m-b-15" />
    <img :src="selected" style="width: 100%" class="img-thumbnail m-b-15" v-else>

    <label>Search Giphy.com</label>
    <input class="form-control m-b-15" type="text" v-model="keyword" @keyup="search($event)" />

    <div class="row">
      <div class="col-md-6" v-for="(gif, index) in gifs" :key="index">
        <img :src="gif.thumbnail" style="width: 100%" class="img-thumbnail" @click="select(gif)">
      </div>
    </div>
  </div>
</template>

<script>
import debounce from 'debounce';
import GiphyApi from 'api/ext/giphy.api';
import ImageUpload from './ImageUpload';

export default {
  name: 'giphy-search',
  props: {
    value: [Object, String],
    upload: Boolean
  },
  data () {
    return {
      keyword: '',
      gifs: [],
      loading: false,
      selected: null
    };
  },
  components: {
    ImageUpload
  },
  created () {
    this.initialize(this.value);
  },
  watch: {
    value: 'initialize'
  },
  methods: {
    initialize (value) {
      if (value instanceof Object) {
        this.selected = value.path;
      } else {
        this.selected = value;
      }
    },
    search: debounce(function (e) {
      this.collapsed = false;
      this.loading = true;

      GiphyApi.search(e.target.value, 0, 10, gifs => {
        this.gifs = gifs;
        this.loading = false;
      });
    }, 500),
    select (gif) {
      this.selected = gif.thumbnail;
      this.$emit('input', gif.path);
    }
  }
};
</script>
