<template>
  <div class="hero-unit">
    <img v-if="background" :src="background" class="hero-background">
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

.hero-unit {
  color: white;
  padding: 80px 0;
  margin-top: -20px;
  margin-bottom: 20px;
  position: relative;
  overflow: hidden;
  background-color: #3bafda;

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
    top: 50%;
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
    height: 200%;
    left: 0;
    top: 50%;
    transform: @original_transform;
    animation: movingAnimation ease 10s infinite;
  }
}
</style>
