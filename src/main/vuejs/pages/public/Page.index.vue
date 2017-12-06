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
            <a v-for="(branch, index) in branches" 
              :key="branch.id" 
              class="list-group-item waves-effect" 
              :class="{ 'active' : filters.branchId == branch.id }" 
              :data-index="index" @click="selectBranch(branch.id)">

              {{ $t('category.' + currentCategory.code + '.' + branch.code) }}
            </a>
          </div>
        </div>

        <div class="col-md-9">
          <masonry v-loading="loading" :columns="4">
            <masonry-item class="page-panel"   
              v-for="(page, index) in pagesPaginator.content" 
              :key="index"
              :style="{ 'background-image': page.poster != null ? `url(${page.poster.path})` : 'none' }">
            
              <router-link 
                :to="{ name: 'page.public', params: { id: page.slug || page.id } }">
                <div class="title">
                  <h5>
                    {{ page.name }}
                  </h5>
                  <small class="text-muted" v-if="page.category != null">
                    {{ $t('category.' + page.category.code + '.' + page.branch.code) }}
                  </small>
                </div>
              </router-link>
              <div class="actions">
                <router-link 
                  :to="{ name: 'page.settings', params: { id: page.id } }" 
                  v-if="page.privilege && page.privilege.role.level >= 60" 
                  class="pull-right text-primary">
                  <i class="fa fa-pencil"></i>
                </router-link>
              </div>
            </masonry-item>
          </masonry>

          <div class="row">
            <div class="col-xs-12 text-center">
              <paginator :history="true" :paginator="pagesPaginator" :fetch="getPages"></paginator>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { Paginator, Masonry, MasonryItem } from 'elements';
import PublicApi from 'api/public.api';
import { mapGetters } from 'vuex';
import RootApi from 'api/api';

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
    Paginator,
    Masonry,
    MasonryItem
  },
  created () {
    this.filters = {
      ...this.$route.query
    };

    this.getCategories();
  },
  computed: {
    ...mapGetters(['authenticated']),
    currentCategory () {
      return this.categories.filter(x => {
        return x.id === this.filters.categoryId;
      })[0];
    }
  },
  methods: {
    getPages (page) {
      this.loading = true;

      const fetch = this.authenticated ? RootApi.getPages : PublicApi.getPages;
      fetch(page, 12, this.filters, paginator => {
        this.pagesPaginator = paginator;
        this.loading = false;
      });
    },
    getCategories () {
      PublicApi.getCategories(categories => {
        this.categories = categories;
      });
    },
    getBranches (category_id) {
      PublicApi.getBranches(category_id, branches => {
        this.branches = branches;
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
  background: @color-primary;
  background-position: center;
  background-size: cover;

  box-shadow: 0 1px 3px rgba(0,0,0,0.12), 0 1px 2px rgba(0,0,0,0.24);
  transition: all 0.3s cubic-bezier(.25,.8,.25,1);

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
      text-transform: uppercase;
      color: #000;
      line-height: 18px;
    }
  }

  .actions {
    border-top: 1px solid #ccc;
    text-align: right;
    display: flex;
    background: #fff;

    & > a {
      transition: all .3s ease-in-out;
      color: #000;
      padding: 10px;
      display: inline-block;
      flex: 1 1 auto;
      text-align: center;

      &:hover {
        background-color: @color-primary;
        color: #fff;
      }
    }
  }

  &:hover {
    box-shadow: 0 14px 28px rgba(0,0,0,0.25), 0 10px 10px rgba(0,0,0,0.22);
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
