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
      passwordConfirm : null,
      company : {
        name : null,
        ico : null
      }
    },
    fieldErrors: [],
  },
  methods : {
    submit : function() {
      this.working = true;
      this.fieldErrors = [];
      var self = this;

      $.ajax({
        type : "POST",
        url : "/auth/register",
        data : JSON.stringify(this.user),
        success : function(data) {
          window.location.href = application.context;
        },
        error: function(data) {
          self.fieldErrors = data.responseJSON.fieldErrors;
          self.working = false;
        },
        contentType : "application/json",
        dataType : "json"
      });
    }
  }
});