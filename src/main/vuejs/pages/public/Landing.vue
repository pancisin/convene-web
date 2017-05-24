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
  
    <section class="section bg-gray">
      <div class="container">
        <div class="row">
          <div class="col-md-4 col-md-offset-4">
  
            <div class="text-center">
              <h2 class="title">Still don't know what to do today ? </h2>
              <p class="sub-title">Bookster is full of public events right from your neighborhood just select one and join. Do not forget to tell your friends about that. ;) </p>
            </div>
  
            <div class="inbox-widget">
              <router-link :to="'event/' + event.id" v-for="event in eventsPaginator.content" :key="event.id">
                <div class="inbox-item">
                  <div class="inbox-item-img" v-if="event.bannerUrl != null">
                    <img :src="event.bannerUrl" class="img-circle" alt="">
                  </div>
                  <p class="inbox-item-author" v-text="event.name"></p>
                  <p class="inbox-item-text" v-if="event.summary != null" v-strip="event.summary.substr(0, 200)"></p>
                  <p class="inbox-item-date">{{ event.date | moment('DD.MM.YYYY') }}</p>
                </div>
              </router-link>
  
              <div class="text-center">
                <paginator :paginator="eventsPaginator" @navigate="eventsPaginatorNavigate" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  
    <section class="section" id="how-it-work">
      <div class="container">
        <div class="row">
          <div class="col-md-8 col-md-offset-2">
            <div class="text-center">
              <h2 class="title">How It Works ?</h2>
              <p class="sub-title">Constituto voluptatibus mei ex. Eum soleat lorem Ipsum is simply dummy
                <br> text of the printing and typesetting industry. </p>
            </div>
  
            <div class="row">
              <div class="col-sm-4">
                <div class="service-item">
                  <i class="fa fa-pencil-square-o" aria-hidden="true"></i>
                  <div class="service-detail">
                    <h4>Simple planning</h4>
                    <p>We put a lot of effort in design, as it’s the most important ingredient of successful website.Sed ut perspiciatis unde omnis iste natus error sit.</p>
                  </div>
                </div>
              </div>
  
              <div class="col-sm-4">
                <div class="service-item">
                  <i class="fa fa-share-alt" aria-hidden="true"></i>
                  <div class="service-detail">
                    <h4>Share your events</h4>
                    <p>We are putting social medias and sharing to the first place to address crowds.</p>
                  </div>
                </div>
              </div>
  
              <div class="col-sm-4">
                <div class="service-item">
                  <i class="fa fa-envelope-open-o" aria-hidden="true"></i>
                  <div class="service-detail">
                    <h4>Manage attendants</h4>
                    <p>With bookster it is easy to see who is going to take place on your event. And even you can notify attendants about programme changes and updates.</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
  
      </div>
    </section>
    <section class="section bg-gray" id="features">
      <div class="container">
  
        <div class="row">
          <div class="col-sm-6 col-md-4 col-md-offset-2">
            <div class="feature-detail">
              <h2 class="title">Clean and Ultra Modern Design</h2>
              <p class="sub">Constituto voluptatibus mei ex. Eum soleat lorem Ipsum is simply dummy
                <br> text of the printing and typesetting industry. </p>
  
              <ul class="list-unstyled">
                <li>
                  <i class="fa fa-angle-right"></i>
                  Aliquam a augue suscipit, bibendum luctus neque. Vestibulum laoreet rhoncus ipsum, bibendum ullamcorper hendrerit tellus.
                </li>
                <li>
                  <i class="fa fa-angle-right"></i>
                  We put a lot of effort in design, as it’s the most important ingredient of successful website.
                </li>
  
              </ul>
  
              <a href="" class="btn btn-primary btn-rounded w-lg">Buy Now</a>
            </div>
          </div>
  
          <div class="col-sm-6 col-md-4">
            <img src="https://us.123rf.com/450wm/WaD/WaD1508/WaD150800036/44047236-flat-vector-document-icon-flat-design-long-shadow-isolated.jpg?ver=6" class="img-responsive">
          </div>
  
        </div>
      </div>
    </section>
  
    <section class="section" id="pricing">
      <pricing />
    </section>
  
    <section class="section bg-gray" id="faq">
      <div class="container">
  
        <div class="row">
          <div class="col-sm-12 text-center">
            <h1 class="title zoomIn animated wow animated" data-wow-delay=".1s" style="visibility: visible; animation-delay: 0.1s; animation-name: zoomIn;">FAQ</h1>
            <p class="sub-title zoomIn animated wow animated" data-wow-delay=".2s" style="visibility: visible; animation-delay: 0.2s; animation-name: zoomIn;">Constituto voluptatibus mei ex. Eum soleat lorem Ipsum is simply dummy
              <br> text of the printing and typesetting industry. </p>
          </div>
        </div>
        <!-- end row -->
  
        <div class="row">
          <div class="col-md-5 col-md-offset-1">
            <!-- Question/Answer -->
            <div class="animated fadeInLeft wow animated" data-wow-delay=".1s" style="visibility: visible; animation-delay: 0.1s; animation-name: fadeInLeft;">
              <h4 class="question" data-wow-delay=".1s">What is Lorem Ipsum?</h4>
              <p class="answer">Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s.</p>
            </div>
  
            <!-- Question/Answer -->
            <div class="animated fadeInLeft wow animated" data-wow-delay=".3s" style="visibility: visible; animation-delay: 0.3s; animation-name: fadeInLeft;">
              <h4 class="question">Why use Lorem Ipsum?</h4>
              <p class="answer m-b-0">Lorem ipsum dolor sit amet, in mea nonumes dissentias dissentiunt, pro te solet oratio iriure. Cu sit consetetur moderatius intellegam, ius decore accusamus te. Ne primis suavitate disputando nam. Mutat convenirete.</p>
            </div>
  
          </div>
          <!--/col-md-5 -->
  
          <div class="col-md-5">
            <!-- Question/Answer -->
            <div class="animated fadeInRight wow animated" data-wow-delay=".2s" style="visibility: visible; animation-delay: 0.2s; animation-name: fadeInRight;">
              <h4 class="question">Is safe use Lorem Ipsum?</h4>
              <p class="answer">Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s.</p>
            </div>
  
            <!-- Question/Answer -->
            <div class="animated fadeInRight wow animated" data-wow-delay=".4s" style="visibility: visible; animation-delay: 0.4s; animation-name: fadeInRight;">
              <h4 class="question">When can be used?</h4>
              <p class="answer m-b-0">Lorem ipsum dolor sit amet, in mea nonumes dissentias dissentiunt, pro te solet oratio iriure. Cu sit consetetur moderatius intellegam, ius decore accusamus te. Ne primis suavitate disputando nam. Mutat convenirete.</p>
            </div>
  
          </div>
          <!--/col-md-5-->
        </div>
  
      </div>
    </section>
  </div>
</template>

<script>
import Pricing from '../static/Pricing.vue'
import Paginator from '../../elements/Paginator.vue'
export default {
  name: 'landing',
  components: {
    Pricing, Paginator
  },
  data() {
    return {
      eventsPaginator: {},
    }
  },
  created() {
    this.getEvents(0);
  },
  methods: {
    getEvents(page) {
      var url = ['public/events', page, 5].join('/');
      this.$http.get(url).then(response => {
        this.eventsPaginator = response.body;
      })
    },
    eventsPaginatorNavigate(e) {
      if (e.direction != null) {
        this.getEvents(this.eventsPaginator.number + e.direction);
      } else if (e.page != null) {
        this.getEvents(e.page);
      }
    }
  }
}
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
  background-size: 100%;
}

.hr {
  display: inline-block;
  border: 0;
  width: 35px;
  margin: 12px 0px 25px;
  padding: 0;
  height: 0;
  border-top: 2px solid #3bafda;
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
</style>
