<template>
  <div>
    <a :href="image" target="_blank" @click.prevent="show(true)">
      <slot></slot>
    </a>
    <transition name="fade">
      <div class="lightbox" v-if="visible">
        <div class="lightbox__close" @click="show(false)">
          &times;
        </div>

        <div class="lightbox__element">
          <div
            class="lightbox__arrow lightbox__arrow--left"
            v-stream:click="{ subject: navigate$, data: { offset: -1 } }"
            :class="{'lightbox__arrow--invisible': !navigator.hasPrev}">
            <svg height="24" viewBox="0 0 24 24" width="24" xmlns="http://www.w3.org/2000/svg">
              <path d="M15.41 16.09l-4.58-4.59 4.58-4.59L14 5.5l-6 6 6 6z"/>
              <path d="M0-.5h24v24H0z" fill="none"/>
            </svg>
          </div>

          <div class="lightbox__image">
            <img :src="images[navigator.index]">
          </div>

          <div
            class="lightbox__arrow lightbox__arrow--right"
            v-stream:click="{ subject: navigate$, data: { offset: 1 } }"
            :class="{'lightbox__arrow--invisible': !navigator.hasNext}">
            <svg height="24" viewBox="0 0 24 24" width="24" xmlns="http://www.w3.org/2000/svg">
              <path d="M8.59 16.34l4.58-4.59-4.58-4.59L10 5.75l6 6-6 6z"/>
              <path d="M0-.25h24v24H0z" fill="none"/>
            </svg>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import { Subject, Observable } from 'rxjs';
export default {
  name: 'light-box',
  props: {
    image: String
  },
  data () {
    return {
      visible: false
    };
  },
  subscriptions () {
    const onCreate$ = this.$eventToObservable('hook:created');
    this.navigate$ = new Subject();

    const images = onCreate$
      .flatMap(() => Observable
        .of(...this.$parent.$children)
        .filter(c => c.$options.name === this.$options.name)
        .map(component => component.$options.propsData.image)
        .reduce((acc, cur) => {
          acc.push(cur);
          return acc;
        }, [])
      );

    const defaultIndex$ = images
      .map(images => {
        return {
          index: images.findIndex(image => image === this.image)
        };
      });

    const index$ = Observable
      .merge(
        this.navigate$.pluck('data'),
        defaultIndex$)
      .scan((acc, cur) => {
        if (cur.index) {
          return cur.index;
        } else {
          return acc + cur.offset;
        }
      }, 0);

    const navigator = Observable
      .combineLatest(images, index$.startWith(0))
      .map(([images, i]) => {
        return {
          index: i,
          hasNext: i + 1 < images.length,
          hasPrev: i - 1 >= 0
        };
      });

    return {
      navigator,
      images
    };
  },
  methods: {
    show (value) {
      this.visible = value;
      document.body.classList.toggle('noscroll', value);
    }
  }
};
</script>

<style lang="less">
@color_1: #fff;

.lightbox {
  position: fixed;
  top: 0;
  left: 0;
  background: rgba(0, 0, 0, 0.8);
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 800;
}
.lightbox__close {
  position: fixed;
  right: 0;
  top: 0;
  padding: 1rem;
  // font-size: 1.5rem;
  font-size: 48px;
  cursor: pointer;
  color: @color_1;
  width: 4rem;
  height: 4rem;
}
.lightbox__arrow--invisible {
  visibility: hidden;
}
.lightbox__element {
  display: flex;
  height: fit-content;
}
.lightbox__arrow {
  padding: 0 2rem;
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
  svg {
    fill: #fff;
    pointer-events: none;
  }
}
.lightbox__image {
  flex: 1;
  img {
    width: 100%;
    height: auto !important;
  }
}
@media screen and (max-width: 720px) {
  .lightbox__arrow {
    padding: 0 1rem;
  }
}
@media screen and (max-width: 500px) {
  .lightbox__element {
    position: relative;
  }
  .lightbox__arrow {
    position: absolute;
    padding: 0 2rem;
    height: 100%;
  }
  .lightbox__arrow--right {
    right: 0;
    background: linear-gradient(to right, transparent, rgba(0, 0, 0, 0.3));
  }
  .lightbox__arrow--left {
    left: 0;
    background: linear-gradient(to left, transparent, rgba(0, 0, 0, 0.3));
  }
}
</style>
