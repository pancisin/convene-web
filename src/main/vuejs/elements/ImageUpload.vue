<template>
  <div class="fileupload waves-effect">
    <img :src="source" class="img-thumbnail dropzone" style="width: 100%" />
    <input type="file" class="upload" @change="onImageChange">
  </div>
</template>

<script>
export default {
  name: 'image-upload',
  props: ['value', 'path', 'media'],
  computed: {
    source () {
      if (this.value != null) {
        return this.value;
      } else {
        return this.media != null ? this.media.path : this.path;
      }
    }
  },
  methods: {
    onImageChange (e) {
      var files = e.target.files || e.dataTransfer.files;
      if (!files.length) {
        return;
      }

      var file = files[0];
      var reader = new FileReader();

      reader.onload = (e) => {
        this.$emit('input', e.target.result);
      };

      reader.readAsDataURL(file);
    }
  }
};
</script>

<style>

</style>
