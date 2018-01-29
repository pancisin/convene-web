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
            <small class="text-muted" v-if="conference.category != null">
              {{ $t('category.' + conference.category.code + '.' + conference.branch.code) }}
            </small>
          </div>

          <div class="image-wrapper" v-if="conference.poster"> 
            <vue-image :src="conference.poster.path" />
          </div>
        </router-link>
        <div class="actions" v-if="conference.privilege && conference.privilege.active">
          <router-link 
            :to="{ name: 'conference.settings', params: { id: conference.id } }" 
            class="pull-right text-primary">
            <i class="fa fa-pencil-square-o" aria-hidden="true"></i>
          </router-link>
        </div>
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
