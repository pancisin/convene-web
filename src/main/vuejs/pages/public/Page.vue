<template>
  <div class="container" v-if="page != null">
  
    <div class="row">
      <div class="col-md-8">
        <div class="panel panel-border panel-default">
          <div class="panel-heading">
            <h3 class="panel-title">{{ page.name }}
              <span class="label label-primary"> {{ page.followersCount }} followers</span>
            </h3>
  
            <p class="panel-sub-title font-13 text-muted"></p>
          </div>
          <div class="panel-body">
            <div v-html="page.summary" class="m-b-20"></div>
  
            <a class="btn btn-rounded btn-inverse" @click="toggleFollow">
              <span v-if="follows">Unfollow</span>
              <span v-else>Follow</span>
            </a>
          </div>
        </div>
      </div>
      <div class="col-md-4">
  
      </div>
    </div>
  
  </div>
</template>

<script>
export default {
  name: 'page',
  data() {
    return {
      follows: false,
      page: null,
    }
  },
  watch: {
    '$route': 'getPage'
  },
  created() {
    this.getPage();
  },
  methods: {
    getPage() {
      var page_id = this.$route.params.id;
      if (page_id != null) {
        this.$http.get('api/page/' + page_id).then(response => {
          this.page = response.body;
          this.checkFollow();
        })
      }
    },
    checkFollow() {
      var url = ['api/page', this.page.id, 'follow-status'].join('/');
      this.$http.get(url).then(response => {
        this.follows = response.body;
      })
    },
    toggleFollow() {
      var url = ['api/page', this.page.id, 'toggle-follow'].join('/');
      this.$http.patch(url).then(response => {
        this.follows = response.body;
      })
    }
  }
}
</script>