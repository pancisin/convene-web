<template>
  <div class="fileupload waves-effect">
    <img :src="value" class="img-thumbnail dropzone" style="width: 100%" />
    <input type="file" class="upload" @change="onImageChange">
  </div>
</template>

<script>
export default {
  name: 'image-upload',
  props: ['value'],
  data() {
    return {

    }
  },
  methods: {
    onImageChange(e) {
      var self = this;

      var files = e.target.files || e.dataTransfer.files;
      if (!files.length)
        return;

      var file = files[0];

      var image = new Image();
      var reader = new FileReader();

      reader.onload = (e) => {
        this.$emit('input', e.target.result);
      };

      reader.readAsDataURL(file);
    },
  }
}
</script>

<style>

</style>
