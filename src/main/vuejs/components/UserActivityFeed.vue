<template>
  <div class="activity-feed">
    <div 
      v-for="activity in activities" 
      :key="activity.id" class="activity-feed-card" 
      :class="{ 'af-media-card': activity.object_type === 'MEDIA' }"
      v-if="activity.type.public">

      <div class="af-card-header">
        <img class="img img-circle" :src="activity.subject.poster.path">

        <div>
          <h4>
            <router-link :to="{ name: 'page.public', params: { id: activity.subject.id } }">
              {{ activity.subject.name }} 
            </router-link>
            <span class="text-muted">{{ $t(activity.type.code) }}.</span>
          </h4>
          <p class="text-muted"><small>{{ activity.created | luxon('f') }}</small></p>
        </div>
      </div>

      <div class="af-card-content">
        <div v-if="activity.object_type === 'MEDIA'">
          <light-box :image="activity.objectThumbnail.path" v-if="activity.objectThumbnail" >
            <img :src="activity.objectThumbnail.path">
          </light-box>
        </div>
        <div v-else-if="activity.object_type === 'ARTICLE'">
          <img :src="activity.objectThumbnail.thumbnail.path" style="width:100%">
          <h4>
            <router-link :to="{ name: 'article.public', params: { article_id: activity.object_id }}">
              {{ activity.objectThumbnail.title }}
            </router-link>
          </h4>
          <p v-strip="activity.objectThumbnail.content.substring(0, 200)">...</p>
          <div class="text-right m-t-20 clearfix">
            <button type="button" class="btn btn-link pull-right">Read more</button>
          </div>
        </div>
        <div v-else-if="activity.object_type === 'SERVICE'">
          {{ activity.objectThumbnail.name }}
        </div>
        <div v-else-if="activity.object_type === 'EVENT'">
          <h4>{{ activity.objectThumbnail.name }}</h4>
          <p>{{ activity.object_type.date | luxon('FF') }}</p>
          <p v-if="activity.objectThumbnail.summary" v-strip="activity.objectThumbnail.summary.substring(0, 200)">...</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import UserApi from 'api/user.api';
import { LightBox } from 'elements';

export default {
  name: 'user-activity-feed',
  data () {
    return {
      activities: []
    };
  },
  components: {
    LightBox
  },
  created () {
    this.getFeeds();
  },
  methods: {
    getFeeds () {
      UserApi.getActivityFeed(0, 100, activities => {
        this.activities = activities;
      });
    }
  }
};
</script>

<style lang="less">
@import (reference) '~less/variables.less';
.activity-feed {
  margin-bottom: 20px;

  .activity-feed-card {
    background: #fff;
    box-shadow: 0px 1px 2px 0px rgba(0, 0, 0, 0.1); 

    .af-card-content {
      padding: 20px;
    }

    .af-card-header {
      line-height: 18px;
      padding: 15px 20px;
      border-bottom: 1px solid #eee;
      display: flex;
      flex-direction: row;
      // align-items: center;
      
      & > div {
        display: inline-block;
        flex-grow: 1;

        p {
          margin: 0;
        }
      }

      img.img-circle {
        height: 50px;
        width: 50px;
        margin-right: 15px;
      }

      h4 {
        display: inline;
        line-height: inherit;
        font-size: 14px;

        a {
          color: @color-primary;
        }

        span {
          font-weight: normal
        }
      }
    }

    & ~ .activity-feed-card {
      margin-top: 20px;
    }

    &.af-media-card {
      .af-card-content {
        padding: 0;
      }

      img {
        width: 100%;
      }
    }
  }
}
</style>

