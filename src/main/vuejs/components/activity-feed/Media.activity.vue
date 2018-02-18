<template>
  <div>
    <light-box 
      :image="activity.objectThumbnail.path" 
      v-if="activity.objectThumbnail" >
      
      <img :src="activity.objectThumbnail.path">
    </light-box>
  </div>   
</template>

<script>
export default {
  name: 'media-activity-feed',
  props: {
    obj: Object
  }
};
</script>

<style>

</style>
