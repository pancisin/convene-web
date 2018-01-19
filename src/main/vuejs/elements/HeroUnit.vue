<template>
  <div class="hero-unit" 
    :class="{ 'hero-unit-fluid': !solid, 'hero-overlay': overlay }" 
    :style="bg_style">

    <slot name="background"></slot>

    <div class="hero-unit-content" 
      :style="content_style" 
      ref="hero_unit_content">

      <div class="container">
        <slot>
        </slot>
      </div>
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
    solid: Boolean,
    overlay: {
      type: Boolean,
      default () {
        return true;
      }
    }
  },
  watch: {
    background (newVal) {
      this.bg_style = {
        ...this.bg_style,
        'background-image': `url(${this.background})`
      };
    }
  },
  data () {
    return {
      bg_style: {
        'background-position': 'center',
        'background-image': `url(${this.background})`
      },
      content_style: {
        top: '50%',
        transform: 'translateY(-50%)'
      }
    };
  },
  mounted () {
    window.addEventListener('scroll', this.handleScroll);
    this.$nextTick(() => {
      this.handleScroll();
    });
  },
  destroyed () {
    window.removeEventListener('scroll', this.handleScroll);
  },
  methods: {
    handleScroll () {
      let offset = -(this.$el.offsetTop - window.scrollY);

      if (this.$el.offsetHeight < this.$el.offsetTop) {
        offset = -(this.$el.offsetTop - (window.innerHeight - this.$el.offsetHeight) / 2 - window.scrollY);
      }

      const height = this.$slots.background
        ? this.$slots.background[0].elm.offsetHeight
        : this.$refs.hero_unit_content.offsetHeight + 160;
      this.bg_style = {
        ...this.bg_style,
        'background-position': `center calc(50% + ${offset * 0.5}px)`,
        height: `${height}px`
      };

      this.content_style = {
        ...this.content_style,
        top: `calc(50% + ${offset * 0.3}px)`,
        opacity: 1 / (Math.abs(offset) / 100)
      };
    }
  }
};
</script>

<style lang="less">
@import (reference) '~less/variables.less';
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
  // padding: 80px 0;
  overflow: hidden;
  animation: backgroundAnimation ease-in-out 60s infinite alternate;

  background-size: cover;

  min-height: 225px;
  position: relative;

  &.hero-unit-fluid {
    &:first-child {
      margin-top: -20px;
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

  &:not(.hero-unit-fluid) {
    .container {
      width: 98%;
      margin: 0 auto;
    }
  }

  & > .hero-unit-content {
    position: absolute;
    z-index: 2;
    width: 100%;
  }

  &.hero-overlay {
    &:after {
      content: '';
      z-index: 1;
      // background: rgba(0, 0, 0, 0.6);
      position: absolute;
      width: 100%;
      height: 100%;
      top: 0;
      left: 0;

      background: linear-gradient(#444f5c, #334159);
      opacity: 0.85;
    }
  }
}
</style>
