<template>
  <div class="venue-editor">
    <div class="editor-toolbar" v-if="canvas != null">
      <ul>
        <li v-for="(fobj, index) in fabric_objects" :key="index">
          <a @click="addObject(fobj)">{{ $t(fobj.code) }}</a>
        </li>
        <li>
          <a @click="submit">Save</a>
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
</template>

<script>
import { fabric } from 'fabric';
import * as fabric_objects from '../services/fabric/objects';

export default {
  name: 'fabric-canvas',
  data () {
    return {
      canvas: null
    };
  },
  mounted () {
    let canvas = new fabric.Canvas(document.getElementById('fabric-canvas'), {
      allowTouchScrolling: true
    });

    fabric.RoundTable = fabric_objects.RoundTable;
    fabric.Seat = fabric_objects.Seat;
    fabric.SeatsInRows = fabric_objects.SeatsInRows;
    fabric.SquaredTable = fabric_objects.SquaredTable;

    const venue_stored = window.localStorage.getItem('venue_stored');
    if (venue_stored != null) {
      canvas.loadFromJSON(venue_stored, () => {
        canvas.renderAll();
      });
    }

    var grid = 20;

    let calibrateSize = () => {
      canvas.setWidth(this.$el.scrollWidth);
      canvas.setHeight(window.innerHeight - 200);
    };

    calibrateSize();

    canvas.on('object:moving', (e) => {
      e.target.set({
        left: Math.round(e.target.left / grid) * grid,
        top: Math.round(e.target.top / grid) * grid
      });
    });

    window.addEventListener('resize', calibrateSize);
    this.canvas = canvas;
  },
  computed: {
    fabric_objects () {
      return fabric_objects;
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
      window.localStorage.setItem('venue_stored', JSON.stringify(this.canvas));
      this.$success('Venue layout saved !', '');
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
      }
    }

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
  }
}
</style>
