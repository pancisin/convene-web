<template>
  <div class="member-list">
    <router-link :to="{ name: 'user', params: { user_id: user.id } }" class="member-list-card" v-for="user in users" :key="user.id">
      <vue-image :src="user.profilePicture ? user.profilePicture.path : null" class="img-circle" placeholder />
      <p>{{ user.displayName }}</p>
    </router-link>
  </div>
</template>

<script>
import VueImage from './VueImage';
export default {
  name: 'member-list',
  props: {
    users: {
      type: Array,
      default () {
        return [];
      }
    }
  },
  render (h) {
    return h('div', {
      class: 'member-list'
    }, this.users.map(u => h('router-link', {
      attrs: {
        to: { name: 'user', params: { user_id: u.id } }
      },
      class: 'member-list-card'
    }, [
      h('vue-image', {
        attrs: {
          src: u.profilePicture ? u.profilePicture.path : null
        },
        class: 'img-circle'
      }),
      h('p', u.displayName)
    ])));
  },
  components: {
    VueImage
  }
};
</script>

<style lang="less">
.member-list {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  
  .member-list-card {
    text-align: center;
    flex: 1 1 25%;
    text-align: center;
    padding: 10px;
    transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);

    img {
      width: 50px;
      height: 50px;
      margin-bottom: 5px;
    }

    p {
      font-size: 12px;
      line-height: 15px;
      // color: #000;
    }

    &:hover {
      box-shadow: 0 1px 3px rgba(0, 0, 0, 0.12), 0 1px 2px rgba(0, 0, 0, 0.24);
    }
  }
}
</style>
