<template>
  <div class="container">
    <h3 v-if="conference != null">
      {{conference.name}}
    </h3>
  
    <panel>
    </panel>
  </div>
</template>

<script>
export default {
  name: 'article',
  data() {
    return {
      article: {},
    }
  },
  created() {

  }
}
</script>
