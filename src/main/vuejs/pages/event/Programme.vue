<template>
  <panel type="table">
    <span slot="title">Programme</span>
  
    <table class="table table-striped">
      <thead>
        <tr>
          <th>time</th>
          <th>Description</th>
          <th class="text-center">Action</th>
        </tr>
      </thead>
      <tbody is="transition-group" name="fade">
        <tr v-for="programme in event.programme" :key="programme.id">
          <td v-text="programme.time"></td>
          <td v-text="programme.description"></td>
          <td class="text-center">
            <a @click="deleteProgramme(programme)" class="btn btn-rounded btn-xs btn-danger">
              <i class="fa fa-trash"></i>
            </a>
          </td>
        </tr>
        <tr key="new_programme">
          <td>
            <time-picker v-model="new_programme.time" />
          </td>
          <td>
            <input type="text" v-model="new_programme.description" class="form-control" @keyup.enter="submitProgramme"/>
          </td>
          <td class="text-center">
            <a @click="submitProgramme" class="btn btn-rounded btn-xs btn-success">
              <i class="fa fa-save" />
            </a>
          </td>
        </tr>
      </tbody>
    </table>
  </panel>
</template>

<script>
import TimePicker from '../../elements/TimePicker.vue'
export default {
  props: ['event'],
  data() {
    return {
      new_programme: new Object(),
    }
  },
  components: {
    TimePicker
  },
  methods: {
    submitProgramme: function () {
      var url = ['api/event', this.event.id, 'programme'].join('/');

      if (this.new_programme.description == null || this.new_programme.description == "") return;

      this.$http.post(url, this.new_programme).then(response => {
        if (this.event.programme == null)
          this.event.programme = [];
          
        this.event.programme.push(response.body);
        this.new_programme = {
          time: null,
          description: null
        };
        this.event.programme.sort((a, b) => {
          return a.time > b.time;
        })
      })
    },
    deleteProgramme(programme) {
      this.$http.delete('api/programme/' + programme.id).then(response => {
        this.event.programme = this.event.programme.filter(p => {
          return p.id != programme.id
        });
      })
    }
  }
}
</script>