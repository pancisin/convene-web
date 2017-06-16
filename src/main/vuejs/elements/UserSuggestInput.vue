<template>
  <div class="suggest-input">
    <input class="form-control" @keyup="search($event); update($event.target.value)" v-model="selected" @focus="collapsed = false" @blur="collapsed = true" />
  
    <transition name="fade-up">
      <div class="suggest-box" v-show="!collapsed">
        <div class="inbox-widget">
          <a @click="select(option)" v-for="option in options">
            <div class="inbox-item">
              <div class="inbox-item-img">
                <img :src="getAvatar(option)" class="img-circle">
              </div>
              <p class="inbox-item-author">{{ option.firstName }} {{ option.lastName }}</p>
              <p class="inbox-item-text" v-text="option.email">
              </p>
            </div>
          </a>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import debounce from 'debounce'
import gravatar from 'gravatar'

export default {
  props: {
    value: String,
    options: Array,
  },
  watch: {
    value(v) {
      this.selected = v;
    }
  },
  data() {
    return {
      loading: false,
      selected: null,
      collapsed: true,
    }
  },
  methods: {
    search: debounce(function (e) {
      this.collapsed = false;
      this.$emit('search',
        e.target.value,
        (v) => {
          this.loading = v;
        }
      );
    }, 500),
    select(option) {
      this.collapsed = true;
      this.selected = option.firstName + ' ' + option.lastName;
      this.update(option.email);
    },
    update(value) {
      this.$emit('input', value)
    },
    getAvatar(user) {
      return gravatar.url(user.email, {
        protocol: 'https',
        size: 30
      });
    }
  }
}
</script>

<style lang="less">
.suggest-input {
  position: relative;

  .suggest-box {
    position: absolute;
    background: #fff;
    width: 100%;
    border: 1px solid #ccc;
    max-height: 350px;
    overflow-y: auto;
    left: 0;
    top: 34px;

    .inbox-item {
      padding-left: 10px;
      padding-right: 10px;
    }
  }
}
</style>
