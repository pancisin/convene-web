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
        <div class="row">
          <div class="col-xs-8">
            <vue-input 
              v-model="userClone.address.street" 
              name="street" 
              label="Street"/>
          </div>
          <div class="col-xs-4">
            <vue-input 
              v-model="userClone.address.number" 
              name="number"
              rules="required|numeric"
              label="Number"/>
          </div>
        </div>

        <div class="row">
          <div class="col-xs-6">
            <vue-input 
              v-model="userClone.address.zip" 
              name="zip" 
              rules="required|numeric"
              label="ZIP"/>
          </div>
          <div class="col-xs-6">
            <vue-input 
              v-model="userClone.address.city" 
              name="city" 
              label="City"/>
          </div>
        </div>

        <vue-input 
          v-model="userClone.address.state" 
          name="state" 
          label="State"/>
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
import { VueInput } from 'elements';

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
    VueInput
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
      this.userClone = {
        ...us,
        address: {
          ...us.address
        }
      };
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
