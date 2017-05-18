<template>
  <div class="time-picker-container" v-click-outside="outside">
    <input type="text" ref="input" :placeholder="placeholder" :value="timeString" class="form-control" @focus="focusChanged" @blur="focusChanged">
  
    <transition name="slide-down">
      <div class="time-picker" v-show="displayTimePicker">
        <div class="hour-selector">
          <button @click="offsetHour(1)" class="btn btn-link">
            <i class="fa fa-angle-up"></i>
          </button>
          <div class="hour" v-text="time.hour"></div>
          <button @click="offsetHour(-1)" class="btn btn-link">
            <i class="fa fa-angle-down"></i>
          </button>
        </div>
  
        <span class="separator">:</span>
  
        <div class="minute-selector">
          <button @click="offsetMinute(15)" class="btn btn-link">
            <i class="fa fa-angle-up"></i>
          </button>
          <div class="minute" v-text="time.minute"></div>
          <button @click="offsetMinute(-15)" class="btn btn-link">
            <i class="fa fa-angle-down"></i>
          </button>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import moment from "moment"
export default {
  name: 'time-picker',
  props: ['value', 'placeholder'],
  data() {
    return {
      displayTimePicker: false,
      time: {
        hour: parseInt(moment().format("HH")),
        minute: 0,
        second: 0,
      },
      focus: false,
    }
  },
  computed: {
    timeString: {
      get() {
        var min = this.time.minute == 0 ? "00" : this.time.minute;
        return this.time.hour + ":" + min + ":00";
      }
    }
  },
  created() {
      this.$emit('input', this.timeString);
  },
  watch: {
    focus(newVal) {
      if (newVal) this.displayTimePicker = true;
    },
    'value'(newVal) {
      this.selected = newVal;
    }
  },
  methods: {
    focusChanged(e) {
      this.focus = e.type == "focus";
    },
    outside: function () {
      if (focus) this.displayTimePicker = false;
    },
    offsetHour(value) {
      var new_val = this.time.hour + value;

      if (new_val >= 0 && new_val < 60) {
        this.time.hour = new_val;
          this.$emit('input', this.timeString);
      }
    },
    offsetMinute(value) {
      var new_val = this.time.minute + value

      if (new_val >= 0 && new_val < 60) {
        this.time.minute = new_val;
        this.$emit('input', this.timeString);
      }
    }
  }
}
</script>

<style lang="less">
.time-picker-container {
  position: relative;

  .time-picker-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    text-transform: uppercase;
  }

  .time-picker {
    position: absolute;
    left: 0;
    background: #fff;
    z-index: 2;
    border: 1px solid #ccc;
    padding: 10px;
    overflow: hidden;
    font-size: 25px;

    .hour-selector,
    .minute-selector {
      display: inline-block;
      vertical-align: middle;
      text-align: center;
    }
  }
}
</style>
