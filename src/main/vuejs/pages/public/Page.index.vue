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
        <div class="col-md-3" v-if="branches.length > 0">
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

        <div class="col-md-9" :class="{ 'col-md-6': branches.length > 0 }">
          <masonry v-loading="loading" :columns="branches.length > 0 ? 3 : 4">
            <masonry-item class="card"   
              v-for="page in pagesPaginator.content" 
              :key="page.id">
            
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

                <div class="image-wrapper" v-if="page.poster">
                  <vue-image :src="page.poster.path" placeholder />
                </div>
              </router-link>
              <div class="actions" v-if="page.privilege && page.privilege.active">
                <router-link 
                  :to="{ name: 'page.settings', params: { id: page.id } }" 
                  class="pull-right text-primary">
                  <i class="fa fa-pencil-square-o" aria-hidden="true"></i>
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
        <div class="col-md-3">
          <h3 class="text-uppercase">Featured events</h3>
          <featured-events />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {
  Paginator,
  Masonry,
  MasonryItem,
  VueImage
} from 'elements';
import PublicApi from 'api/public.api';
import { mapGetters } from 'vuex';
import RootApi from 'api/api';
import { FeaturedEvents } from 'components';

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
    MasonryItem,
    VueImage,
    FeaturedEvents
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
      fetch(page, 15, this.filters, paginator => {
        this.pagesPaginator = paginator;
        this.loading = false;
      });
    },
    getCategories () {
      PublicApi.getCategories({ used: true }, categories => {
        this.categories = categories;
      });
    },
    getBranches (category_id) {
      PublicApi.getBranches(category_id, { used: true }, branches => {
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

.categories-nav {
  background: #fff;
  margin-top: -20px;
  margin-bottom: 20px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.12), 0 1px 2px rgba(0, 0, 0, 0.24);

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
