<template>
  <div class="wrapper-page container">
    <product-logo large />

    <login-form class="m-t-20" :success="redirect" />
  </div>
</template>
<script>
import { ProductLogo } from 'elements';
import { LoginForm } from 'elements/forms';

export default {
  components: {
    ProductLogo,
    LoginForm
  },
  methods: {
    redirect () {
      var redirect = this.$route.query.redirect
        ? this.$route.query.redirect
        : '/';
      this.$router.push({ path: redirect });
    }
  }
};
</script>

<style lang="less">
.wrapper-page {
  margin: 7.5% auto;
  max-width: 360px;
}
</style>
