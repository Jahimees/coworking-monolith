<script setup>

import {computed, onMounted} from "vue";

const labels = [
  "ID", "ФИО", "Имя пользователя", "Email", "Отдел", "Роль", "Рабочее место", "Новый пароль"
]

const inputNames = [
  "id", "name", "username", "email", "department", "roles", "workspace", "password"
]

const $emit = defineEmits(['close'])

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

let userParams
onMounted(() => {
  userParams = computed(() => Array.from(props.data))
})

function updateUser() {
  console.log(userParams.value)
  close()
}

function createUser() {

  close()
}

function deleteUser() {
  close()
}

function close() {
  $emit('close')
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
              <div v-for="(item, index) in userParams" :key="index">
                <label>{{ labels[index] }}</label>
                <input
                    :name="inputNames[index]"
                    v-model="userParams[index]"
                    :disabled="index === 0"
                />
              </div>
              <div>
                <label>Новый пароль</label>
                <input name="password" type="password">
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
                aria-label="Delete modal">
              Удалить
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
          </slot>
        </footer>
      </div>
    </div>
  </transition>
</template>
<style>

#modalDescription {
  margin: 0 auto;
  display: block;
}

.modal-backdrop {
  position: fixed;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.3);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal {
  background: #FFFFFF;
  box-shadow: 2px 2px 20px 1px;
  overflow-x: auto;
  display: flex;
  flex-direction: column;
  width: 50%;
  top: 100px;
  left: auto;
  height: fit-content;
}

.modal-header,
.modal-footer {
  padding: 15px;
  display: flex;
}

.modal-header {
  font-size: 1.5em;
  border-bottom: 1px solid #eeeeee;
  color: #3082bf;
  justify-content: space-between;
}

.modal-footer {
  border-top: 1px solid #eeeeee;
  justify-content: flex-end;
}

.modal-body {
  position: relative;
  padding: 20px 10px;
}


#modal-box > div {
  display: flex;
  margin: 1em 0 0 0;
  font-size: 1.2em;
}

#modal-box > div > label {
  margin-right: 15px;
}

#modal-box input {
  margin: auto 0 0 auto
}

</style>