<template>
  <div class="row">
    <div class="col-xs-12">
      <h3 v-text="page.name" class="page-title"></h3>
    </div>
    <div class="col-md-3">
      <div class="list-group">
        <router-link to="overview" class="list-group-item">
          Overview
        </router-link>
        <router-link to="events" class="list-group-item">
          Event calendar
        </router-link>
        <router-link to="services" class="list-group-item">
          Services
        </router-link>
        <router-link to="followers" class="list-group-item">
          Followers
        </router-link>
      </div>
    </div>
    <div class="col-md-9">
      <transition name="fade-up" mode="out-in">
        <router-view :page="page"></router-view>
      </transition>
    </div>
  </div>
</template>

<script>
import Modal from '../elements/Modal.vue'
export default {
  name: 'page',
  data() {
    return {
      page: new Object(),
      edit: false,
      display: {
        modalEdit: false,
      },
      categories: [],
    }
  },
  components: {
    Modal
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
      console.log(page_id);
      if (page_id != null) {
        this.$http.get('api/page/' + page_id).then(response => {
          this.page = response.body;
          this.edit = true;
        })
      } else {
        this.page = new Object();
        this.display.modalEdit = true;
      }
    }
  }
}
</script>