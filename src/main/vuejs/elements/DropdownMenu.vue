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
        const xoff = window.innerWidth - (rec.left + rec.width) - 30;
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

<style lang="less">
.dropdown-menu {
  padding: 4px 0;
  display: initial;
  border: 0;
  box-shadow: 0 2px 5px 0 rgba(0, 0, 0, 0.26);
  margin-top: 2px !important;
  > li {
    > a {
      padding: 6px 20px;
    }
  }

  .divider {
    margin: 0;
  }

  &.dropdown-menu-lg {
    width: 300px;
    .list-group {
      margin-bottom: 0px;
    }
    .list-group-item {
      border: none;
      padding: 10px 20px;
    }
    .media-heading {
      margin-bottom: 0px;
    }
    .media-body {
      p {
        color: #828282;
      }
    }
  }
}
</style>
