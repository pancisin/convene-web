<template>
  <div class="fileupload waves-effect">
    <img :src="source" class="img-thumbnail dropzone" style="width: 100%" />
    <input type="file" class="upload" @change="onImageChange">
  </div>
</template>

<script>
export default {
  name: 'image-upload',
  props: {
    value: {
      type: String,
      default () {
        return '';
      }
    },
    path: {
      type: String,
      default () {
        return '';
      }
    },
    media: {
      type: Object,
      default () {
        return {};
      }
    }
  },
  data () {
    return {
      reader: null,
    }
  },
  computed: {
    source: {
      get () {
        return this.value ? this.value : (this.media ? this.media.path : this.path);
      }
    }
  },
  created () {
    this.reader = new FileReader();
    this.reader.onload = (e) => {
      this.$emit('input', e.target.result);
    };
  },
  methods: {
    onImageChange (e) {
      var files = e.target.files || e.dataTransfer.files;

      if (!files.length) {
        return;
      }

      var file = files.item(0);
      this.reader.readAsDataURL(file);
    }
  }
};
</script>
