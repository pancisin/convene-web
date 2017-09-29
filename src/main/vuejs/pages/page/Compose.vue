<template>
  <div class="row">
    <div class="col-md-8">
      <panel type="default">
        <span slot="title">
          {{ $t('admin.page.overview') }}
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
  components: {
    TextEditor, ImageUpload, Categorizer
  },
  methods: {
    ...mapActions([
      'removePage', 'updatePage', 'togglePagePublished'
    ]),
    submit () {
      this.updatePage(this.page).then(page => {
        this.$success('notification.page.updated' + page.name);
      });
    }
  }
};
</script>
