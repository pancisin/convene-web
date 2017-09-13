import { fabric } from 'fabric';

export default class Room extends fabric.Rect {

  static get code () {
    return 'venue_editor.building.room';
  }

  initialize (el, options = {}) {
    super.initialize(el, options);

    this.on('mouseover', () => {
      this.strokeStyle = '#F00';
      console.warn('mouse over');
    });

    this.on('modified', (e) => {
      this.width = this.getWidth();
      this.height = this.getHeight();

      this.scale(1);
    });

    this.type = 'room';
  }

  _render (ctx) {
    ctx.strokeStyle = '#000';
    ctx.lineWidth = 10;

    ctx.translate(-this.width / 2, -this.height / 2);
    ctx.strokeRect(0, 0, this.width, this.height);
  }

  static fromObject (object, callback) {
    const table = new Room({
      ...object
    });

    callback(table);
  }
};
