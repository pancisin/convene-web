<template>
  <div class="wrapper-page">
  
    <div class="text-center">
      <router-link to="/" class="logo-lg">
        <i class="fa fa-book"></i>
        <span>Bookster</span>
      </router-link>

      <p class="text-muted">
        is now verifying your token, please wait...
      </p>
    </div>
  
  </div>
</template>

<script>
export default {
  name: 'confirm-email',
  created () {
    if (this.$route.query.token) {
      this.verify(this.$route.query.token);
    }
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