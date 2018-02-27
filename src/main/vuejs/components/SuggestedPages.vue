<template>
  <div 
    class="suggested-pages-outer" 
    v-loading="loading">

    <h3>You might also like some of these pages</h3>

    <div>
      <button 
        type="button" 
        v-stream:click="{ subject: navigate$, data: { offset: -1 } }" 
        class="btn btn-link">
        <i class="fa fa-angle-left fa-3x"></i>
      </button>
      <div class="suggested-pages-wrapper">
        <div class="suggested-pages" ref="spnavigator">
          <stagger-transition>
            <div 
              v-for="(page, index) in suggestions" 
              :key="index"
              class="card"
              :class="{ 'card-focused' : page.focused }"
              v-stream:click="{ subject: navigate$, data: { index } }">

              <div>
                <div class="title">
                  <h5>{{ page.name }}</h5>
                  <small 
                    class="text-muted" 
                    v-if="page.category != null">
                    {{ $t('category.' + page.category.code + '.' + page.branch.code) }}
                  </small>
                </div>

                <div 
                  class="image-wrapper" 
                  v-if="page.poster">
                  
                  <vue-image 
                    :src="page.poster.path" 
                    placeholder />
                </div>
              </div>
            </div>
          </stagger-transition>
        </div>
      </div>

      <button 
        type="button" 
        v-stream:click="{ subject: navigate$, data: { offset: 1 } }"
        class="btn btn-link">
        <i class="fa fa-angle-right fa-3x"></i>
      </button>
    </div>
  </div>
</template>

<script>
import UserApi from 'api/user.api';
import { Observable, Subject } from 'rxjs';
import { VueImage } from 'elements';
import velocity from 'velocity-animate';
import { StaggerTransition } from '../functional/transitions';

export default {
  name: 'suggested-pages',
  data () {
    return {
      loading: false
    };
  },
  components: {
    VueImage,
    StaggerTransition
  },
  subscriptions () {
    this.navigate$ = new Subject();
    const onCreate$ = this.$eventToObservable('hook:created');

    const navigator$ = this.navigate$
      .pluck('data')
      .startWith({ index: 0 })
      .scan((acc, cur) => {
        if (cur.offset != null) {
          const next = acc + cur.offset;
          if (next >= this.suggestions.length || next < 0) {
            return acc;
          }
          return acc + cur.offset;
        }
        return cur.index;
      }, 0);

    const suggestions = onCreate$
      .do(() => { this.loading = true; })
      .flatMap(() => Observable.create(ob => {
        UserApi.getSuggestedPages(null, 0, 15, pages => {
          ob.next(pages);
        });
      }))
      .do(() => { this.loading = false; })
      .pluck('content')
      .combineLatest(navigator$, (pages, index) => {
        this.$nextTick(() => {
          const el = this.$refs.spnavigator;
          velocity(el, {
            left: `${el.offsetWidth / 3 - index * 160 - 5}px`
          }, {
            queue: false,
            duration: 200,
            easing: 'ease-in-out'
          });
        });

        return pages.map((p, i) => {
          return {
            ...p,
            focused: i === index
          };
        });
      });

    return {
      suggestions
    };
  }
};
</script>

<style lang="less">
.suggested-pages-outer {
  & > div {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
  }

  & > h3 {
    color: #fff;
  }

  button.btn-link {
    color: #fff;
  }
}

.suggested-pages-wrapper {
  overflow-x: hidden;
  text-align: center;
  margin: 0 15px;
}

.suggested-pages {
  white-space: nowrap;
  position:relative;

  .card {
    white-space: normal;
    display: inline-block;
    height: 200px;
    width: 150px;
    overflow: hidden;
    cursor: pointer;

    & ~ .card {
      margin-left: 10px;
    }

    &.card-focused {
      // border: 1px solid red;
    }
  }
}
</style>
