import { fabric } from 'fabric';

/**
 * Round table object representation for fabric js canvas.
 */
export default class SeatsInRows extends fabric.Object {
  static get code () {
    return 'venue_editor.objects.seats_in_rows';
  }

  get boundingBox () {
    const coords = this.calcCoords();

    var wx = coords.mr.x - coords.ml.x;
    var wy = coords.mr.y - coords.ml.y;

    var hx = coords.mb.x - coords.mt.x;
    var hy = coords.mb.y - coords.mt.y;

    return {
      width: Math.sqrt(Math.pow(wx, 2) + Math.pow(wy, 2)),
      height: Math.sqrt(Math.pow(hx, 2) + Math.pow(hy, 2))
    };
  }

  initialize (el, options = {}) {
    super.initialize(el, options);

    this.on('modified', (e) => {
      this.width = this.boundingBox.width;
      this.height = this.boundingBox.height;

      this.scale(1);
    });
  }

  _render (ctx) {
    ctx.fillStyle = this.fill;
    ctx.strokeStyle = '#000';

    ctx.translate(-this.width / 2, -this.height / 2);
    ctx.clearRect(0, 0, this.width, this.height);

    const chair = {
      width: 15,
      height: 15,
      gap: 5
    };

    for (var row = chair.gap / 2; row < this.height; row += chair.height + chair.gap) {
      for (var col = chair.gap / 2; col < this.width; col += chair.width + chair.gap) {
        ctx.strokeRect(col, row, chair.width, chair.height);
      }
    }
  }
};
