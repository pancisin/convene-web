const HISTORY_SIZE = 15;

export default class HistoryManager {

  constructor (state) {
    this.history = [];
    this.history.push(state);
    this.pointer = 0;
  }

  get historyLength () {
    return this.history.length > 1 ? this.history.length - 1 : 0;
  }

  get currentVersion () {
    return this.history[this.pointer];
  }

  putItem (item) {
    if (this.pointer === this.history.length - 1) {
      this.history.push(item);
    } else {
      this.history.splice(this.pointer + 1, this.history.length - this.pointer - 1, item);
    }

    this.pointer = this.history.length - 1;
  }

  goBack () {
    this.pointer -= this.pointer > 0 ? 1 : 0;
  }

  goForward () {
    this.pointer += this.pointer + 1 < this.history.length ? 1 : 0;
  }

  canGoBack () {
    return this.history.length > 0 && this.pointer > 0;
  }

  canGoForward () {
    return this.history.length > this.pointer + 1;
  }
};
