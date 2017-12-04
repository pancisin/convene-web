<template>
  <li class="dropdown"
    v-click-outside="closeDropDown">
    <a @click="open"
      class="dropdown-toggle waves-effect waves-light">
      <slot name="button"></slot>
    </a>

    <transition name="fade">
      <ul class="dropdown-menu"
        :class="{ 'dropdown-menu-lg' : large }"
        v-show="display"
        :style="style">
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
      display: false,
      style: {}
    };
  },
  methods: {
    open () {
      this.display = true;
      const dropdown = this.$el.getElementsByClassName('dropdown-menu')[0];

      this.style = {
        left: 0,
        top: 0
      };

      this.$nextTick(() => {
        const rec = dropdown.getBoundingClientRect();
        const xoff = window.innerWidth - (rec.left + rec.width);
        this.style = {
          left: xoff < 0 ? `${xoff}px` : 0
        };
      });
    },
    closeDropDown (e) {
      if (this.display) {
        this.display = false;
      }
    }
  }
};
</script>
