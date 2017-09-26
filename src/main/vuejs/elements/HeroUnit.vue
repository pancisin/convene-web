<template>
  <div class="hero-unit">
    <img v-if="background" :src="background" class="hero-background" :style="bg_style">
    <div class="container">
      <slot>

      </slot>
    </div>
  </div>
</template>

<script>
export default {
  name: 'hero-unit',
  data () {
    return {
      bg_style: {}
    };
  },
  created () {
    window.addEventListener('scroll', this.handleScroll);
  },
  destroyed () {
    window.removeEventListener('scroll', this.handleScroll);
  },
  methods: {
    handleScroll () {
      this.bg_style = {
        top: `calc(50% + ${window.scrollY * 0.5}px)`
      };
    }
  },
  props: {
    background: {
      type: String,
      default: null
    }
  }
};
</script>

<style lang="less">
@original_transform: rotateZ(-20deg) translateY(-50%);

@keyframes movingAnimation {
  0% {
    opacity: 1;
    transform: @original_transform translateX(0);
  }
  50% {
    opacity: .3;
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
    background-color: desaturate(spin(#3bafda, @index * 50), 10%);
  }
  .color_loop(@n, (@index + 1));
}

@keyframes backgroundAnimation {
  .color_loop(10);
}

.hero-unit {
  color: white;
  padding: 80px 0;
  margin-top: -20px;
  margin-bottom: 20px;
  position: relative;
  overflow: hidden;
  animation: backgroundAnimation ease-in-out 60s infinite alternate;

  &>.container {
    position: relative;
    z-index: 2;
  }

  .hero-background {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    transform: translateY(-50%);
    top: calc(50% + 0px);
    min-height: 100%;
  }

  &:after {
    content: '';
    z-index: 1;
    background: linear-gradient(to bottom, rgba(0, 0, 0, 0.4) 0%, rgba(255, 255, 255, 0.4) 100%);
    position: absolute;
    width: 100%;
    height: 100%;
    top: 0;
    left: 0;
  }

  &:before {
    content: '';
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
</style>
