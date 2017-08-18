<template>
  <div class="row">
    <div class="col-md-6 col-md-offset-2">
      <wizard @finish="submit">
        <wizard-page title="About conferences" icon="suitcase">
          As enterprise subscriber you have an ability to create conferences. These are prepared for managing company conferences and massive meetings.
        </wizard-page>
        <wizard-page title="Basic information" icon="pencil">
          <div class="form-group">
            <label class="control-label">Name: </label>
            <input class="form-control required" v-model="conference.name" type="text">
          </div>
          <div class="form-group">
            <label class="control-label">Visibility: </label>
            <select v-model="conference.visibility" class="form-control">
              <option :value="option" v-for="option in visibility_options" v-text="option" :key="option"></option>
            </select>
          </div>
  
        </wizard-page>
      </wizard>
    </div>
  </div>
</template>

<script>
import Wizard from '../../elements/Wizard';
import WizardPage from '../../elements/WizardPage';
import { mapActions } from 'vuex';

export default {
  name: 'create-conference',
  components: {
    Wizard, WizardPage
  },
  data () {
    return {
      conference: {}
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
    submit () {
      this.createConference(this.conference).then(conference => {
        this.$success('Success !', 'Conference ' + conference.name + ' has been created.');
        this.$router.push({ name: 'conference.overview', params: { id: conference.id } });
      });
    }
  }
};
</script>

<style>

</style>
