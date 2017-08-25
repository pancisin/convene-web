<template>
  <li class="dropdown hidden-xs" :class="{ 'open' : display }" v-click-outside="closeLanguage">
    <a @click="display = !display" class="dropdown-toggle waves-effect waves-light">
      <i class="fa fa-language"></i>
    </a>
    <ul class="dropdown-menu">
      <li v-for="loc in locales" :key="loc.code">
        <a @click="selectLoc(loc)">
          {{ $t(loc.code) }}
        </a>
      </li>
    </ul>
  </li>
</template>

<script>
import moment from 'moment';
import { mapGetters, mapActions } from 'vuex';

export default {
  name: 'language-switcher',
  data () {
    return {
      locales: [],
      display: false
    };
  },
  created () {
    this.getLocales();
  },
  computed: {
    ...mapGetters({
      storeLocale: 'locale'
    }),
    ...mapGetters(['locale', 'authenticated']),
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
      'initializeUser'
    ]),
    getLocales () {
      this.$http.get('public/locales').then(response => {
        this.locales = response.body;
      });
    },
    closeLanguage (e) {
      if (this.display) {
        this.display = false;
      }
    },
    selectLoc (locale) {
      this.locale = locale;
      this.closeLanguage();
    }
  }
};
</script>