<template>
  <h1 v-text="conference.name"></h1>
</template>

<script>
export default {
  name: 'conference',
  data() {
    return {
      conference: null,
    }
  },
  created() {
    var conference_id = this.$route.params.id;
    if (conference_id != null) {
      this.$http.get('api/conference/' + conference_id).then(response => {
        this.conference = response.body;
      })
    }
  }
}
</script>