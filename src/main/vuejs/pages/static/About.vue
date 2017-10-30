<template>
  <div>
    <section class="about-section">
      <hero-unit fluid :background="notepad">
        <div class="row">
          <div class="col-sm-8 col-sm-offset-2">
            <div class="text-center">
              <product-logo large />
              <p>
                Our goal is to provide clean, fast and simple way for managing events, conferences and even booking services of your business. Everything on one place and available for everybody.
              </p>
            </div>
          </div>
        </div>
      </hero-unit>
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
import Notepad from 'assets/img/notepad.jpg';

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

</style>