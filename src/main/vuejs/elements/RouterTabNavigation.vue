<template>
  <div class="router-tab-navigation">
    <ul class="router-tab-navigation-tabs">
      <li 
        v-for="(route, index) in menu.routes" 
        :key="index"
        :class="{ 'active' : $route.name === route.name }"
        ref="tabs">

        <router-link :to="{ name: route.name }">
          <i class="fa" :class="`fa-${route.icon}`"></i>
          {{ $t(route.code) }}
        </router-link>
      </li>
    </ul>
    <div class="nav-tab-indicator" :style="indicatorStyle"></div>
    <div class="router-tab-navigation-content">
      <slot>
        <router-view></router-view>
      </slot>
    </div>
  </div>
</template>

<script>
import debounce from 'debounce';
export default {
  name: 'router-tab-navigation',
  props: ['menu'],
  data () {
    return {
      indicatorStyle: {}
    };
  },
  mounted () {
    window.onresize = debounce((event) => {
      this.recalculateIndicator();
    }, 200);
    this.recalculateIndicator();
  },
  watch: {
    '$route' () {
      this.$nextTick(this.recalculateIndicator);
    }
  },
  methods: {
    recalculateIndicator () {
      const currentTab = this.$refs.tabs.find(t => t.className.indexOf('active') !== -1);
      this.indicatorStyle = {
        width: `${currentTab.offsetWidth}px`,
        left: `${currentTab.offsetLeft}px`
        // top: `${currentTab.offsetHeight - 4}px`
      };
    }
  }
};
</script>

<style lang="less">
@import (reference) '~less/variables.less';

.router-tab-navigation {
  margin-bottom: 20px;
  position: relative;

  ul.router-tab-navigation-tabs {
    display: flex;
    flex-wrap: nowrap;
    flex-direction: row;
    list-style-type: none;
    margin: 0;
    padding: 0;

    li {
      transition: background-color 0.2s ease;

      a {
        padding: 15px 40px;
        text-align: center;
        display: block;
        color: #333333;
        font-weight: 500;
        transition: color 0.1s ease;
        font-family: 'Roboto', sans-serif;

        &:hover {
          color: @color-primary;
        }
      }

      i {
        margin-right: 10px;
      }

      &.active {
        a {
          color: @color-primary;
        }

        background-color: #fff;
      }
    }
  }

  .router-tab-navigation-content {
    box-shadow: 0px 1px 2px 0px rgba(0, 0, 0, 0.1) !important;
    background: #fff;
    padding: 15px;
    overflow-x: auto;
  }

  .nav-tab-indicator {
    height: 2px;
    position: absolute;
    background: @color-primary;
    transition: all 0.4s ease-out;
    top: 0px;
  }
}

@media (max-width: 767px) {
  .router-tab-navigation {
    ul.router-tab-navigation-tabs {
      flex-direction: column;

      li a {
        text-align: left;
      }
    }

    .nav-tab-indicator {
      display: none;
    }
  }
}
</style>
