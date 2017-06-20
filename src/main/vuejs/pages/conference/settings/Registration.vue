<template>
  <panel type="default">
    <div class="row">
      <div class="col-md-4">
        <ul class="data-list">
          <li :class="{ 'active': selected && selected.id == null }">
            <a @click="selected = {}">
              + Create new
            </a>
          </li>
          <li v-for="field in metaFields" :class="{ 'active' : selected && selected.id == field.id }">
            <a @click="selected = field" class="waves-effect">
              {{ field.name }}
            </a>
          </li>
        </ul>
      </div>
      <transition name="fade">
        <div class="col-md-8" v-if="selected">
          <div class="form-group">
            <label class="control-label">Name: </label>
            <input class="form-control required" v-model="selected.name" type="text">
          </div>
  
          <div class="form-group">
            <label class="control-label">Type: </label>
            <input class="form-control required" v-model="selected.type" type="text">
          </div>
  
          <div class="text-center">
            <a class="btn btn-primary btn-rounded" @click="submit">Save</a>
          </div>
        </div>
      </transition>
    </div>
  </panel>
</template>

<script>
export default {
  name: 'registration-settings',
  props: ['conference'],
  inject: ['api'],
  data() {
    return {
      metaFields: [],
      selected: null,
    }
  },
  created() {
    this.initialize();
  },
  watch: {
    'conference': 'initialize',
  },
  methods: {
    initialize() {
      this.api.getMetaFields(this.conference.id, metaFields => {
        this.metaFields = metaFields;
      })
    },
    submit() {
      if (this.selected.id == null) {
        this.api.postMetaField(this.conference.id, this.selected, field => {
          this.metaFields.push(field);
        });
      } else {
        // put
      }
    },
  }
}
</script>

<style lang="less">
.data-list {
  border: 1px solid #ccc;
  list-style-type: none;
  margin: 0;
  padding: 0;

  li {
    transition: all .3s ease;

    a {
      display: block;
      padding: 10px;
      color: #000;
    }

    &.active {
      background-color: rgba(152, 166, 173, 0.1);

      a {
        color: #3bafda;
      }
    }
  }
}
</style>
