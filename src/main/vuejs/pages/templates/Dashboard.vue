<template>
  <div>
    <transition name="fade-up" mode="out-in">
      <div v-if="editMode" key="edit">
        <div class="text-center">
          <a class="btn btn-danger" @click="editModeSwitch(false)">Cancel</a>
          <a class="btn btn-primary" v-show="touched" @click="saveWidgets">Save</a>
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
        <a class="btn btn-primary btn-rounded btn-lg" @click="editModeSwitch(true)">
          <i class="fa fa-pencil"></i>
        </a>
      </div>
    </transition>

    <grid-layout :layout="widgets" :col-num="6" :row-height="100" :is-draggable="editMode" :is-resizable="editMode" :vertical-compact="true" :margin="[10, 10]" :use-css-transforms="true">
      <grid-item v-for="(item, index) in widgets" :key="index" :x="item.x" :y="item.y" :w="item.w" :h="item.h" :i="item.i">
        <div class="widget-wrapper" :class="getWidgetClass(item.w, item.h)">
          <transition name="fade">
            <a class="pull-right text-danger widget-delete" @click="removeWidget(item)" v-show="editMode">
              <i class="fa fa-times fa-lg"></i>
            </a>
          </transition>

          <component :is="item.type.component" :title="$t(item.type.code)"></component>
        </div>
      </grid-item>
    </grid-layout>
  </div>
</template>
  
<script>
import { GridLayout, GridItem } from 'vue-grid-layout';
import * as WidgetComponents from '../../elements/widgets';
import PublicApi from 'api/public.api';
import { calculateHash } from '../../services/helpers';

export default {
  name: 'dashboard',
  inject: ['provider'],
  data () {
    return {
      activities: [],
      widgets: [],
      editMode: false,
      widgetTypes: [],
      original_widgets: []
    };
  },
  components: {
    ...WidgetComponents,
    GridLayout, GridItem
  },
  computed: {
    touched () {
      return calculateHash(JSON.stringify(this.widgets)) !== calculateHash(JSON.stringify(this.original_widgets));
    },
    api () {
      if (this.provider != null) {
        return this.provider.api;
      }
    }
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
      this.editMode = false;
      this.api.getWidgets(widgets => {
        this.widgets = widgets;
        this.original_widgets = widgets;
      });
    },
    getWidgetTypes () {
      PublicApi.getWidgetTypes(widgetTypes => {
        this.widgetTypes = widgetTypes;
      });
    },
    getWidgetClass (width, height) {
      if (width === 1 || height === 1) {
        return 'widget-xs';
      } else if (width === 2 || height === 2) {
        return 'widget-md';
      } else if (width >= 3 || height >= 3) {
        return 'widget-lg';
      }
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

      this.api.putWidgets(data, widgets => {
        this.widgets = widgets;
        this.editMode = false;
        this.$success('Dashboard saved !', 'Your conference dashboard has been saved successfully.');
      });
    },
    editModeSwitch (enabled) {
      if (!enabled && this.touched) {
        this.$prompt('Discard dashboard changes.', 'Are you sure discard changes you made to this dashboard ?', () => {
          this.widgets = this.original_widgets;
          this.editMode = false;
        });
      } else {
        this.editMode = enabled;
      }

    }
  }
};
</script>

<style lang="less">
.vue-grid-item {
  .panel {
    height: 100%;
    overflow-y: auto;
  }
}

.widget-wrapper {
  position: relative;
  height: 100%;

  &>* {
    height: 100%;
  }

  .widget-delete {
    position: absolute;
    top: 15px;
    right: 15px;
  }
}

.widget-types-list {
  margin: 0;
  padding: 0;
  border-bottom: 1px solid gray;

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
