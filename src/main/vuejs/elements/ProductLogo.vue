<template>
   <div class="product-logo" :style="style">
      <router-link 
        to="/"
        :class="`text-${type}`">

        <span>convene</span>
        <slot></slot>
      </router-link>
    </div>
</template>

<script>
export default {
  name: 'product-logo',
  props: {
    large: Boolean,
    inline: Boolean,
    type: String
  },
  computed: {
    style () {
      return {
        'font-size': this.large ? '32px' : '21px',
        'float': this.inline ? 'left' : 'none',
        'margin-top': this.inline ? '7px' : 0,
        'text-align': 'center'
      };
    }
  }
};
</script>

<style lang="less">
@import url('https://fonts.googleapis.com/css?family=Comfortaa:400,700');
@import (reference) '~less/variables.less';

.product-logo {
  font-family: 'Comfortaa', cursive;
  line-height: 47px;

  a,
  & a.active {
    color: @color-primary;
  }
}

#topnav {
  .product-logo {
    a, & a.active {
      color: #fff !important;
    }
  }
}

@media (max-width: 400px) {
  #topnav {
    .product-logo {
      display: none;
    }
  }
}

</style>
