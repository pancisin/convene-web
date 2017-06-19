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
      <div class="fileupload waves-effect">
        <img :src="page.bannerUrl" class="img-thumbnail dropzone" style="width: 100%" />
        <input type="file" class="upload" @change="onLogoChange">
      </div>
    </div>
  </div>
</template>

<script>
import TextEditor from '../../elements/TextEditor.vue'
import { mapActions } from 'vuex'
import PageApi from '../../services/api/page.api.js'
import PublicApi from '../../services/api/public.api.js'

export default {
  name: 'page-compose',
  props: {
    page: {
      type: Object,
      default() {
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
  data() {
    return {
      categories: [],
      branches: []
    }
  },
  components: {
    TextEditor
  },
  created() {
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
    submit() {
      if (this.edit) {
        PageApi.putPage(this.page, page => {
          this.$emit('updated', page);
          this.$success('Success !', 'Page ' + page.name + ' has been updated.')
        })
      } else {
        this.api.postPage(this.page, page => {
          this.addPage(page);
          this.$success('Success !', 'Page ' + page.name + ' has been created.');
          this.$router.push({ name: 'page.settings', params: { id: page.id } });
        })
      }
    },
    getCategories() {
      this.branches = [];
      PublicApi.getCategories(categories => {
        this.categories = categories;
      })
    },
    onLogoChange(e) {
      var self = this;

      var files = e.target.files || e.dataTransfer.files;
      if (!files.length)
        return;

      var file = files[0];

      var image = new Image();
      var reader = new FileReader();

      reader.onload = (e) => {
        self.page.bannerUrl = e.target.result;
      };

      reader.readAsDataURL(file);
    },
    getBranches() {
      if (this.page.category == null) return;
      PublicApi.getBranches(this.page.category.id, branches => {
        this.branches = branches;
      })
    },
    publishPage() {
      this.api.publishPage(this.page.id, page => {
        this.$emit('updated', page);
      })
    },
    deactivatePage() {
      this.api.deactivatePage(this.page.id, page => {
        this.$emit('updated', page);
      })
    }
  }
}
</script>
