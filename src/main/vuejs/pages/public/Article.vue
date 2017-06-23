<template>
  <div class="container">
    <h3 v-if="article != null">
      {{article.title}}
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
