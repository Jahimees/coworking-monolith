<script setup>

import {computed, onMounted, ref} from "vue";
import SearchSelect from "@/components/util/SearchSelect.vue";

const labels = [
  "ID", "ФИО", "Имя пользователя", "Email", "Отдел", "Роль", "Рабочее место", "Новый пароль"
]

const errorLabels = [
  "ID неизменяем",
  "ФИО: до 3 слов на кириллице, разделенных пробелами. <60 символов",
  "Имя пользователя: не менее 5 и не более 30 латинских символов и цифр",
  "Email не соответствует стандартному шаблону", "", "", "", "Длина пароля должна быть не менее 5 и не более 50 символов"
]

const inputNames = [
  "id", "name", "username", "email", "department", "roles", "workspace", "password"
]

const url = "http://localhost:8080/api/v1/users"

let isIdCorrect = ref(true)
let isNameCorrect = ref(true)
let isUsernameCorrect = ref(true)
let isEmailCorrect = ref(true)
let isPasswordCorrect = ref(true)

const $emit = defineEmits(['close', 'reloadTable', 'success', 'fail'])

const props = defineProps({
  title: {
    type: String,
    required: true
  },
  action: {
    type: String,
    default: false
  },
  data: {
    type: Array,
    default: () => []
  },
  departmentMap: {
    type: Array
  },
  roleMap: {
    type: Array
  }
})

let userParams
onMounted(() => {
  userParams = computed(() => Array.from(props.data))
})

let departmentId = "not_changed"
let roleId = "not_changed"

function updateUser() {
  clearErrors()

  let json = constructJsonForRequest();

  if (json === false) {
    return
  }

  fetch(url + "/" + props.data[2], {
    method: "PATCH",
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(json)
  })
      .then(data => data.json())
      .then(json => {
        $emit('success')
        $emit('reloadTable')
      })
      .catch(ev => {
        $emit('fail')
      })


  close()
}

function validateFields(userParams) {
  let id = userParams[0]
  let fullName = userParams[1]
  let username = userParams[2]
  let email = userParams[3]
  let password = $("input[name=password]")[0].value

  if (id !== props.data[0]) {
    isIdCorrect.value = false;
  }

  let clearName = typeof fullName === 'undefined' ? '' : fullName.replace(/\s+/g, ' ').trim();
  let splittedName = clearName.split(' ')
  if (splittedName[0] === '' || (splittedName.length < 1 || splittedName.length > 3)
      || !/^[А-Яа-яЁё\s]+$/.test(clearName) || splittedName.length > 60) {
    isNameCorrect.value = false;
  }

  if (typeof username === "undefined" || username.length < 5 || username.length > 30
      || !/^[A-Za-z0-9]+$/.test(username)) {
    isUsernameCorrect.value = false
  }

  if (!/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(email)) {
    isEmailCorrect.value = false
  }

  if (props.action === 'create' && (password.length < 5 || password.length > 50)) {
    isPasswordCorrect.value = false
  }

  return isIdCorrect.value && isNameCorrect.value && isUsernameCorrect.value && isEmailCorrect.value && isPasswordCorrect.value
}

function createUser() {
  clearErrors()

  let json = constructJsonForRequest();

  if (json === false) {
    return
  }

  fetch(url, {
    method: "POST",
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(json)
  })
      .then(data => data.json())
      .then(json => {
        $emit('success')
        $emit('reloadTable')
      })
      .catch(ev => {
        console.log(ev)
        $emit('fail', ev)
      })

  close()
}

function constructJsonForRequest() {

  let mutableUserParams = userParams._value
  if (!validateFields(mutableUserParams)) {
    return false;
  }

  let fullName = mutableUserParams[1]
  let username = mutableUserParams[2]
  let email = mutableUserParams[3]

  if (departmentId === "not_changed") {
    departmentId = mutableUserParams[4]
  }

  if (roleId === "not_changed") {
    roleId = mutableUserParams[6]
  }

  let password = $("input[name=password]")[0].value

  let splittedName = fullName.split(' ')
  let firstName = splittedName[1];
  let lastName = splittedName[0];
  let middleName = splittedName[2];

  return {
    firstName: firstName,
    lastName: lastName,
    middleName: middleName,
    username: username,
    email: email,
    password: password,
    department: {
      id: departmentId
    },
    roles: [{
      id: roleId
    }]
  }
}


const isDeleteConfirmation = ref(false)

