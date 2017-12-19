<template>
  <div class="error-component">
    <div class="error-component-content">
      <h1>{{ status }}</h1>

      <panel class="m-b-20">
        <p>
          We are so sorry. {{ message }} Or try it later, it might help sometimes.
        </p>
        <br />
        <a @click="goBack" class="btn btn-primary">
          Go back
        </a>
      </panel>
    </div>
  </div>
</template>

<script>
export default {
  name: 'error',
  props: {
    status: String | Number
  },
  computed: {
    message () {
      switch (this.status) {
        case 403:
          return 'You have not access rights to this resource. If you feel that you can gain them try to contact system administrator.';
        case 404:
          return 'Resource cannot be found.';
        case 500:
          return 'Internal server error ! We are so sorry.';
      }
    }
  },
  methods: {
    goBack () {
      this.$router.go(-1);
    }
  }
};
</script>

<style lang="less">
@import (reference) '~less/variables.less';
.error-component {
  width: 100%;
  padding: 10vh 0;

  .error-component-content {
    margin: 0 auto;
    width: 40vw;

    // color: @color-pink;
    text-align: center;

    h1 {
      font-size: 15vw;
      line-height: 15vw;
      text-align: center;
    }
  }
}

@media (max-width: 767px) {
  .error-component {
    .error-component-content {
      width: auto;
    }
  }
}
</style>
 