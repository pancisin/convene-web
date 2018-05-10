<template>
  <div class="activity-feed" v-loading="loading">
    <div class="text-center" v-if="receivedActivities > 0" v-stream:click="showReceived$">
      <button type="button" class="btn btn-link">
        {{ `Load ${receivedActivities} more activities.` }}
      </button>
    </div>

    <div 
      v-if="paginator.content && paginator.content.length === 0" 
      class="card-box text-center">
      <h3 class="text-pink">You haven't got any recent activity yet.</h3>

      <small class="text-muted">
        There is any content for you. Select some pages to follow.
      </small>
    </div>
    <div
      v-for="activity in paginator.content" 
      :key="activity.id" class="activity-feed-card" 
      :class="{ 
        'af-media-card': activity.object_type === 'MEDIA', 
        'af-event-card' : activity.object_type === 'EVENT' }"
      v-if="activity.type.public">

      <div class="af-card-header">
        <vue-image v-if="activity.subject.poster" :src="activity.subject.poster.path" class="img-circle" />

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
        <component :is="`${activity.object_type.toLowerCase()}-activity`" :obj="activity.objectThumbnail" />
      </div>
    </div>

    <div class="text-center m-t-20" v-if="!paginator.last">
      <button 
        type="button" 
        class="btn btn-default" 
        v-stream:click="loadMore$">

      Load more</button>
    </div>
  </div>
</template>

<script>
import UserApi from 'api/user.api';
import { LightBox, VueImage } from 'elements';
import { Observable, Subject } from 'rxjs';
import * as activities from './activity-feed';
import { map } from 'rxjs/operators';
import { StaggerTransition } from '../functional/transitions';

export default {
  name: 'user-activity-feed',
  components: {
    LightBox,
    VueImage,
    StaggerTransition,
    ...activities
  },
  data () {
    return {
      loading: false
    };
  },
  subscriptions () {
    this.loadMore$ = new Subject();
    this.showReceived$ = new Subject();
    const onCreateStream = this.$eventToObservable('hook:created');

    const wsStream = Observable
      .fromPromise(this.connectWM('/stomp'))
      .flatMap(() => Observable.create(ob => {
        this.$stompClient.subscribe('/user/queue/activity', response => {
          const activity = JSON.parse(response.body);
          ob.next(activity);
        });
      }));

    const receivedBufferStream = wsStream
      .bufferWhen(() => this.showReceived$)
      .map(a => {
        return {
          ...this.paginator,
          content: [
            ...a,
            ...this.paginator.content
          ]
        };
      });

    const concatContent = map(p => {
      const items = this.paginator.content || [];
      return {
        ...p,
        content: [ ...items ].concat(p.content)
      };
    });

    return {
      receivedActivities: Observable.merge(wsStream.mapTo({ delta: 1 }), this.showReceived$.mapTo({ reset: true }))
        .scan((acc, cur) => {
          return cur.reset ? 0 : acc + cur.delta;
        }, 0),
      paginator: Observable.merge(this.loadMore$, onCreateStream)
        .throttleTime(400)
        .mapTo(1)
        .scan((acc, cur) => acc + cur, -1)
        .do(() => { this.loading = true; })
        .flatMap(page => Observable.create(ob => {
          UserApi.getActivityFeed(page, 10, paginator => {
            ob.next(paginator);
          });
        }))
        .pipe(concatContent)
        .do(() => { this.loading = false; })
        .merge(receivedBufferStream)
        .startWith({})
    };
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
  }
}
</style>

