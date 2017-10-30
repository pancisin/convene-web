<template>
  <ul class="pagination">
    <li v-if="!paginator.first">
      <a @click="navigatePage(paginator.number - 1)">
        <i class="fa fa-angle-left"></i>
      </a>
    </li>
    <li v-for="index in pages"
      :class="{'active' : paginator.number === index}"
      :key="index">
      <a @click="navigatePage(index)">
        {{ index + 1}}
      </a>
    </li>
    <li v-if="!paginator.last">
      <a @click="navigatePage(paginator.number + 1)">
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
    },
    // fetch expects to be function
    // and have an page number as parameter.
    fetch: Function
  },
  created () {
    const page = this.$route.query.page || 0;
    this.navigatePage(page);
  },
  watch: {
    'paginator.number': function (newVal) {
      if (this.history) {
        this.$router.replace({
          query: {
            page: newVal
          }
        });
      }
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
    navigatePage (page) {
      if (this.fetch != null) {
        this.fetch(page);
      }
    }
  }
};
</script>
