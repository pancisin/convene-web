<script>
import { DatePicker } from 'elements';
import { CustomForm } from 'elements/forms';
import ServiceApi from 'api/service.api';

export default {
  name: 'service-book',
  props: {
    service: Object
  },
  render (h) {
    return h('custom-form', {
      props: {
        form: this.service.form,
        submitFunc: this.submit
      }
    });
  },
  components: {
    DatePicker,
    CustomForm
  },
  data () {
    return {
      serviceRequest: {
        units: 0
      }
    };
  },
  methods: {
    submit (values) {
      ServiceApi.postServiceRequest(this.service.id, values, request => {
        this.$emit('submit', request);
      });
    }
  }
};
</script>
