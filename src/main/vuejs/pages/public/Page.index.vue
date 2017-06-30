<template>
  <div>
    <div class="categories-nav">
      <div class="container">
        <ul class="list-inline" v-if="categories != null">
          <li v-for="cat in categories" :key="cat.id">
            <a class="waves-effect" @click="selectCategory(cat.id)" :class="{ 'active' : filters.categoryId == cat.id }">
              {{ $t('category.' + cat.code + '.default') }}
            </a>
          </li>
        </ul>
      </div>
    </div>
  
    <div class="container">
      <div class="row">
        <div class="col-md-3">
          <router-link to="/admin/page/create" class="btn btn-block btn-rounded btn-success">Create page</router-link>
  
          <div class="list-group m-t-10">
            <stagger-transition>
              <a v-for="(branch, index) in branches" :key="branch.id" class="list-group-item waves-effect" :class="{ 'active' : filters.branchId == branch.id }" :data-index="index" @click="selectBranch(branch.id)">
                {{ $t('category.' + currentCategory.code + '.' + branch.code) }}
              </a>
            </stagger-transition>
          </div>
        </div>
  
        <div class="col-md-9">
          <explorer-transition tag="div" class="explore-container">
            <div class="page-panel" v-for="(page, index) in pagesPaginator.content" :data-index="index" :key="page.id">
              <img v-if="page.bannerUrl != null" :src="page.bannerUrl">
              <img v-else src="/bookster_logo.png" style="min-width:auto">
  
              <div class="title">
                <router-link :to="'page/' + page.id">
                  <h4 v-text="page.name"></h4>
                </router-link>
                <small class="text-muted" v-if="page.category != null">
                  {{ $t('category.' + page.category.code + '.' + page.branch.code) }}
                </small>
              </div>
            </div>
          </explorer-transition>
  
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
import StaggerTransition from '../../functional/StaggerTransition.vue'
import ExplorerTransition from '../../functional/ExplorerTransition.vue'
import Paginator from '../../elements/Paginator.vue'
export default {
  name: 'page-explore',
  data() {
    return {
      pages: [],
      categories: [],
      branches: [],
      pagesPaginator: {},
      filters: null,
    }
  },
  components: {
    StaggerTransition, Paginator, ExplorerTransition
  },
  created() {
    this.filters = {
      branchId: this.$route.query.branchId,
      categoryId: this.$route.query.categoryId
    }

    this.getCategories();
    this.getPages(0);
  },
  computed: {
    currentCategory() {
      return this.categories.filter(x => {
        return x.id == this.filters.categoryId;
      })[0];
    }
  },
  methods: {
    getPages(page) {
      var url = ['public/pages', page, 10].join('/');
      this.$http.get(url, { params: this.filters }).then(response => {
        this.pagesPaginator = response.body;
      })
    },
    getCategories() {
      this.$http.get('public/categories').then(response => {
        this.categories = response.body.filter(c => {
          return c != null;
        });

        if (this.filters.categoryId == null) {
          this.filters.categoryId = this.categories[0].id;
        }

        this.getBranches(this.filters.categoryId);
      })
    },
    getBranches(category_id) {
      var url = ['public/categories', category_id, 'branches'].join('/');
      this.$http.get(url).then(response => {
        this.branches = response.body;
      })
    },
    selectCategory(category_id) {
      if (this.filters.categoryId == category_id) return;

      this.filters = {
        branchId: null,
        categoryId: category_id
      }

      this.getBranches(category_id);
      this.getPages(0);
      this.$router.replace({ query: this.filters })
    },
    selectBranch(branch_id) {
      if (this.filters.branchId == branch_id) return;

      this.filters = {
        branchId: branch_id,
        categoryId: this.filters.categoryId
      }

      this.getPages(0);
      this.$router.replace({ query: this.filters })
    },
    pagesPaginatorNavigate(e) {
      if (e.direction != null) {
        this.getPages(this.pagesPaginator.number + e.direction);
      } else if (e.page != null) {
        this.getPages(e.page);
      }
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
  // max-width: 350px;

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
