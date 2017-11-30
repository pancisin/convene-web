<template>
  <transition name="fade">
    <div class="modal" role="dialog" v-show="show">
      <div class="modal-dialog" :class="{ 'modal-full' : full }" v-click-outside="outside">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" @click="close"><i class="fa fa-times"></i></button>
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
    accept: Function
  },
  watch: {
    show (newVal) {
      document.body.classList.toggle('noscroll', newVal);
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

        h4 {
          color: #fff;
        }

        .close {
          margin: 0;
          text-shadow: none;
        }
      }
      .modal-body {
        padding: 30px;
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