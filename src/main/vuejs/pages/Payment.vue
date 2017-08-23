<template>
  <panel class="col-md-6 col-md-offset-3">
    <div class="form-group" :class="{ 'has-error ' : errors.has('card_number') }">
      <label class="control-label">Card number *</label>
      <input class="form-control required" name="card_number" v-model="card.card_number" type="text" v-validate data-vv-rules="required|credit_card" autocomplete="cc-number">
      <span class="text-danger" v-if="errors.has('card_number')">{{ errors.first('card_number') }}</span>
    </div>
  
    <div class="row">
      <div class="col-sm-3">
        <div class="form-group" :class="{ 'has-error ' : errors.has('card_code') }">
          <label class="control-label">Card code *</label>
          <input class="form-control required" name="card_code" v-model="card.card_code" type="text" v-validate data-vv-rules="required|digits:3" autocomplete="cc-csc">
          <span class="text-danger" v-if="errors.has('card_code')">{{ errors.first('card_code') }}</span>
       </div>
      </div>
  
      <div class="col-sm-4">
      </div>
  
      <div class="col-sm-5">
        <div class="form-group" :class="{ 'has-error ' : errors.has('expiration_month') || errors.has('expiration_year') }">
          <label class="control-label">Expiration *</label>
          <div class="input-daterange input-group" id="date-range">
            <input type="text" class="form-control" name="expiration_month" v-model="card.expiration_month" v-validate data-vv-rules="required|between:1,12">
            <span class="input-group-addon bg-primary b-0 text-white">/</span>
            <input type="text" class="form-control" name="expiration_year" v-model="card.expiration_year" v-validate data-vv-rules="required|numeric">
          </div>
          <span class="text-danger" v-if="errors.has('expiration_month')">{{ errors.first('expiration_month') }}</span>
          <span class="text-danger" v-if="errors.has('expiration_year')">{{ errors.first('expiration_year') }}</span>
        </div>
      </div>
    </div>
  
    <div class="form-group">
      <label class="control-label">Name on card</label>
      <input class="form-control required" v-model="card.name_on_card" type="text" autocomplete="cc-name">
    </div>
  
    <div class="text-center">
      <a class="btn btn-success btn-rounded" @click="submitPayment">Submit payment</a>
    </div>
  </panel>
</template>

<script>
export default {
  name: 'invoice',
  data() {
    return {
      license: {},
      card: {}
    };
  },
  created() {
    this.getLicense();
  },
  methods: {
    getLicense() {
      var invoice_id = this.$route.params.invoice_id;

      this.$http.get('api/license/' + invoice_id).then(response => {
        this.license = response.body;
      });
    },
    submitPayment() {
      // var res = this.$resource('api/license/{license_id}/payment');
      // res.save({ license_id: this.license.id}, this.card).then(response => {
      // })

      this.$validator.validateAll().then(valid => {
        if (valid) {
          var url = ['api/license', this.license.id, 'payment'].join('/');
          this.$http.post(url, this.card).then(response => {
            if (response.body.successful === true) {
              this.$router.push('/admin/invoice/' + this.license.id);
            }
            console.log(response);
          }, response => {
            console.error(response);
          });
        }
      });
    }
  }
};
</script>

<style>

</style>
