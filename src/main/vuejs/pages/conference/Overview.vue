<template>
  <div>
    <transition name="fade-up" mode="out-in">
      <div v-if="editMode" key="edit">
        <div class="text-center">
          <a class="btn btn-danger" @click="editMode = false">Cancel</a>
          <a class="btn btn-primary" @click="saveWidgets">Save</a>
        </div>

        <div class="">
          <h5>Available widgets: </h5>

          <ul class="widget-types-list">
            <li v-for="w_type in widgetTypes" :key="w_type.name">
              {{ $t(w_type.code) }}
              <a class="btn btn-default btn-block" @click="addWidget(w_type)">Add</a>
            </li>
          </ul>

        </div>
      </div>
      <div v-else key="pending">
        <a class="btn btn-info btn-rounded btn-lg" @click="editMode = true">
          <i class="fa fa-pencil"></i>
        </a>
      </div>
    </transition>

    <grid-layout :layout="widgets" :col-num="6" :row-height="100" :is-draggable="editMode" :is-resizable="editMode" :vertical-compact="true" :margin="[10, 10]" :use-css-transforms="true">
      <grid-item v-for="(item, index) in widgets" :key="index" :x="item.x" :y="item.y" :w="item.w" :h="item.h" :i="item.i">
        <panel type="default">
          <span slot="title">
            {{ $t(item.type.code) }}

            <transition name="fade">
              <a class="pull-right text-danger" @click="removeWidget(item)" v-show="editMode">
                <i class="fa fa-times fa-lg"></i>
              </a>
            </transition>
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
import PublicApi from 'api/public.api';

export default {
  name: 'dashboard',
  props: ['conference'],
  data () {
    return {
      activities: [],
      widgets: [],
      editMode: false,
      widgetTypes: []
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
    this.getWidgetTypes();
  },
  methods: {
    getWidgets () {
      ConferenceApi.getWidgets(this.conference.id, widgets => {
        this.widgets = widgets;
      });
    },
    getWidgetTypes () {
      PublicApi.getWidgetTypes(widgetTypes => {
        this.widgetTypes = widgetTypes;
      });
    },
    addWidget (type) {
      let new_widget = {
        type,
        x: 0,
        y: 0,
        w: 1,
        h: 1,
        i: this.widgets.length
      };

      this.widgets.push(new_widget);
    },
    removeWidget (widget) {
      this.widgets = this.widgets.filter(w => {
        return w.id !== widget.id;
      });
    },
    saveWidgets () {
      let data = this.widgets.map((w, index) => {
        return {
          ...w,
          i: index,
          type: w.type.name
        };
      });

      ConferenceApi.putWidgets(this.conference.id, data, widgets => {
        this.widgets = widgets;
        this.editMode = false;
        this.$success('Dashboard saved !', 'Your conference dashboard has been saved successfully.');
      });
    }
  }
};
</script>

<style lang="less">
.vue-grid-item {
  .panel {
    height: 100%;
    overflow: hidden;
  }
}

.widget-types-list {
  margin: 0;
  padding: 0;

  li {
    &:before {
      content: '';
      height: 100%;
    }

    display: inline-block;
    width: 150px;
    height: 150px;

    &~li {
      margin-left: 10px;
    }
  }
}
</style>
