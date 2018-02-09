<template>
  <transition name="fade">
    <div class="modal" role="dialog" v-show="show">
      <div class="modal-dialog" :class="{ 'modal-full' : full, 'modal-sm' : small }" v-click-outside="outside">
        <div class="modal-content">
          <div class="modal-header pattern-background">
            <button type="button" class="close" data-dismiss="modal" @click="close">
              <i class="material-icons">clear</i>
            </button>
            <h4 class="modal-title">
              <slot name="header">Header</slot>
            </h4>
          </div>
          <div class="modal-body">
            <slot name="body"></slot>
          </div>
          <!-- <div class="modal-footer">
            <slot name="footer"></slot>
          </div> -->
        </div>
      </div>
    </div>
  </transition>
</template>

<script>
export default {
  props: {
    show: {
      type: Boolean,
      default: false
    },
    question: Boolean,
    full: {
      type: Boolean,
      default: false
    },
    small: Boolean,
    accept: Function
  },
  watch: {
    show (newVal) {
      document.body.classList.toggle('noscroll', newVal);
    },
    $route: function () {
      this.close();
    }
  },
  methods: {
    close () {
      this.$emit('update:show', false);
    },
    outside () {
      // if (this.show) {
      //   this.close();
      // }
    }
  }
};
</script>

<style lang="less" scoped>
@import (reference) '~less/variables.less';
@import (reference) '~less/mixins.less';
.modal {
  display: block;
  background: rgba(0, 0, 0, 0.4);
  overflow-y: auto !important;
  max-height: 100%;

  padding: 0;
  text-align: center;
  z-index: 999;

  &:before {
    content: '';
    display: inline-block;
    height: 100%;
    vertical-align: middle;
    margin-right: -4px;
  }

  .modal-dialog {
    display: inline-block;
    text-align: left;
    vertical-align: middle;
    margin: 15px;
    box-shadow: 0 14px 28px rgba(0, 0, 0, 0.25), 0 10px 10px rgba(0, 0, 0, 0.22);

    .modal-content {
      -moz-box-shadow: none;
      -webkit-box-shadow: none;
      border-color: #DDDDDD;
      border-radius: 0;
      border: none;
      box-shadow: none;
      .modal-header {
        padding: 15px 30px;
        background-color: @color-primary;
        color: #fff;
        border-bottom: none;

        h4 {
          color: #fff;
        }

        .close {
          margin: 0;
          text-shadow: none;
          color: #fff;
          opacity: 1;
        }
      }
      .modal-body {
        padding: 30px;
        // box-shadow: inset 0px 6px 4px -4px rgba(0, 0, 0, 0.23)
      }
      .modal-footer {
        padding: 15px 30px;
      }
    }
  }
}

.modal-full {
  width: 98%;
}

.modal-content {
  .nav.nav-tabs {
    &+ {
      .tab-content {
        margin-bottom: 0px;
      }
    }
  }
  .panel-group {
    margin-bottom: 0px;
  }
  .panel {
    border-top: none;
  }
}

.modal-demo {
  background-color: #FFF;
  width: 600px;
  -webkit-border-radius: 4px;
  border-radius: 4px;
  -moz-border-radius: 4px;
  background-clip: padding-box;
  display: none;
  .close {
    position: absolute;
    top: 15px;
    right: 25px;
    color: #eeeeee;
  }
}

.custom-modal-title {
  padding: 15px 25px 15px 25px;
  line-height: 22px;
  font-size: 18px;
  background-color: @color-primary;
  color: #ffffff;
  text-align: left;
  margin: 0px;
}

.custom-modal-text {
  padding: 20px;
}

.custombox-modal-flash {
  .close {
    top: 20px;
    z-index: 9999;
  }
}

.custombox-modal-rotatedown {
  .close {
    top: 20px;
    z-index: 9999;
  }
}
</style>