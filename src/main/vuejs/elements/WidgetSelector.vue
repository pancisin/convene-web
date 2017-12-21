<template>
  <div>
    <div class="text-center">
      <a class="btn btn-danger" @click="editModeSwitch(false)">Cancel</a>
      <a class="btn btn-primary" @click="saveWidgets">Save</a>
    </div>

    <div class="">
      <h5>Available widgets: </h5>

      <ul class="widget-types-list">
        <li v-for="(w_type, key) in widgetTypes" :key="key">
          {{ $t(w_type.code) }}
          <a class="btn btn-default btn-block" @click="addWidget(w_type)">
            Add
          </a>

          <img :src="w_type.thumbnail" />
        </li>
      </ul>

    </div>
  </div>
</template>

<script>
export default {
  name: 'widgets-selector'

};
</script>

<style>

</style>
