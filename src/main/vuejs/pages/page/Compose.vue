<template>
  <div class="row">
    <div :class="{ 'col-md-8' : edit, 'col-xs-12' : !edit }">
      <panel type="default">
        <span slot="title">
          {{ edit ? $t('admin.page.overview') : $t('admin.menu.page_create') }}
        </span>
        <div class="row">
          <div :class="{ 'col-md-6' : edit, 'col-xs-12' : !edit }">
            <div class="form-group">
              <label class="control-label">{{ $t('page.name') }}</label>
              <input class="form-control required" v-model="page.name" type="text">
            </div>
          </div>
          <div class="col-md-6" v-if="edit">
            <div class="form-group">
              <label class="control-label">{{ $t('page.category') }}</label>
              <select v-model="page.category" class="form-control">
                <option v-for="cat in categories" :value="cat">{{ $t('category.' + cat.code + '.default') }}</option>
              </select>
            </div>
  
            <div class="form-group">
              <label class="control-label">{{ $t('page.branch') }}</label>
              <select v-model="page.branch" class="form-control">
                <option v-for="branch in branches" :value="branch" v-if="page.category != null">{{ $t('category.' + page.category.code + '.' + branch.code) }}</option>
              </select>
            </div>
          </div>
        </div>
  
        <div class="form-group">
          <label class="control-label">{{ $t('page.summary') }}</label>
          <text-editor v-model="page.summary"></text-editor>
        </div>
  
        <div class="text-center">
          <button class="btn btn-rounded btn-danger" @click="deactivatePage" v-if="edit && page.state == 'PUBLISHED'">Deactivate</button>
          <a class="btn btn-rounded btn-success" @click="publishPage" v-if="edit && page.state == 'DEACTIVATED'">
            Publish
          </a>
          <button class="btn btn-rounded btn-primary" type="submit" @click="submit">
            <span v-if="edit">Save</span>
            <span v-else>Submit</span> {{ page.name }}</button>
        </div>
      </panel>
    </div>
    <div class="col-md-4" v-if="edit">
      <image-upload v-model="page.bannerUrl" />
    </div>
  </div>
</template>

<script>
import TextEditor from '../../elements/TextEditor.vue';
import { mapActions } from 'vuex';
import PublicApi from '../../services/api/public.api.js';
import ImageUpload from '../../elements/ImageUpload.vue';

export default {
  name: 'page-compose',
  props: {
    page: {
      type: Object,
      default () {
        return {
          name: null,
          category: null,
          branch: null,
          summary: null
        };
      }
    },
    edit: {
      type: Boolean,
      default: false
    }
  },
  inject: ['api'],
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
      'addPage', 'removePage'
    ]),
    submit () {
      if (this.edit) {
        this.api.putPage(this.page, page => {
          this.$emit('updated', page);
          this.$success('Success !', 'Page ' + page.name + ' has been updated.');
        });
      } else {
        this.api.postPage(this.page, page => {
          this.addPage(page);
          this.$success('Success !', 'Page ' + page.name + ' has been created.');
          this.$router.push({ name: 'page.settings', params: { id: page.id } });
        });
      }
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
    },
    publishPage () {
      this.api.publishPage(page => {
        this.$emit('updated', page);
      });
    },
    deactivatePage () {
      this.api.deactivatePage(page => {
        this.$emit('updated', page);
      });
    }
  }
};
</script>
