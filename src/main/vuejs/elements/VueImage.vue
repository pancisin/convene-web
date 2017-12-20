<template>
  <img 
    :src="source" 
    @error.prevent="error" 
    @load="load"/>
</template>

<script>
import Placeholder from 'assets/img/notepad.jpg';
export default {
  name: 'image',
  props: {
    src: String
  },
  data () {
    return {
      source: null
    };
  },
  mounted () {
    this.source = this.src;
  },
  methods: {
    error (event) {
      this.source = Placeholder;
    },
    load (event) {

    }
  }
};
</script>

<style>

</style>
