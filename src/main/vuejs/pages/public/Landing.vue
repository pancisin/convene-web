<template>
  <div>
    <section class="home bg-img-1">
      <div class="bg-overlay"></div>
      <div class="container">
        <div class="row">
          <div class="col-sm-8 col-sm-offset-2">
            <div class="home-wrapper  text-center">
              <h1 class="text-uppercase text-primary">Event booking monster</h1>
              <p>Our goal is to provide clean, fast and simple way for managing events, conferences and even booking services of your business. Everything on one place and available for everybody.
              </p>
              <a class="btn btn-primary btn-rounded">Try it right now !</a>
              <div class="clearfix"></div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="section bg-gray" id="how-it-work">
      <how-it-works />
    </section>

    <section class="section events">
      <div class="bg-overlay"></div>
      <div class="container">
        <div class="row">
          <div class="col-md-6 col-md-offset-3 content bg-gray">

            <div class="text-center">
              <h2 class="title">Still don't know what to do today ? </h2>
              <p class="sub-title">Bookster is full of public events right from your neighborhood just select one and join. Do not forget to tell your friends about that. ;) </p>
            </div>

            <div class="inbox-widget">
              <stagger-transition tag="span">
                <router-link :to="'event/' + event.id" v-for="(event, index) in eventsPaginator.content" :key="event.id" :data-index="index" class="inbox-item">
                  <div class="inbox-item-img" v-if="event.bannerUrl != null">
                    <img :src="event.bannerUrl" class="img-circle" alt="">
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
      </div>
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
import { Paginator } from '../../elements';
import StaggerTransition from '../../functional/StaggerTransition.vue';
import faq from '../static/FAQ';
import HowItWorks from '../static/HowItWorks';

export default {
  name: 'landing',
  components: {
    Pricing, Paginator, StaggerTransition, faq, HowItWorks
  },
  data () {
    return {
      eventsPaginator: {}
    };
  },
  created () {
    this.getEvents(0);
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
.section {
  padding-top: 120px;
  padding-bottom: 120px;
}

.bg-gray {
  background-color: #fafafa;
  border-bottom: 1px solid #f5f5f5;
  border-top: 1px solid #f5f5f5;
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

.bg-img-1 {
  background: url(http://www.dolcemexico.com/restaurant/wp-content/uploads/2015/01/contacto1.jpg);
  background-repeat: no-repeat;
  background-size: cover;
}

.title {
  font-weight: 300;
  line-height: 50px;
  margin-top: 0px;
}

.sub-title {
  color: #97a0af;
  margin-bottom: 50px;
}

.m-b-0 {
  margin-bottom: 0px !important;
}

.parallax {
  background-position: 0 0;
  background-repeat: no-repeat;
  background-size: 100% auto;
  width: 100%;
  background-size: cover;
  background-attachment: fixed;
}

.home {
  position: relative;
  margin-top: -15px;
}

.home-wrapper {
  color: #ffffff;
  padding: 150px 0px 150px 0px;
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
    color: #3bafda;
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
      color: #3bafda;
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
  background: url(http://www.citi.io/wp-content/uploads/2015/08/1168-00-06.jpg);
  background-repeat: no-repeat;
  background-size: 100%;
  position: relative;

  .content {
    background: #fff;
    padding: 5%;
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
