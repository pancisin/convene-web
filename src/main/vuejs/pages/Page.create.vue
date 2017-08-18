<template>
  <div class="container">
    <div class="row">
      <div class="col-lg-6 col-lg-offset-2">
        <wizard @finish="submit">
          <wizard-page title="What are pages ?" icon="suitcase" :valid="true">
            Bookster allows you to create your bussiness page where all information about events and services are displayed. Please keep in mind that there are rules for posting media content to public. All these resctrictions are described in terms & conditions. Please read carefully.
          </wizard-page>
          <wizard-page title="Basic information" icon="pencil" :valid="valid.basic">
            <form data-vv-scope="basic">
              <div class="form-group" :class="{ 'has-error' : errors.has('name') }">
                <label class="control-label">Name: </label>
                <input class="form-control" v-model="page.name" type="text" name="name" v-validate data-vv-rules="required|min:3" @change="validate('basic')">
                <span class="text-danger" v-if="errors.has('name')">{{ errors.first('name') }}</span>
              </div>
              <div class="form-group m-t-20">
                <label class="control-label">Summary: </label>
                <text-editor v-model="page.summary"></text-editor>
              </div>
            </form>
          </wizard-page>
          <wizard-page title="Categorize" icon="suitcase" :valid="valid.category">
            <form data-vv-scope="category">
              <div class="form-group" :class="{ 'has-error' : errors.has('category') }">
                <label class="control-label">{{ $t('page.category') }}</label>
                <select v-model="page.category" class="form-control" name="category" v-validate data-vv-rules="required" @change="validate('category')">
                  <option v-for="cat in categories" :value="cat" :key="cat">{{ $t('category.' + cat.code + '.default') }}</option>
                </select>
                <span class="text-danger" v-if="errors.has('category')">{{ errors.first('category') }}</span>
              </div>
  
              <div class="form-group" v-if="page.category != null" :class="{ 'has-error' : errors.has('branch') }">
                <label class="control-label">{{ $t('page.branch') }}</label>
                <select v-model="page.branch" class="form-control" name="branch" v-validate data-vv-rules="required" @change="validate('category')">
                  <option v-for="branch in branches" :value="branch" :key="branch">{{ $t('category.' + page.category.code + '.' + branch.code) }}</option>
                </select>
                <span class="text-danger" v-if="errors.has('branch')">{{ errors.first('branch') }}</span>
              </div>
            </form>
          </wizard-page>
        </wizard>
      </div>
    </div>
  </div>
</template>

<script>
import Wizard from '../elements/Wizard';
import WizardPage from '../elements/WizardPage';
import { mapActions } from 'vuex';
import TextEditor from '../elements/TextEditor';
import PublicApi from 'api/public.api';

export default {
  name: 'create-page',
  components: {
    Wizard, WizardPage, TextEditor
  },
  data () {
    return {
      page: {
        name: null,
        summary: null,
        category: null,
        branch: null
      },
      valid: {
        basic: false,
        category: false
      },
      categories: [],
      branches: []
    };
  },
  created () {
    this.getCategories();
    this.$watch('page.category', () => {
      this.getBranches();
    });
  },
  methods: {
    ...mapActions(
      ['createPage']
    ),
    validate (scope) {
      this.$validator.validateAll(scope).then(result => {
        this.valid[scope] = result;
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
    },
    submit () {
      this.createPage(this.page).then(page => {
        this.$success('Success !', 'Page ' + page.name + ' has been created.');
        this.$router.push({ name: 'page.settings', params: { id: page.id } });
      });
    }
  }
};
</script>
