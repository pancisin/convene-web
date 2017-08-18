<template>
  <div class="" v-show="active">
    <h4 v-text="title"></h4>
    <hr />
    <slot>
    </slot>
  </div>
</template>

<script>
export default {
  props: ['title', 'icon', 'valid'],
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
