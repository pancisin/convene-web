<template>
  <transition name="fade-left">
    <div class="left side-menu" v-show="!collapsed">
      <div id="sidebar-menu">
        <ul>
          <li class="menu-title">{{ $t('admin.menu.main') }}</li>

          <li v-for="(route, index) in routes.main" :key="index">
            <router-link :to="{ name: route.name }" class="waves-effect">
              <i class="material-icons" v-text="route.icon"></i>
              <span>{{ $t(route.code) }}</span>
            </router-link>
          </li>
        </ul>

        <ul v-loading="loadingPages">
          <li class="menu-title">{{ $t('admin.menu.pages') }}</li>

          <drop-down v-for="page in pages" :key="page.id" ref="items" @opened="closeDropdowns">
            <i class="material-icons" slot="title" v-if="page.state == 'PUBLISHED'">work</i>
            <i class="material-icons" slot="title" v-else-if="page.state == 'DEACTIVATED'">visibility_off</i>
            <i class="material-icons" slot="title" v-else-if="page.state == 'BLOCKED'">highlight_off</i>

            <span v-text="page.name" slot="title"></span>

            <li slot="item" v-for="(route, index) in routes.page" :key="index">
              <router-link :to="{ name: route.name, params: { id : page.id }}" class="list-group-item waves-effect">
                {{ $t(route.code) }}
              </router-link>
            </li>
          </drop-down>

          <li>
            <router-link to="/admin/page/create" class="waves-effect text-muted">
              <i class="material-icons">add</i>
              <span>{{ $t('admin.menu.page_create') }}</span>
            </router-link>
          </li>
        </ul>

        <ul v-loading="loadingConferences">
          <li class="menu-title">{{ $t('admin.menu.conferences') }}
            <span class="label label-warning pull-right">Enterprise</span>
          </li>

          <drop-down v-for="conference in conferences" :key="conference.id" ref="items" @opened="closeDropdowns">

            <i class="material-icons" slot="title" v-if="conference.state == 'PUBLISHED'">work</i>
            <i class="material-icons" slot="title" v-else-if="conference.state == 'DEACTIVATED'">visibility_off</i>
            <i class="material-icons" slot="title" v-else-if="conference.state == 'BLOCKED'">highlight_off</i>

            <span v-text="conference.name" slot="title"></span>

            <li slot="item" v-for="(route, index) in routes.conference" :key="index">
              <router-link :to="{ name: route.name, params: { id : conference.id }}" class="list-group-item waves-effect">
                {{ $t(route.code) }}
              </router-link>
            </li>
          </drop-down>

          <li>
            <router-link to="/admin/conference/create" class="waves-effect text-muted">
              <i class="material-icons">add</i>
              <span>{{ $t('admin.menu.conference_create') }}</span>
            </router-link>
          </li>

        </ul>
        <ul>
          <li class="menu-title">{{ $t('admin.menu.about') }}</li>

          <li v-for="(route, index) in routes.footer" :key="index">
            <router-link :to="{ name: route.name }" class="waves-effect">
              <i class="material-icons" v-text="route.icon"></i>
              <span>{{ $t(route.code) }}</span>
            </router-link>
          </li>
        </ul>

        <div class="clearfix"></div>
      </div>

      <div class="clearfix"></div>
    </div>
  </transition>
</template>

<script>
import VerticalMenuDrop from '../elements/VerticalMenuDrop.vue';
import { mapActions, mapGetters } from 'vuex';
import routes from '../services/maps/routes.map';

export default {
  name: 'sidebar',
  props: {
    collapsed: {
      type: Boolean,
      default: true
    }
  },
  computed: {
    ...mapGetters([
      'pages', 'conferences', 'loadingPages', 'loadingConferences'
    ]),
    routes () {
      return routes;
    }
  },
  components: {
    DropDown: VerticalMenuDrop
  },
  created () {
    this.initializePages();
    this.initializeConferences();
  },
  methods: {
    ...mapActions([
      'initializePages', 'initializeConferences'
    ]),
    closeDropdowns (except) {
      if (!except.collapsed) {
        this.$refs.items.forEach(item => {
          if (item !== except && !item.collapsed) {
            item.toggleCollapse();
          }
        });
      }
    }
  }
};
</script>

<style lang="less" scoped>
@media(min-width: 768px) {
  .side-menu.left {
    display: block !important;
  }
}

@media(max-width: 767px) {
  .side-menu.left {
    box-shadow: 0px 0px 13px 5px rgba(0, 0, 0, 0.18);
  }
}
</style>