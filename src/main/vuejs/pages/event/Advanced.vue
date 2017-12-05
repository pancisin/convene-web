<template>
  <div class="row">
    <div class="col-md-4">
      <panel type="default">
        <span slot="title">User filters</span>
        <form class="form" @submit.prevent="submit">
          <div class="form-group">
            <div class="checkbox checkbox-primary">
              <input id="checkbox-filter-domain" type="checkbox" v-model="filterDomain">
              <label for="checkbox-filter-domain">
                Filter by domain
              </label>
            </div>
          </div>

          <div class="firm-group" v-show="filterDomain">
            <label>Domain</label>
            <input type="text" class="form-control m-b-15" placeholder="domain, eg. example.com">
          
            <div class="alert alert-warning">
              Only accounts with specified domain address in registered email will have access to this event.
            </div>
            <hr>
          </div>

          <div class="form-group">
            <div class="checkbox checkbox-primary">
              <input id="checkbox-filter-country" type="checkbox" v-model="filterCountry" disabled>
              <label for="checkbox-filter-country">
                Filter by country
              </label>
            </div>
          </div>

          <div class="firm-group" v-show="filterCountry">
            <label>Country</label>
            <input type="text" class="form-control" placeholder="domain">
          </div>

          <div class="text-right">
            <button type="submit" class="btn btn-primary">Save</button>
          </div>
        </form>
      </panel>  
    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      filterDomain: false,
      filterCountry: false
    };
  },
  method: {
    submit () {

    }
  }
};
</script>

<style>

</style>
