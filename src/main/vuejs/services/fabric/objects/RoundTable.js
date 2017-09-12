import { fabric } from 'fabric';

/**
 * Round table object representation for fabric js canvas.
 */
export default class RoundTable extends fabric.Object {
  static get code () {
    return 'venue_editor.objects.round_table';
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

    this.noScaleCache = true;
    this.originX = 'left';
    this.originY = 'top';
    this.seats = 0;
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

    ctx.beginPath();
    let radius = this.width <= this.height ? this.width / 2 : this.height / 2;
    ctx.arc(this.width / 2, this.height / 2, radius - (chair.height + chair.gap), 0, 2 * Math.PI);
    ctx.fill();
    ctx.stroke();

    this.seats = Math.round((2 * Math.PI * (radius - chair.height)) / chair.width) - 1;

    ctx.translate(this.width / 2, this.height / 2);
    for (let rotation = 0; rotation < 2 * Math.PI; rotation += 2 * Math.PI / this.seats) {
      ctx.rotate(rotation);
      ctx.strokeRect(-chair.width / 2, -this.height / 2, chair.width, chair.height);
      ctx.restore();
    }
  }
};
