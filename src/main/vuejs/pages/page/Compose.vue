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
import { TextEditor, ImageUpload, Categorizer } from 'elements';
import { mapActions } from 'vuex';

export default {
  name: 'page-compose',
  props: {
    page: Object
  },
  data () {
    return {
      deleteConfirm: null
    };
  },
  components: {
    TextEditor, ImageUpload, Categorizer
  },
  methods: {
    ...mapActions([
      'removePage', 'updatePage', 'togglePagePublished', 'deletePage'
    ]),
    submit () {
      this.updatePage(this.page).then(page => {
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
