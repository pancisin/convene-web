<template>
  <div>
    <div class="form-group">
      <label>Subject</label>
      <input 
        type="text" 
        class="form-control" 
        v-model="report.subject">
    </div>
    
    <div class="form-group">
      <label>Description</label>
      <text-editor 
        v-model="report.description">
      </text-editor>
    </div>
    
    <div class="form-group" 
      v-loading="loadingScreenshot">
      <label>Screenshot</label>

      <img 
        :src="report.screenshot" 
        class="img-thumbnail">
    </div>

    <div class="text-right">
      <a class="btn btn-danger" @click="submit">Submit</a>
    </div>

  </div>
</template>

<script>
import TextEditor from './TextEditor';
import html2canvas from 'html2canvas';

export default {
  name: 'abuse-report',
  props: {
    subject: Object
  },
  components: {
    TextEditor
  },
  data () {
    return {
      report: {
        screenshot: null
      },
      loadingScreenshot: false
    };
  },
  mounted () {
    // const element = document.getElementsByClassName('content')[0];
    this.loadingScreenshot = true;
    html2canvas(document.body).then(canvas => {
      this.report.screenshot = canvas.toDataURL();
      this.loadingScreenshot = false;
    });
  },
  methods: {
    submit () {

    }
  }
};
</script>

<style>

</style>
