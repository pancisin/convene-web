<template>
  <li class="dropdown"
    :class="{ 'open' : display }"
    v-click-outside="closeDropDown">
    <a @click="display = !display"
      class="dropdown-toggle waves-effect waves-light">
      <slot name="button"></slot>
    </a>
    <ul class="dropdown-menu"
      :class="{ 'dropdown-menu-lg' : large }">
      <slot></slot>
    </ul>
  </li>
</template>

<script>
export default {
  props: {
    large: Boolean
  },
  data () {
    return {
      display: false
    };
  },
  methods: {
    closeDropDown (e) {
      if (this.display) {
        this.display = false;
      }
    }
  }
};
</script>