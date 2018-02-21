<template>
  <div 
    class="vue-input-tag-wrapper"
    :class="{'read-only': readonly}" 
    @click="focusNewTag">

    <span 
      v-for="(tag, index) in tags" 
      :key="index" 
      class="input-tag">
      
      <span>{{ tag }}</span>
      <a 
        v-if="!readonly" 
        v-stream:click="{ subject: deleteTag$, data: tag }"
        class="remove">
        <i class="fa fa-times"></i>
      </a>
    </span>

    <input 
      v-if="!readonly" 
      :placeholder="placeholder" 
      type="text" 
      v-stream:keydown="submitTag$"
      class="new-tag"
      ref="newtag" />
  </div>
</template>

<script>
import { Subject, Observable } from 'rxjs';
export default {
  name: 'input-tag',
  props: {
    value: {
      type: Array,
      default () {
        return [];
      }
    },
    placeholder: {
      type: String,
      default () {
        return '';
      }
    },
    readonly: {
      type: Boolean
    }
  },

  data () {
    return {
      newTag: ''
    };
  },
  subscriptions () {
    this.submitTag$ = new Subject();
    this.deleteTag$ = new Subject();

    const deletion = Observable.merge(
      this.deleteTag$,
      this.submitTag$.filter(e => e.event.keyCode === 8 && !e.event.target.value)
    )
    .map(d => {
      return {
        value: d.data,
        delete: true
      };
    });

    const fromProps$ = Observable.merge(
      this.$eventToObservable('hook:created').mapTo(this.value),
      // this.$watchAsObservable('value').pluck('newValue')
    )
    .flatMap(x => x)
    .map(x => {
      return {
        value: x
      };
    });

    return {
      tags: this.submitTag$
        .pluck('event')
        .filter(e => [13, 32, 9].includes(e.keyCode))
        .do(e => e.preventDefault())
        .pluck('target')
        .map(t => {
          t.focus();
          const res = {
            value: t.value
          };
          t.value = '';
          return res;
        })
        .distinct(x => x.value)
        .merge(deletion, fromProps$)
        .scan((acc, cur) => {
          if (cur.delete) {
            if (cur.value) {
              acc = acc.filter(c => c !== cur.value);
            } else {
              acc.pop();
            }
          } else {
            acc.push(cur.value.trim());
          }
          return acc;
        }, [])
        .do(x => {
          // if (this.value.every(v => x.every(i => i === v))) {
          this.$emit('input', [ ...x ]);
          // }
        })
    };
  },
  methods: {
    focusNewTag () {
      if (!this.readonly) {
        this.$refs.newtag.focus();
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