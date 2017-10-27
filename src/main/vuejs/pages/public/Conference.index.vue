<template>
  <div class="container">
    <masonry :columns="4">
      <masonry-item class="conference-panel"   
        v-for="(conference, index) in conferences" 
        :key="index"
        :style="{ 'background-image': conference.poster != null ? `url(${conference.poster.path})` : 'none' }">
        
        <router-link 
          :to="{ name: 'conference', params: { id: conference.id } }">
          <div class="title">
            <h5>
              {{ conference.name }}
            </h5>
          </div>
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

<style lang="less">
@import (reference) '~less/variables.less';
.conference-panel {
  overflow: hidden;
  box-shadow: 5px 3px 15px 0px rgba(111, 110, 110, 0.3);
  background: @color-primary;
  background-position: center;
  background-size: cover;
  transition: all .2s ease-in-out;

  & > a {
    display: block;
    height: 200px;
  }

  .title {
    width: 100%;
    background: white;
    margin: 0;
    padding: 15px;
    border-bottom: 1px solid #ccc;

    h5 {
      margin: 0;
    }
  }

  &:hover {
    box-shadow: 0px 0px 15px 2px rgba(111, 110, 110, 0.3);
  }
}
</style>
