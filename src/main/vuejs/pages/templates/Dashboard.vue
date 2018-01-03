<template>
  <div>
    <transition name="fade" mode="out-in">
      <div v-if="editMode" key="edit">
        <div class="text-center">
          <a class="btn btn-danger" @click="editModeSwitch(false)">Cancel</a>
          <a class="btn btn-primary" @click="saveWidgets">Save</a>
        </div>

        <div class="">
          <h5>Available widgets: </h5>

          <ul class="widget-types-list">
            <li v-for="(w_type, index) in widgetTypes" :key="index">
              {{ $t(w_type.code) }}
              <a class="btn btn-default btn-block" @click="addWidget(w_type)">
                Add
              </a>

              <img :src="w_type.thumbnail" />
            </li>
          </ul>

        </div>
      </div>
      
      <div v-else key="pending" class="clearfix">
        <a class="btn btn-link pull-right" @click="editModeSwitch(true)">
          Edit dashboard
        </a>
      </div>
    </transition>

    <grid-layout 
      :layout="widgets" 
      :col-num="6" 
      :row-height="100" 
      :is-draggable="editMode" 
      :is-resizable="editMode" 
      :vertical-compact="true" 
      :margin="[10, 10]" 
      :use-css-transforms="true">

      <grid-item 
        v-for="(item, index) in widgets" 
        :key="index" 
        :x="item.x" 
        :y="item.y" 
        :w="item.w" 
        :h="item.h" 
        :i="item.i">

        <div class="widget-wrapper" :class="{ 'edit-mode' : editMode }">
          <transition name="fade">
            <a class="pull-right text-danger widget-delete" @click="removeWidget(item)" v-show="editMode">
              <i class="fa fa-times fa-lg"></i>
            </a>
          </transition>

          <component 
            class="widget-wrapper-content" 
            :is="item.type.component" 
            :title="$t(item.type.code)" 
            :parent="api.parent_type">
          </component>
        </div>
        
      </grid-item>
    </grid-layout>
  </div>
</template>
  
<script>
import { GridLayout, GridItem } from 'vue-grid-layout';
import * as WidgetComponents from 'elements/widgets';
import { calculateHash } from '../../services/helpers';
import DashboardWidgetsMap from '../../services/maps/dashboard-widgets.map';

export default {
  name: 'dashboard',
  inject: ['provider'],
  data () {
    return {
      activities: [],
      widgets: [],
      editMode: false,
      original_widgets: null
    };
  },
  components: {
    ...WidgetComponents,
    GridLayout, GridItem
  },
  computed: {
    widgetTypes () {
      return DashboardWidgetsMap;
    },
    touched () {
      return calculateHash(JSON.stringify(this.widgets)) !== calculateHash(this.original_widgets);
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
  },
  methods: {
    getWidgets () {
      this.editMode = false;
      this.api.getWidgets(widgets => {
        this.original_widgets = JSON.stringify(widgets);
        this.widgets = widgets.map(widget => {
          const type = this.evaluateWidget(widget.type);
          return {
            ...widget,
            type
          };
        });
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

      this.api.putWidgets(data, widgets => {
        this.original_widgets = JSON.stringify(widgets);
        this.widgets = widgets.map(w => {
          const type = this.evaluateWidget(w.type);
          return {
            ...w,
            type
          };
        });

        this.editMode = false;
        this.$success('notification.dashboard.saved');
      });
    },
    editModeSwitch (enabled) {
      if (!enabled && this.touched) {
        this.$prompt('notification.leave_prompt', null, () => {
          const widgets = JSON.parse(this.original_widgets);
          this.widgets = widgets.map(w => {
            const type = this.evaluateWidget(w.type);
            return {
              ...w,
              type
            };
          });

          this.editMode = false;
        });
      } else {
        this.editMode = enabled;
      }
    },
    evaluateWidget (type) {
      const widget = this.widgetTypes.filter(w => w.name === type);

      if (widget && widget.length > 0) {
        return widget[0];
      } else return {};
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
    z-index: 1;
  }

  .widget-wrapper-content {
    margin-bottom: 0;
    overflow: hidden;

    .panel-body {
      overflow: auto;
      height: 91%;
    }
  }

  &.edit-mode {
    .widget-wrapper-content {
      pointer-events: none;
      user-select: none;

      &:after {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        bottom: 0;
        width: 100%;
        background-color: rgba(255, 255, 255, .4);
      }
    }
  }
}

.widget-types-list {
  margin: 0;
  padding: 0;
  border-bottom: 1px solid #ddd;

  li {
    &:before {
      content: '';
      height: 100%;
    }

    display: inline-block;
    width: 150px;
    height: 150px;

    img {
      width: 100%;
    }

    &~li {
      margin-left: 10px;
    }
  }
}
</style>
