<template>
  <div>
    <div class="categories-nav">
      <div class="container">
        <ul class="list-inline" v-if="categories != null">
          <li v-for="cat in categories" :key="cat.id">
            <a class="waves-effect" 
              @click="selectCategory(cat.id)" 
              :class="{ 'active' : filters.categoryId == cat.id }">

              {{ $t('category.' + cat.code + '.default') }}
            </a>
          </li>
        </ul>
      </div>
    </div>

    <div class="container">
      <div class="row">
        <div class="col-md-3">
          <div class="list-group m-t-10">
            <stagger-transition>
              <a v-for="(branch, index) in branches" 
                :key="branch.id" 
                class="list-group-item waves-effect" 
                :class="{ 'active' : filters.branchId == branch.id }" 
                :data-index="index" @click="selectBranch(branch.id)">

                {{ $t('category.' + currentCategory.code + '.' + branch.code) }}
              </a>
            </stagger-transition>
          </div>
        </div>

        <div class="col-md-9">
          <masonry v-loading="loading" :columns="4">
            <masonry-item class="page-panel"   
              v-for="(page, index) in pagesPaginator.content" 
              :key="index"
              :style="{ 'background-image': page.poster != null ? `url(${page.poster.path})` : 'none' }">
              
              <router-link 
                :to="{ name: 'page.public', params: { id: page.id } }">
                <div class="title">
                  <h5 v-text="page.name"></h5>
                  <small class="text-muted" v-if="page.category != null">
                    {{ $t('category.' + page.category.code + '.' + page.branch.code) }}
                  </small>
                </div>
              </router-link>
            </masonry-item>
          </masonry>

          <div class="row">
            <div class="col-xs-12 text-center">
              <paginator :history="true" :paginator="pagesPaginator" @navigate="pagesPaginatorNavigate"></paginator>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import StaggerTransition from '../../functional/StaggerTransition.vue';
import { Paginator, Masonry, MasonryItem } from 'elements';
export default {
  name: 'page-explore',
  data () {
    return {
      pages: [],
      categories: [],
      branches: [],
      pagesPaginator: {},
      filters: null,
      loading: false
    };
  },
  components: {
    StaggerTransition, Paginator, Masonry, MasonryItem
  },
  created () {
    this.filters = {
      ...this.$route.query
    };

    this.getCategories();
    this.getPages(0);
  },
  computed: {
    currentCategory () {
      return this.categories.filter(x => {
        return x.id === this.filters.categoryId;
      })[0];
    }
  },
  methods: {
    getPages (page) {
      this.loading = true;
      var url = ['public/pages', page, 12].join('/');
      this.$http.get(url, { params: this.filters }).then(response => {
        this.pagesPaginator = response.body;
        this.loading = false;
      });
    },
    getCategories () {
      this.$http.get('public/categories').then(response => {
        this.categories = response.body.filter(c => {
          return c != null;
        });

        this.getBranches(this.filters.categoryId);
      });
    },
    getBranches (category_id) {
      var url = ['public/categories', category_id, 'branches'].join('/');
      this.$http.get(url).then(response => {
        this.branches = response.body;
      });
    },
    selectCategory (category_id) {
      if (this.filters.categoryId === category_id) return;

      this.filters = {
        branchId: null,
        categoryId: category_id
      };

      this.getBranches(category_id);
      this.getPages(0);
      this.$router.replace({ query: this.filters });
    },
    selectBranch (branch_id) {
      if (this.filters.branchId === branch_id) return;

      this.filters = {
        branchId: branch_id,
        categoryId: this.filters.categoryId
      };

      this.getPages(0);
      this.$router.replace({ query: this.filters });
    },
    pagesPaginatorNavigate (e) {
      if (e.direction != null) {
        this.getPages(this.pagesPaginator.number + e.direction);
      } else if (e.page != null) {
        this.getPages(e.page);
      }
    }
  }
};
</script>

<style lang="less">
@import (reference) '~less/variables.less';
.explore-container {
  display: flex;
  flex-wrap: wrap;
  width: 100%;
}

.page-panel {
  overflow: hidden;
  box-shadow: 5px 3px 15px 0px rgba(111, 110, 110, 0.3);
  background: @color-primary;
  background-position: center;
  background-size: cover;
  transition: all .2s ease-in-out;

  & > a {
    display: block;
    height: 200px;
  }

  .title {
    width: 100%;
    background: white;
    margin: 0;
    padding: 15px;
    border-bottom: 1px solid #ccc;

    h5 {
      margin: 0;
    }
  }

  &:hover {
    box-shadow: 0px 0px 15px 2px rgba(111, 110, 110, 0.3);
  }
}

.categories-nav {
  background: #fff;
  margin-top: -20px;
  margin-bottom: 20px;

  ul {
    margin: 0;

    li {
      padding: 0;

      a {
        padding: 20px;
        color: #2a3142;
        font-weight: 500;

        &.active {
          background-color: @color-light;
          color: @color-primary;
        }

        &:hover {
          background-color: @color-light;
        }
      }
    }
  }
}

@media only screen and (max-width: 480px) {
  .categories-nav {
    ul {
      li {
        display: block;
        a {
          width: 100%;
        }
      }
    }
  }
}
</style>
