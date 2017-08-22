<template>
  <div class="container">
    <ul class="conference-list">
      <li v-for="conf in conferences" :key="conf.id">
        <router-link :to="{ name: 'conference', params: { id: conf.id } }">
          <div class="banner">
          <img :src="conf.bannerUrl">
          </div>

          <div class="data">
            <h5>
              {{ conf.name }}
            </h5>
          </div>
        </router-link>
      </li>
    </ul>
  </div>
</template>

<script>
import RootApi from 'api/api';
import { mapGetters } from 'vuex';

export default {
  name: 'conference-index',
  data () {
    return {
      conferences: []
    };
  },
  created () {
    this.getConferences();
  },
  computed: {
    ...mapGetters(['authenticated'])
  },
  methods: {
    getConferences () {
      RootApi.getConferences(0, 10, this.authenticated, conferences => {
        this.conferences = conferences.content;
      });
    }
  }
};
</script>

<style lang="less">
ul.conference-list {
  display: flex;
  list-style-type: none;
  margin: 0;
  padding: 0;
  flex-wrap: wrap;

  li {
    flex: 250px 1 1;
    max-width: 530px;
  }

  li a {
    box-shadow: 5px 3px 15px 0px rgba(111, 110, 110, 0.3);
    margin: 10px;
    display: block;
    background-color: #fff;

    .banner {
      max-height: 150px;
      overflow: hidden;
    }

    .data {
      padding: 15px;

      h5 {
        margin: 0;
      }
    }

    img {
      display: block;
      width: 100%;
      margin: -14% auto;
      transition: all .3s ease;
    }

    &:hover img {
      transform: scale(1.1);
    }
  }
}
</style>
