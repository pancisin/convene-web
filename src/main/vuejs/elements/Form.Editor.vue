<template>
  <div class="form-editor">
    <transition-group name="fade-down">
      <div class="row field-row" v-for="(field, index) in formCopy.formFields" :key="index">
        <div class="col-md-2 col-lg-1 text-center">
          <h3>
            {{ index + 1 }}.
          </h3>

          <div class="m-b-10">
            <button type="button" class="btn btn-default btn-rounded btn-xs" @click="moveField(index, -1)" v-if="index > 0">
              <i class="fa fa-arrow-up"></i>
            </button>

            <button type="button" class="btn btn-default btn-rounded btn-xs" @click="moveField(index, 1)" v-if="index + 1 < formCopy.formFields.length">
              <i class="fa fa-arrow-down"></i>
            </button>
          </div>

          <button type="button" class="btn btn-danger btn-rounded btn-xs" @click="removeField(index)">
            <i class="fa fa-minus"></i>
          </button>
        </div>

        <div class="col-md-5">
          <div class="form-group">
            <label class="control-label">Name</label>
            <input class="form-control required" v-model="field.name" type="text">
          </div>
          <div class="form-group">
            <label class="control-label">Description</label>
            <textarea class="form-control" v-model="field.description" rows="3"></textarea>
          </div>
          <div class="form-group">
            <div class="checkbox checkbox-primary">
              <input :id="'optional-' + index" type="checkbox" v-model="field.optional">
              <label :for="'optional-' + index">
                Optional
              </label>
            </div>
          </div>
        </div>
        <div class="col-md-5 col-lg-6">
          <div class="form-group">
            <label class="control-label">Type: </label>

            <select class="form-control" v-model="field.type">
              <option v-for="mtype in meta_types" v-text="mtype" :key="mtype"></option>
            </select>
          </div>

          <div class="form-group" v-if="field.type == 'SELECT' || field.type == 'RADIO'">
            <label class="control-label">Options: </label>

            <ul class="list-unstyled">
              <li v-for="(option, op_in) in field.options" :key="option" class="m-b-10">
                {{ option }}
                <a class="pull-right" @click="deleteOption(field, op_in)">
                  <i class="fa fa-times text-danger"></i>
                </a>
              </li>
              <li>
                <div class="form-group">
                  <div class="input-group">
                    <input type="text" class="form-control" name="option">
                    <span class="input-group-btn">
                      <button type="button" class="btn btn-default" @click="addOption(field, $event)">
                        <i class="fa fa-plus"></i> Add
                      </button>
                    </span>
                  </div>
                </div>
              </li>
            </ul>
          </div>
        </div>

      </div>
    </transition-group>

    <div class="text-center">
      <button type="button" @click="addField" class="btn btn-rounded m-t-15 m-b-15">
        <i class="fa fa-plus"></i> Add field
      </button>
    </div>
  </div>
</template>

<script>
import PublicApi from 'api/public.api';
export default {
  name: 'form-editor',
  props: {
    value: {
      type: Object,
      default () {
        return {
          formFields: []
        };
      }
    }
  },
  data () {
    return {
      meta_types: [],
      formCopy: {}
    };
  },
  created () {
    PublicApi.getMetaTypes(meta_types => {
      this.meta_types = meta_types;
    });

    this.formCopy = { formFields: [], ...this.value };
  },
  watch: {
    value (newVal) {
      if (newVal.id !== this.formCopy.id) {
        this.formCopy = { formFields: [], ...newVal };
      }
    },
    formCopy: {
      handler () {
        const data = {
          ...this.formCopy,
          formFields: this.formCopy.formFields.map((field, index) => {
            return {
              ...field,
              ordering: index
            };
          })
        };

        this.$emit('input', data);
      },
      deep: true
    }
  },
  methods: {
    removeField (index) {
      this.formCopy.formFields.splice(index, 1);
    },
    moveField (index, direction) {
      let element = this.formCopy.formFields[index];
      this.formCopy.formFields.splice(index, 1);
      this.formCopy.formFields.splice(index + direction, 0, element);
    },
    addField () {
      if (this.formCopy.formFields.filter(x => Object.keys(x).length === 0).length === 0) {
        this.formCopy.formFields.push({});
      }
    },
    deleteOption (field, index) {
      field.options.splice(index, 1);
    },
    addOption (field, e) {
      let option = e.target.option.value;

      if (field.options == null) {
        field.options = [];
      }

      if (option != null && option.trim() !== '') {
        field.options.push(option);
        e.target.option.value = null;
      }
    }
  }
};
</script>

<style lang="less">
.form-editor {
  .field-row {

    & ~ .field-row {
      margin-top: 20px;
      padding-top: 20px;
      border-top: 1px dashed #ddd;
    }
  }
}
</style>
