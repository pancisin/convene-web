<template>
  <div>
    <section class="about-section">
      <div class="container">
        <div class="row">
          <div class="col-sm-8 col-sm-offset-2">
            <div class="text-center">
              <product-logo large />

              <p>Our goal is to provide clean, fast and simple way for managing events, conferences and even booking services of your business. Everything on one place and available for everybody.
              </p>
            </div>
          </div>
        </div>
      </div>
    </section>
    <section class="team-section">
      <div class="container">
        <h2>Our team</h2>
      </div>
    </section>
  </div>
</template>

<script>
import { HeroUnit, ProductLogo } from 'elements';

export default {
  name: 'about',
  data () {
    return {

    };
  },
  components: {
    HeroUnit,
    ProductLogo
  },
  created () {
    console.warn(Notepad);
  },
  methods: {

  }
};
</script>

<style lang="less">
.about-section {
  padding-top: 120px;
  padding-bottom: 120px;
  padding: 150px 0px 150px 0px;
}
</style>