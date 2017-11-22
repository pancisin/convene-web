<template>
  <div class="row">
    <div class="col-sm-6 col-sm-offset-3">
      <panel type="default">
        <span slot="title" v-if="subscription != null">sign up to {{ $t(subscription.code) }}</span>
  
        <div class="row">
          <div class="col-md-6">
            <div class="form-group">
              <label class="control-label">Email</label>
              <input class="form-control required" v-model="user.email" type="text" disabled>
            </div>
  
            <div class="form-group">
              <label class="control-label">First name</label>
              <input class="form-control required" v-model="user.firstName" type="text">
            </div>
  
            <div class="form-group">
              <label class="control-label">Last name</label>
              <input class="form-control required" v-model="user.lastName" type="text">
            </div>
          </div>
          <div class="col-md-6">
            <div class="row">
              <div class="col-xs-8">
                <div class="form-group">
                  <label class="control-label">Street</label>
                  <input class="form-control required" v-model="user.address.street" type="text">
                </div>
              </div>
              <div class="col-xs-4">
                <div class="form-group">
                  <label class="control-label">Number</label>
                  <input class="form-control required" v-model="user.address.number" type="text">
                </div>
              </div>
            </div>
  
            <div class="row">
              <div class="col-xs-6">
                <div class="form-group">
                  <label class="control-label">ZIP</label>
                  <input class="form-control required" v-model="user.address.zip" type="text">
                </div>
              </div>
              <div class="col-xs-6">
                <div class="form-group">
                  <label class="control-label">City</label>
                  <input class="form-control required" v-model="user.address.city" type="text">
                </div>
              </div>
            </div>
  
            <div class="form-group">
              <label class="control-label">State</label>
              <input class="form-control required" v-model="user.address.state" type="text">
            </div>
          </div>
        </div>
  
        <hr>
  
        <div class="text-center">
          <a class="btn btn-rounded btn-success" @click="submit">Submit</a>
        </div>
      </panel>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex';
export default {
  name: 'subscription-signup',
  data () {
    return {
      subscriptions: [],
      subscription: null
    };
  },
  created () {
    this.getSubscriptions();
  },
  computed: {
    ...mapGetters(['user'])
  },
  methods: {
    ...mapActions([
      'initializeUser'
    ]),
    getSubscriptions () {
      this.$http.get('/public/subscriptions').then(response => {
        this.subscriptions = response.body;

        this.subscription = this.getByName(this.$route.params.subscription);
      });
    },
    getByName (name) {
      var filter = this.subscriptions.filter(x => x.name === name);
      if (filter.length > 0) {
        return filter[0];
      }

      return null;
    },
    submit () {
      var data = {
        user: this.user,
        subscription: this.subscription.name
      };

      data.user.role = data.user.role.name;
      data.user.locale = data.user.locale.code;
      this.$http.post('/api/user/subscription', data).then(response => {
        this.initializeUser();
        console.log('FINE !');
      });
    }
  }
};
</script>