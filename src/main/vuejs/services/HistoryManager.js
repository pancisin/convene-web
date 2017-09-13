// const history_size = 15;

export default class HistoryManager {

  constructor () {
    this.history = [];
    this.pointer = 0;
  }

  get currentVersion () {
    return this.history[this.pointer];
  }

  putItem (item) {
    if (this.history.length === this.pointer + 1) {
      this.history.push(item);
    } else {
      this.history.splice(this.pointer, this.history.length - this.pointer + 1, item);
    }

    this.pointer = this.history.length - 1;
  }

  goBack () {
    this.pointer -= 1;
  }

  goForward () {
    this.pointer += 1;
  }

  canGoBack () {
    return this.pointer - 1 >= 0;
  }

  canGoForward () {
    return this.history.length > this.pointer + 1;
  }

};
