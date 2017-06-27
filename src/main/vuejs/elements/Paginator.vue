<template>
  <ul class="pagination">
    <li v-if="!paginator.first">
      <a @click="paginatorNavigate(-1, null)">
        <i class="fa fa-angle-left"></i>
      </a>
    </li>
    <li v-for="(page, index) in paginator.totalPages" :class="{'active' : paginator.number == index}" :key="index">
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
  props: ['paginator', 'history'],
  created() {
    if (this.$route.query.page)
      this.paginatorNavigate(null, this.$route.query.page);
  },
  methods: {
    paginatorNavigate(direction, page) {
      if (this.history)
        this.$router.replace({
          query: {
            page: page,
          }
        })

      this.$emit('navigate', {
        direction: direction,
        page: page
      })
    }
  }
}
</script>