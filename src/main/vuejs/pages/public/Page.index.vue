<template>
  <div class="container">
    <div class="row">
      <div class="col-xs-12">
        <ul class="list-inline">
          <li v-for="cat in categories" v-text="cat.name">
          </li>
        </ul>
      </div>
  
      <div class="explore-container">
        <div class="page-panel" v-for="page in pages">
          <img src="http://imgsv.imaging.nikon.com/lineup/lens/zoom/normalzoom/af-s_dx_18-140mmf_35-56g_ed_vr/img/sample/sample1_l.jpg" />
  
          <div class="title">
            <router-link :to="'page/' + page.id">
              <h4 v-text="page.name"></h4>
            </router-link>
            <small class="text-muted" v-text="page.category.name"></small>
          </div>  
        </div>
      </div>
    </div>
  
  </div>
</template>

<script>
export default {
  name: 'page-explore',
  data() {
    return {
      pages: [],
      categories: []
    }
  },
  created() {
    this.getCategories();
    this.getPages();
  },
  methods: {
    getPages() {
      this.$http.get('api/pages/0/100').then(response => {
        this.pages = response.body.content;
      })
    },
    getCategories() {
      this.$http.get('api/categories').then(response => {
        this.categories = response.body;
      })
    }
  }
}
</script>

<style lang="less">
.explore-container {
  display: flex;
  flex-wrap: wrap;
  width: 100%;
}

.page-panel {
  flex: 250px 1 1;
  position: relative;
  margin: 10px;
  overflow: hidden;

  img {
    width: 100%;
    transition: all .3s ease;
  }

  .title {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    background: white;
    margin: 0;
    padding: 10px 20px;
  }

  &:hover {
    img {
      transform: scale(1.1);
    }
  }
}
</style>
