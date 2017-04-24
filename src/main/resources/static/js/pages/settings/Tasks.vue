<template>
  <div class="row">
    <div class="col-xs-12">
      <div class="card card-tab card-mini">
        <div class="card-header">
          <ul class="nav nav-tabs">
            <li role="tab1"
                v-for="attribute in attributes"
                :class="{ 'active' : selectedAttribute.id == attribute.id }">
              <a aria-controls="tab1"
                 role="tab"
                 data-toggle="tab"
                 @click="selectedAttribute = attribute"
                 aria-expanded="false">{{ $t(attribute.name) }}</a>
            </li>
            <li role="tab1">
              <a aria-controls="tab1"
                 role="tab"
                 data-toggle="tab"
                 aria-expanded="false">
                <i class="fa fa-plus"
                   aria-hidden="true"></i>
              </a>
            </li>
          </ul>
        </div>
        <div class="card-body">
          <transition name="fade">
            <component :is="currentTab"
                       :attribute="selectedAttribute"></component>
          </transition>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Attribute from './Tasks/Attribute.vue'
export default {
  name: "task-settings",
  data: function () {
    return {
      currentTab: 'attribute',
      selectedAttribute: null,
      attributes: [],
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
  }
}
</script>