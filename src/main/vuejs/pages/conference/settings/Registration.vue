<template>
  <panel>
    <div class="row">
      <div class="col-md-4">
        <ul class="data-list">
          <li v-for="field in metaFields" :class="{ 'active' : selected && selected.id == field.id }" :key="field.id">
            <a @click="selected = field" class="waves-effect">
              {{ field.name }}
            </a>
          </li>
          <li :class="{ 'active': selected && selected.id == null }">
            <a @click="selected = {}">
              + Create new
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
            <label class="control-label">Description: </label>
            <input class="form-control" v-model="selected.description" type="text">
          </div>
  
          <div class="form-group">
            <div class="checkbox checkbox-primary">
              <input id="optional-checkbox" type="checkbox" v-model="selected.optional">
              <label for="optional-checkbox">
                Optional
              </label>
            </div>
          </div>
  
          <div class="form-group">
            <label class="control-label">Type: </label>
  
            <select class="form-control" v-model="selected.type">
              <option v-for="mtype in metaTypes" v-text="mtype" :key="mtype"></option>
            </select>
          </div>
  
          <div class="form-group" v-if="selected.type == 'SELECT' || selected.type == 'RADIO'">
            <label class="control-label">Options: </label>
  
            <ul class="options-list">
              <li v-for="option in selected.options" :key="option">
                {{ option }}
                <a class="pull-right" @click="deleteOption(option)">
                  <i class="fa fa-times text-danger"></i>
                </a>
              </li>
              <li>
                <div class="form-group">
                  <div class="input-daterange input-group" id="date-range">
                    <input type="text" class="form-control" v-model="new_option">
                    <a class="input-group-addon btn btn-primary" @click="addOption">Add</a>
                  </div>
                </div>
              </li>
            </ul>
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
import MetaFieldApi from 'api/meta-field.api';
import PublicApi from 'api/public.api';

export default {
  name: 'registration-settings',
  props: ['conference'],
  inject: ['provider'],
  data () {
    return {
      metaFields: [],
      metaTypes: [],
      selected: null,
      new_option: null
    };
  },
  created () {
    this.initialize();
  },
  watch: {
    'conference': 'initialize'
  },
  computed: {
    api () {
      return this.provider.api;
    }
  },
  methods: {
    initialize () {
      this.api.getMetaFields(metaFields => {
        this.metaFields = metaFields;
      });

      PublicApi.getMetaTypes(metaTypes => {
        this.metaTypes = metaTypes;
      });
    },
    submit () {
      if (this.selected.id == null) {
        this.api.postMetaField(this.selected, field => {
          this.metaFields.push(field);
          this.$success('notification.meta_field.created', field.name);
        });
      } else {
        MetaFieldApi.putMetaField(this.selected, field => {
          this.selected = field;
          this.$success('notification.meta_field.updated', field.name);
        });
      }
    },
    deleteOption (option) {
      this.selected.options = this.selected.options.filter(o => {
        return o !== option;
      });
    },
    addOption () {
      if (this.selected.options == null) {
        this.selected.options = [];
      }

      if (this.new_option != null && this.new_option.trim() !== '') {
        this.selected.options.push(this.new_option);
        this.new_option = null;
      }
    }
  }
};
</script>

<style lang="less">
@import (reference) '~less/variables.less';
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
        color: @color-primary;
      }
    }
  }
}

.options-list {
  list-style-type: none;
  margin: 0;
  padding: 0;
  border: 1px solid #ccc;

  li {
    padding: 10px;
  }
}
</style>
