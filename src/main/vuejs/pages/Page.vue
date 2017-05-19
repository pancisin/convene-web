<template>
  <div class="row">
    <div class="col-xs-12">
      <h3 v-text="page.name" class="page-title"></h3>
    </div>
    <div class="col-md-3">
      <div class="list-group">
        <router-link to="overview" class="list-group-item waves-effect">
          {{ $t('admin.page.overview') }}
        </router-link>
        <!--<router-link to="calendar" class="list-group-item">
                            Event calendar
                          </router-link>-->
        <router-link to="events" class="list-group-item waves-effect">
          {{ $t('admin.page.events') }}
        </router-link>
        <router-link to="places" class="list-group-item waves-effect">
          {{ $t('admin.page.places') }}
        </router-link>
      </div>
  
      <div class="list-group">
        <div class="list-group-item">
          <h4 class="header-title">
            <b>{{ $t('admin.page.advanced') }}</b>
            <span class="label label-info pull-right">Pro</span>
          </h4>
  
        </div>
        <router-link to="administrators" class="list-group-item waves-effect">
          {{ $t('admin.page.administrators') }}
        </router-link>
        <router-link to="services" class="list-group-item waves-effect">
          {{ $t('admin.page.services') }}
        </router-link>
        <router-link to="requests" class="list-group-item waves-effect">
          {{ $t('admin.page.requests') }}
        </router-link>
      </div>
  
      <div class="widget-simple-chart text-right card-box">
        <h3 class="text-primary counter" v-text="page.followersCount"></h3>
        <p class="text-muted text-nowrap">Followers</p>
      </div>
    </div>
    <div class="col-md-9">
      <transition name="fade-up" mode="out-in">
        <keep-alive>
          <router-view :page="page" :edit="edit"></router-view>
        </keep-alive>
      </transition>
    </div>
  </div>
</template>

<script>
export default {
  name: 'page',
  data() {
    return {
      page: new Object(),
      edit: false,
    }
  },
  watch: {
    '$route': 'getPage'
  },
  created() {
    this.getPage();
  },
  methods: {
    getPage() {
      var page_id = this.$route.params.id;

      if (this.page.id != null && this.page.id == page_id)
        return;
      this.page = new Object();
      if (page_id != null) {
        this.$http.get('api/page/' + page_id).then(response => {
          this.page = response.body;
          this.edit = true;
        })
      } else {
        this.page = new Object();
      }

    }
  }
}
</script>