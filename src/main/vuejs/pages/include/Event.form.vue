<template>
  <div class="row">
    <div class="col-xs-12">
  
      <form class="form" @submit.prevent="submit">
        <div class="form-group clearfix">
          <label class="col-md-3 control-label">Name *</label>
          <div class="col-md-9">
            <input class="form-control required" v-model="event.name" type="text">
          </div>
        </div>
      </form>
    </div>
  
  </div>
</template>
<script>
export default {
  name: 'event-form',
  props: {
    event: Object,
    default: new Object()
  },
  methods: {
    submit() {

    }
  }
}
</script>