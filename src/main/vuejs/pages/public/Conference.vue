<template>
  <div v-loading="loading">
    <div v-if="conference != null">
      <hero-unit :background="conference.poster != null ? conference.poster.path : null" class="m-b-20">
        <h1 class="text-uppercase text-primary">{{ conference.name }}</h1>
      </hero-unit>

      <div class="container">
        <div class="row">
          <div class="col-sm-8 col-md-5 col-md-offset-2 custom-content">
            <panel>
              <div v-html="conference.summary" class="m-b-20"></div>
            </panel>

            <panel>
              <span slot="title">News</span>
              <articles-list 
                :articles="articlesPaginator.content">
              </articles-list>
              
              <div class="text-center">
                <paginator 
                  :paginator="articlesPaginator" 
                  :fetch="getArticles" />
              </div>
            </panel>
          </div>

          <div class="col-sm-4 col-md-3">
            <light-box v-if="conference.poster != null" :image="conference.poster.path">
              <vue-image :src="conference.poster.path" class="img-poster" placeholder />
            </light-box>        
            
            <div v-if="authenticated">
              <panel v-if="!attend_status" type="primary">
                <span slot="title">Join conference</span>
                <custom-form v-if="conference.registrationForm" :form="conference.registrationForm" :submitFunc="registrationSubmit" />
                <div v-else class="text-center">
                  <button type="button" class="btn btn-primary">Sign up</button>
                </div>
              </panel>
              <div v-else>
                <survey-list />
                <events-list :events="eventsPaginator.content"></events-list>
                <div class="text-center">
                  <paginator 
                    :fetch="getEvents" 
                    :paginator="eventsPaginator" />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <error v-if="error" :status="error.status" />
  </div>
</template>

<script>
import { CustomForm } from 'elements/forms';
import {
  ArticlesList,
  HeroUnit,
  LightBox,
  Paginator,
  VueImage,
  Error
} from 'elements';
import EventsList from './conference/Events.list.vue';
import SurveyList from './conference/Survey.list.vue';

import { mapGetters } from 'vuex';

import ConferenceApi from 'api/conference.api';
import PublicApi from 'api/public.api';
import InjectorGenerator from '../../services/InjectorGenerator';

export default {
  name: 'conference',
  provide () {
    const provider = {};

    Object.defineProperty(provider, 'api', {
      get: () => this.injector
    });

    return { provider };
  },
  components: {
    CustomForm,
    ArticlesList,
    EventsList,
    HeroUnit,
    LightBox,
    Paginator,
    VueImage,
    Error,
    SurveyList
  },
  data () {
    return {
      conference: null,
      attend_status: false,
      articlesPaginator: {},
      injector: null,
      eventsPaginator: {},
      error: null,
      loading: false
    };
  },
  computed: {
    ...mapGetters(['authenticated'])
  },
  created () {
    const api = this.authenticated ? ConferenceApi : PublicApi.conference;

    this.loading = true;
    api.getConference(this.$route.params.id, conference => {
      this.conference = conference;
      this.injector = InjectorGenerator.generate(api, conference.id);

      if (this.authenticated) {
        this.injector.getAttendStatus(member => {
          this.attend_status = member.active;
        });
      }

      this.loading = false;
    }, error => {
      this.error = error;
      this.loading = false;
    });
  },
  methods: {
    getEvents (page) {
      this.injector.getEvents(page, 3, paginator => {
        this.eventsPaginator = paginator;
      });
    },
    getArticles (page) {
      this.injector.getArticles(page, 5, paginator => {
        this.articlesPaginator = paginator;
      });
    },
    registrationSubmit (values) {
      this.injector.postAttend(values, result => {
        console.log(result);
        this.attend_status = result.active;
      });
    }
  }
};
</script>

<style lang="less">
.img-poster {
  width: 100%;
  border: 1px solid #ccc;
  margin-bottom: 20px;
}
</style>