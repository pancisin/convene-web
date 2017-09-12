<template>
  <div class="venue-editor">
    <div class="editor-toolbar" v-if="canvas != null">
      <ul>
        <li v-for="(fobj, index) in fabric_objects" :key="index">
          <a @click="addObject(fobj)">{{ $t(fobj.code) }}</a>
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

    <img :src="image" />
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
    var grid = 10;

    function snapToGrid (options) {
      options.target.set({
        left: Math.round(options.target.left / grid) * grid,
        top: Math.round(options.target.top / grid) * grid
      });
    }

    let calibrateSize = () => {
      canvas.setWidth(this.$el.scrollWidth);
      canvas.setHeight(window.innerHeight - 200);
    };

    calibrateSize();

    canvas.on('object:moving', snapToGrid);
    canvas.on('object:scaling', (e) => {

    });

    window.addEventListener('resize', calibrateSize);
    this.canvas = canvas;
  },
  computed: {
    fabric_objects () {
      return fabric_objects;
    },
    image () {
      if (this.canvas != null) {
        return this.canvas.toDataURL();
      }
    }
  },
  methods: {
    addObject (Obj) {
      this.canvas.add(new Obj({
        width: 100,
        height: 100,
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
