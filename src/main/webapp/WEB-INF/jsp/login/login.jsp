<%-- 
    Document   : login
    Created on : 4 de out de 2018, 13:22:51
    Author     : romulo.douro
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="tmp" tagdir="/WEB-INF/tags/" %>
<tmp:Layout>

 <script src="${cp}/resources/vendor/vue/vue.min.js" type="text/javascript"></script>

 <div id="appLogin">
  <form v-on:submit.prevent="loga">
   <input type="hidden" name="redir" v-model="redir"/>
   <fieldset>
    <legend>Login</legend>
    <div class="row">
     <div class="col-lg-3 col-sm-3">
      <input placeholder="Login" v-model="login" type="text" class="form-control" autofocus> 
     </div>
     <div class="col-lg-3 col-sm-3">
      <input placeholder="Senha" v-model="senha" type="password" class="form-control" value="">  
     </div>
     <div class="col-lg-3 col-sm-3">
      <input type="submit" class="btn btn-default" value="Logar"/>
     </div>
    </div>
   </fieldset>
  </form>
 </div>

 <script>
  var vLogin = new Vue({
   el: "#appLogin",
   data: {
    redir: '${cp}',
    login: '',
    senha: ''
   },
   methods: {
    loga: function () {
     var urlLoga = '${cp}/login/logaWS';
     var dados = {login: vLogin.login, senha: vLogin.senha};

     $.ajax({
      type: "POST",
      //the url where you want to sent the userName and password to
      url: "login/logaWS",
      //contentType: "application/json",
      //json object to sent to the authentication url
      //data: JSON.stringify(dados),
      data: dados,
      beforeSend: function (xhr) {   //Include the bearer token in header
       //xhr.setRequestHeader("Authorization", 'Bearer ' + jwt);
      },
      success: function (dt, status, request) {
       //console.log(request.getAllResponseHeaders());
       var auth = request.getResponseHeader('authorization').split(' ')[1];
       sessionStorage.setItem('Authorization', auth);
       document.location = vLogin.redir;
      },
      error: function (xhr, ajaxOptions, thrownError) {
       alert(xhr.status);
       alert(thrownError);
      }
     });

     return false;
    }
   }
  });
 </script>
</tmp:Layout>
