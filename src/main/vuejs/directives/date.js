import moment from 'moment'
export default {
  update(timestamp) {
    this.el.innerHTML = moment(timestamp).fromNow()
  },
}
