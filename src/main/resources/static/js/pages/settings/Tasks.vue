<template>
  <div class="row">
    <div class="col-xs-12">
      <div class="card card-tab card-mini">
        <div class="card-header">
          <ul class="nav nav-tabs">
            <li v-for="attribute in attributes"
                :class="{ 'active' : selectedAttribute.id == attribute.id }">
              <a aria-controls="tab1"
                 role="tab"
                 data-toggle="tab"
                 @click="selectedAttribute = attribute"
                 aria-expanded="false">{{ $t(attribute.name) }}</a>
            </li>
            <li>
              <a aria-controls="tab1"
                 role="tab"
                 data-toggle="tab"
                 aria-expanded="false"
                 @click="display.modal = true">
                <i class="fa fa-plus"
                   aria-hidden="true"></i>
              </a>
            </li>
          </ul>
        </div>
        <div class="card-body">
          <attribute :attribute="selectedAttribute"
                     @destructed="removeAttribute"
                     @error="displayError"></attribute>
        </div>
      </div>
    </div>
  
    <modal :show="display.modal"
           :question="true"
           @accept="submitAttribute"
           @close="display.modal = false">
      <h4 slot="header">Create Attribute</h4>
      <p slot="body">
        <label>Attribute</label>
        <input type="text"
               class="form-control"
               v-model="new_attribute" />
      </p>
    </modal>
  
    <modal :show.sync="display.error_modal"
           @close="display.error_modal = false">
      <h4 slot="header"
          v-if="error != null"
          v-text="error.title"></h4>
      <p slot="body"
         v-if="error != null"
         v-text="error.message"></p>
    </modal>
  </div>
</template>

<script>
import Attribute from './Tasks/Attribute.vue'
export default {
  name: "task-settings",
  data: function () {
    return {
      selectedAttribute: null,
      attributes: [],
      creating_attr: false,
      new_attribute: null,
      display: {
        modal: false,
        error_modal: false,
      }, 
      error: null,
    }
  },
  components: {
    Attribute
  },
  created: function () {
    this.getAttributes();
  },
  methods: {
    getAttributes: function () {
      var url = ['api/company', this.$store.getters.company_id, 'attributes'].join('/');
      this.$http.get(url).then(response => {
        this.attributes = response.body;
        if (this.attributes.length > 0) {
          this.selectedAttribute = this.attributes[0];
        }
      })
    },
    submitAttribute: function () {
      var url = ['api/company', this.$store.getters.company_id, 'attributes'].join('/');

      this.$http.post(url, JSON.stringify({
        name: this.new_attribute
      })).then(response => {
        this.new_attribute = null;
        this.attributes.push(response.body);
        this.display.modal = false;
      })
    },
    removeAttribute: function (attribute) {
      this.attributes = this.attributes.filter(x => {
        return x.id != attribute.id;
      });

      if (this.attributes.length > 0)
        this.selectedAttribute = this.attributes[0];
    },
    displayError: function (error) {
      this.error = error;
      this.display.error_modal = true;
    }
  }
}
</script>