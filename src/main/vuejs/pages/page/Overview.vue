<template>
  <div class="card-box" v-if="page != null">
    <h4 class="text-dark  header-title m-t-0">Overview</h4>
    <div class="row">
      <div class="col-md-6">
        <div class="form-group">
          <label class="control-label">Name: </label>
          <input class="form-control required" v-model="page.name" type="text">
        </div>
      </div>
      <div class="col-md-6">
        <div class="form-group">
          <label class="control-label">Category: </label>
          <select v-model="page.category" class="form-control">
            <option v-for="cat in categories" v-text="cat.name" :value="cat"></option>
          </select>
        </div>
      </div>
    </div>
  
    <div class="form-group">
      <label class="control-label">Summary: </label>
      <froala :tag="'textarea'" v-model="page.summary"></froala>
    </div>
  
    <div class="text-center">
      <button class="btn btn-rounded btn-danger" @click="deletePage">Delete</button>
      <button class="btn btn-rounded btn-primary" type="submit" @click="submit">
        <span v-if="edit">Save</span>
        <span v-else>Submit</span> {{ page.name }}</button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'page-overview',
  props: {
    page: {
      type: Object,
      default() {
        return new Object();
      }
    },
    edit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      categories: [],
    }
  },
  created() {
    this.getCategories();
  },
  methods: {
    submit() {
      if (this.edit) {
        this.$http.put('api/page/' + this.page.id, this.page).then(response => {
          this.page = response.body;
          this.$success('Success !', 'Page ' + this.page.name + ' has been updated.')
        });
      } else {
        this.$http.post('api/user/page', this.page).then(response => {
          this.edit = true;
          this.page = response.body;
          this.$store.state.user.pages.push(this.page);
          this.$success('Success !', 'Page ' + this.page.name + ' has been created.');
          this.$router.push('/admin/page/' + this.page.id);
        });
      }
    },
    deletePage() {
      this.$http.delete('api/page/' + this.page.id).then(response => {
        this.$router.push('/admin');
        this.$store.state.user.pages = this.$store.state.user.pages.filter(p => {
          return p.id != this.page.id;
        })
      })
    },
    getCategories() {
      this.$http.get('api/categories').then(response => {
        this.categories = response.body;
      })
    },
  }
}
</script>
