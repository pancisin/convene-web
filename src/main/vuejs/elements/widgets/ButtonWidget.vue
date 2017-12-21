<template>
  <div>
    <a class="button-widget card-box">
      fsdfds
    </a>
  </div>
</template>

<script>
export default {
  name: 'button-widget'
};
</script>

<style lang="less">
@import (reference) '~less/variables.less';
.button-widget {
  background-color: #fff;
  display: block;
  transition: border-color 0.3s ease;

  &:hover {
    border-color: @color-primary;
  }
}
</style>
