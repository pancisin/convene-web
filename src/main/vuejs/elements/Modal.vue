<template>
  <transition name="fade-up">
    <div class="modal" v-click-outside="outside" role="dialog" v-show="show">
      <div class="modal-dialog modal-full">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" @click="close">×</button>
            <h4 class="modal-title"><slot name="header">Header</slot></h4>
          </div>
          <div class="modal-body">
            <slot name="body"></slot>
          </div>
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
      default: false,
    },
    question: Boolean,
  },
  methods: {
    accept() {
      this.$emit('accept');
    },
    close() {
      this.$emit('close');
    },
    outside() {
      if (show)
        this.$emit('close');
    }
  },
}
</script>

<style lang="less" scoped>
.modal {
  display: block;
  .modal-dialog {
    .modal-content {
      -moz-box-shadow: none;
      -webkit-box-shadow: none;
      border-color: #DDDDDD;
      border-radius: 2px;
      box-shadow: none;
      padding: 30px;
      .modal-header {
        border-bottom-width: 2px;
        margin: 0;
        padding: 0;
        padding-bottom: 15px;
      }
      .modal-body {
        padding: 20px 0;
      }
      .modal-footer {
        padding: 0;
        padding-top: 15px;
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
  background-color: #3bafda;
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