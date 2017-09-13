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
        <tr v-for="programme in programme" :key="programme.id">
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
            <time-picker v-model="new_programme.time"></time-picker>
          </td>
          <td>
            <input type="text" v-model="new_programme.description" class="form-control" @keyup.enter="submitProgramme">
          </td>
          <td class="text-center">
            <a @click="submitProgramme" class="btn btn-rounded btn-xs btn-success">
              <i class="fa fa-save"></i>
            </a>
          </td>
        </tr>
      </tbody>
    </table>
  </panel>
</template>

<script>
import { TimePicker } from '../../elements';
import ProgrammeApi from 'api/programme.api';
export default {
  inject: ['provider'],
  data () {
    return {
      programme: [],
      new_programme: {}
    };
  },
  computed: {
    api () {
      return this.provider.api;
    }
  },
  components: {
    TimePicker
  },
  created () {
    this.api.getProgramme(programme => {
      this.programme = programme;
    });
  },
  methods: {
    submitProgramme: function () {
      if (this.new_programme.description == null || this.new_programme.description === '') return;

      this.api.postProgramme(this.new_programme, programme => {
        this.programme.push(programme);

        this.new_programme = {};
        this.programme.sort((a, b) => {
          return a.time > b.time;
        });
      });
    },
    deleteProgramme (programme) {
      ProgrammeApi.deleteProgramme(programme.id, result => {
        this.programme = this.programme.filter(p => {
          return p.id !== programme.id;
        });
      });
    }
  }
};
</script>