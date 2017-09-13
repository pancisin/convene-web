<template>
  <div class="venue-editor">
    <div class="editor-toolbar">
      <ul>
        <li>
          <div class="btn-group">
            <a type="button" class="btn btn-secondary dropdown-toggle waves-effect waves-light btn-navbar" data-toggle="dropdown" aria-expanded="false">File
              <i class="fa fa-angle-down"></i>
            </a>
            <div class="dropdown-menu">
              <a class="dropdown-item">Open</a>
              <a class="dropdown-item">Save</a>
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
              <a class="dropdown-item">Open</a>
            </div>
          </div>
        </li>
        <li>
          <div class="btn-group">
            <a type="button" class="btn btn-secondary dropdown-toggle waves-effect waves-light btn-navbar" data-toggle="dropdown" aria-expanded="false">Help
              <i class="fa fa-angle-down"></i>
            </a>
            <div class="dropdown-menu">
              <a class="dropdown-item">Open</a>
            </div>
          </div>
        </li>
      </ul>
    </div>

    <div class="editor-toolbar" v-if="canvas != null">
      <ul>
        <li>
          <a @click="submit">
            <i class="fa fa-save"></i>
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
</template>

<script>
import { fabric } from 'fabric';
import * as fabric_objects from '../services/fabric/objects';
import * as fabric_building from '../services/fabric/building';

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
    fabric.Room = fabric_building.Room;

    const venue_stored = window.localStorage.getItem('venue_stored');
    if (venue_stored != null) {
      canvas.loadFromJSON(venue_stored, () => {
        canvas.renderAll();
      });
    }

    var grid = 20;
    var edgedetection = 20; //pixels to snap

    let calibrateSize = () => {
      canvas.setWidth(this.$el.scrollWidth);
      canvas.setHeight(window.innerHeight - 315);
    };

    calibrateSize();

    canvas.on('object:moving', function (e) {
      var obj = e.target;
      obj.setCoords(); // Sets corner position coordinates based on current angle, width and height

      if (obj.getLeft() < edgedetection) {
        obj.setLeft(0);
      }

      if (obj.getTop() < edgedetection) {
        obj.setTop(0);
      }

      if ((obj.getWidth() + obj.getLeft()) > (canvas.getWidth() - edgedetection)) {
        obj.setLeft(canvas.getWidth() - obj.getWidth());
      }

      if ((obj.getHeight() + obj.getTop()) > (canvas.getHeight() - edgedetection)) {
        obj.setTop(canvas.getHeight() - obj.getHeight());
      }

      canvas.forEachObject(function (targ) {
        var activeObject = canvas.getActiveObject();

        if (targ === activeObject) return;


        if (Math.abs(activeObject.oCoords.tr.x - targ.oCoords.tl.x) < edgedetection) {
          activeObject.left = targ.left - activeObject.currentWidth;
        }
        if (Math.abs(activeObject.oCoords.tl.x - targ.oCoords.tr.x) < edgedetection) {
          activeObject.left = targ.left + targ.currentWidth;
        }
        if (Math.abs(activeObject.oCoords.br.y - targ.oCoords.tr.y) < edgedetection) {
          activeObject.top = targ.top - activeObject.currentHeight;
        }
        if (Math.abs(targ.oCoords.br.y - activeObject.oCoords.tr.y) < edgedetection) {
          activeObject.top = targ.top + targ.currentHeight;
        }
        if (activeObject.intersectsWithObject(targ) && targ.intersectsWithObject(activeObject)) {
          targ.strokeWidth = 10;
          targ.stroke = 'red';
        } else {
          targ.strokeWidth = 0;
          targ.stroke = false;
        }
        if (!activeObject.intersectsWithObject(targ)) {
          activeObject.strokeWidth = 0;
          activeObject.stroke = false;
        }
      });
    });

    window.addEventListener('resize', calibrateSize);
    this.canvas = canvas;
  },
  computed: {
    fabric_objects () {
      return fabric_objects;
    },
    fabric_building () {
      return fabric_building;
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

      .dropdown-item {
        display: block;
        border: none;
      }
    }
  }
}
</style>
