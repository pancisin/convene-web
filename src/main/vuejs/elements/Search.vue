<template>
  <form role="search"
    class="app-search">
    <input type="text"
      placeholder="Search..."
      class="form-control"
      v-model="q">
    <a href="">
      <i class="fa fa-search"></i>
    </a>
  </form>
</template>

<script>
import EventApi from 'api/event.api';
export default {
  name: 'app-search',
  data () {
    return {
      q: ''
    };
  },
  subscriptions () {
    return {
      data: this.$watchAsObservable('q')
        .pluck('newValue')
        .filter(q => !!q)
        .debounceTime(250)
        .map(e => e.toUpperCase())
        .do(q => {
          EventApi.searchEvents(q, events => {
            console.log(events);
          })
        })
    };
  }
};
</script>

<style lang="less">
.app-search {
  position: relative;
  margin: 14px 20px 14px 10px;
  a {
    position: absolute;
    top: 6px;
    right: 20px;
    color: rgba(255, 255, 255, 0.7);
  }
  .form-control {
    border: none;
    font-size: 13px;
    color: #ffffff;
    padding-left: 20px;
    padding-right: 40px;
    background: rgba(255, 255, 255, 0.2);
    box-shadow: none;
    border-radius: 30px;
    height: 34px;
    width: 180px;
    &:focus {
      border: none;
      font-size: 13px;
      color: #ffffff;
      padding-left: 20px;
      padding-right: 40px;
      background: rgba(255, 255, 255, 0.2);
      box-shadow: none;
      border-radius: 30px;
      height: 34px;
      width: 180px;
    }
  }
  input {
    &::-webkit-input-placeholder {
      color: rgba(255, 255, 255, 0.7);
      font-weight: normal;
    }
    &:-moz-placeholder {
      color: rgba(255, 255, 255, 0.7);
    }
    &::-moz-placeholder {
      color: rgba(255, 255, 255, 0.7);
    }
    &:-ms-input-placeholder {
      color: rgba(255, 255, 255, 0.7);
    }
  }
}
</style>
