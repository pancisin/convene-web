<template>
  <ul class="pagination">
    <li v-if="!paginator.first">
      <a @click="paginatorNavigate(-1, null)">
        <i class="fa fa-angle-left"></i>
      </a>
    </li>
    <li v-for="index in pages"
      :class="{'active' : paginator.number === index}"
      :key="index">
      <a @click="paginatorNavigate(null, index)">
        {{ index + 1}}
      </a>
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
  props: {
    paginator: {
      type: Object,
      default: {}
    },
    history: {
      type: Boolean,
      default: false
    }
  },
  created () {
    if (this.$route.query.page) {
      this.paginatorNavigate(null, this.$route.query.page);
    }
  },
  computed: {
    pages () {
      if (this.paginator.totalPages > 5) {
        let total = [...Array(this.paginator.totalPages).keys()];
        const index = this.paginator.number;
        return total.slice(index - 2 > 0 ? index - 2 : 0, index + 3);
      } else {
        return [...Array(this.paginator.totalPages).keys()];
      }
    }
  },
  methods: {
    paginatorNavigate (direction, page) {
      if (this.history) {
        this.$router.replace({
          query: {
            page
          }
        });
      }

      this.$emit('navigate', {
        direction,
        page
      });
    }
  }
};
</script>

<style lang="less">

</style>