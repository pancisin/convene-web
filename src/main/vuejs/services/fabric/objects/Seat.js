import { fabric } from 'fabric';

/**
 * Round table object representation for fabric js canvas.
 */
export default class Seat extends fabric.Object {

  static get code () {
    return 'venue_editor.objects.seat';
  }

  constructor (offsetX, offsetY) {
    super({});

    this.offsetX = offsetX;
    this.offsetY = offsetY;
  }

  static get dimension () {
    return {
      width: 15,
      height: 15
    };
  }

  initialize (el) {
    super.initialize(el);

    this.on('mouseover', () => {
      this.strokeStyle = '#F00';
      console.warn('mouse over');
    });

    this.lockScalingX = true;
    this.lockScalingY = true;
    this.lockUniScaling = true;
    this.width = Seat.dimension.width;
    this.height = Seat.dimension.height;
  }

  _render (ctx) {
    ctx.fillStyle = this.fill;
    ctx.strokeStyle = '#000';

    const pos = {
      x: this.offsetX || -this.width / 2,
      y: this.offsetY || -this.height / 2
    };

    ctx.strokeRect(pos.x, pos.y, this.width, this.height);
    ctx.fillText('s', pos.x + 2, pos.y + Seat.dimension.height / 2);
  }

  toSVG () {
    const coords = this.calcCoords();
    return `<rect x="${coords.tl.x}" y="${coords.tl.y}" width="${this.width}" height="${this.height}" style="fill:${this.fill};stroke-width:3;stroke:${this.stroke}" />`;
  }
};
