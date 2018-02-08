<template>
  <div 
    class="checkbox"
    v-if="option != null">
    
    <input 
      type="checkbox"
      :id="option.id + '-input'"
      @change="updateInput($event.target.checked)"
      :checked="isChecked">
    
    <label 
      :for="option.id + '-input'"
      v-text="option.value"></label>
  </div>
</template>

<script>
export default {
  name: 'v-select',
  props: ['value', 'option'],
  methods: {
    updateInput: function (value) {
      if (this.value instanceof Array) {
        if (value) {
          this.value.push(this.option);
        } else {
          this.value = this.value.filter(e => {
            return e.id !== this.option.id;
          });
        }
      } else {
        this.value = value ? this.option : null;
      }

      this.$emit('input', this.value);
    }
  },
  isChecked: function () {
    if (this.value instanceof Array) {
      return this.value.includes(x => x.id === this.option.id);
    } else {
      return this.value.id === this.option.id;
    }
  }
};
</script>