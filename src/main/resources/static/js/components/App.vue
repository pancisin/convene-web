<template>
  <router-view>
  </router-view>
</template>

<script>
export default {

}
</script>

<style scoped>
a {
  cursor: pointer;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity .2s
}

.fade-enter,
.fade-leave-to {
  opacity: 0
}
</style>