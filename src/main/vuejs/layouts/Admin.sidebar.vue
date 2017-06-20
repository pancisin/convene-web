<template>
  <transition name="fade-left">
    <div class="left side-menu" v-show="!collapsed">
      <div id="sidebar-menu">
        <ul>
          <li class="menu-title">{{ $t('admin.menu.main') }}</li>
  
          <li>
            <router-link to="/admin/dashboard" class="waves-effect">
              <i class="material-icons">dashboard</i>
              <span>{{ $t('admin.menu.dashboard') }}</span>
            </router-link>
          </li>
  
          <li>
            <router-link to="/admin/event" class="waves-effect">
              <i class="material-icons">event</i>
              <span>{{ $t('admin.menu.events') }}</span>
            </router-link>
          </li>
  
          <li>
            <router-link to="/admin/notifications" class="waves-effect">
              <i class="material-icons">notifications_none</i>
              <span>{{ $t('admin.menu.notifications') }}</span>
            </router-link>
          </li>
  
          <li class="menu-title">{{ $t('admin.menu.pages') }}</li>
  
          <drop-down v-for="page in pages" :key="page.id" ref="items" @opened="closeDropdowns">
            <i class="material-icons" slot="title" v-if="page.state == 'PUBLISHED'">work</i>
            <i class="material-icons" slot="title" v-else-if="page.state == 'DEACTIVATED'">visibility_off</i>
            <i class="material-icons" slot="title" v-else-if="page.state == 'BLOCKED'">highlight_off</i>
  
            <span v-text="page.name" slot="title"></span>
            <!--<li slot="item">
                  <router-link :to="{ name: 'page.overview', params: { id : page.id }}" class="list-group-item waves-effect">
                    {{ $t('admin.page.overview') }}
                  </router-link>
                </li>-->
            <li slot="item">
              <router-link :to="{ name: 'page.events', params: { id: page.id }}" class="list-group-item waves-effect">
                {{ $t('admin.page.events') }}
              </router-link>
            </li>
            <li slot="item">
              <router-link :to="{ name: 'page.places', params: { id: page.id }}" class="list-group-item waves-effect">
                {{ $t('admin.page.places') }}
              </router-link>
            </li>
            <li slot="item">
              <router-link :to="{ name: 'page.administrators', params: { id: page.id }}" class="list-group-item waves-effect">
                {{ $t('admin.page.administrators') }}
              </router-link>
            </li>
            <li slot="item">
              <router-link :to="{ name: 'page.services', params: { id: page.id }}" class="list-group-item waves-effect">
                {{ $t('admin.page.services') }}
              </router-link>
            </li>
            <li slot="item">
              <router-link :to="{ name: 'page.requests', params: { id: page.id }}" class="list-group-item waves-effect">
                {{ $t('admin.page.requests') }}
              </router-link>
            </li>
            <li slot="item">
              <router-link :to="{ name: 'page.settings', params: { id : page.id }}" class="list-group-item waves-effect">
                {{ $t('admin.page.settings') }}
              </router-link>
            </li>
          </drop-down>
  
          <li>
            <router-link to="/admin/page/create" class="waves-effect text-muted">
              <i class="material-icons">add</i>
              <span>{{ $t('admin.menu.page_create') }}</span>
            </router-link>
          </li>
  
          <li class="menu-title">{{ $t('admin.menu.conferences') }}
            <span class="label label-warning pull-right">Enterprise</span>
          </li>
  
          <drop-down v-for="conference in conferences" :key="conference.id" ref="items" @opened="closeDropdowns">
            <i class="material-icons" slot="title">work</i>
            <span v-text="conference.name" slot="title"></span>
            <li slot="item">
              <router-link :to="{ name: 'conference.overview', params: { id : conference.id }}" class="list-group-item waves-effect">
                {{ $t('admin.page.overview') }}
              </router-link>
            </li>
            <li slot="item">
              <router-link :to="{ name: 'conference.events', params: { id : conference.id }}" class="list-group-item waves-effect">
                {{ $t('admin.page.events') }}
              </router-link>
            </li>
            <li slot="item">
              <router-link :to="{ name: 'conference.administrators', params: { id: conference.id }}" class="list-group-item waves-effect">
                {{ $t('admin.page.administrators') }}
              </router-link>
            </li>
            <li slot="item">
              <router-link :to="{ name: 'conference.attendees', params: { id : conference.id }}" class="list-group-item waves-effect">
                Attendees
              </router-link>
            </li>
            <li slot="item">
              <router-link :to="{ name: 'conference.settings', params: { id : conference.id }}" class="list-group-item waves-effect">
                Settings
              </router-link>
            </li>
          </drop-down>
  
          <li>
            <router-link to="/admin/conference/create" class="waves-effect text-muted">
              <i class="material-icons">add</i>
              <span>{{ $t('admin.menu.conference_create') }}</span>
            </router-link>
          </li>
  
          <li class="menu-title">{{ $t('admin.menu.about') }}</li>
  
          <li>
            <router-link to="/admin/faq" class="waves-effect">
              <i class="fa fa-question" aria-hidden="true"></i>
              <span>{{ $t('admin.menu.faq') }}</span>
            </router-link>
          </li>
          <li>
            <router-link to="/admin/terms" class="waves-effect">
              <i class="material-icons">lock</i>
              <span>{{ $t('admin.menu.terms') }}</span>
            </router-link>
          </li>
          <li>
            <router-link to="/admin/privacy-policy" class="waves-effect">
              <i class="material-icons">lock</i>
              <span>{{ $t('admin.menu.privacy') }}</span>
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
import VerticalMenuDrop from '../elements/VerticalMenuDrop.vue'
import { mapActions, mapGetters } from 'vuex'

export default {
  name: 'sidebar',
  props: {
    collapsed: {
      type: Boolean,
      default: true
    }
  },
  computed: {
    ...mapGetters({
      pages: 'getPages',
      conferences: 'getConferences'
    })
  },
  components: {
    DropDown: VerticalMenuDrop,
  },
  created() {
    this.initializePages();
    this.initializeConferences();
  },
  methods: {
    ...mapActions([
      'initializePages', 'initializeConferences'
    ]),
    closeDropdowns(except) {
      if (!except.collapsed)
        this.$refs.items.forEach(item => {
          if (item != except && !item.collapsed)
            item.toggleCollapse();
        })
    }
  }
}
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