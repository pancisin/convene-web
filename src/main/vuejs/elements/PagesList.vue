<template>
   <div class="inbox-widget">
    <router-link :to="{ name: 'page.public', params: { id: page.id }}"
      v-for="(page, index) in pages"
      :key="index"
      class="inbox-item">

      <div class="inbox-item-img"
        v-if="page.poster != null">
        <img :src="page.poster.path"
          class="img-circle">
      </div>
      <p class="inbox-item-author">
        {{ page.name }}
        <span class="pull-right label label-default" v-if="followers">
          {{ page.followersCount }} followers
        </span>
      </p>
      <p class="inbox-item-text"
        v-if="page.category != null">
        {{ $t('category.' + page.category.code + '.' + page.branch.code) }}
      </p>
    </router-link>
  </div>
</template>

<script>
export default {
  name: 'pages-list',
  props: {
    pages: Array,
    followers: Boolean
  }
};
</script>

<style>

</style>
