<template>
  <div 
    class="form-group" 
    :class="{ 'has-error ' : errors.has(name) }">

    <label class="control-label">
      {{ label }}
    </label>
    
    <input 
      :name="name"
      class="form-control" 
      type="text" 
      @input="updateInput"
      :disabled="disabled"
      v-validate="rules"
      :value="value"
      :placeholder="label">

    <span 
      class="text-danger" 
      v-if="errors.has(name)">
      {{ errors.first(name) }}
    </span>

  </div>
</template>

<script>
export default {
  render (h) {
    return h('div', {
      class: `form-group ${this.errors.has(this.name) ? 'has-error' : ''}`
    }, [
      h('label', { class: 'control-label' }, this.label),
      h('input', {
        class: 'form-control',
        props: {
          name: this.name,
          value: this.value,
          disabled: this.disabled
        },
        attrs: {
          type: 'text'
        },
        on: {
          change: this.updateInput
        }
      }),
      h('span', {}, this.errors.first(this.name))
    ]);
  },
  inject: ['$validator'],
  props: {
    value: {
      type: String | null,
      required: true,
      default () {
        return '';
      }
    },
    disabled: Boolean,
    name: {
      type: String | null,
      required: false,
      default () {
        return this._uid;
      }
    },
    rules: {
      type: String,
      default () {
        return 'required';
      }
    },
    label: {
      type: String,
      required: false,
      default () {
        return '';
      }
    }
  },
  methods: {
    updateInput (event) {
      this.$emit('input', event.target.value);
    }
  }
};
</script>
 