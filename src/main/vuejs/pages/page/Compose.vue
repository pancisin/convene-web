<template>
  <div class="row">
    <div class="col-md-8">
      <panel type="default">
        <span slot="title">
          {{ $t('admin.page.overview') }}
        </span>
        <div class="row">
          <div class="col-md-6">
            <div class="form-group">
              <label class="control-label">{{ $t('page.name') }}</label>
              <input class="form-control required" v-model="page.name" type="text">
            </div>
          </div>
          <div class="col-md-6">
            <div class="form-group">
              <label class="control-label">{{ $t('page.category') }}</label>
              <select v-model="page.category" class="form-control">
                <option v-for="cat in categories" :value="cat" :key="cat">{{ $t('category.' + cat.code + '.default') }}</option>
              </select>
            </div>
  
            <div class="form-group" v-if="page.category != null">
              <label class="control-label">{{ $t('page.branch') }}</label>
              <select v-model="page.branch" class="form-control">
                <option v-for="branch in branches" :value="branch" :key="branch">{{ $t('category.' + page.category.code + '.' + branch.code) }}</option>
              </select>
            </div>
          </div>
        </div>
  
        <div class="form-group">
          <label class="control-label">{{ $t('page.summary') }}</label>
          <text-editor v-model="page.summary"></text-editor>
        </div>
  
        <div class="text-center">
          <button class="btn btn-rounded btn-danger" @click="togglePagePublished(page)" v-if="page.state == 'PUBLISHED'">Deactivate</button>
          <a class="btn btn-rounded btn-success" @click="togglePagePublished(page)" v-if="page.state == 'DEACTIVATED'">
            Publish
          </a>
          <button class="btn btn-rounded btn-primary" type="submit" @click="submit" :disabled="page.state == 'BLOCKED'">
            Save
          </button>
        </div>
      </panel>
    </div>
    <div class="col-md-4">
      <image-upload v-model="page.posterData" :media="page.poster" />
    </div>
  </div>
</template>

<script>
import { TextEditor, ImageUpload } from 'elements';
import { mapActions } from 'vuex';
import PublicApi from 'api/public.api';

export default {
  name: 'page-compose',
  props: {
    page: Object
  },
  data () {
    return {
      categories: [],
      branches: []
    };
  },
  components: {
    TextEditor, ImageUpload
  },
  created () {
    this.getCategories();
  },
  watch: {
    'page.category': 'getBranches',
    'page': 'getCategories'
  },
  methods: {
    ...mapActions([
      'removePage', 'updatePage', 'togglePagePublished'
    ]),
    submit () {
      this.updatePage(this.page).then(page => {
        this.$success('notification.page.updated' + page.name);
      });
    },
    getCategories () {
      this.branches = [];
      PublicApi.getCategories(categories => {
        this.categories = categories;
      });
    },
    getBranches () {
      if (this.page.category == null) return;
      PublicApi.getBranches(this.page.category.id, branches => {
        this.branches = branches;
      });
    }
  }
};
</script>
