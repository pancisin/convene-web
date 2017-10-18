<template>
  <li class="dropdown"
    v-click-outside="closeDropDown">
    <a @click="display = !display"
      class="dropdown-toggle waves-effect waves-light">
      <slot name="button"></slot>
    </a>

    <transition name="fade-up">
      <ul class="dropdown-menu"
        :class="{ 'dropdown-menu-lg' : large }"
        v-show="display">
        <slot></slot>
      </ul>
    </transition>
  </li>
</template>

<script>
export default {
  name: 'dropdown-menu',
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
