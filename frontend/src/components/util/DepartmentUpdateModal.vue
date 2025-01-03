<script setup>

import {ref} from "vue";
import SearchSelect from "@/components/util/SearchSelect.vue";

const labels = {
  DEPARTMENT_ID: "ID отдела",
  DEPARTMENT_NAME: "Название отдела",
  USER_FULLNAME: "ФИО начальника"
}

const errorLabels = [
  "ID неизменяем",
  "Название отдела: не менее 1 и не более 50 символов",
]

const url = "http://localhost:8080/api/v1/departments"

let isDepartmentIdCorrect = ref(true)
let isDepartmentNameCorrect = ref(true)

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
  usersMap: {
    type: Array
  }
})


const selectedUserId = ref(-1)

function updateDepartment() {
  clearErrors()

  if (!validateFields()) {
    return;
  }

  let id = props.data[0]
  let name = props.data[1]
  let userId = selectedUserId.value

  let jsonObj = {
    id: id,
    name: name,
    boss: {
      id: userId
    }
  }

  fetch(url + "/" + jsonObj.id, {
    method: "PATCH",
    body: JSON.stringify(jsonObj),
    headers: {
      "Content-Type": "application/json"
    }
  })
      .then(data => data.json())
      .then(json => {
            if (json.status === 500) {
              $emit('fail')
            } else {
              $emit('success')
              $emit('reloadTable')
            }
          }
      )
      .catch((err) => {
        $emit("fail")
        console.log(err)
      })

  close()
}

function validateFields() {

  let id = props.data[0]
  let name = props.data[1]

  if (id !== props.data[0]) {
    isDepartmentIdCorrect.value = false;
  }

  if (name === '' || name.length < 1 || name.length > 50) {
    isDepartmentNameCorrect.value = false;
  }

  return isDepartmentIdCorrect.value && isDepartmentNameCorrect.value
}

const isDeleteConfirmation = ref(false)

function deleteDepartment() {
  isDeleteConfirmation.value = true
}

function confirmDeleteDepartment() {
  let id = props.data[0]

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
  $emit('close')
}

function clearErrors() {
  isDepartmentIdCorrect.value = true;
  isDepartmentNameCorrect.value = true;
}

function isInputCorrect(inputName) {
  return this['is' + inputName.charAt(0).toUpperCase() + inputName.slice(1) + 'Correct'];
}

function returnIdCallback(id) {
  selectedUserId.value = id
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
              <div>
                <div>
                  <label>{{ labels.USER_FULLNAME }}</label>
                  <SearchSelect :options="props.usersMap" @return-id="returnIdCallback"
                                :default-value="{id: props.data[2], name: props.data[3]}"/>
                </div>
              </div>
              <div>
                <label :name="'departmentId'" class="err-label"
                       v-if="!isInputCorrect('departmentId')">
                  {{ errorLabels[0] }}</label>
                <div>
                  <label>{{ labels.DEPARTMENT_ID }}</label>
                  <input
                      :name="'departmentId'"
                      v-model="props.data[0]"
                      disabled="disabled"
                  />
                </div>
              </div>
              <div>
                <label :name="'departmentName'" class="err-label"
                       v-if="!isInputCorrect('departmentName') && index < 2">
                  {{ errorLabels[index] }}</label>
                <div>
                  <label>{{ labels.DEPARTMENT_NAME }}</label>
                  <input
                      :name="'departmentName'"
                      v-model="props.data[1]"
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
                class="confirm-deletion"
                type="button"
                @click="confirmDeleteDepartment"
                aria-label="Delete modal"
                v-if="isDeleteConfirmation">
              Подтвердить удаление
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