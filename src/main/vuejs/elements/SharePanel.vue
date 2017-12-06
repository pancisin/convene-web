<template>
  <div class="share-panel">
    <a 
      v-for="(service, key) in services" 
      :key="key" 
      :class="service.class" 
      :href="service.url({ title, description, media })"
      target="_blank">

      <i class="fa" :class="service.icon"></i>
      {{ service.content }}
    </a>
  </div>
</template>

<script>
import ShareServicesMap from '../services/maps/share-services.map';

export default {
  name: 'share-panel',
  props: {
    title: String,
    description: String,
    media: String
  },
  computed: {
    services () {
      return ShareServicesMap;
    }
  }
};
</script>

<style lang="less">
@color-facebook: #3b5998;
@color-google: #dd4b39;
@color-pinterest: #cb2027;

.share-panel {
  display: flex;
  flex-direction: row;

  a {
    flex: 1 1 50px;
    display: inline-block;
    padding: 15px;
    transition: background-color .3s ease-in-out;
    text-align: center;
    line-height: 21px;

    i {
      display: block;
    }

    & ~ a {
      margin-left: 1px;
    }
  }
  
  a.share-panel-facebook {
    background-color: @color-facebook;
    color: #fff;
  
    &:hover {
      background-color: darken(@color-facebook, 5%)
    }
  }

  a.share-panel-google {
    background-color: @color-google;
    color: #fff;

    &:hover {
      background-color: darken(@color-google, 5%);
    }
  }

  a.share-panel-pinterest {
    background-color: @color-pinterest;
    color: #fff;

    &:hover {
      background-color: darken(@color-pinterest, 5%);
    }
  }
}
</style>
