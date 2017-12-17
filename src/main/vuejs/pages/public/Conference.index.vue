<template>
  <div class="container" v-loading="loading">
    <masonry :columns="4">
      <masonry-item class="card"   
        v-for="(conference, index) in paginator.content" 
        :key="index">
        
        <router-link 
          :to="{ name: 'conference', params: { id: conference.id } }">
          <div class="title">
            <h5>
              {{ conference.name }}
            </h5>
          </div>

          <div class="image-wrapper" v-if="conference.poster"> 
            <vue-image :src="conference.poster.path" />
          </div>
        </router-link>
      </masonry-item>
    </masonry>

    <div class="text-center">
      <paginator :paginator="paginator" :fetch="getConferences" />
    </div>
  </div>
</template>

<script>
import RootApi from 'api/api';
import { mapGetters } from 'vuex';
import PublicApi from 'api/public.api';
import {
  Masonry,
  MasonryItem,
  VueImage,
  Paginator
} from 'elements';

export default {
  name: 'conference-index',
  data () {
    return {
      paginator: {},
      loading: false
    };
  },
  created () {
    this.getConferences();
  },
  computed: {
    ...mapGetters(['authenticated'])
  },
  components: {
    Masonry,
    MasonryItem,
    VueImage,
    Paginator
  },
  methods: {
    getConferences (page) {
      this.loading = true;

      const api = this.authenticated ? RootApi : PublicApi;
      api.getConferences(page, 6, paginator => {
        this.paginator = paginator;
        this.loading = false;
      });
    }
  }
};
</script>
