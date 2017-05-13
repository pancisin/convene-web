<template>
  <div class="card-box">
    <h4 class="text-dark  header-title m-t-0">Programme</h4>
  
    <table class="table table-striped table-bordered">
      <thead>
        <tr>
          <th>time</th>
          <th>Description</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="programme in event.programme">
          <td v-text="programme.time"></td>
          <td v-text="programme.description"></td>
        </tr>
        <tr>
          <td>
            <input type="text" v-model="new_programme.time" class="form-control" />
          </td>
          <td>
            <input type="text" v-model="new_programme.description" class="form-control" />
            <a @click="submitProgramme" class="btn bnt-link">
              <i class="fa fa-save" />
            </a>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  props: ['event'],
  data() {
    return {
       new_programme: {
        time: null,
        description: null
      }
    }
  },
  methods: {
    submitProgramme: function () {
    var url = ['api/event', this.event.id, 'programme'].join('/');

    this.$http.post(url, this.new_programme).then(response => {
      this.event.programme.push(response.body);
      this.new_programme = {
        time: null,
        description: null
      }
    })
  }
  }
}
</script>

<style>

</style>
