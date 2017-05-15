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
  data() {
  	return { 
      timer: null 
    }
	},
  created() {
    this.timer = setTimeout(function () {
      this.triggerClose(this.notification)
    }.bind(this), 5000)
  },
  methods: {
    triggerClose() {
    	clearTimeout(this.timer)
      this.$emit('close-notification', this.notification)
    }
  }
}
</script>
