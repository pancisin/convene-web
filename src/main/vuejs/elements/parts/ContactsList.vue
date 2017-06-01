<template>
  <div class="inbox-widget">
    <a @click="selectUser(user)" v-for="user in users">
      <div class="inbox-item">
        <div class="inbox-item-img">
          <img src="https://static1.squarespace.com/static/56ba4348b09f95db7f71a726/t/58d7f267ff7c50b172895560/1490547315597/justin.jpg" class="img-circle" alt="">
        </div>
        <p class="inbox-item-author" v-text="user.displayName"></p>
        <!--<p class="inbox-item-text">Hey! there I'm available...</p>-->
        <p class="inbox-item-date">13:40 PM</p>
      </div>
    </a>
  </div>
</template>

<script>
export default {
  name: 'contacts-list',
  data() {
    return {
      users: [],
    }
  },
  created() {
    this.getContacts();
  },
  methods: {
    getContacts() {
      this.$http.get('api/user/contacts').then(response => {
        this.users = response.body;
      });
    },
    selectUser(user) {
      this.$emit('selected', user);
    }
  }
}
</script>

<style lang="less">
.inbox-widget {
  max-height: 380px;
  min-height: 380px;

  overflow-y: auto;
  padding: 10px;
}
</style>
