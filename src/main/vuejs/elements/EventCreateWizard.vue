<template>
  <wizard @finish="submit">
    <wizard-page title="About Events" icon="suitcase" :valid="true">
      Convene is everything you'll need to prepare and plan an event. So let's go to create one !
    </wizard-page>
    <wizard-page title="Basic information" icon="pencil" :valid="valid.basic">
      <form data-vv-scope="basic">
        <div class="form-group" :class="{ 'has-error' : errors.has('name') }">
          <label class="control-label">Name: </label>
          <input class="form-control" v-model="event.name" type="text" name="name" v-validate data-vv-rules="required|min:3" @change="validate('basic')">
          <span class="text-danger" v-if="errors.has('name')">{{ errors.first('name') }}</span>
        </div>
        <div class="form-group" :class="{ 'has-error' : errors.has('date') }">
          <label class="control-label">Date</label>
          <date-picker v-model="event.date" v-validate data-vv-rules="required" name="date"  @change="validate('basic')"></date-picker>
          <span class="text-danger" v-if="errors.has('name')">{{ errors.first('date') }}</span>
        </div>
        <div class="form-group" :class="{ 'has-error' : errors.has('visibility') }">
          <label class="control-label">Visibility: </label>
          <select v-model="event.visibility" class="form-control" name="visibility" v-validate data-vv-rules="required" @change="validate('basic')">
            <option :value="option" v-for="option in visibility_options" v-text="option" :key="option"></option>
          </select>
        </div>
      </form>
    </wizard-page>
    <wizard-page title="Event details" icon="suitcase" :valid="true">
      <form data-vv-scope="detail">
        <div class="form-group m-t-20">
          <label class="control-label">Summary: </label>
          <text-editor v-model="event.summary"></text-editor>
        </div>
      </form>
    </wizard-page>
  </wizard>
</template>

<script>
import Wizard from './Wizard';
import WizardPage from './WizardPage';
import DatePicker from './DatePicker';
import TextEditor from './TextEditor';

export default {
  name: 'event-create-wizard',
  props: {
    postFunc: Function,
    date: [ String, Number ]
  },
  data () {
    return {
      event: {},
      valid: {
        basic: false
      }
    };
  },
  created () {
    this.event.date = parseInt(this.date, 10);
  },
  components: {
    Wizard,
    WizardPage,
    DatePicker,
    TextEditor
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
    submit () {
      this.postFunc(this.event, event => {
        this.$emit('success', this.event);
      });
    },
    validate (scope) {
      this.$validator.validateAll(scope).then(result => {
        this.valid[scope] = result;
      });
    }
  }
};
</script>