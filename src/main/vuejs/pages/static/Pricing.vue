<template>
  <div class="container">
    <div class="text-center m-b-10">
      <h1 class="title">Pricing for authors</h1>
      <p class="sub-title">
        Select subscription that fits your business. We care about you so
        <b>first month is on us !</b>
      </p>
    </div>

    <div class="row">
      <div class="col-lg-8 col-lg-offset-2">
        <div class="row">

          <div 
            v-for="(subscription, index) in subscriptions" 
            :key="index" 
            class="col-sm-4 col-md-4 col-lg-4">

            <div class="pricing-item">
              <div class="pricing-item-inner">
                <div class="pricing-wrap">

                  <div class="pricing-num pricing-num-pink">
                    <sup>
                      <i class="fa fa-euro"></i>
                    </sup>{{ subscription.price }}
                  </div>
                  <div class="pr-per">
                    per month
                  </div>

                  <div class="pricing-title">
                    {{ subscription.name }}
                  </div>

                  <div class="pricing-features">
                    <ul class="sf-list pr-list" >
                      <li 
                        v-for="(feature, findex) in subscription.features" 
                        :key="findex">{{ feature }}</li>
                    </ul>
                  </div>

                  <div class="pr-button">
                    <a 
                      @click="selectSubscription(subscription.type)" 
                      class="btn btn-primary btn-rounded" 
                      v-if="cur_sub.prop != subscription.type">

                      Sign up now !
                    </a>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <modal :show.sync="displaySubscribeModal">
      <span slot="header">{{ selectedSubscription }}</span>
      <div slot="body">
        <div class="alert alert-warning">
          <i 
            class="fa fa-exclamation-triangle fa-2x pull-left" 
            style="margin-right: 20px">
          </i>
          Please review or update your billing information in order to generate correct invoice.
        </div>

        <subscribe-form 
          :license="selectedSubscription" 
          @submit="updateLicense" />
      </div>
    </modal>

  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex';
import { SubscribeForm } from 'elements/forms';
import SubscriptionsMap from '../../services/maps/subscriptions.map';

export default {
  name: 'pricing',
  data () {
    return {
      displaySubscribeModal: false,
      selectedSubscription: null
    };
  },
  components: {
    SubscribeForm
  },
  computed: {
    ...mapGetters(['license']),
    cur_sub () {
      if (this.license != null) {
        return this.license.subscription;
      };

      return {
        name: 'NOTHING'
      };
    },
    subscriptions () {
      return SubscriptionsMap;
    }
  },
  methods: {
    ...mapActions(['initializeUser']),
    selectSubscription (name) {
      this.$tryAuthenticate(() => {
        if (this.cur_sub !== name) {
          this.selectedSubscription = name;
          this.displaySubscribeModal = true;
        }
      });
    },
    updateLicense (result) {
      this.displaySubscribeModal = false;
      this.initializeUser();
    }
  }
};
</script>