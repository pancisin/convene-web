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

    &.img-thumbnail {
      box-shadow: 3px 3px 10px 0px rgba(111, 110, 110, 0.3);
      padding: 0;
    }
  }
}
</style>
