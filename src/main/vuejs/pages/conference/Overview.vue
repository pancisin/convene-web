<template>
  <div>
    <div class="text-center" v-if="editMode">
      <a class="btn btn-danger" @click="editMode = false">Cancel</a>
      <a class="btn btn-primary" @click="saveWidgets">Save</a>
      <a class="btn btn-primary" @click="addWidget('FOLLOWERS')">Add widget</a>
    </div>
    <div v-else>
      <a class="btn btn-info btn-rounded btn-lg">
        <i class="fa fa-pencil" @click="editMode = true"></i>
      </a>
    </div>

    <grid-layout :layout="widgets" :col-num="6" :row-height="100" :is-draggable="editMode" :is-resizable="editMode" :vertical-compact="true" :margin="[10, 10]" :use-css-transforms="true">
      <grid-item v-for="(item, index) in widgets" :key="index" :x="item.x" :y="item.y" :w="item.w" :h="item.h" :i="item.i">
        <panel type="default">
          <span slot="title">
            {{ $t(item.type.code) }}
          </span>

          <component :is="item.type.component"></component>
        </panel>
      </grid-item>
    </grid-layout>
  </div>
</template>
  
<script>
import ConferenceApi from 'api/conference.api';
import { GridLayout, GridItem } from 'vue-grid-layout';
import * as WidgetComponents from '../../elements/widgets/index';

export default {
  name: 'dashboard',
  props: ['conference'],
  data () {
    return {
      activities: [],
      widgets: [],
      editMode: false
    };
  },
  components: {
    ...WidgetComponents,
    GridLayout, GridItem
  },
  watch: {
    '$route': 'getWidgets'
  },
  created () {
    this.getWidgets();
  },
  methods: {
    getWidgets () {
      ConferenceApi.getWidgets(this.conference.id, widgets => {
        this.widgets = widgets;
      });
    },
    addWidget (type) {
      this.widgets.push({
        type: {
          name: type
        },
        x: 0,
        y: 0,
        w: 1,
        h: 1,
        i: String(this.widgets.length)
      });
    },
    saveWidgets () {
      let data = this.widgets.map(w => {
        return {
          ...w,
          type: w.type.name
        };
      });

      ConferenceApi.putWidgets(this.conference.id, data, widgets => {
        this.widgets = widgets;
        this.$success('Dashboard saved !');
      });
    }
  }
};
</script>

<style lang="less">
.vue-grid-item {
  .panel {
    height: 100%;
  }
}
</style>
