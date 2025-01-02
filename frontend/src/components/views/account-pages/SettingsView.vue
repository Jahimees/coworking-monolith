<script setup>

import {onMounted, ref} from "vue";

const props = defineProps({parsedToken: Object})

const usernameValue = ref("")
const emailValue = ref("")
const firstNameValue = ref("")
const lastNameValue = ref("")
const middleNameValue = ref("")
const rawPasswordValue = ref("")

const isUsernameEmpty = ref(false)
const isEmailEmpty = ref(false)

onMounted(() => {
  console.log(props.parsedToken.sub)

  fetch("http://localhost:8080/api/v1/users/" + props.parsedToken.sub,
      {
        method: "GET",
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

        throw Error("Credentials are wrong")
      })
      .then((responseJson) => {
        usernameValue.value = responseJson.username
        emailValue.value = responseJson.email
        firstNameValue.value = responseJson.firstName
        lastNameValue.value = responseJson.lastName
        middleNameValue.value = responseJson.middleName
      })
      .catch((error) => {
        console.log(error)
      });
})

function patchUser() {
  isUsernameEmpty.value = false
  isEmailEmpty.value = false


  if (usernameValue.value === null || usernameValue.value.trim() === "") {
    isUsernameEmpty.value = true
  }

  if (emailValue.value === null || emailValue.value.trim() === "") {
    isEmailEmpty.value = true
  }

  if (isUsernameEmpty.value || isEmailEmpty.value) {
    return
  }

  const body = getBody()

  fetch("http://localhost:8080/api/v1/users/" + props.parsedToken.sub, {
    method: "PATCH",
    body: JSON.stringify(body),
    headers: {
      "Content-Type": "application/json"
    },
    crossDomain: true
  }).then(async (response) => {
    if (response.ok) {
      alert("Изменения прошли успешно")
      return response.json();
    }

    throw Error("Credentials are wrong")
  })
      .then((responseJson) => {
        console.log(responseJson)
      })
      .catch((error) => {
        console.log(error)
      });
}

function getBody() {
  return {
    "username": usernameValue.value ? usernameValue.value.trim() : "",
    "email": emailValue.value ? emailValue.value.trim() : "",
    "rawPassword": rawPasswordValue.value ? rawPasswordValue.value.trim() : "",
    "firstName": firstNameValue.value ? firstNameValue.value.trim() : "",
    "lastName": lastNameValue.value ? lastNameValue.value.trim() : "",
    "middleName": middleNameValue.value ? middleNameValue.value.trim() : ""
  }
}
</script>

<template>
  <div class="view-header">
    Настройки
  </div>
<!--  {{ props.parsedToken }}-->

  <div style="margin: 0 auto; width: min-content">

    <div class="row" style="flex-wrap: nowrap">
      <div class="setting-block col">
        <div class="setting-title">Главные настройки</div>
        <div>
          <label>Имя пользователя</label>
          <div class="err-field" v-if="isUsernameEmpty">Имя пользователя не может быть пустым</div>
          <input id="username-input" v-model="usernameValue">
        </div>
        <div>
          <label>Электронная почта</label>
          <div class="err-field" v-if="isEmailEmpty">Электронная почта не может быть пустой</div>
          <input id="email-input" v-model="emailValue">
        </div>
        <div>
          <label>Пароль</label>
          <input id="password-input" type="password" v-model="rawPasswordValue">
        </div>
        <div>

        </div>
      </div>
      <div class="setting-block col">
        <div class="setting-title">Информация</div>
        <div>
          <label>Имя</label>
          <input id="firstname-input" v-model="firstNameValue">
        </div>
        <div>
          <label>Фамилия</label>
          <input id="lastname-input" v-model="lastNameValue">
        </div>
        <div>
          <label>Отчество</label>
          <input id="middlename-input" v-model="middleNameValue">
        </div>
      </div>
<!--      <div class="setting-block col">-->
<!--        <div class="setting-title">Информация</div>-->
<!--        <div>-->
<!--          <label>Имя</label>-->
<!--          <input id="firstname-input" v-model="firstNameValue">-->
<!--        </div>-->
<!--        <div>-->
<!--          <label>Фамилия</label>-->
<!--          <input id="lastname-input" v-model="lastNameValue">-->
<!--        </div>-->
<!--        <div>-->
<!--          <label>Отчество</label>-->
<!--          <input id="middlename-input" v-model="middleNameValue">-->
<!--        </div>-->
<!--      </div>-->
    </div>
<!--    <div class="row" style="flex-wrap: nowrap">-->
<!--      <div class="setting-block col">-->
<!--        <div class="setting-title">Главные настройки</div>-->
<!--        <div>-->
<!--          <label>Имя пользователя</label>-->
<!--          <div class="err-field" v-if="isUsernameEmpty">Имя пользователя не может быть пустым</div>-->
<!--          <input id="username-input" v-model="usernameValue">-->
<!--        </div>-->
<!--        <div>-->
<!--          <label>Электронная почта</label>-->
<!--          <div class="err-field" v-if="isEmailEmpty">Электронная почта не может быть пустой</div>-->
<!--          <input id="email-input" v-model="emailValue">-->
<!--        </div>-->
<!--        <div>-->
<!--          <label>Пароль</label>-->
<!--          <input id="password-input" v-model="rawPasswordValue">-->
<!--        </div>-->
<!--        <div>-->

<!--        </div>-->
<!--      </div>-->
<!--      <div class="setting-block col">-->
<!--        <div class="setting-title">Информация</div>-->
<!--        <div>-->
<!--          <label>Имя</label>-->
<!--          <input id="firstname-input" v-model="firstNameValue">-->
<!--        </div>-->
<!--        <div>-->
<!--          <label>Фамилия</label>-->
<!--          <input id="lastname-input" v-model="lastNameValue">-->
<!--        </div>-->
<!--        <div>-->
<!--          <label>Отчество</label>-->
<!--          <input id="middlename-input" v-model="middleNameValue">-->
<!--        </div>-->
<!--      </div>-->
<!--      <div class="setting-block col">-->
<!--        <div class="setting-title">Информация</div>-->
<!--        <div>-->
<!--          <label>Имя</label>-->
<!--          <input id="firstname-input" v-model="firstNameValue">-->
<!--        </div>-->
<!--        <div>-->
<!--          <label>Фамилия</label>-->
<!--          <input id="lastname-input" v-model="lastNameValue">-->
<!--        </div>-->
<!--        <div>-->
<!--          <label>Отчество</label>-->
<!--          <input id="middlename-input" v-model="middleNameValue">-->
<!--        </div>-->
<!--      </div>-->
<!--    </div>-->
  </div>
  <div class="btn-container">

    <button @click="patchUser">Сохранить</button>
  </div>

</template>

<style scoped>
label {
  font-size: 1.3em;
  margin: 1vh 0 0 0;
  text-align: left;
}

.setting-title {
  font-size: 1.5em;
  font-weight: bold;
  text-align: center;
}

.setting-block {
  margin: 2vh 0 1vh 1.5vh;
  padding: 2vh 5vh 3vh 5vh;
  border-radius: 10px;
  background-color: #dbeaff;
}

input {
  display: block;
}

.err-field {
  color: #a10000;
  font-size: 1em;
}

.btn-container {
  margin: 0 1.5vh;
  bottom: 5%;
  position: fixed;
}
</style>