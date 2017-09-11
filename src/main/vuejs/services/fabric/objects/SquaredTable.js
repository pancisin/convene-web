import {
  fabric
} from 'fabric';

/**
 * Round table object representation for fabric js canvas.
 */
export default class SquaredTable extends fabric.Object {
  static get code() {
    return 'venue_editor.objects.squared_table';
  }

  get boundingBox() {
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

  initialize(el, options = {}) {
    super.initialize(el, options);

    this.on('modified', (e) => {
      this.width = this.boundingBox.width;
      this.height = this.boundingBox.height;

      this.scale(1);
    });

    this.noScaleCache = true;
    this.originX = 'left';
    this.originY = 'top';
  }

  _render(ctx) {
    ctx.fillStyle = this.fill;
    ctx.strokeStyle = '#000';

    ctx.translate(-this.width / 2, -this.height / 2);
    ctx.clearRect(0, 0, this.width, this.height);

    const chair = {
      width: 15,
      height: 15,
      gap: 5
    };

    ctx.fillRect(0, 20, this.width, this.height - 40);

    for (let i = 0; i < this.width / (chair.width + chair.gap); i++) {
      let gap = i > 0 ? chair.gap : 0;
      ctx.strokeRect(i * (chair.width + gap), 0, chair.width, chair.height);
      ctx.strokeRect(i * (chair.width + gap), this.height - 15, chair.width, chair.height);
    }
  }
};
