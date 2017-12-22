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
            Size
          </th>
          <th>
            Created
          </th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="media in medias" :key="media.id">
          <td>
            <img :src="media.path" class="img-thumbnail" style="width: 100px">
          </td>
          <td>
            {{ media.title }}
          </td>
          <td>
            {{ media.size | bytes }}
          </td>
          <td>
            {{ media.created | luxon('ff') }}
          </td>
        </tr>
      </tbody>
    </table>
  </panel>
</template>

<script>
import UserApi from 'api/user.api';
import { bytes } from 'filters';

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
  filters: {
    bytes
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
