<script>
import ServiceApi from 'api/service.api';
import { VueTable } from 'elements';
import { DateTime } from 'luxon';
import ServiceRequestApi from 'api/service-request.api';

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
  render (h) {
    return h('vue-table', {
      props: {
        data: this.requests,
        func: this.tableRender,
        contextmenu: this.contextmenu
      }
    });
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
        state: {
          el: 'i',
          class: request.approved ? 'fa fa-check text-success' : 'fa fa-question'
        }
      };
    },
    contextmenu (item) {
      return [
        item('Accept', request => {
          ServiceRequestApi.acceptRequest(request.id, req => {
            const index = this.requests.findIndex(r => r.id === req.id);
            this.requests.splice(index, 1, req);
          });
        }),
        item('Refuse', request => {
          ServiceRequestApi.refuseRequest(request.id, req => {
            const index = this.requests.findIndex(r => r.id === req.id);
            this.requests.splice(index, 1, req);
          });
        })
      ];
    }
  }
};
</script>

<style>

</style>
