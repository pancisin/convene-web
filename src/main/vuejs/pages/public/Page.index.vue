<template>
  <div class="container">
  
    <ul class="list-inline" v-if="categories != null">
      <li v-for="cat in categories" :key="cat.id">
        <a class="btn btn-default" @click="selectCategory(cat)">
          {{ $t('category.' + cat.code + '.default') }}
        </a>
      </li>
    </ul>
  
    <ul class="list-inline" v-if="filters.category != null">
      <li v-for="branch in branches" :key="branch.id">
        <a class="btn btn-default">
          {{ $t('category.' + filters.category.code + '.' + branch.code) }}
        </a>
      </li>
    </ul>
  
    <div class="row">
      <div class="explore-container">
        <div class="page-panel" v-for="page in pages">
          <img v-if="page.bannerUrl != null" :src="page.bannerUrl" />
          <img v-else src="/bookster_logo.png" style="min-width:auto" />
  
          <div class="title">
            <router-link :to="'page/' + page.id">
              <h4 v-text="page.name"></h4>
            </router-link>
            <small class="text-muted" v-if="page.category != null">
              {{ $t('category.' + page.category.code + '.' + page.branch.code) }}
            </small>
          </div>
        </div>
      </div>
    </div>
  
  </div>
</template>

<script>
export default {
  name: 'page-explore',
  data() {
    return {
      pages: [],
      categories: [],
      branches: [],

      filters: {
        category: null,
      }
    }
  },
  created() {
    this.getCategories();
    this.getPages();
  },
  methods: {
    getPages() {
      this.$http.get('api/pages/0/100').then(response => {
        this.pages = response.body.content;
      })
    },
    getCategories() {
      this.$http.get('api/categories').then(response => {
        this.categories = response.body;
      })
    },
    selectCategory(category) {
      this.filters.category = category;
      var url = ['api/categories', category.id, 'branches'].join('/');
      this.$http.get(url).then(response => {
        this.branches = response.body;
      })
    },
  }
}
</script>

<style lang="less">
.explore-container {
  display: flex;
  flex-wrap: wrap;
  width: 100%;
}

.page-panel {
  flex: 250px 1 1;
  position: relative;
  margin: 10px;
  overflow: hidden;
  box-shadow: 5px 3px 15px 0px rgba(111, 110, 110, 0.3);
  background: #3bafda;
  text-align: center;

  img {
    min-width: 100%;
    transition: all .3s ease;
    height: 200px;
  }

  .title {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    background: white;
    margin: 0;
    padding: 10px 20px;
  }

  &:hover {
    img {
      transform: scale(1.1);
    }
  }
}
</style>
