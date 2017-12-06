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
  background: @color-primary;
  background-position: center;
  background-size: cover;
  box-shadow: 0 1px 3px rgba(0,0,0,0.12), 0 1px 2px rgba(0,0,0,0.24);
  transition: all 0.3s cubic-bezier(.25,.8,.25,1);


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
      text-transform: uppercase;
      color: #000;
    }
  }

  &:hover {
    box-shadow: 0 14px 28px rgba(0,0,0,0.25), 0 10px 10px rgba(0,0,0,0.22);
  }
}
</style>
