<template>
  <div class="form-group">
    <label v-text="label"></label>
    <select class="form-control"
            @change="updateValue">
      <option v-for="(opt, index) in options"
              :value="index"
              :selected="value == opt"
              v-text="opt[vval]">
      </option>
    </select>
  </div>
</template>

<script>
export default {
  name: 'patrik-select',
  props: ['options', 'label', 'value', 'vkey', 'vval'],
  methods: {
    updateValue: function (event) {
      var index = event.target.value;
      var value = this.options[index];

      if (value != null) {
        this.$emit('input', [ value ]);
      }
    }
  }
}
</script>