<template>
  <panel type="table">
    <span slot="title">Medias</span>
    <table class="table">
      <thead>
        <tr>
          <th>
            Image
          </th>
          <th>
            Title
          </th>
          <th>
            Created
          </th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(media, index) in medias" :key="media.id">
          <td>
            <img :src="media.path" class="img-thumbnail" style="width: 100px">
          </td>
          <td>
            {{ media.title }}
          </td>
          <td>
            {{ media.created | moment('L') }}
          </td>
        </tr>
      </tbody>
    </table>
  </panel>
</template>

<script>
import UserApi from 'api/user.api';
export default {
  name: 'media-manager',
  data () {
    return {
      medias: []
    };
  },
  created () {
    this.getMedia();
  },
  methods: {
    getMedia () {
      UserApi.getMedia(medias => {
        this.medias = medias;
      });
    }
  }
};
</script>

<style>

</style>
