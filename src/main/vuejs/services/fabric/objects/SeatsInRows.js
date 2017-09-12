import { fabric } from 'fabric';
import Seat from './Seat';

/**
 * Round table object representation for fabric js canvas.
 */
export default class SeatsInRows extends fabric.Object {

  static get code () {
    return 'venue_editor.objects.seats_in_rows';
  }

  adjustSeats () {
    const gap = 5;
    const dimension = Seat.dimension;
    this.seats = [];

    var i = 0;
    for (var row = gap / 2; row < this.height; row += dimension.height + gap) {
      for (var col = gap / 2; col < this.width; col += dimension.width + gap) {
        this.seats.push(new Seat(row, col, i++));
      }
    }
  }

  initialize (el, options = {}) {
    super.initialize(el, options);

    this.on('modified', (e) => {
      this.width = this.getWidth();
      this.height = this.getHeight();
      this.adjustSeats();

      this.scale(1);
    });

    this.seats = [];
    this.adjustSeats();
    this.type = 'seats-in-rows';
  }

  _render (ctx) {
    ctx.fillStyle = this.fill;
    ctx.strokeStyle = '#000';

    ctx.translate(-this.width / 2, -this.height / 2);
    ctx.clearRect(0, 0, this.width, this.height);

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
    const table = new SeatsInRows({
      ...object
    });

    callback(table);
  }
};
