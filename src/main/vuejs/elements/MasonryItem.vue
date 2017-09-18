<template>
  <div class="masonry-item">
    <slot></slot>
  </div>
</template>

<script>
export default {
  name: 'masonry-item'
};
</script>

<style lang="less">
.masonry-item {
  display: inline-block;
  margin: 0 0 1.5em;
  width: 100%;
  box-sizing: border-box;
  -moz-box-sizing: border-box;
  -webkit-box-sizing: border-box;
  font-size: 12px;
  position: relative;

  img {
    width: 100%;
  }
}
</style>
