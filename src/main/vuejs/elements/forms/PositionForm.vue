<template>
  <form class="form" @submit.prevent="submit">
    <h3 class="text-pink">We can't get your position. :(</h3>
    <div class="form-group">
      <small class="text-muted">
        We are having troubles with getting your current position. There is something wrong or you blocked access to your position.
        Please provide your position for better your experience at convene.
      </small>
    </div>
    
    <div class="form-group">
      <place-picker v-model="location" />
    </div>
  </form>
</template>

<script>
import { PlacePicker } from 'elements';
import { mapActions } from 'vuex';

export default {
  name: 'position-form',
  data () {
    return {

    };
  },
  components: {
    PlacePicker
  },
  computed: {
    location: {
      get () {
        return this.position;
      },
      set (value) {
        if (value.lat && value.lng) {
          this.setPosition(value);
        }
      }
    }
  },
  methods: {
    ...mapActions(['setPosition']),
    submit () {

    }
  }
};
</script>
