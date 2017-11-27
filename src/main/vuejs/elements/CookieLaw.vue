<template>
  <transition name="fade">
    <div class="cookie-law" v-if="!collapsed">
      <div class="container">
        <div class="cookie-law-content">
          <slot name="message">{{ message }}</slot>
          <a class="btn btn-primary" @click="accept">{{ buttonText }}</a>
        </div>
      </div>
    </div>
  </transition>
</template>

<script>
export default {
  props: {
    message: {
      type: String,
      default () {
        return 'This website uses cookies to ensure you get the best experience on our website.';
      }
    },
    buttonText: {
      type: String,
      default () {
        return 'Got it!';
      }
    }
  },
  data () {
    return {
      collapsed: false
    };
  },
  created () {
    this.collapsed = localStorage.getItem('cookie:accepted');
  },
  methods: {
    accept () {
      localStorage.setItem('cookie:accepted', true);
      this.collapsed = true;
    }
  }
};
</script>

<style lang="less">
@import (reference) '~less/variables.less';
.cookie-law {
  // position: fixed;
  // overflow: hidden;
  // box-sizing: border-box;
  // z-index: 9999;
  
  bottom: 0;
  padding: 15px 0px;

  background-color: @color-inverse;
  color: #98a6ad;

  border-top: 1px solid rgba(0, 0, 0, 0.1);

  .cookie-law-content {
    display: flex;
    justify-content: space-between;
    align-items: baseline;
    flex-direction: row;
  }
}
</style>
