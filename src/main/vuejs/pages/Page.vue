<template>
  <div class="row">
    <div class="col-sm-12">
      <div class="page-title-box">
        <h4 class="page-title" v-text="page.name"></h4>
      </div>
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
import PageInjector from '../services/injectors/page.injector.js'

export default {
  name: 'page',
  data() {
    return {
      page: new Object(),
      edit: false,
    }
  },
  provide() {
    return {
      api: this.api,
    }
  },
  computed: {
    api() {
      var page_id = this.$route.params.id;
      return new PageInjector(page_id);
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
      if (this.$route.params.id != null && this.page.id == page_id)
        return;

      this.api.getPage(true, page => {
        this.page = page;
      });
    },
    pageUpdated(page) {
      this.page = page;
      this.updatePage(page);
    }
  }
}
</script>