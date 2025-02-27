<template>
  <form @submit.prevent="e => {}" v-loading="loadingCategories || loadingBranches">
    <div class="form-group" :class="{ 'has-error' : errors.has('category') }">
      <label class="control-label">{{ $t('page.category') }}</label>
      <select v-model="categoryId" class="form-control" name="category" v-validate :data-vv-rules="required ? 'required' : ''" @change="updateCategory">
        <option v-for="cat in categories" :value="cat.id" :key="cat.id">{{ $t('category.' + cat.code + '.default') }}</option>
      </select>
      <span class="text-danger" v-if="errors.has('category')">{{ errors.first('category') }}</span>
    </div>

    <div class="form-group" v-if="category != null" :class="{ 'has-error' : errors.has('branch') }">
      <label class="control-label">{{ $t('page.branch') }}</label>
      <select v-model="branchId" class="form-control" name="branch" v-validate :data-vv-rules="required ? 'required' : ''" @change="updateBranch">
        <option v-for="branch in branches" :value="branch.id" :key="branch.id">{{ $t('category.' + category.code + '.' + branch.code) }}</option>
      </select>
      <span class="text-danger" v-if="errors.has('branch')">{{ errors.first('branch') }}</span>
    </div>
  </form>
</template>

<script>
import { mapGetters, mapActions } from 'vuex';

export default {
  name: 'categorizer',
  data () {
    return {
      categoryId: null,
      branchId: null
    };
  },
  props: {
    category: {
      type: Object,
      default () {
        return {};
      }
    },
    branch: {
      type: Object,
      default () {
        return {};
      }
    },
    required: Boolean
  },
  watch: {
    category () {
      this.categoryId = this.category.id;
    },
    branch () {
      this.branchId = this.branch.id;
    }
  },
  created () {
    if (this.category != null) {
      this.categoryId = this.category.id;
    }

    if (this.branch != null) {
      this.branchId = this.branch.id;
    }

    if (this.categories.length === 0) {
      this.initializeCategories();
    }
  },
  computed: {
    ...mapGetters(['categories', 'getBranchesByCategoryId', 'loadingCategories', 'loadingBranches']),
    branches () {
      if (this.category != null) {
        let branches = this.getBranchesByCategoryId(this.category.id);

        if (branches == null || branches.length === 0) {
          this.initalizeBranches(this.category.id);
        }

        return branches;
      }
    }
  },
  methods: {
    ...mapActions(['initializeCategories', 'initalizeBranches']),
    updateCategory () {
      let category = null;
      this.categories.forEach(c => {
        if (c.id === this.categoryId) {
          category = c;
        };
      });

      this.$emit('update:category', category);
      this.$emit('change', {});
    },
    updateBranch () {
      let branch = null;
      this.branches.forEach(b => {
        if (b.id === this.branchId) {
          branch = b;
        }
      });

      this.$emit('update:branch', branch);
      this.$emit('change', {});
    }
  }
};
</script>
