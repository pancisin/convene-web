<template>
  <fullscreen :fullscreen.sync="fullscreen_on">
    <div class="venue-editor">
      <div class="editor-statusbar">
        <ul>
          <li>
            <div class="btn-group">
              <a type="button" class="btn btn-secondary dropdown-toggle waves-effect waves-light btn-navbar" data-toggle="dropdown" aria-expanded="false">File
                <i class="fa fa-angle-down"></i>
              </a>
              <div class="dropdown-menu">
                <a class="dropdown-item">Open</a>
                <a class="dropdown-item" @click="submit" :class="{ 'disabled' : !touched }">Save</a>
                <a class="dropdown-item">Save as</a>
                <a class="dropdown-item">Export</a>
              </div>
            </div>
          </li>
          <li>
            <div class="btn-group">
              <a type="button" class="btn btn-secondary dropdown-toggle waves-effect waves-light btn-navbar" data-toggle="dropdown" aria-expanded="false">Edit
                <i class="fa fa-angle-down"></i>
              </a>
              <div class="dropdown-menu">
                <a class="dropdown-item">Open</a>
              </div>
            </div>
          </li>
          <li>
            <div class="btn-group">
              <a type="button" class="btn btn-secondary dropdown-toggle waves-effect waves-light btn-navbar" data-toggle="dropdown" aria-expanded="false">View
                <i class="fa fa-angle-down"></i>
              </a>
              <div class="dropdown-menu">
                <a class="dropdown-item" @click="fullscreen_on = !fullscreen_on"><i class="fa fa-check" v-show="fullscreen_on"></i> Fullscreen</a>
              </div>
            </div>
          </li>
          <li>
            <div class="btn-group">
              <a type="button" class="btn btn-secondary dropdown-toggle waves-effect waves-light btn-navbar" data-toggle="dropdown" aria-expanded="false">Help
                <i class="fa fa-angle-down"></i>
              </a>
              <div class="dropdown-menu">
                <a class="dropdown-item">About Bookster venue editor</a>
              </div>
            </div>
          </li>
        </ul>

        <ul class="pull-right">
          <li>
            <a class="btn btn-secondary" @click="fullscreen_on = true" v-if="!fullscreen_on">
              <i class="fa fa-expand"></i>
            </a>
            <a class="btn btn-secondary" @click="fullscreen_on = false" v-else>
              <i class="fa fa-compress"></i>
            </a>
          </li>
        </ul>
      </div>

      <div class="editor-toolbar" v-if="canvas != null">
        <ul>
          <li class="disabled">
            <a>
              <i class="fa fa-file-text-o" aria-hidden="true"></i>
            </a>
          </li>
          <li class="disabled">
            <a>
              <i class="fa fa-folder-open" aria-hidden="true"></i>
            </a>
          </li>
          <li :class="{ 'disabled' : !touched }">
            <a @click="submit">
              <i class="fa fa-save"></i>
            </a>
          </li>
          <li :class="{ 'disabled' : !historyManager.canGoBack() }">
            <a @click="goHistory(-1)">
              <i class="fa fa-arrow-left"></i>
            </a>
          </li>
          <li :class="{ 'disabled' : !historyManager.canGoForward() }">
            <a @click="goHistory(1)">
              <i class="fa fa-arrow-right"></i>
            </a>
          </li>

          <li>
            <div class="btn-group">
              <a type="button" class="btn btn-secondary dropdown-toggle waves-effect waves-light btn-navbar" data-toggle="dropdown" aria-expanded="false">Add building
                <i class="fa fa-angle-down"></i>
              </a>
              <div class="dropdown-menu">
                <a class="dropdown-item" v-for="(fobj, index) in fabric_building" :key="index" @click="addObject(fobj)">{{ $t(fobj.code) }}</a>
              </div>
            </div>
            <div class="btn-group">
              <a type="button" class="btn btn-secondary dropdown-toggle waves-effect waves-light btn-navbar" data-toggle="dropdown" aria-expanded="false">Add object
                <i class="fa fa-angle-down"></i>
              </a>
              <div class="dropdown-menu">
                <a class="dropdown-item" v-for="(fobj, index) in fabric_objects" :key="index" @click="addObject(fobj)">{{ $t(fobj.code) }}</a>
              </div>
            </div>
          </li>
        </ul>
        <ul class="pull-right">
          <li>
            <a @click="toggleDrawingMode" :class="{ 'selected' : canvas.isDrawingMode }">
              <i class="fa fa-pencil"></i>
            </a>
          </li>
          <li>
            <a @click="zoom(-0.2)">
              <i class="fa fa-minus"></i>
            </a>
          </li>
          <li>
            <a @click="zoom(0.2)">
              <i class="fa fa-plus"></i>
            </a>
          </li>

        </ul>
      </div>

      <canvas id="fabric-canvas">
      </canvas>
    </div>
  </fullscreen>
