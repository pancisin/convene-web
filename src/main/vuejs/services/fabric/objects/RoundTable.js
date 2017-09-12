import { fabric } from 'fabric';
import Seat from './Seat';

/**
 * Round table object representation for fabric js canvas.
 */
export default class RoundTable extends fabric.Object {

  static get code () {
    return 'venue_editor.objects.round_table';
  }

  get radius () {
    return this.width <= this.height ? this.width / 2 : this.height / 2;
  }

  initialize (el, options = {}) {
    super.initialize(el, options);

    this.on('modified', (e) => {
      this.width = this.getWidth();
      this.height = this.getHeight();

      this.adjustSeats();
      this.scale(1);
    });

    this.adjustSeats();
  }

  adjustSeats () {
    const seats_count = Math.round((2 * Math.PI * (this.radius - Seat.dimension.height)) / Seat.dimension.width) - 1;

    this.seats = [];
    for (let i = 0; i < seats_count; i++) {
      this.seats.push(new Seat(Seat.dimension.width / 2, -this.height / 2, i));
    }
  }

  _render (ctx) {
    ctx.fillStyle = this.fill;
    ctx.strokeStyle = '#000';

    ctx.translate(-this.width / 2, -this.height / 2);
    ctx.clearRect(0, 0, this.width, this.height);

    ctx.beginPath();
    ctx.arc(this.width / 2, this.height / 2, this.radius - (Seat.dimension.height + 5), 0, 2 * Math.PI);
    ctx.fill();
    ctx.stroke();

    ctx.translate(this.width / 2, this.height / 2);

    this.seats.forEach((s, index) => {
      ctx.rotate(index * (2 * Math.PI / this.seats.length));
      s.drawObject(ctx);
      ctx.restore();
    });
  }

  toSVG () {
    return this.seats.map(s => {
      return s.toSVG();
    });
  }
};
