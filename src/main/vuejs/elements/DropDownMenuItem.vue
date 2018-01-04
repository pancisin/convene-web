<template>
  <li :class="{ 'dropdown-header' : header, 'divider': divider }">
    <slot></slot>
  </li>
</template>

<script>
export default {
  name: 'dropdown-menu-item',
  props: {
    header: Boolean,
    divider: Boolean
  }
};
</script>

<style lang="less">
@import (reference) '~less/variables.less';
.dropdown-header {
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  font-size: 15px;
  text-transform: uppercase;
  font-weight: 600;
  padding: 10px 20px;
  color: @color-inverse;
  font-family: 'Roboto', sans-serif;
}
</style>
