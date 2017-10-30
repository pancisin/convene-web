<template>
  <div>
    <section>
      <hero-unit fluid :background="notepad">
        <div class="row">
          <div class="col-sm-8 col-sm-offset-2">
            <div class="home-wrapper  text-center">
              <product-logo large />
              <p>Our goal is to provide clean, fast and simple way for managing events, conferences and even booking services of your business. Everything on one place and available for everybody.
              </p>
              <a class="btn btn-primary btn-rounded">Try it right now !</a>
            </div>
          </div>
        </div>
      </hero-unit>
    </section>

    <section class="section bg-gray" id="how-it-work">
      <how-it-works />
    </section>

    <section class="events">
      <hero-unit :background="crowd">
        <div class="row">
          <div class="col-md-6 col-md-offset-3 content bg-gray">

            <div class="text-center">
              <h2 class="title">Still don't know what to do today ? </h2>
              <p class="sub-title">Convene is full of public events right from your neighborhood just select one and join. Do not forget to tell your friends about that. ;) </p>
            </div>

            <div class="inbox-widget">
              <stagger-transition tag="span">
                <router-link :to="'event/' + event.id" v-for="(event, index) in eventsPaginator.content" :key="event.id" :data-index="index" class="inbox-item">
                  <div class="inbox-item-img" v-if="event.poster != null">
                    <img :src="event.poster.path" class="img-circle" alt="">
                  </div>
                  <p class="inbox-item-author" v-text="event.name"></p>
                  <p class="inbox-item-text" v-if="event.author != null" v-text="event.author.displayName"></p>
                  <p class="inbox-item-date">{{ event.date | moment('L') }}</p>
                </router-link>
              </stagger-transition>

            </div>

            <div class="text-center">
              <paginator :paginator="eventsPaginator" @navigate="eventsPaginatorNavigate" />
            </div>
          </div>
        </div>
      </hero-unit>
    </section>

    <section class="section" id="pricing">
      <pricing />
    </section>

    <section class="section bg-gray" id="faq">
      <faq />
    </section>

  </div>
</template>

<script>
import Pricing from '../static/Pricing.vue';
import { Paginator, ProductLogo, HeroUnit } from 'elements';
import StaggerTransition from '../../functional/StaggerTransition.vue';
import faq from '../static/FAQ';
import HowItWorks from '../static/HowItWorks';

import Notepad from 'assets/img/notepad.jpg';
import Crowd from 'assets/img/crowd.jpg';

export default {
  name: 'landing',
  components: {
    Pricing,
    Paginator,
    StaggerTransition,
    faq,
    HowItWorks,
    ProductLogo,
    HeroUnit
  },
  data () {
    return {
      eventsPaginator: {}
    };
  },
  created () {
    this.getEvents(0);
  },
  computed: {
    notepad () {
      return Notepad;
    },
    crowd () {
      return Crowd;
    }
  },
  methods: {
    getEvents (page) {
      var url = ['public/events', page, 5].join('/');
      this.$http.get(url).then(response => {
        this.eventsPaginator = response.body;
      });
    },
    eventsPaginatorNavigate (e) {
      if (e.direction != null) {
        this.getEvents(this.eventsPaginator.number + e.direction);
      } else if (e.page != null) {
        this.getEvents(e.page);
      }
    }
  }
};
</script>

<style lang="less">
@import (reference) '~less/variables.less';
@hero-img: url('~assets/img/notepad.jpg');
@events-img: url('~assets/img/crowd.jpg');

.section {
  padding-top: 120px;
  padding-bottom: 120px;
}

.bg-gray {
  background-color: #fafafa;
  border-bottom: 1px solid @color-light;
  border-top: 1px solid @color-light;
}

.bg-dark {
  background: -webkit-linear-gradient(#444F5C, #334159);
  background: -moz-linear-gradient(#444F5C, #334159);
  background: -ms-linear-gradient(#444F5C, #334159);
  background: -o-linear-gradient(#444F5C, #334159);
  background: linear-gradient(#444F5C, #334159);
}

.bg-overlay {
  background: -webkit-linear-gradient(#444F5C, #334159);
  background: -moz-linear-gradient(#444F5C, #334159);
  background: -ms-linear-gradient(#444F5C, #334159);
  background: -o-linear-gradient(#444F5C, #334159);
  background: linear-gradient(#444F5C, #334159);
  opacity: 0.85;
  position: absolute;
  height: 100%;
  width: 100%;
  top: 0;
}

.page-sub-title {
  margin: 10px 0px !important;
}

.padding-t-0 {
  padding-top: 0px;
}

.bg-img {
  background: @hero-img;
  background-repeat: no-repeat;
  background-size: cover;
}

.sub-title {
  color: #97a0af;
  margin-bottom: 50px;
}

.home {
  position: relative;
  margin-top: -20px;
}

.home-wrapper {
  color: #ffffff;
  padding: 70px 0px 70px 0px;
  p {
    line-height: 24px;
    margin-top: 30px;
    color: #eee;
    margin-bottom: 50px;
    font-size: 16px;
  }
}

.service-item {
  margin-top: 30px;
  margin-bottom: 30px;
  text-align: center;
  i {
    font-size: 36px;
    margin-bottom: 20px;
    display: block;
    line-height: 36px;
    color: @color-accent;
  }
  .service-detail {
    p {
      color: #9aa7af;
      line-height: 24px;
      font-weight: 300;
    }
  }
}

.feature-detail {
  ul {
    li {
      padding-left: 25px;
      padding-bottom: 20px;
      position: relative;
      color: #9aa7af;
      i {
        position: absolute;
        left: 0px;
        top: 3px;
      }
    }
  }
  p.sub {
    margin-bottom: 30px;
    font-size: 15px;
  }
}

.fun-facts {
  position: relative;
  padding-top: 80px;
  padding-bottom: 80px;
  color: #ffffff;
  .facts {
    i {
      color: @color-accent;
      font-size: 36px;
    }
  }
}

.question {
  color: #444;
  font-weight: 400;
  font-size: 16px;
}

.answer {
  color: #97a0af;
  margin-bottom: 30px;
}

.events {
  .content {
    background: #fff;
    padding: 5%;
  }

  .title {
    font-weight: 300;
    line-height: 50px;
    margin-top: 0px;
  }
}

@media only screen and (max-width: 992px) {
  .events {
    .bg-overlay {
      display: none;
    }

    .container {
      background: #fff;
    }

    .content {
      padding-left: 10px;
      padding-right: 10px;
    }
  }
}
</style>
