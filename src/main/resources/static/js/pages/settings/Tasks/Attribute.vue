<template>
  <div class="row">
    <div class="col-md-6">
      <table class="table table-bordered">
        <tbody>
          <tr v-for="(opt, index) in attribute.options">
            <th v-text="index"></th>
            <td v-text="opt.value"></td>
            <td>
              <a @click="deleteOption(opt)">remove</a>
            </td>
          </tr>
          <tr>
            <th><i class="fa fa-plus"
                 aria-hidden="true"></i></th>
            <td colspan="2">
              <input type="text"
                     v-model="new_option"
                     @keyup.enter="submitOption"
                     class="form-control" />
              <a @click="submitOption"
                 class="btn btn-xs btn-primary pull-right">Add</a>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="col-md-6 text-center">
      <a class="btn btn-danger"
         @click="deleteAttribute">Delete attribute</a>
    </div>
  </div>
</template>

<script>
export default {
  name: 'attribute',
  props: ['attribute'],
  data: function () {
    return {
      new_option: null,
    }
  },
  methods: {
    submitOption: function () {
      var url = ['api/attribute', this.attribute.id, 'options'].join('/');

      this.$http.post(url, JSON.stringify({
        value: this.new_option,
      })).then(response => {
        if (this.attribute.options == null)
          this.attribute.options = [];
        this.attribute.options.push(response.body);
        this.new_option = null;
      })
    },
    deleteOption: function (option) {
      var url = ['api/attribute', this.attribute.id, 'options', option.id].join('/');

      this.$http.delete(url).then(response => {
        this.attribute.options = this.attribute.options.filter(x => {
          return x.id != option.id;
        })
      }, response => {
        this.$emit('error', {
          title: response.body.error,
          message: response.body.message
        })
      })
    },
    deleteAttribute: function () {
      var url = ['api/attribute', this.attribute.id].join('/');

      this.$http.delete(url).then(response => {
        this.$emit('destructed', this.attribute);
      }, response => {
        this.$emit('error', {
          title: response.body.error,
          message: response.body.message
        })
      })
    }
  }
}
</script>