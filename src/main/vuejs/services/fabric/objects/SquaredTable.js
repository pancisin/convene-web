import { fabric } from 'fabric';
import Seat from './Seat';

/**
 * Round table object representation for fabric js canvas.
 */
export default class SquaredTable extends fabric.Object {

  static get code () {
    return 'venue_editor.objects.squared_table';
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
    this.type = 'squared-table';
  }

  adjustSeats () {
    const dim = Seat.dimension;

    this.seats = [];
    var i = 0;
    for (let col = 5 / 2; col < this.width; col += dim.width + 5) {
      this.seats.push(new Seat(col, 0, i++));
      this.seats.push(new Seat(col, this.height - dim.height, i++));
    }
  }

  _render (ctx) {
    ctx.fillStyle = this.fill;
    ctx.strokeStyle = '#000';

    ctx.translate(-this.width / 2, -this.height / 2);
    ctx.clearRect(0, 0, this.width, this.height);

    ctx.fillRect(0, 20, this.width, this.height - 40);
    ctx.strokeRect(0, 20, this.width, this.height - 40);

    this.seats.forEach(s => {
      s.drawObject(ctx);
    });
  }

  toObject () {
    return {
      ...super.toObject(),
      seats: this.seats
    };
  }

  static fromObject (object, callback) {
    const table = new SquaredTable({
      ...object
    });

    callback(table);
  }
};
