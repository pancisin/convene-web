<template>
  <div class="container">
    <div class="text-center m-b-10">
      <h1 class="title">Pricing for authors</h1>
      <p class="sub-title">Select subscription that fits your business. We care about you so
        <b>first month is on us !</b>
      </p>
    </div>

    <div class="row">
      <div class="col-lg-8 col-lg-offset-2">
        <div class="row">

          <div class="col-sm-4 col-md-4 col-lg-4">
            <div class="pricing-item">
              <div class="pricing-item-inner">
                <div class="pricing-wrap">

                  <div class="pricing-num pricing-num-pink">
                    <sup>
                      <i class="fa fa-euro"></i>
                    </sup>9
                  </div>
                  <div class="pr-per">
                    per month
                  </div>

                  <div class="pricing-title">
                    Starter Pack
                  </div>

                  <div class="pricing-features">
                    <ul class="sf-list pr-list">
                      <li>5 events / month</li>
                      <li>1 page</li>
                    </ul>
                  </div>

                  <div class="pr-button">
                    <a @click="selectSubscription('STARTER')" class="btn btn-primary btn-rounded" v-if="cur_sub.prop != 'STARTER'">
                      Sign up now !
                    </a>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="col-sm-4 col-md-4 col-lg-4">
            <div class="pricing-item main">
              <div class="ribbon">
                <span>POPULAR</span>
              </div>
              <div class="pricing-item-inner">
                <div class="pricing-wrap">

                  <div class="pricing-num">
                    <sup>
                      <i class="fa fa-euro"></i>
                    </sup>29
                  </div>
                  <div class="pr-per">
                    per month
                  </div>

                  <div class="pricing-title">
                    Premium Pack
                  </div>

                  <div class="pricing-features">
                    <ul class="sf-list pr-list">
                      <li>Unlimited events</li>
                      <li>5 pages</li>
                      <li>5 team members</li>
                      <li>Limited customers chat</li>
                      <li>Free support</li>
                    </ul>
                  </div>

                  <div class="pr-button">
                    <a @click="selectSubscription('PROFESSIONAL')" class="btn btn-primary btn-rounded" v-if="cur_sub.prop != 'PROFESSIONAL'">
                      Sign up now !
                    </a>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="col-sm-4 col-md-4 col-lg-4">
            <div class="pricing-item">
              <div class="pricing-item-inner">
                <div class="pricing-wrap">

                  <div class="pricing-num pricing-num-yellow">
                    <sup>
                      <i class="fa fa-euro"></i>
                    </sup>119
                  </div>
                  <div class="pr-per">
                    per month
                  </div>

                  <div class="pricing-title">
                    Enterprise Pack
                  </div>

                  <div class="pricing-features">
                    <ul class="sf-list pr-list">
                      <li>Unlimited pages & events</li>
                      <li>Unlimited conferences</li>
                      <li>Unlimited team members</li>
                      <li>Customers chat</li>
                      <li>Team chat / task board</li>
                      <li>Free Support</li>
                    </ul>
                  </div>

                  <div class="pr-button">
                    <a @click="selectSubscription('ENTERPRISE')" class="btn btn-primary btn-rounded" v-if="cur_sub.prop != 'ENTERPRISE'">
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
  </div>
</template>

<script>
import { mapGetters } from 'vuex';
export default {
  name: 'pricing',
  computed: {
    ...mapGetters(['license']),
    cur_sub () {
      if (this.license != null) {
        return this.license.subscription;
      };

      return {
        name: 'NOTHING'
      };
    }
  },
  methods: {
    selectSubscription (name) {
      this.$router.push({
        name: 'sub.signup',
        params: {
          subscription: name
        }
      });
    }
  }
};
</script>