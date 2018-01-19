<template>
  <div class="event-map"></div>
</template>

<script>
import gmapStyle from './gmapStyle.js';
import { DateTime } from 'luxon';

export default {
  name: 'event-map',
  data () {
    return {
      map: null,
      loading: false
    };
  },
  props: {
    events: Array,
    draggable: Boolean
  },
  mounted () {
    this.loading = true;

    this.$googleMapApi.load(context => {
      this.map = context.map(this.$el, {
        disableDefaultUI: true,
        styles: gmapStyle,
        draggable: this.draggable
      });

      const infoWindow = context.infoWindow({
        maxWidth: 250
      });
      const bounds = context.bounds();
      this.events.map(e => {
        const position = {
          lat: e.latitude,
          lng: e.longitude
        };
        bounds.extend(position);
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
          infoWindow.setContent(`<h5><a href="/event/${e.id}">${e.name}</a></h5><small class="text-muted">${DateTime.fromMillis(e.date).toFormat('ff')}</small><hr />${e.summary.substr(0, 200)}...`);
          infoWindow.open(this.map, marker);
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
  }
};
</script>

<style lang="less">
.event-map {
  height: 400px;
  width: 100%;
  box-shadow: 0px 1px 2px 0px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;

  div#markerLayer {
    img {
      border-radius: 100%;
      border: 1px solid #ccc !important;
    }
  }
}
</style>
