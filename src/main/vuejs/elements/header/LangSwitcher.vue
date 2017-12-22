<template>
  <dropdown-menu>
    <span slot="button">
      <i class="fa fa-language"></i>
    </span>
    <dropdown-menu-item header>
      Language
    </dropdown-menu-item>
    <dropdown-menu-item v-for="loc in locales"
      :key="loc.code">
      <a @click="selectLoc(loc)">
        {{ $t(loc.code) }}
      </a>
    </dropdown-menu-item>
  </dropdown-menu>
</template>

<script>
import moment from 'moment';
import { mapGetters, mapActions } from 'vuex';
import DropdownMenu from '../DropdownMenu';
import DropdownMenuItem from '../DropdownMenuItem';
import { Settings } from 'luxon';

export default {
  name: 'lang-switcher',
  created () {
    if (this.locales.length === 0) {
      this.initializeLocales();
    }

    if (!this.authenticated) {
      moment.locale(navigator.language || 'en');
    }
  },
  components: {
    DropdownMenu, DropdownMenuItem
  },
  computed: {
    ...mapGetters({
      storeLocale: 'locale'
    }),
    ...mapGetters(['authenticated', 'locales']),
    locale: {
      get () {
        return this.authenticated ? this.storeLocale : this.$i18n.locale;
      },
      async set (value) {
        if (this.authenticated) {
          await this.setLocale(JSON.stringify(value.name));
        }

        moment.locale(value.name);
        Settings.defaultLocale = value.name;
        this.$i18n.locale = value.name;
      }
    }
  },
  methods: {
    ...mapActions([
      'initializeUser', 'initializeLocales', 'setLocale'
    ]),
    selectLoc (locale) {
      this.locale = locale;
    }
  }
};
</script>