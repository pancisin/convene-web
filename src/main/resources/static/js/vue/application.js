var application = new Vue({
  el: '#application-container',
  components: {
    headerBar: {
      data: function() {
        return {
          user: null
        }
      },
      created: function() {
        this.getUser();
      },
      methods: {
        getUser: function() {
          $.ajax({
            method: 'GET',
            
          });
        }
      }
    }
  }
}); 