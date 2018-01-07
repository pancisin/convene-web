<template>
  <div class="wrapper-page container">
    <product-logo large />

    <signup-form />
  </div>
</template>

<script>
import { ProductLogo } from 'elements';
import { SignupForm } from 'elements/forms';

export default {
  components: {
    SignupForm,
    ProductLogo
  }
};
</script>

<style lang="less">
.wrapper-page {
  margin: 7.5% auto;
  max-width: 360px;
}
</style>