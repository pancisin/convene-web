<template>
  <div class="wrapper-page">
  
    <div class="text-center">
      <product-logo large />

      <p class="text-muted">
        is now verifying your token, please wait...
      </p>
    </div>
  
  </div>
</template>

<script>
import { ProductLogo } from 'elements';

export default {
  name: 'confirm-email',
  created () {
    if (this.$route.query.token) {
      this.verify(this.$route.query.token);
    }
  },
  components: {
    ProductLogo
  },
  methods: {
    verify (token) {
      this.$http.put('verify', token).then(response => {
        this.$router.push('/');
      }, response => {
        console.error(response);
      });
    }
  }
};
</script>