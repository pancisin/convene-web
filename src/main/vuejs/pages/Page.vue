<template>
  <div class="row">
    <div class="col-sm-12">
      <div class="page-title-box">
        <h4 class="page-title" v-text="page.name"></h4>
      </div>
    </div>

    <div class="col-xs-12">
      <div class="alert alert-danger" v-if="page.state == 'BLOCKED'">This page state is blocked. You are not allowed to make changes to this page.</div>
      <transition name="fade-down" mode="out-in">
        <keep-alive>
          <router-view :page="page" :edit="edit"></router-view>
        </keep-alive>
      </transition>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';
import PageInjector from '../services/injectors/page.injector.js';

export default {
  name: 'page',
  data () {
    return {
      edit: false,
      injector: null
    };
  },
  provide () {
    const provider = {};

    Object.defineProperty(provider, 'api', {
      get: () => this.injector
    });

    return { provider };
  },
  computed: {
    ...mapGetters([
      'pages', 'getPageById'
    ]),
    page () {
      let page_id = Number.parseInt(this.$route.params.id, 10);
      let page = this.getPageById(page_id);

      if (page != null) {
        this.edit = true;
        this.injector = new PageInjector(page_id);
      }

      return page;
    }
  }
};
</script>