<template>
  <div class="row">
    <div class="col-md-8">
      <panel type="default">
        <span slot="title">
          {{ $t('admin.page.settings') }}
        </span>
        <div class="row">
          <div class="col-md-6">
            <div class="form-group">
              <label class="control-label">{{ $t('page.name') }}</label>
              <input class="form-control required" v-model="page.name" type="text">
            </div>
          </div>
          <div class="col-md-6">
            <categorizer :category.sync="page.category" :branch.sync="page.branch" required />
          </div>
        </div>

        <div class="form-group">
          <label class="control-label">{{ $t('page.summary') }}</label>
          <text-editor v-model="page.summary"></text-editor>
        </div>

        {{ metadata }}

        <div class="form-group" v-for="(meta, index) in metadata" :key="index">
          <div class="input-group">
            <input class="form-control required" :value="meta.key" type="text">
            <span class="input-group-addon">
              <i class="fa fa-code"></i>
            </span>
            <input class="form-control required" :value="meta.value" type="text">
            <span class="input-group-btn">
              <button class="btn btn-danger" type="button">
                <i class="fa fa-trash-o"></i>
              </button>
            </span>
          </div>
        </div>

        <div class="text-center">
          <a class="btn btn-xs btn-primary btn-rounded">
            <i class="fa fa-plus"></i>
          </a>
        </div>

        <div class="text-center">
          <button class="btn btn-rounded btn-danger" @click="togglePagePublished(page)" v-if="page.state == 'PUBLISHED'">Deactivate</button>
          <a class="btn btn-rounded btn-success" @click="togglePagePublished(page)" v-if="page.state == 'DEACTIVATED'">
            Publish
          </a>
          <button class="btn btn-rounded btn-primary" type="submit" @click="submit" :disabled="page.state == 'BLOCKED'">
            Save
          </button>
        </div>
      </panel>

      <panel type="danger">
        <span slot="title">Completely delete page</span>
        <p class="m-b-15">Write name of page to ensure it's not misstake and confirm with 'Delete'.</p>

        <br />
        <div class="row">
          <div class="col-md-6">
            <input type="text" class="form-control" v-model="deleteConfirm">
          </div>
          <div class="col-md-6">
            <a class="btn btn-danger" :disabled="deleteConfirm !== page.name" @click="deleteP">Delete</a>
          </div>
        </div>
      </panel>
    </div>
    <div class="col-md-4">
      <image-upload v-model="page.posterData" :media="page.poster" />
    </div>
  </div>
</template>

<script>
import {
  TextEditor,
  ImageUpload,
  Categorizer
} from 'elements';
import { mapActions } from 'vuex';

export default {
  name: 'page-compose',
  props: {
    page: Object
  },
  data () {
    return {
      deleteConfirm: null,
      metadata: []
    };
  },
  watch: {
    'page': 'initializeMeta'
  },
  components: {
    TextEditor, ImageUpload, Categorizer
  },
  created () {
    this.initializeMeta(this.page);
  },
  methods: {
    ...mapActions([
      'removePage',
      'updatePage',
      'togglePagePublished',
      'deletePage'
    ]),
    initializeMeta (newVal) {
      const result = [];
      for (let key in newVal.metadata) {
        result.push({
          key,
          value: this.page.metadata[key]
        });
      }
      this.metadata = result;
    },
    submit () {
      const data = {
        ...this.page,
        metadata: this.metadata.reduce((map, obj) => {
          map[obj.key] = obj.value;
          return map;
        })
      };

      this.updatePage(data).then(page => {
        this.$success('notification.page.updated' + page.name);
      });
    },
    deleteP () {
      this.$prompt('notification.page.delete_prompt', () => {
        this.deletePage(this.page).then(() => {
          console.log('test');
        });
      });
    }
  }
};
</script>
