<template>
  <drop-down-menu>
    <span slot="button">
      <i class="fa fa-language"></i>
    </span>
    <drop-down-menu-item header>
      Language
    </drop-down-menu-item>
    <drop-down-menu-item v-for="loc in locales"
      :key="loc.code">
      <a @click="selectLoc(loc)">
        {{ $t(loc.code) }}
      </a>
    </drop-down-menu-item>
  </drop-down-menu>
</template>

<script>
import moment from 'moment';
import { DropDownMenu, DropDownMenuItem } from 'elements';
import { mapGetters, mapActions } from 'vuex';

export default {
  name: 'language-switcher',
  created () {
    if (this.locales.length === 0) {
      this.initializeLocales();
    }
  },
  components: {
    'drop-down-menu': DropDownMenu,
    'drop-down-menu-item': DropDownMenuItem
  },
  computed: {
    ...mapGetters({
      storeLocale: 'locale'
    }),
    ...mapGetters(['locale', 'authenticated', 'locales']),
    locale: {
      get () {
        return this.authenticated ? this.storeLocale : this.$i18n.locale;
      },
      set (value) {
        if (this.authenticated) {
          this.$http.put('api/user/locale', JSON.stringify(value.name)).then(response => {
            this.initializeUser();
            moment.locale(value.name);
            this.$i18n.locale = value.name;
          });
        } else {
          moment.locale(value.name);
          this.$i18n.locale = value.name;
        }
      }
    }
  },
  methods: {
    ...mapActions([
      'initializeUser', 'initializeLocales'
    ]),
    selectLoc (locale) {
      this.locale = locale;
    }
  }
};
</script>