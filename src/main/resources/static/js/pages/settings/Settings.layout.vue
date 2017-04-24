<template>
  <div class="row">
    <div class="col-md-9">
      <transition name="fade"
                  mode="out-in">
        <router-view></router-view>
      </transition>
    </div>
    <div class="col-md-3">
      <div class="card card-mini">
        <div class="card-header">
          {{ $t('settings.default') }}
        </div>
        <div class="card-body no-padding">
          <div class="list-group">
            <router-link to="/settings/account"
                         class="list-group-item">{{ $t('settings.account') }}</router-link>
            <router-link to="/settings/company"
                         class="list-group-item">{{ $t('settings.company') }}</router-link>
            <!--<router-link to="/settings/license"
                         class="list-group-item">{{ $t('settings.license') }}</router-link>-->
            <router-link to="/settings/task"
                         class="list-group-item">{{ $t('settings.task') }}</router-link>
            <router-link to="/settings/users"
                         class="list-group-item">{{ $t('settings.users') }}</router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'settings',
}
</script>

<style lang="less">
.list-group>.list-group-item {
  border: none;
}
</style>