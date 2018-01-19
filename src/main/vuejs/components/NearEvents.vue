<template>
  <div class="near-events">
    <div v-if="!positionAcquired" class="text-center position-picker" >
      <position-form />
    </div>
    <event-map v-if="paginator.content && paginator.content.length > 0" :events="paginator.content" :draggable="draggable" />
  </div>
</template>

<script>
import { EventMap } from 'elements';
import PublicApi from 'api/public.api';
import { DateTime } from 'luxon';
import { PositionForm } from 'elements/forms';
import { mapGetters } from 'vuex';

export default {
  name: 'near-events',
  data () {
    return {
      paginator: {},
      positionAcquired: true
    };
  },
  props: {
    fromDate: {
      type: Number,
      default () {
        return DateTime.utc().valueOf();
      }
    },
    toDate: {
      type: Number,
      default () {
        return DateTime.utc().plus({ months: 1 }).valueOf();
      }
    },
    draggable: Boolean,
    showWarning: Boolean
  },
  components: {
    EventMap,
    PositionForm
  },
  computed: {
    ...mapGetters(['position'])
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
      this.$tryGetPosition(position => {
        this.getEvents(position);
      }, () => {
        if (this.showWarning) {
          this.positionAcquired = false;
        }
      });
    },
    getEvents (position) {
      PublicApi.getNearEvents(0, 100, {
        ...position,
        distance: 10,
        fromDate: this.fromDate,
        toDate: this.toDate
      }, paginator => {
        this.paginator = paginator;
      });
    }
  }
};
</script>

<style lang="less">
.near-events {
  .position-picker {
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.12), 0 1px 2px rgba(0, 0, 0, 0.24);
    background-color: #fff;
    padding: 10px;
  }
}
</style>