</template>

<script>
import { fabric } from 'fabric';
import * as fabric_objects from '../services/fabric/objects';
import * as fabric_building from '../services/fabric/building';
import { calculateHash } from '../services/helpers';
import HistoryManager from '../services/HistoryManager';

export default {
  name: 'venue-editor',
  props: {
    json: {
      type: String,
      default: null
    }
  },
  data () {
    return {
      canvas: null,
      history: [],
      historyManager: null,
      fullscreen_on: false
    };
  },
  watch: {
    json (newValue) {
      this.canvas.loadFromJSON(newValue, () => {
        this.canvas.renderAll();
        this.historyManager = new HistoryManager(JSON.stringify(this.canvas));
      });
    }
  },
  mounted () {
    let canvas = new fabric.Canvas(document.getElementById('fabric-canvas'), {
      allowTouchScrolling: true
    });

    this.canvas = canvas;

    fabric.RoundTable = fabric_objects.RoundTable;
    fabric.Seat = fabric_objects.Seat;
    fabric.SeatsInRows = fabric_objects.SeatsInRows;
    fabric.SquaredTable = fabric_objects.SquaredTable;
    fabric.Room = fabric_building.Room;

    var grid = 20;

    let calibrateSize = () => {
      canvas.setWidth(this.$el.scrollWidth);
      let height = this.fullscreen_on ? window.screen.height - 75 : window.innerHeight - 315;
      canvas.setHeight(height);
    };

    canvas.on('object:moving', (e) => {
      e.target.set({
        left: Math.round(e.target.left / grid) * grid,
        top: Math.round(e.target.top / grid) * grid
      });
    });

    canvas.on('object:modified', (e) => {
      this.historyManager.putItem(JSON.stringify(this.canvas));
    });

    window.addEventListener('resize', calibrateSize);
    calibrateSize();

    if (this.json != null) {
      canvas.loadFromJSON(this.json, () => {
        canvas.renderAll();
        this.historyManager = new HistoryManager(JSON.stringify(canvas));
      });
    }

    this.historyManager = new HistoryManager(JSON.stringify(canvas));
  },
  computed: {
    fabric_objects () {
      return fabric_objects;
    },
    fabric_building () {
      return fabric_building;
    },
    touched () {
      return calculateHash(this.json) !== calculateHash(JSON.stringify(this.canvas));
    }
  },
  methods: {
    addObject (Obj) {
      this.canvas.add(new Obj({
        width: 75,
        height: 75,
        fill: '#ccc',
        top: 100,
        left: 100
      }));
    },
    zoom (value) {
      let coef = 1 + value;
      this.canvas.setZoom(this.canvas.getZoom() * coef);
    },
    toggleDrawingMode () {
      this.canvas.isDrawingMode = !this.canvas.isDrawingMode;
    },
    submit () {
      this.$emit('submit', JSON.stringify(this.canvas));
    },
    goHistory (direction) {
      if (direction > 0) {
        this.historyManager.goForward();
      } else {
        this.historyManager.goBack();
      }

      this.canvas.loadFromJSON(this.historyManager.currentVersion, () => {
        this.canvas.renderAll();
      });
    }
  }
};
</script>

<style lang="less">
.venue-editor {
  background-color: #fff;

  .editor-toolbar {
    background-color: #fff;
    border-bottom: 1px solid #eee;

    ul {
      display: inline-flex;
      margin: 0;
      padding: 0;
      list-style-type: none;

      li {
        display: inline-block;

        a {
          padding: 10px 15px;
          color: #000;
          border-right: 1px solid #eee;
          transition: background-color .3s ease;
          display: inline-block;

          &:hover {
            background-color: #eee;
          }

          &.selected {
            background-color: #0f0;
          }
        }

        &.disabled a {
          color: #bbb;
          pointer-events: none;
        }
      }

      .dropdown-item {
        display: block;
        border: none;
      }
    }
  }

  .editor-statusbar {
    background-color: #fff;
    border-bottom: 1px solid #eee;
    font-size: 12px;

    ul {
      display: inline-flex;
      margin: 0;
      padding: 0;
      list-style-type: none;

      li {
        a {
          color: #333;
          transition: background-color .3s ease;
          font-size: inherit;

          &:hover {
            background-color: #eee;
          }
        }
      }

      .dropdown-menu {
        font-size: inherit;
      }

      .dropdown-item {
        display: block;
        border: none;
        padding: 3px 10px;
        font-size: inherit;

        &.disabled {
          color: #bbb;
          pointer-events: none;
        }
      }
    }
  }
}
</style>
