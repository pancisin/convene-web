<template>
  <div class="container">
    <div class="row">
      <div class="col-lg-6 col-lg-offset-2">
        <wizard @finish="submit" style="padding: 30px; background: #fff">
          <wizard-page title="What are pages ?" icon="suitcase" :valid="true">
            Convene allows you to create your bussiness page where all information about events and services are displayed. Please keep in mind that there are rules for posting media content to public. All these resctrictions are described in terms & conditions. Please read carefully.
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
            <categorizer :category.sync="page.category" :branch.sync="page.branch" required />
          </wizard-page>
        </wizard>
      </div>
    </div>
  </div>
</template>

<script>
import { Wizard, WizardPage, TextEditor, Categorizer } from 'elements';
import { mapActions } from 'vuex';

export default {
  name: 'create-page',
  components: {
    Wizard, WizardPage, TextEditor, Categorizer
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
      }
    };
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
    submit () {
      this.createPage(this.page).then(page => {
        this.$success('notification.page.created', page.name);
        this.$router.push({ name: 'page.settings', params: { id: page.id } });
      });
    }
  }
};
</script>
