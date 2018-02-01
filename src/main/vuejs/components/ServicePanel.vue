<template>
  <div v-loading="loading">
    <vue-table :data="requests" :func="tableRender" :contextmenu="contextmenu" />
  </div>
</template>

<script>
import ServiceApi from 'api/service.api';
import { VueTable } from 'elements';
import { DateTime } from 'luxon';

export default {
  name: 'service-panel',
  props: {
    service: Object
  },
  components: {
    VueTable
  },
  data () {
    return {
      requests: [],
      loading: false
    };
  },
  created () {
    this.getSubmission();
  },
  methods: {
    getSubmission () {
      this.loading = true;
      ServiceApi.getServiceRequests(this.service.id, requests => {
        this.requests = requests;
        this.loading = false;
      });
    },
    tableRender (request) {
      const fields = this.service.form.formFields.reduce((acc, field) => {
        var value = request.submission.values.find(v => v.field === field.id);

        if (value != null) {
          if (field.type === 'DATE') {
            const tm = parseInt(value.value, 10);
            acc[field.name] = DateTime.fromMillis(tm).toFormat('F');
          } else {
            acc[field.name] = value.value;
          }
        } else {
          acc[field.name] = '';
        }

        return acc;
      }, {});

      return {
        user: request.submission.user,
        date: DateTime.fromMillis(request.submission.created).toFormat('F'),
        ...fields,
        approved: request.approved
      };
    },
    contextmenu (item) {
      return [
        item('Respond')
      ];
    }
  }
};
</script>

<style>

</style>
