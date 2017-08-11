<template>
  <div class="alert" :class="notification.type ? 'alert-' + notification.type : 'secondary'">
    <button type="button" @click="triggerClose" class="close" data-dismiss="alert" aria-hidden="true">
      <i class="material-icons">close</i>
    </button>
    <strong v-if="notification.title">{{notification.title}}</strong>
    <p>
      {{notification.message}}
    </p>
  </div>
</template>

<script>
export default {
  props: ['notification'],
  methods: {
    triggerClose () {
      this.$emit('close-notification', this.notification);
    }
  }
};
</script>
