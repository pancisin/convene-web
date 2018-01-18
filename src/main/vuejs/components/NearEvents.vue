<template>
  <div class="near-events">
    <div v-if="!positionAcquired" class="text-center position-picker" >
      <h3 class="text-pink">We can't get your position. :(</h3>
      <small class="text-muted">
        We are having troubles with getting your current position. There is something wrong or you blocked access to your position.
        Please provide your position for better your experience at convene.
      </small>
      <place-picker v-model="position" />
    </div>
    <event-map v-if="paginator.content && paginator.content.length > 0" :events="paginator.content" />
  </div>
</template>

<script>
import { EventMap, PlacePicker } from 'elements';
import PublicApi from 'api/public.api';
import { DateTime } from 'luxon';

export default {
  name: 'near-events',
  data () {
    return {
      position: {
        lat: 0,
        lng: 0
      },
      paginator: {},
      positionAcquired: true
    };
  },
  components: {
    EventMap,
    PlacePicker
  },
  created () {
    this.loadPosition();
  },
  watch: {
    position (newVal) {
      this.getEvents(newVal);
      this.positionAcquired = true;
    }
  },
  methods: {
    loadPosition () {
      navigator.geolocation.getCurrentPosition(position => {
        this.getEvents({
          lat: position.coords.latitude,
          lng: position.coords.longitude
        });
      }, () => {
        this.positionAcquired = false;
      });
    },
    getEvents (position) {
      PublicApi.getNearEvents(0, 100, {
        ...position,
        distance: 10,
        fromDate: DateTime.utc().valueOf(),
        toDate: DateTime.utc().plus({ months: 1 }).valueOf()
      }, paginator => {
        this.paginator = paginator;
      });
    }
  }
};
</script>

<style lang="less">
.near-events {
  margin-bottom: 20px;

  .position-picker {
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.12), 0 1px 2px rgba(0, 0, 0, 0.24);
    background-color: #fff;
    padding: 10px;

    input {
      margin-top: 20px;
    }
  }
}
</style>
