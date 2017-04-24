<template>
  <div class="row">
    <div class="col-md-6">
      <table class="table table-bordered">
        <tbody>
          <tr v-for="(opt, index) in attribute.options">
            <th v-text="index"></th>
            <td v-text="opt.value"></td>
          </tr>
          <tr>
            <th><i class="fa fa-plus"
                 aria-hidden="true"></i></th>
            <td>
              <input type="text" v-model="new_option" />
              <a @click="submitOption">Add</a>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
export default {
  name: 'attribute',
  props: ['attribute'],
  data: function() {
    return {
      new_option: null,
    }
  },
  methods: {
    submitOption: function() {
      // TODO : Prepare and implement backend for submitting attribute options. Attribute controller required.

      this.attribute.options.push({
        value: this.new_option,
      });
      this.new_option = null;
    }
  }
}
</script>