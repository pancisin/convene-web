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
  name: 'drop-down-menu',
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

<style lang="less">
.dropdown-menu {
  padding: 4px 0; 
  display: initial;
  border: 0;
  box-shadow: 0 2px 5px 0 rgba(0, 0, 0, 0.26);
  margin-top: 2px;
  >li {
    >a {
      padding: 6px 20px;
    }
  }

  .divider {
    margin: 0;
  }
}
</style>