<script>
import { RouterTabNavigation } from 'elements';
import { UserSettingsMenu } from '../services/maps/menus';

export default {
  name: 'user-settings',
  components: {
    RouterTabNavigation
  },
  render (h) {
    return h('div', {
      class: 'container'
    }, [
      h('h4', {
        class: 'page-title'
      }, this.$t('settings.default')),
      h('router-tab-navigation', {
        props: {
          menu: UserSettingsMenu
        }
      })
    ]);
  }
};
</script> 