function deleteUser() {
  isDeleteConfirmation.value = true
}

function confirmDeleteUser() {
  let id = userParams._value[0]


  fetch(url + "/" + id, {
    method: "DELETE",
    headers: {
      'Content-Type': 'application/json'
    },
  })
      .then(data => {
            $emit('success', "Пользователь успешно удален")
            $emit('reloadTable')
          }
      )
      .catch(ev => {
        $emit('fail', ev)
      })

  close()
}

function close() {
  isDeleteConfirmation.value = false
  $emit('close')
}

function clearErrors() {
  isIdCorrect.value = true;
  isNameCorrect.value = true;
  isEmailCorrect.value = true;
  isUsernameCorrect.value = true;
  isPasswordCorrect.value = true;
}

function isInputCorrect(inputName) {
  return this['is' + inputName.charAt(0).toUpperCase() + inputName.slice(1) + 'Correct'];
}

function setRoleId(id) {
  roleId = id
}

function setDepartmentId(id) {
  departmentId = id
}

//TODO load departments, load workspaces, load offices???, roles like select

</script>
<template>
  <transition name="modal-fade">
    <div class="modal-backdrop">
      <div class="modal"
           role="dialog"
           aria-labelledby="modalTitle"
           aria-describedby="modalDescription">
        <header
            class="modal-header"
            id="modalTitle">
          <slot name="header">
            {{ title }}

            <button
                type="button"
                class="btn-close"
                @click="close"
                aria-label="Close modal">
            </button>
          </slot>
        </header>
        <section
            class="modal-body"
            id="modalDescription">
          <slot name="body">
            <div id="modal-box">
              <div v-for="(item, index) in userParams" :key="index">
                <label :name="inputNames[index]" class="err-label"
                       v-if="index < 4 && !isInputCorrect(inputNames[index])">
                  {{ errorLabels[index] }}
                </label>
                <div v-if="index < 4">
                  <label>{{ labels[index] }}</label>
                  <input
                      :name="inputNames[index]"
                      v-model="userParams[index]"
                      :disabled="index === 0"
                  />
                </div>
              </div>
              <div>
                <div>
                  <label>Отдел</label>
                  <SearchSelect :options="props.departmentMap"
                                :default-value="{id: props.data[4], name: props.data[5]}"
                                @return-id="setDepartmentId"
                  />
                </div>
              </div>
              <div>
                <div>
                  <label>Роль</label>
                  <SearchSelect :options="props.roleMap"
                                :default-value="{id: props.data[6], name: props.data[7]}"
                                @return-id="setRoleId "/>
                </div>
              </div>
              <div>
                <div>
                  <label>Рабочее место</label>
                  <!--                  <SearchSelect />-->
                </div>
              </div>
              <div>
                <label :name="'password'" class="err-label"
                       v-if="!isInputCorrect('password')">
                  {{ errorLabels[7] }}</label>
                <div>
                  <label>Новый пароль</label>
                  <input name="password" type="password">
                </div>
              </div>
            </div>
          </slot>
        </section>
        <footer class="modal-footer">
          <slot name="footer">
            <button
                type="button"
                @click="updateUser"
                v-if="action === 'update'"
                aria-label="Update modal">
              Обновить
            </button>
            <button
                type="button"
                @click="deleteUser"
                v-if="action === 'update'"
                aria-label="Delete modal">
              Удалить
            </button>
            <button
                class="confirm-deletion"
                type="button"
                @click="confirmDeleteUser"
                v-if="isDeleteConfirmation"
                aria-label="Delete modal">
              Подтвердить удаление
            </button>
            <button
                type="button"
                @click="createUser"
                v-if="action === 'create'"
                aria-label="Create modal">
              Создать
            </button>
            <button
                type="button"
                @click="close"
                aria-label="Close modal">
              Отменить
            </button>

            {{ props.action }}
          </slot>
        </footer>
      </div>
    </div>
  </transition>
</template>
<style>

#modal-box > div > div {
  display: flex;
  margin: 0 0 0 0;
  font-size: 1.2em;
}

#modal-box > div {
  margin: 1em 0 0 0;
}

#modal-box > div > div > label {
  margin-right: 15px;
}

#modal-box input {
  margin: auto 0 0 auto
}

#modal-box > div > div > div {
  margin: auto 0 0 auto
}

.confirm-deletion {
  background-color: red;
}

</style>