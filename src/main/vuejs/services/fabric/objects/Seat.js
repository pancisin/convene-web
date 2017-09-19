import { fabric } from 'fabric';

/**
 * Round table object representation for fabric js canvas.
 */
export default class Seat extends fabric.Object {

  static get code () {
    return 'venue_editor.objects.seat';
  }

  constructor (offsetX, offsetY, identifier) {
    super({});

    this.offsetX = offsetX;
    this.offsetY = offsetY;
    this.identifier = identifier;
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
    this.type = 'seat';
  }

  _render (ctx) {
    ctx.fillStyle = this.fill;
    ctx.strokeStyle = '#000';

    const pos = {
      x: this.offsetX,
      y: this.offsetY
    };

    ctx.strokeRect(pos.x, pos.y, this.width, this.height);
    ctx.fillText(this.identifier, pos.x + 2, pos.y + Seat.dimension.height / 2 + 2);
  }

  toObject () {
    return {
      type: this.type,
      offsetX: this.offsetX,
      offsetY: this.offsetY
    };
  }

  static fromObject (object, callback) {
    console.warn(object);
  }

  toSVG () {
    const coords = this.calcCoords();
    return `<rect x="${coords.tl.x}" y="${coords.tl.y}" width="${this.width}" height="${this.height}" style="fill:${this.fill};stroke-width:3;stroke:${this.stroke}" />`;
  }
};
