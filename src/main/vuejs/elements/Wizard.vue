<template>
  <div class="wizard">
    <div class="wizard-navigation">
      <a 
        class="wizard-navigation-step" 
        :class="{ 'disabled' : !canNavigate(index) && index > activePage, 'active' : index == activePage }" 
        v-for="(page, index) in pages" 
        :key="page.title" 
        @click="navigateTo(index)">

        <div class="number-circle">
          <i class="fa " :class="'fa-' + page.icon" v-if="page.icon"></i>
          <span v-else>
            {{ index + 1 }}
          </span>
        </div>
        <h5 v-text="page.title"></h5>
      </a>
    </div>
  
    <div class="steps-container">
      <div id="bar" class="progress progress-striped">
        <div 
          class="bar progress-bar progress-bar-primary" 
          :style="progressBarStyle"></div>
      </div>
  
      <slot>
      </slot>
  
      <div class="wizard-navigation-buttons clearfix m-t-10">
        <a class="btn btn-primary" 
          @click="navigateTo(activePage - 1)" 
          v-if="canNavigate(activePage - 1)">
        Previous</a>
        
        <div class="text-right">
          <a class="btn btn-primary pull-right" 
            @click="navigateTo(activePage + 1)" 
            v-if="canNavigate(activePage + 1)">Next</a>
          <a class="btn btn-success pull-right" 
            v-if="activePage + 1 === pages.length" 
            @click="complete">Submit</a>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'wizard',
  data () {
    return {
      pages: [],
      activePage: 0
    };
  },
  computed: {
    progressBarStyle () {
      return {
        width: `${this.progress}%`
      };
    },
    progress () {
      if (this.activePage > 0) {
        return (1 / (this.pages.length * 2) * 100) * ((this.activePage * 2) + 1);
      } else return 1 / (this.pages.length * 2) * 100;
    }
  },
  methods: {
    addPage (page_component) {
      this.pages.push(page_component);

      if (this.pages.length === 1) {
        this.navigateTo(0);
      }
    },
    navigateTo (index) {
      if (this.pages[this.activePage].valid || index < this.activePage) {
        this.activePage = index;

        this.pages.forEach(page => {
          page.active = false;
        });

        this.pages[index].active = true;
      }
    },
    movePage (direction) {
      this.navigateTo(this.activePage + direction);
    },
    canNavigate (index) {
      if ((this.pages.length > 0 && !this.pages[this.activePage].valid && index > this.activePage) || index === -1) return false;
      return this.pages.length - index > 0 && this.activePage + 1 === index;
    },
    complete () {
      this.$emit('finish');
    }
  }
};
</script>

<style lang="less">
@import (reference) '~less/variables.less';
.wizard {
  .wizard-navigation {
    margin: 0;
    padding: 0;
    list-style-type: none;
    display: flex;
    justify-content: space-around;

    .wizard-navigation-step {
      width: auto;
      text-align: center;
      display: inline-block;
      padding: 15px;

      .number-circle {
        margin: 0 auto;
        background-color: white;
        border-radius: 100%;
        width: 50px;
        height: 50px;
        font-weight: bold;
        text-align: center;
        line-height: 50px;
        margin-bottom: 15px;
        transition: .5s ease;
        color: #505458;

        border: 1px solid #dfdfdf;
        background: #fafafa;
      }

      &.active {
        h5 {
          color: #000;
        }

        .number-circle {
          background-color: @color-primary;
          color: #fff;
        }
      }

      &.disabled {
        pointer-events: none;
        cursor: default;

        h5 {
          color: lightgray;
        }

        .number-circle {
          color: lightgray;
        }
      }
    }
  }

  .steps-container {
    margin-top: 15px;
    background-color: #fff;
  }
}
</style>
