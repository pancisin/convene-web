<template>
  <transition name="zoom-in">
    <div v-if="visible" class="context-menu" :style="style" tabindex="-1" @contextmenu.capture.prevent @blur="close" @click="close" v-click-outside="close">
      <slot :data="data"></slot>
    </div>
  </transition>
</template>

<script>
export default {
  name: 'context-menu',
  data() {
    return {
      x: null,
      y: null,
      visible: false,
      data: {},
    }
  },
  computed: {
    style() {
      return this.isVisible ? {
        top: this.y - document.body.scrollTop + 'px',
        left: this.x + 'px'
      } : {}
    },
    isVisible() {
      return this.x !== null && this.y !== null
    }
  },
  methods: {
    open(evt, data) {
      this.data = data;
      this.visible = true;
      this.x = evt.pageX || evt.clientX
      this.y = evt.pageY || evt.clientY
      this.$nextTick(() => this.$el.focus())
    },
    close(evt) {
      this.visible = false;
      this.data = null;
      // this.x = null
      // this.y = null
      // this.userData = null
    }
  }
}
</script>

<style lang="less">
.context-menu {
  background: #fff;
  border: 1px solid #ccc;
  position: fixed;
  z-index: 999;
  box-shadow: 0 0 10px 3px rgba(128, 128, 128, 0.19);
  font-size: 12px;

  ul {
    list-style-type: none;
    margin: 0;
    padding: 0;

    li {
      a {
        padding: 10px 20px;
        display: block;
        color: #000;

        &:hover {
          background-color: #eee;
        }
      }

      &.separator {
        border-top: 1px solid #ccc;
      }
    }
  }

  &:focus {
    outline: none;
  }
}
</style>
