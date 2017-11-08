<template>
  <footer class="footer text-right background-inverse">
    <div class="container">
      <div class="row">
        <div class="col-md-4 text-muted">
          <product-logo large/>

          <div class="footer-branding">
            2017 © Convene team. All rights reserved.
            <hr />
            Our goal is to provide clean, fast and simple way for managing events, conferences and even booking services of your business. Everything on one place and available for everybody.
          </div>
        </div>

        <div class="col-sm-4 col-md-offset-2 col-md-2 text-center">
          <nav>
            <span>About us</span>
            <ul>
              <li v-for="(item, index) in menus.about.routes" :key="index">
                <router-link :to="{ name: item.name }">
                  {{ $t(item.code) }}
                </router-link>
              </li>
            </ul>
          </nav>
        </div>

        <div class="col-sm-4 col-md-2 text-center">
          <nav>
            <span>Products</span>
            <ul>
              <li><a href="">ARTIST PORTFOLIO</a></li>
            </ul>
          </nav>
        </div>

        <div class="col-sm-4 col-md-2 text-center">
          <nav>
            <span>Contacts</span>
            <ul>
              <li><a href="">support@convene.sk</a></li>
            </ul>
          </nav>
        </div>
      </div>
    </div>
  </footer>
</template>

<script>
import { ProductLogo } from 'elements';
import debounce from 'debounce';
import menus from '../services/maps/menus.map';

export default {
  name: 'footer',
  components: {
    ProductLogo
  },
  computed: {
    menus () {
      return menus;
    }
  },
  mounted () {
    document.body.style.paddingBottom = `${this.$el.offsetHeight}px`;

    window.addEventListener('resize', debounce(() => {
      document.body.style.paddingBottom = `${this.$el.offsetHeight}px`;
    }, 200));
  }
};
</script>

<style lang="less">
@import (reference) '~less/variables.less';

.footer {
  background-color: @color-inverse;
  // background-color: #f9f9f9;
  border-top: 1px solid rgba(0, 0, 0, 0.1);
  bottom: 0px;
  color: #58666e;
  text-align: left !important;
  padding: 20px 0px;
  position: absolute;
  right: 0px;
  left: 0px;

  .footer-branding {
    font-size: 12px;
    margin-top: 15px;
    margin-bottom: 30px;

    hr {
      border-top: 1px solid lighten(@color-inverse, 5%);
    }
  }

  nav {
    & > span {
      letter-spacing: 0.03em;
      margin-bottom: 23px;
      color: @color-primary;
      text-transform: uppercase;
      display: block;
    }

    ul {
      margin: 0;
      // padding: 10px 0;
      list-style: none;
      padding: 0;

      li {
        margin: 3px 0;

        a {
          color: #98a6ad;
        }
      }
    }
  }
}

@media (max-width: 767px) { 
  .footer {
    nav {
      & > span {
        margin: 20px 0;
      }
    }
  }
}
</style>