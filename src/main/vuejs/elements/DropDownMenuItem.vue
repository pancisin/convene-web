<template>
  <li :class="{ 'dropdown-header' : header, 'divider': divider }">
    <slot></slot>
  </li>
</template>

<script>
export default {
  name: 'drop-down-menu-item',
  props: {
    header: Boolean,
    divider: Boolean
  }
};
</script>
