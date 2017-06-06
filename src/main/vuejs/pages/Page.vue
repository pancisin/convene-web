<template>
  <div class="row">
    <div class="col-xs-12">
      <h3 v-text="page.name" class="page-title"></h3>
    </div>
    <div class="col-xs-12">
      <transition name="fade-down" mode="out-in">
        <keep-alive>
          <router-view :page="page" :edit="edit" @updated="pageUpdated"></router-view>
        </keep-alive>
      </transition>
    </div>
  </div>
</template>

<script>
import { mapActions } from 'vuex'

export default {
  name: 'page',
  data() {
    return {
      page: new Object(),
      edit: false,
    }
  },
  watch: {
    '$route': 'getPage'
  },
  created() {
    this.getPage();
  },
  methods: {
    ...mapActions([
      'updatePage',
    ]),
    getPage() {
      var page_id = this.$route.params.id;

      if (this.page.id != null && this.page.id == page_id)
        return;
      this.page = new Object();
      if (page_id != null) {
        this.$http.get('api/page/' + page_id).then(response => {
          this.page = response.body;
          this.edit = true;
        })
      } else {
        this.page = new Object();
      }
    },
    pageUpdated(page) {
      this.page = page;
      this.updatePage(page);
    }
  }
}
</script>