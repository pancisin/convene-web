<template>
  <div>
    <div class="row">
      <div class="col-xs-8">
        <vue-input 
          v-model="address.street" 
          name="street" 
          label="Street"
          @input="updateInput" />
      </div>
      <div class="col-xs-4">
        <vue-input 
          v-model="address.number" 
          name="number"
          rules="required|numeric"
          label="Number"
          @input="updateInput" />
      </div>
    </div>

    <div class="row">
      <div class="col-xs-6">
        <vue-input 
          v-model="address.zip" 
          name="zip" 
          rules="required|numeric"
          label="ZIP"
          @input="updateInput" />
      </div>
      <div class="col-xs-6">
        <vue-input 
          v-model="address.city" 
          name="city" 
          label="City"
          @input="updateInput" />
      </div>
    </div>

    <vue-input 
      v-model="address.state" 
      name="state" 
      label="State"/>
  </div>
</template>

<script>
import VueInput from './VueInput';

export default {
  name: 'address-editor',
  props: ['value'],
  inject: ['$validator'],
  data () {
    return {
      address: {}
    };
  },
  components: {
    VueInput
  },
  created () {
    this.cloneAddress(this.value);
  },
  watch: {
    value (value) {
      this.cloneAddress(value);
    }
  },
  methods: {
    cloneAddress (address) {
      this.address = { ...address };
    },
    updateInput () {
      this.$emit('input', this.address);
    }
  }
};
</script>

<style>

</style>
