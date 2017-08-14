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
      'pages'
    ]),
    page () {
      let page_id = Number.parseInt(this.$route.params.id, 10);
      var index = this.pages.findIndex(p => {
        return p.id === page_id;
      });

      if (index !== -1) {
        this.edit = true;
        return this.pages[index];
      } else return {};
    }
  },
  created () {
    this.injector = new PageInjector(this.$route.params.id);
  }
};
</script>