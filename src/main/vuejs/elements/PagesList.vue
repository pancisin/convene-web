<template>
   <div class="inbox-widget" :class="{ 'inbox-widget-mini' : mini }">
    <stagger-transition>
      <router-link 
        :to="{ name: 'page.public', params: { id: page.slug || page.id }}"
        v-for="(page, index) in pages"
        :key="index"
        class="inbox-item">

        <div class="inbox-item-img"
          v-if="page.poster != null">
          <vue-image 
            :src="page.poster.path" 
            class="img-circle" 
            placeholder />
        </div>
        <div>
          <p class="inbox-item-author">
            {{ page.name }}
            <span class="pull-right label label-default" v-if="followers">
              {{ page.followersCount }} followers
            </span>
          </p>
          <p class="inbox-item-text"
            v-if="page.category != null && !mini">
            {{ $t('category.' + page.category.code + '.' + page.branch.code) }}
          </p>
        </div>
      </router-link>
    </stagger-transition>
  </div>
</template>

<script>
import VueImage from './VueImage';
import { StaggerTransition } from '../functional/transitions';
export default {
  name: 'pages-list',
  props: {
    pages: Array,
    followers: Boolean,
    mini: Boolean
  },
  components: {
    VueImage,
    StaggerTransition
  }
};
</script>
