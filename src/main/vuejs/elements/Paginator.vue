<template>
  <ul class="pagination">
    <li v-if="!paginator.first">
      <a @click="paginatorNavigate(-1, null)">
        <i class="fa fa-angle-left"></i>
      </a>
    </li>
    <li v-for="(page, index) in paginator.totalPages" :class="{'active' : paginator.number == index}">
      <a v-text="page" @click="paginatorNavigate(null, index)"></a>
    </li>
    <li v-if="!paginator.last">
      <a @click="paginatorNavigate(1, null)">
        <i class="fa fa-angle-right"></i>
      </a>
    </li>
  </ul>
</template>

<script>
export default {
  props: ['paginator'],
  data() {
    return {

    }
  },
  methods: {
    paginatorNavigate(direction, page) {
      this.$emit('navigate', {
        direction: direction,
        page: page
      })
    }
  }
}
</script>