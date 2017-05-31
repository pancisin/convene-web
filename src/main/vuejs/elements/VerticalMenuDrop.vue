<template>
  <li class="has_sub">
    <a class="waves-effect" @click="toggleCollapse" :class="{'subdrop' : !collapsed}">
      <slot name="title">
      </slot>
      <i class="fa fa-angle-right menu-arrow"></i>
    </a>
  
    <slide-transition>
      <ul class="list-unstyled" v-show="!collapsed">
        <slot name="item">
        </slot>
      </ul>
    </slide-transition>
  </li>
</template>

<script>
import SlideTransition from '../functional/SlideTransition.vue'
export default {
  name: 'vertical-menu-dropdown',
  data() {
    return {
      collapsed: true
    }
  },
  components: {
    SlideTransition,
  },
  props: {
    active: Boolean,
  },
  methods: {
    toggleCollapse() {
      this.collapsed = !this.collapsed;
      this.$emit('opened', this);
    }
  }
}
</script>