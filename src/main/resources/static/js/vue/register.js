Vue.config.devtools = true;

var register_app = new Vue({
  el : "#register-app",
  data : {
    working : false,
    user : {
      firstName : null,
      lastName : null,
      email : null,
      password : null,
      confirm_password : null
    },
    company : {
      name : null,
      ico : null
    },
    fieldErrors: [],
  },
  methods : {
    submit : function() {
      this.working = true;
      var self = this;

      $.ajax({
        type : "POST",
        url : "/auth/register",
        data : JSON.stringify(this.user),
        success : function(data) {
          alert('data: ' + data);
        },
        error: function(data) {
          self.fieldErrors = data.responseJSON.fieldErrors;
        },
        complete: function() {
          self.working = false;
        },
        contentType : "application/json",
        dataType : "json"
      });

    }
  }
});