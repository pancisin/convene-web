<template>
  <div class="container conference-bg">
    <div class="row">
      <div class="col-lg-6 col-lg-offset-2">
        <wizard @finish="submit">
          <wizard-page title="About conferences" icon="suitcase" :valid="true">
            As enterprise subscriber you have an ability to create conferences. These are prepared for managing company conferences and massive meetings.
          </wizard-page>
          <wizard-page title="Basic information" icon="pencil" :valid="valid.basic">
            <form data-vv-scope="basic">
              <div class="form-group" :class="{ 'has-error' : errors.has('name') }">
                <label class="control-label">Name: </label>
                <input class="form-control" v-model="conference.name" type="text" name="name" v-validate data-vv-rules="required|min:3" @change="validate('basic')">
                <span class="text-danger" v-if="errors.has('name')">{{ errors.first('name') }}</span>
              </div>
              <div class="form-group" :class="{ 'has-error' : errors.has('visibility') }">
                <label class="control-label">Visibility: </label>
                <select v-model="conference.visibility" class="form-control" name="visibility" v-validate data-vv-rules="required" @change="validate('basic')">
                  <option :value="option" v-for="option in visibility_options" v-text="option" :key="option"></option>
                </select>
              </div>
            </form>
          </wizard-page>
          <wizard-page title="Conference details" icon="suitcase" :valid="true">
            <form data-vv-scope="detail">
              <div class="form-group m-t-20">
                <label class="control-label">Summary: </label>
                <text-editor v-model="conference.summary"></text-editor>
              </div>
            </form>
          </wizard-page>
        </wizard>
      </div>
    </div>
  </div>
</template>

<script>
import { Wizard, WizardPage, TextEditor } from 'elements';
import { mapActions } from 'vuex';

export default {
  name: 'create-conference',
  components: {
    Wizard, WizardPage, TextEditor
  },
  data () {
    return {
      conference: {},
      valid: {
        basic: false
      }
    };
  },
  computed: {
    visibility_options: {
      get () {
        return [
          'PUBLIC', 'PRIVATE', 'INVITED', 'AUTHENTICATED'
        ];
      }
    }
  },
  methods: {
    ...mapActions(
      ['createConference']
    ),
    validate (scope) {
      this.$validator.validateAll(scope).then(result => {
        this.valid[scope] = result;
      });
    },
    submit () {
      this.createConference(this.conference).then(conference => {
        this.$success('Success !', 'Conference ' + conference.name + ' has been created.');
        this.$router.push({ name: 'conference.overview', params: { id: conference.id } });
      });
    }
  }
};
</script>
