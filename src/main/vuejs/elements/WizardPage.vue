<template>
  <div class="" v-show="active">
    <h3 v-text="title"></h3>
    <slot>
    </slot>
  </div>
</template>

<script>
export default {
  props: ['title', 'icon'],
  data () {
    return {
      active: false
    };
  },
  mounted () {
    this.$parent.addPage(this);
  }
};
</script>
 
<style>

</style>
