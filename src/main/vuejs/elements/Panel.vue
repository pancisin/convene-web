<template>
  <div class="panel" :class="'panel-' + type">
    <div class="panel-heading">
      <h3 class="panel-title">
        <slot name="title"></slot>
      </h3>
      <p class="panel-sub-title font-13 text-muted">
        <slot name="subtitle"></slot>
      </p>
    </div>
  
    <div class="panel-body">
      <slot></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: 'panel',
  props: ['type']
}
</script>