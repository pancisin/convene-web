<template>
  <div>
    <transition-group name="fade-down">
      <div class="row formField-field-row" v-for="(field, index) in formCopy.formFields" :key="index">
        <div class="col-md-2 col-lg-1 text-center">
          <h3>
            {{ index + 1 }}.
          </h3>

          <div class="m-b-10">
            <button class="btn btn-default btn-rounded btn-xs" @click="moveMeta(index, -1)" v-if="index > 0">
              <i class="fa fa-arrow-up"></i>
            </button>

            <button class="btn btn-default btn-rounded btn-xs" @click="moveMeta(index, 1)" v-if="index + 1 < formCopy.formFields.length">
              <i class="fa fa-arrow-down"></i>
            </button>
          </div>

          <button class="btn btn-danger btn-rounded btn-xs" @click="removeMeta(index)">
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
            <input class="form-control" v-model="field.description" type="text">
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
                <form @submit.prevent="addOption(field, $event)">
                  <div class="form-group">
                    <div class="input-group">
                      <input type="text" class="form-control" name="option">
                      <span class="input-group-btn">
                        <button class="btn btn-primary" type="submit">
                          <i class="fa fa-plus"></i> Add
                        </button>
                      </span>
                    </div>
                  </div>
                </form>
              </li>
            </ul>
          </div>
        </div>

      </div>
    </transition-group>

    <div class="text-center">
      <button @click="formCopy.formFields.push({})" class="btn btn-rounded m-t-15 m-b-15">
        <i class="fa fa-plus"></i>
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
    formCopy: {
      handler () {
        this.$emit('input', this.formCopy);
      },
      deep: true
    }
  },
  methods: {
    removeMeta (index) {
      this.formCopy.formFields.splice(index, 1);
    },
    moveMeta (index, direction) {
      let element = this.survey.metaFields[index];
      this.survey.metaFields.splice(index, 1);
      this.survey.metaFields.splice(index + direction, 0, element);
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

<style>

</style>
