<template>
  <div class="conference-settings">
    <ul class="nav nav-tabs">
      <li class="tab" 
        v-for="route in routes" 
        :key="route.name"
        :class="{ 'active' : $route.name === route.name }">

        <router-link :to="{ name: route.name }">
          {{ route.label }}
        </router-link>
      </li>
      <div class="indicator"></div>
    </ul>
    <div class="tab-content">
      <transition name="fade" mode="out-in">
        <router-view :conference="conference"></router-view>
      </transition>
    </div>
  </div>
</template>

<script>
export default {
  name: 'conference-settings',
  props: {
    conference: Object
  },
  render (h) {
    const routes = [
      {
        name: 'conference.settings.information',
        label: 'Basic information'
      },
      {
        name: 'conference.settings.registration',
        label: 'Registration form'
      },
      {
        name: 'conference.settings.partners',
        label: 'Partners'
      },
      {
        name: 'conference.settings.deletion',
        label: 'Delete'
      }
    ];

    return h('div', {
      class: 'conference-settings'
    }, [
      h('ul', { class: 'nav nav-tabs' }, routes.map(route => {
        return h('li', {
          class: `tab ${ this.$route.name === route.name ? 'active' : '' }`
        }, [
          h('router-link', {
            props: {
              to: {
                name: route.name
              }
            }
          }, route.label)
        ]);
      })),
      h('div', {
        class: 'tab-content'
      }, [
        h('transition', {
          props: { name: 'fade' }
        }, [
          h('router-view', {
            props: {
              conference: this.conference
            }
          })
        ])
      ])
    ]);
  },
  computed: {
    routes () {
      return [
        {
          name: 'conference.settings.information',
          label: 'Basic information'
        },
        {
          name: 'conference.settings.registration',
          label: 'Registration form'
        },
        {
          name: 'conference.settings.partners',
          label: 'Partners'
        },
        {
          name: 'conference.settings.deletion',
          label: 'Delete'
        }
      ];
    }
  }
};
</script>

<style lang="less">
.conference-settings {
  .tab-content {
    background: #fff;
    padding: 15px;
  }
}
</style>
