<template>
  <div class="row">
    <div class="col-sm-12">
      <div class="page-title-box">
        <h4 class="page-title" v-if="conference != null" v-text="conference.name"></h4>
      </div>
    </div>
  
    <div class="col-xs-12">
      <transition name="fade-down" mode="out-in">
        <keep-alive>
          <router-view :conference="conference" :edit="edit"></router-view>
        </keep-alive>
      </transition>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';
import ConferenceInjector from '../services/injectors/conference.injector.js';

export default {
  name: 'conference',
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
      'conferences'
    ]),
    conference () {
      var conference_id = Number.parseInt(this.$route.params.id, 10);
      let index = this.conferences.findIndex(c => {
        return c.id === conference_id;
      });

      if (index !== -1) {
        this.edit = true;
        this.injector = new ConferenceInjector(this.$route.params.id);
        return this.conferences[index];
      } else return {};
    }
  }
};
</script>