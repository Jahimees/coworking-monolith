<script setup>

import {ref} from "vue";
import Utils from '@/scripts/Utils.js'

const username = ref("")
const rawPassword = ref("")

const wrongCredsErr = ref(false)

async function authorize() {
  const userDto = {
    username: username.value,
    password: rawPassword.value
  }

  fetch("http://localhost:8080/api/v1/auth",
      {
        method: "POST",
        body: JSON.stringify(userDto),
        headers: {
          "Content-Type": "application/json"
        },
        crossDomain: true
      }
  )
      .then(async (response) => {
        if (response.ok) {
          return response.json();
        }

        const responseText = await response.text()

        wrongCredsErr.value = false

        switch (responseText) {
          case "Имя пользователя или пароль неверные": {
            wrongCredsErr.value = true
          }
        }

        throw Error("Credentials are wrong")
      })
      .then((responseJson) => {
        document.cookie = "_at=" + responseJson.token + "; max-age=1800"
        document.location.href = '/?i=auth'
      })
      .catch((error) => {
        console.log(error)
      });
}

function test() {
  fetch("http://localhost:8080/api/v1/test",
      {
        headers: {
          Authorization: "Bearer " + Utils.getCookie("_at")
        }
      })
      .then(async (response) => {

        console.log(await response.text())
      })
}
</script>

<template>
  <a class="block-center m-t-3em" href="/frontend/public">
    <img src="../../../assets/images/nauchsoft_logo.png">
  </a>
  <div class="bg-image p-tb-3em m-t-5em block-center">
    <h1 class="block-center">NAUCHSOFT Авторизация</h1>
    <div class="err-field" v-if="wrongCredsErr">Логин и (или) пароль неверные</div>
    <div class="block-center">
      <div class="lbl-field">Имя пользователя</div>
      <input name="username" v-model="username">
    </div>
    <div class="block-center">
      <div class="lbl-field">Пароль</div>
      <input name="rawPassword" type="password" v-model="rawPassword">
    </div>
    <div>
      <button @click="authorize">Авторизоваться</button>
    </div>
    <hr>
<!--    <div class="block-center" style="color: white">Еще нет аккаунта?-->
<!--      <a style="color: white" href="/reg">-->
<!--        <b>Зарегистрироваться</b>-->
<!--      </a>-->
<!--    </div>-->
  </div>
</template>

<style scoped>

@font-face {
  font-weight: normal;
  font-style: normal;
  font-family: 'codropsicons';
  src: url('../fonts/codropsicons/codropsicons.eot');
  src: url('../fonts/codropsicons/codropsicons.eot?#iefix') format('embedded-opentype'),
  url('../fonts/codropsicons/codropsicons.woff') format('woff'),
  url('../fonts/codropsicons/codropsicons.ttf') format('truetype'),
  url('../fonts/codropsicons/codropsicons.svg#codropsicons') format('svg');
}

.lbl-field {
  font-family: 'codropsicons';
  font-size: 2em;
  color: white;
}

input {
  font-size: 2em;
  font-family: 'codropsicons';
}

.bg-image {
  background-image: url("@/assets/images/auth_back.jpg");
  width: 60%;
  border-radius: 25px;
  opacity: 0.85;
}

h1 {
  font-size: 4em;
  margin: 0.3em 0;
  color: white;
  font-family: 'Raleway', Calibri, Arial, sans-serif;
}

img {
  width: 300px;
  cursor: pointer;
  transition: all 0.5s;
}

img:hover {
  width: 350px;
  transition: all 0.5s;
}

a.block-center:hover {
  background: none;
}

.err-field {
  font-family: 'codropsicons';
  color: #a10000;
  font-size: 1.3em;
}
</style>