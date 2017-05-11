<template>
  <div class="card-box p-b-0" v-if="page != null">
    <h4 class="text-dark  header-title m-t-0" v-text="page.name"></h4>
    <p class="text-muted m-b-25 font-13">
      This basic wizard have no form validation and allows you to skip to another step by clicking on the tab.
    </p>
  
    <a class="btn btn-rounded btn-info" @click="display.modalEdit = true">Edit</a>
    <a class="btn btn-rounded btn-danger" @click="deletePage">Delete</a>
  
    <modal :show.sync="display.modalEdit" @close="display.modalEdit = false">
      <h4 slot="header">Edit page</h4>
      <div slot="body">
        <div class="row">
          <div class="col-md-4">
            <div class="form-group">
              <label class="control-label">Name:</label>
              <input type="text" class="form-control" v-model="page.name">
            </div>
            <div class="form-group">
              <label class="control-label">Category:</label>
              <select v-model="page.category" class="form-control">
                <option v-for="cat in categories" v-text="cat.name" :value="cat"></option>
              </select>
            </div>
          </div>
        </div>
  
        <div class="row">
          <div class="text-center col-xs-12">
            <button class="btn btn-rounded btn-primary" type="submit" @click="submit">Submit</button>
          </div>
        </div>
      </div>
    </modal>
  </div>
</template>

<script>
export default {
  name: 'page-overview',
  props: {
    page : {
      type: Object,
      default: new Object()
    }
  },
  data() {
    return {
      edit: false,
      display: {
        modalEdit: false,
      },
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

<style>

</style>
