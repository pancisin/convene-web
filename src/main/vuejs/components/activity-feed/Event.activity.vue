<template>
  <div class="event-activity-feed" :style="style">
    <div class="event-activity-overlay"></div>

    <div class="event-activity-content">
      <div class="text-center">
        <light-box :image="obj.poster.path">
          <vue-image class="img-thumbnail event-activity-thumbnail" :src="obj.poster.path" />
        </light-box>
      </div>

      <router-link :to="{ name: 'event.public', params: { id: obj.id } }">
        <h2 class="text-pink">{{ obj.name }}</h2>
      </router-link>

      <p class="text-muted">{{ obj.date | luxon('FF') }}</p>
      <p v-if="obj.summary" v-strip="obj.summary.substring(0, 200)">...</p>
    </div>
  </div>
</template>

<script>
import { VueImage, LightBox } from 'elements';
export default {
  name: 'event-activity-feed',
  props: {
    obj: Object
  },
  components: {
    VueImage,
    LightBox
  },
  computed: {
    style () {
      return {
        'background-image': `url(${this.obj.poster.path})`
      };
    }
  }
};
</script>

<style lang="less">
.event-activity-feed {
  background-repeat: no-repeat;
  background-size: cover;
  background-position: center;

  padding: 40px 20px;
  position: relative;

  .event-activity-overlay {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    bottom: 0;
    width: 100%;

    background: linear-gradient(#444f5c, #334159);
    opacity: 0.85;
  }

  .event-activity-content {
    position: relative;
    color: #fff;

    h2 {
      font-weight: normal;
    }

    .event-activity-thumbnail {
      max-width: 45%;
      margin-bottom: 15px;
      box-shadow: 0 14px 28px rgba(0,0,0,.25), 0 10px 10px rgba(0,0,0,.22);
    }
  }
}
</style>
