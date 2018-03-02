<template>
  <form @submit.prevent="submit">
    <div class="row">
      <div class="col-md-6">
        <vue-input 
          v-model="userClone.email" 
          name="email" 
          label="Email" 
          rules="required|email" 
          disabled/>
        <vue-input 
          v-model="userClone.firstName" 
          name="first_name" 
          label="First name"/>
        <vue-input 
          v-model="userClone.lastName" 
          name="last_name" 
          label="Last name"/>
      </div>

      <div class="col-md-6">
        <address-editor v-model="userClone.address" />
      </div>
    </div>
    <hr>
    <div class="text-right">
      <button 
        class="btn btn-primary" 
        type="submit">
        Submit
      </button>
    </div>
  </form>
</template>

<script>
import { mapGetters } from 'vuex';
import {
  VueInput,
  AddressEditor
} from 'elements';

export default {
  name: 'subscribe-form',
  props: {
    license: {
      type: String,
      default () {
        return '';
      }
    }
  },
  components: {
    VueInput,
    AddressEditor
  },
  data () {
    return {
      userClone: {
        address: {}
      }
    };
  },
  computed: {
    ...mapGetters(['user'])
  },
  watch: {
    user (newVal) {
      this.cloneData(newVal);
    }
  },
  created () {
    this.cloneData(this.user);
  },
  methods: {
    cloneData (us) {
      this.userClone = { ...us };
    },
    submit () {
      this.$validator.validateAll().then(valid => {
        if (valid) {
          this.$http.post('/api/user/subscription', {
            user: this.userClone,
            subscription: this.license
          }).then(response => {
            this.$emit('submit', response.body);
            this.$success('success');
          }, ({ body }) => {
            this.$error(body.message);
          });
        }
      });
    }
  }
};
</script>
