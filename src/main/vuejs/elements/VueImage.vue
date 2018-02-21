<template>
  <img 
    :src="source" 
    @error.prevent="error" 
    @load="load"
    v-if="display" />
</template>

<script>
import Placeholder from 'assets/img/notepad.jpg';
export default {
  name: 'image',
  props: {
    src: String,
    placeholder: {
      type: Boolean,
      default () {
        return false;
      }
    }
  },
  data () {
    return {
      source: null,
      display: true
    };
  },
  mounted () {
    this.source = this.src || Placeholder;
  },
  methods: {
    error (event) {
      if (this.placeholder) {
        this.source = Placeholder;
      } else {
        this.display = false;
      }
    },
    load (event) {

    }
  }
};
</script>
