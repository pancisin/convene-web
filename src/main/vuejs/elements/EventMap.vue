<template>
  <div class="event-map">
    <div ref="mapCanvas" class="event-map-canvas"></div>
    <transition name="fade-right" v-if="infoWindow">
      <div class="info-window" v-show="displayInfoWindow" :style="getInfowindowStyle(selectedEvent)">
        <button type="button" class="close" @click="displayInfoWindow = false"><i class="fa fa-times"></i></button>

        <span>
          <router-link :to="{ name: 'event.public', params: { id: selectedEvent.id } }">
            <h4 class="text-primary">{{ selectedEvent.name }}</h4>
          </router-link>
          <small class="text-muted">{{ selectedEvent.date | luxon('ff') }}</small>
          <hr />

          <p>
            {{ selectedEvent.summary ? selectedEvent.summary.substr(0, 200) : '' }}...
          </p>
        </span>

        <router-link :to="{ name: 'event.public', params: { id: selectedEvent.id } }" class="btn btn-default btn-block btn-sm">
          Open
        </router-link>
      </div>
    </transition>
  </div>
</template>

<script>
import gmapStyle from './gmapStyle.js';
import { groupBy } from '../services/helpers';

export default {
  name: 'event-map',
  data () {
    return {
      map: null,
      loading: false,
      displayInfoWindow: false,
      selectedEvent: {}
    };
  },
  props: {
    events: Array,
    draggable: Boolean,
    infoWindow: Boolean
  },
  mounted () {
    this.loading = true;

    this.$googleMapApi.load(context => {
      console.log(this.$refs.mapCanvas);
      this.map = context.map(this.$refs.mapCanvas, {
        disableDefaultUI: true,
        styles: gmapStyle,
        draggable: this.draggable
      });

      const bounds = context.bounds();

      const eventGroups = groupBy(this.events, item => {
        return {
          latitude: item.latitude,
          longitude: item.longitude
        };
      });

      console.log(eventGroups);
      eventGroups.map(g => {
        const position = {
          lat: g.props.latitude,
          lng: g.props.longitude
        };

        bounds.extend(position);

        const e = g.data[0];
        const marker = context.marker({
          position,
          map: this.map,
          title: e.name,
          icon: {
            url: e.poster.path,
            scaledSize: context.size(50, 50),
            anchor: context.point(25, 25)
          },
          optimized: false
        });
        marker.addListener('click', () => {
          this.selectedEvent = e;
          this.displayInfoWindow = true;
        });
        return marker;
      });

      const overlay = context.overlay();
      overlay.draw = function () {
        this.getPanes().markerLayer.id = 'markerLayer';
      };
      overlay.setMap(this.map);

      this.map.setCenter(bounds.getCenter());
      this.map.fitBounds(bounds);

      if (this.map.getZoom() > 17) {
        this.map.setZoom(17);
      }

      this.loading = false;
    });
  },
  methods: {
    getInfowindowStyle (event) {
      if (event != null) {
        return {
          // 'background-image': event.poster ? `url(${event.poster.path})` : ''
        };
      }
    }
  }
};
</script>

<style lang="less">
.event-map {
  position: relative;
  overflow: hidden;
  box-shadow: 0px 1px 2px 0px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;

  .info-window {
    position: absolute;
    top: 0;
    height: 100%; 
    right: 0;
    width: 50%;
    background:#fff;
    box-shadow: 0px 1px 2px 0px rgba(0, 0, 0, 0.1);
    padding: 10px;
    overflow: hidden;

    display: flex;
    flex-direction: column;
    justify-content: space-between;

    background-repeat: no-repeat;
    background-size: cover;
    background-position: center;

    .close {
      position: absolute;
      right: 10px;
    }
  }

  .event-map-canvas {
    height: 400px;
    width: 100%;

    div#markerLayer {
      img {
        border-radius: 100%;
        border: 1px solid #ccc !important;
      }
    }
  }
}

</style>
