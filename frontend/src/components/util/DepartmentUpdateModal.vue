<script setup>

import {computed, onMounted, ref} from "vue";

const labels = [
  "ID", "Название отдела", "Начальник отдела"
]

const errorLabels = [
  "ID неизменяем",
  "Название отдела: не менее 1 и не более 50 символов",
]

const inputNames = [
  "id", "name", "user_id", "user"
]

const url = "http://localhost:8080/api/v1/departments"

let isIdCorrect = ref(true)
let isNameCorrect = ref(true)
let isUserCorrect = ref(true)

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
  }
})

let departmentParams
onMounted(() => {
  departmentParams = computed(() => Array.from(props.data))
  console.log(departmentParams)

})

function updateDepartment() {
  clearErrors()

  let mutableUserParams = departmentParams._value
  if (!validateFields(mutableUserParams)) {
    return;
  }
  //
  // let id = mutableUserParams[0]
  // let fullName = mutableUserParams[1]
  // let username = mutableUserParams[2]
  // let email = mutableUserParams[3]
  // let department = mutableUserParams[4]
  // let role = mutableUserParams[5]
  // let workSpace = mutableUserParams[6]
  // let password = $("input[name=password]")[0].value
  //
  // let splittedName = fullName.split(' ')
  // let firstName = splittedName[1];
  // let lastName = splittedName[0];
  // let middleName = splittedName[2];
  //
  // let json = {
  //   firstName: firstName,
  //   lastName: lastName,
  //   middleName: middleName,
  //   username: username,
  //   email: email,
  //   password: password
  // }
  //
  //
  // fetch(url + "/" + props.data[2], {
  //   method: "PATCH",
  //   headers: {
  //     'Content-Type': 'application/json'
  //   },
  //   body: JSON.stringify(json)
  // })
  //     .then(data => data.json())
  //     .then(json => {
  //       $emit('success')
  //       $emit('reloadTable')
  //     })
  //     .catch(ev => {
  //       $emit('fail')
  //     })


  close()
}

function validateFields(departmentParams) {
  let id = departmentParams[0]
  let name = departmentParams[1].trim()
  let user = departmentParams[2]

  if (id !== props.data[0]) {
    isIdCorrect.value = false;
  }

  if (name === '' || name.length < 1 || name.length > 50) {
    isNameCorrect.value = false;
  }

  // if (typeof username === "undefined" || username.length < 5 || username.length > 30
  //     || !/^[A-Za-z0-9]+$/.test(username)) {
    isUserCorrect.value = false
  // }

  return isIdCorrect.value && isNameCorrect.value && isUserCorrect.value
}

function deleteDepartment() {
  close()
}

function close() {
  $emit('close')
}

function clearErrors() {
  isIdCorrect.value = true;
  isNameCorrect.value = true;
  isUserCorrect.value = true;
}

function isInputCorrect(inputName) {
  return this['is' + inputName.charAt(0).toUpperCase() + inputName.slice(1) + 'Correct'];
}

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
              <div v-for="(item, index) in departmentParams" :key="index">
                <label :name="inputNames[index]" class="err-label" v-if="!isInputCorrect(inputNames[index])">
                  {{ errorLabels[index] }}</label>
                <div>
                  <label>{{ labels[index] }}</label>
                  <input
                      :name="inputNames[index]"
                      v-model="departmentParams[index]"
                      :disabled="index === 0"
                  />
                </div>
              </div>
            </div>
          </slot>
        </section>
        <footer class="modal-footer">
          <slot name="footer">
            <button
                type="button"
                @click="updateDepartment"
                v-if="action === 'update'"
                aria-label="Update modal">
              Обновить
            </button>
            <button
                type="button"
                @click="deleteDepartment"
                aria-label="Delete modal">
              Удалить
            </button>
            <button
                type="button"
                @click="close"
                aria-label="Close modal">
              Отменить
            </button>
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

</style>