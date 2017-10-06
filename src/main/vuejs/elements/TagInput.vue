<template>
  <div @click="focusNewTag()" v-bind:class="{'read-only': readOnly}" class="vue-input-tag-wrapper">
    <span v-for="(tag, index) in tags" v-bind:key="index" class="input-tag">
      <span>{{ tag }}</span>
      <a v-if="!readOnly" @click.prevent.stop="remove(index)" class="remove"><i class="fa fa-times"></i></a>
    </span>
    <input v-if="!readOnly" v-bind:placeholder="placeholder" type="text" v-model="newTag" v-on:keydown.delete.stop="removeLastTag()" v-on:keydown.enter.188.tab.prevent.stop="addNew(newTag)" class="new-tag" />
  </div>
</template>

<script>
export default {
  name: 'InputTag',

  props: {
    tags: {
      type: Array,
      default: () => []
    },
    placeholder: {
      type: String,
      default: ''
    },
    onChange: {
      type: Function
    },
    readOnly: {
      type: Boolean,
      default: false
    }
  },

  data () {
    return {
      newTag: ''
    };
  },

  methods: {
    focusNewTag () {
      if (this.readOnly) { return; }
      this.$el.querySelector('.new-tag').focus();
    },

    addNew (tag) {
      if (tag && this.tags.indexOf(tag) === -1) {
        this.tags.push(tag);
        this.tagChange();
      }
      this.newTag = '';
    },

    remove (index) {
      this.tags.splice(index, 1);
      this.tagChange();
    },

    removeLastTag () {
      if (this.newTag) { return; }
      this.tags.pop();
      this.tagChange();
    },

    tagChange () {
      if (this.onChange) {
        // avoid passing the observer
        this.onChange(JSON.parse(JSON.stringify(this.tags)));
      }
    }
  }
};
</script>

<style lang="less">
.vue-input-tag-wrapper {
  background-color: #fafafa;
  border: 1px solid #eeeeee;
  border-radius: 2px;
  overflow: hidden;
  padding-left: 4px;
  padding-top: 4px;
  cursor: text;
  text-align: left;
  -webkit-appearance: textfield;

  .input-tag {
    background-color: #cde69c;
    border-radius: 2px;
    border: 1px solid #a5d24a;
    color: #638421;
    display: inline-block;
    font-size: 13px;
    font-weight: 400;
    margin-bottom: 4px;
    margin-right: 4px;
    padding: 3px 10px;

    .remove {
      cursor: pointer;
      font-weight: bold;
      color: #638421;

      &:hover {
        text-decoration: none;
      }
    }
  }

  .new-tag {
    background: transparent;
    border: 0;
    color: #777;
    font-size: 13px;
    font-weight: 400;
    margin-bottom: 6px;
    margin-top: 1px;
    outline: none;
    padding: 4px;
    padding-left: 0;
    width: 80px;
  }

  &.read-only {
    cursor: default;
  }
}
</style>