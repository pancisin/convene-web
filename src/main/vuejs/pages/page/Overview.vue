<template>
  <div class="row">
    <div class="col-md-8">
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
          <button class="btn btn-rounded btn-primary" type="submit" @click="submit">
            <span v-if="edit">Save</span>
            <span v-else>Submit</span> {{ page.name }}</button>
          <a class="btn btn-rounded btn-success" @click="publishPage" v-if="edit && page.state == 'DEACTIVATED'">
            Publish
          </a>
        </div>
      </panel>
    </div>
    <div class="col-md-4">
      <div class="fileupload waves-effect">
        <img :src="page.bannerUrl" class="img-thumbnail dropzone" style="width: 100%" />
        <input type="file" class="upload" @change="onLogoChange">
      </div>
    </div>
  </div>
</template>

<script>
import TextEditor from '../../elements/TextEditor.vue'
export default {
  name: 'page-overview',
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
    submit() {
      if (this.edit) {
        this.$http.put('api/page/' + this.page.id, this.page).then(response => {
          this.$emit('updated', response.body);
          this.$success('Success !', 'Page ' + this.page.name + ' has been updated.')
        });
      } else {
        this.$http.post('api/user/page', this.page).then(response => {
          var page = response.body;
          this.edit = true;
          this.$store.commit('addPage', page);
          this.$success('Success !', 'Page ' + page.name + ' has been created.');
          this.$router.push('/admin/page/' + page.id);
        });
      }
    },
    deletePage() {
      this.$http.delete('api/page/' + this.page.id).then(response => {
        this.$router.push('/admin');
        this.$store.commit('removePage', this.page);
      })
    },
    getCategories() {
      this.branches = [],
        this.$http.get('api/categories').then(response => {
          this.categories = response.body;
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
      var url = ['api/categories', this.page.category.id, 'branches'].join('/');
      this.$http.get(url).then(response => {
        this.branches = response.body;
      })
    },
    publishPage() {
      this.$http.patch('api/page/' + this.page.id + '/publish').then(response => {
        this.$emit('updated', response.body);
      })
    },
    deactivatePage() {
      this.$http.patch('api/page/' + this.page.id + '/deactivate').then(response => {
        this.$emit('updated', response.body);
      })
    }
  }
}
</script>
