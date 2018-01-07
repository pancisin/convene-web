<template>
  <transition name="fade-left">
    <div class="left side-menu" v-show="!collapsed">
      <div id="sidebar-menu">
        <!-- <ul v-if="menus.main.hasPermission(user)">
          <li class="menu-title">{{ $t('admin.menu.main') }}</li>

          <li v-for="(route, index) in menus.main.routes" :key="index" v-if="route.hasPermission(user)">
            <router-link :to="{ name: route.name }" class="waves-effect">
              <i class="material-icons" v-text="route.icon"></i>
              <span>{{ $t(route.code) }}</span>
            </router-link>
          </li>
        </ul> -->

        <ul v-if="menus.system.hasPermission(user)">
          <li class="menu-title">{{ $t('admin.menu.system') }}</li>

          <li v-for="(route, index) in menus.system.routes" :key="index" v-if="route.hasPermission(user)">
            <router-link :to="{ name: route.name }" class="waves-effect">
              <i class="material-icons" v-text="route.icon"></i>
              <span>{{ $t(route.code) }}</span>
            </router-link>
          </li>
        </ul>

        <ul 
          v-loading="loadingPages" 
          v-if="menus.page.hasPermission(user)">

          <li class="menu-title">{{ $t('admin.menu.pages') }}</li>

          <drop-down 
            v-for="(page) in pages" 
            :key="page.id" 
            ref="items" 
            @opened="closeDropdowns">

            <i class="material-icons" slot="title" v-if="page.state == 'PUBLISHED'">work</i>
            <i class="material-icons" slot="title" v-else-if="page.state == 'DEACTIVATED'">visibility_off</i>
            <i class="material-icons" slot="title" v-else-if="page.state == 'BLOCKED'">highlight_off</i>

            <span v-text="page.name" slot="title"></span>

            <li slot="item" v-for="(route, index) in menus.page.routes" :key="index" v-if="route.hasPermission(page.privilege)">
              <router-link :to="{ name: route.name, params: { id : page.id }}" class="list-group-item waves-effect">
                {{ $t(route.code) }}
              </router-link>
            </li>
          </drop-down>

          <!-- <li v-if="pages.length > limit">
            <router-link :to="{ name: 'admin.pages' }" class="waves-effect text-muted">
              <i class="material-icons">sort</i>
              Show all
            </router-link>
          </li> -->

          <li>
            <router-link to="/admin/page/create" class="waves-effect text-muted">
              <i class="material-icons">add</i>
              <span>{{ $t('admin.menu.page_create') }}</span>
            </router-link>
          </li>
        </ul>

        <ul v-loading="loadingConferences" v-if="menus.conference.hasPermission(user)"> 
          <li class="menu-title">{{ $t('admin.menu.conferences') }}
            <span class="label label-warning pull-right">Enterprise</span>
          </li>

          <drop-down v-for="conference in conferences" :key="conference.id" ref="items" @opened="closeDropdowns">

            <i class="material-icons" slot="title" v-if="conference.state == 'PUBLISHED'">work</i>
            <i class="material-icons" slot="title" v-else-if="conference.state == 'DEACTIVATED'">visibility_off</i>
            <i class="material-icons" slot="title" v-else-if="conference.state == 'BLOCKED'">highlight_off</i>

            <span v-text="conference.name" slot="title"></span>

            <li slot="item" v-for="(route, index) in menus.conference.routes" :key="index" v-if="route.hasPermission(user)">
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
        <ul v-if="menus.footer.hasPermission(user)">
          <li class="menu-title">{{ $t('admin.menu.about') }}</li>

          <li v-for="(route, index) in menus.footer.routes" :key="index" v-if="route.hasPermission(user)">
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
import { VerticalMenuDrop } from 'elements';
import { mapActions, mapGetters } from 'vuex';
import menus from '../services/maps/menus.map';

export default {
  name: 'sidebar',
  props: {
    collapsed: {
      type: Boolean,
      default: true
    },
    limit: {
      type: Number,
      default: 5
    }
  },
  data () {
    return {
      pagesCollapsed: true
    };
  },
  computed: {
    ...mapGetters([
      'pages',
      'conferences',
      'loadingPages',
      'loadingConferences',
      'user'
    ]),
    menus () {
      return menus;
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
    ...mapActions(['initializePages', 'initializeConferences']),
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
.sidebar-list {
  transition: all .3s ease-in;

  &.collapsed {
    position: relative;
    max-height: 400px;
    overflow: hidden;
  
    &:after {
      content: '';
      background: linear-gradient(to bottom, rgba(0, 0, 0, 0) 0%, #ffffff 70%);
      position: absolute;
      bottom: 0;
      left: 0;
      width: 100%;
      z-index: 1;
      height: 20%;
    }
  }
}

@media (min-width: 768px) {
  .side-menu.left {
    display: block !important;
  }
}

@media (max-width: 767px) {
  .side-menu.left {
    box-shadow: 0px 0px 13px 5px rgba(0, 0, 0, 0.18);
  }
}
</style>