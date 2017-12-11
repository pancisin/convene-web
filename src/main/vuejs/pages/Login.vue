<template>
  <div class="wrapper-page container">
    <product-logo large />

    <login-form />
  </div>
</template>
<script>
import { ProductLogo } from 'elements';
import { LoginForm } from 'elements/forms';

export default {
  components: {
    ProductLogo, LoginForm
  }
};
</script>