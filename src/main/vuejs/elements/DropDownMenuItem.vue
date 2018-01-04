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
