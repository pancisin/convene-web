<template>
  <canvas id="fabric-canvas" width="500" height="500">

  </canvas>
</template>

<script>
import { fabric } from 'fabric';
export default {
  name: 'fabric-canvas',
  data () {
    return {
      serialized: null,
    }
  },
  mounted () {
    let canvas = new fabric.Canvas(this.$el);

    var grid = 10;
    for (var i = 0; i < (600 / grid); i++) {
      canvas.add(new fabric.Line([i * grid, 0, i * grid, 600], { stroke: '#eee', selectable: false }));
      canvas.add(new fabric.Line([0, i * grid, 600, i * grid], { stroke: '#eee', selectable: false }))
    }

    var rect = new fabric.Rect({
      left: 100,
      top: 100,
      fill: "#FF0000",
      stroke: "#000",
      width: 100,
      height: 100,
      strokeWidth: 10,
      opacity: .8
    });

    function snapToGrid (options) {
      options.target.set({
        left: Math.round(options.target.left / grid) * grid,
        top: Math.round(options.target.top / grid) * grid
      });
    }

    canvas.on('object:moving', snapToGrid);
    canvas.on('object:scaling', snapToGrid);

    canvas.add(rect);
  },
  methods: {

  }
}
</script>

<style>

</style>
