<template>
  <div class="toast" :class="notification.type ? 'toast-' + notification.type : 'secondary'">
    <button type="button" @click="triggerClose" class="close">
      <i class="material-icons">close</i>
    </button>
    <!-- <strong v-if="notification.code">{{ $t(`${notification.code}.title`, { subject: notification.subject }) }}</strong> -->
    <p>
      {{ $t(notification.code, { object: notification.target, subject: notification.subject }) }}
    </p>

    <div class="toast-buttons m-t-20 text-center" v-if="notification.type === 'prompt'">
      <a class="btn btn-primary" @click="selectOption(notification.agree)">Yes</a>
      <a class="btn btn-default" @click="selectOption(notification.disagree)">No</a>
    </div>
  </div>
</template>

<script>
export default {
  props: ['notification'],
  methods: {
    triggerClose () {
      this.$emit('close-notification', this.notification);
    },
    selectOption (callback) {
      if (callback != null) {
        callback();
      }

      this.triggerClose();
    }
  }
};
</script>

<style lang="less">
.toast {
  padding: 15px 25px 15px 32px;
  background-color: #333;
  color: #adadad;
  margin-top: 5px;
  position: relative;
  box-shadow: 2px 2px 10px rgba(0, 0, 0, 0.55);

  strong {
    color: #fff;
  }

  p {
    margin-bottom: 0px;
  }

  .close {
    color: #fff;
  }

  &:before {
    content: '';
    background-color: #525252;
    position: absolute;
    left: 0px;
    top: 0;
    bottom: 0;
    width: 7px;
  }

  &.toast-success {
    &:before {
      background-color: rgb(82, 187, 86);
    }
  }

  &.toast-danger {
    &:before {
      background-color: rgb(239, 83, 80);
    }
  }

  &.toast-warning {
    &:before {
      background-color: rgb(241, 181, 61);
    }
  }

  &.toast-info {
    &:before {
      background-color: rgb(103, 209, 248);
    }
  }
}
</style>
