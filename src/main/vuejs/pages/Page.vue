<template>
  <div v-if="page != null">
    <div class="card-box p-b-0">
      <h4 class="text-dark  header-title m-t-0" v-text="page.name"></h4>
      <p class="text-muted m-b-25 font-13">
        This basic wizard have no form validation and allows you to skip to another step by clicking on the tab.
      </p>
  
    </div>
  </div>
</template>

<script>
export default {
  name: 'page',
  data() {
    return {
      page: null,
    }
  },
  beforeRouteUpdate(to, from, next) {
    this.$http.get('api/page/' + to.params.id).then(response => {
      this.page = response.body;
    })
  
  },
  created() {
    // var page_id = this.$route.params.id;
    // if (page_id != null) {
    //   this.$http.get('api/page/' + page_id).then(response => {
    //     this.page = response.body;
    //   })
    // }
  }
}
</script>