<template>
  <transition name="modal">
    <div class="modal"
         v-show="show">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <slot name="header">
              Information
            </slot>
          </div>
  
          <div class="modal-body">
            <slot name="body">
            </slot>
          </div>
  
          <div class="modal-footer">
            <slot name="footer">
              <button type="button"
                      class="btn btn-sm btn-default"
                      data-dismiss="modal"
                      @click="close">
                <span v-if="question">Disagree</span><span v-else>Ok</span>
              </button>
              <button v-if="question"
                      type="button"
                      class="btn btn-sm btn-success"
                      @click="accept">I agree</button>
            </slot>
          </div>
        </div>
      </div>
    </div>
  </transition>
</template>

<script>
export default {
  props: {
    show: {
      type: Boolean,
      default: false,
    },
    question: Boolean,
  },
  methods: {
    accept: function () {
      this.$emit('accept');
    },
    close: function() {
      this.$emit('close');
    }
  }
}
</script>

<style>
.modal {
  background-color: rgba(0, 0, 0, .5);
  transition: opacity .3s ease;
  display: block;
}

.modal-enter-active {
  transition: all .3s ease;
}

.modal-enter {
  opacity: 0;
}

.modal-leave-active {
  opacity: 0;
  transition: all .3s ease;
}

.modal-enter .modal-content,
.modal-leave-active .modal-content {
  -webkit-transform: scale(1.1);
  transform: scale(1.1);
}
</style>