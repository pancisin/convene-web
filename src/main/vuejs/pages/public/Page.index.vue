<template>
  <div>
    <div class="categories-nav">
      <div class="container">
        <ul class="list-inline" v-if="categories != null">
          <li v-for="cat in categories" :key="cat.id">
            <a class="waves-effect" @click="selectCategory(cat)" :class="{ 'active' : filters.category.id == cat.id }">
              {{ $t('category.' + cat.code + '.default') }}
            </a>
          </li>
        </ul>
      </div>
    </div>
  
    <div class="container">
  
      <!--<ul class="list-inline" v-if="categories != null">
                    <li v-for="cat in categories" :key="cat.id">
                      <a class="btn btn-default waves-effect" @click="selectCategory(cat)">
                        {{ $t('category.' + cat.code + '.default') }}
                      </a>
                    </li>
                  </ul>
              -->
      <!--<ul class="list-inline" v-if="filters.category != null">
                            <li v-for="branch in branches" :key="branch.id">
                              <a class="btn btn-default">
                                {{ $t('category.' + filters.category.code + '.' + branch.code) }}
                              </a>
                            </li>
                          </ul>-->
  
      <div class="row">
        <div class="col-md-3">
          <div class="list-group m-t-10">
            <stagger-transition>
              <a v-for="(branch, index) in branches" :key="branch.id" class="list-group-item waves-effect" :data-index="index" @click="filters.branch = branch">
                {{ $t('category.' + filters.category.code + '.' + branch.code) }}
              </a>
            </stagger-transition>
          </div>
        </div>
  
        <div class="col-md-9">
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
    </div>
  </div>
</template>

<script>
import StaggerTransition from '../../functional/StaggerTransition.vue'
export default {
  name: 'page-explore',
  data() {
    return {
      pages: [],
      categories: [],
      branches: [],

      filters: {
        category: 0,
        branch: 0,
      }
    }
  },
  components: {
    StaggerTransition
  },
  watch: {
    filters: {
      handler() {
        this.getPages();
      },
      deep: true
    }
  },
  created() {
    this.getCategories();
    this.getPages();
  },
  methods: {
    getPages() {
      this.$http.get('public/pages/0/100', {
        params: {
          categoryId: this.filters.category ? this.filters.category.id : null,
          branchId: this.filters.branch ? this.filters.branch.id : null,
        }
      }).then(response => {
        this.pages = response.body.content;
      })
    },
    getCategories() {
      this.$http.get('public/categories').then(response => {
        this.categories = response.body.filter(c => {
          return c != null;
        });
        this.selectCategory(this.categories[0])
      })
    },
    selectCategory(category) {
      if (this.filters.category != null && this.filters.category.id == category.id) return;

      this.filters.branch = 0;
      this.filters.category = category;
      var url = ['public/categories', category.id, 'branches'].join('/');
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
  max-width: 350px;

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

.categories-nav {
  background: #fff;
  margin-top: -15px;
  margin-bottom: 10px;

  ul {
    margin: 0;

    li {
      padding: 0;

      a {
        padding: 20px;
        color: #2a3142;
        font-weight: 500;

        &.active {
          background-color: #f5f5f5;
          color: #3bafda;
        }

        &:hover {
          background-color: #f5f5f5;
        }
      }
    }
  }
}
</style>
