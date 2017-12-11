<template>
  <div class="container">
    <masonry :columns="4">
      <masonry-item class="card"   
        v-for="(conference, index) in conferences" 
        :key="index">
        
        <router-link 
          :to="{ name: 'conference', params: { id: conference.id } }">
          <div class="title">
            <h5>
              {{ conference.name }}
            </h5>
          </div>

          <img v-if="conference.poster" :src="conference.poster.path" >
        </router-link>
      </masonry-item>
    </masonry>
  </div>
</template>

<script>
import RootApi from 'api/api';
import { mapGetters } from 'vuex';
import PublicApi from 'api/public.api';
import { Masonry, MasonryItem } from 'elements';

export default {
  name: 'conference-index',
  data () {
    return {
      conferences: []
    };
  },
  created () {
    this.getConferences();
  },
  computed: {
    ...mapGetters(['authenticated'])
  },
  components: {
    Masonry, MasonryItem
  },
  methods: {
    getConferences () {
      if (this.authenticated) {
        RootApi.getConferences(0, 10, paginator => {
          this.conferences = paginator.content;
        });
      } else {
        PublicApi.getConferences(0, 10, paginator => {
          this.conferences = paginator.content;
        });
      }
    }
  }
};
</script>
