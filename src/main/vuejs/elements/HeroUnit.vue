<template>
  <div class="hero-unit" 
    :class="{ 'hero-unit-fluid': !solid }" 
    :style="bg_style">

    <div class="container">
      <slot>
      </slot>
    </div>
  </div>
</template>

<script>
export default {
  name: 'hero-unit',
  props: {
    background: {
      type: String,
      default: null
    },
    solid: Boolean
  },
  data () {
    return {
      bg_style: {
        'background-position': 'center',
        'background-image': `url(${this.background})`
      }
    };
  },
  mounted () {
    window.addEventListener('scroll', this.handleScroll);
  },
  destroyed () {
    window.removeEventListener('scroll', this.handleScroll);
  },
  methods: {
    handleScroll () {
      this.bg_style = {
        ...this.bg_style,
        'background-position': `center calc(50% + ${-(this.$el.offsetTop - window.scrollY) * 0.5}px)`
      };
    }
  }
};
</script>

<style lang="less">
@import (reference) "~less/variables.less";
@original_transform: rotateZ(-20deg) translateY(-50%);

@keyframes movingAnimation {
  0% {
    opacity: 1;
    transform: @original_transform translateX(0);
  }
  50% {
    opacity: 0.3;
    transform: @original_transform translateX(50px);
  }
  100% {
    opacity: 1;
    transform: @original_transform translateX(0);
  }
}

.color_loop (@n, @index: 0) when (@index <=@n) {
  @keyframeSel: @index/@n * 100%;
  @{keyframeSel} {
    background-color: desaturate(spin(@color-primary, @index * 50), 20%);
  }
  .color_loop(@n, (@index + 1));
}

@keyframes backgroundAnimation {
  .color_loop(10);
}

.hero-unit {
  color: white;
  padding: 80px 0;
  position: relative;
  overflow: hidden;
  animation: backgroundAnimation ease-in-out 60s infinite alternate;

  background-size: cover;


  &.hero-unit-fluid {
    &:first-child {
      margin-top: -20px;
    }

    &:before {
      content: "";
      position: absolute;
      background-color: rgba(255, 255, 255, 0.6);
      z-index: 1;
      width: 40%;
      height: 250%;
      left: 0;
      top: 50%;
      transform: @original_transform;
      animation: movingAnimation ease 10s infinite;
    }
  }

  &:not(.hero-unit-fluid) {
    .container {
      width: 98%;
      margin: 0 auto;
    }
  }

  & > .container {
    position: relative;
    z-index: 2;
  }

  &:after {
    content: "";
    z-index: 1;
    // background: rgba(0, 0, 0, 0.6);
    position: absolute;
    width: 100%;
    height: 100%;
    top: 0;
    left: 0;

    background: linear-gradient(#444F5C, #334159);
    opacity: .85;
  }
}
</style>
