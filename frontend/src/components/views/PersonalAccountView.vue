<script setup>
import Utils from "@/scripts/Utils.js";
import {onMounted, ref} from "vue";
import SettingsView from "@/components/views/SettingsView.vue";
import DepartmentManagement from "@/components/views/DepartmentManagement.vue";
import OfficeManagement from "@/components/views/OfficeManagement.vue";
import ManagePeople from "@/components/views/PeopleManagement.vue";
import Modal from "@/components/util/UserUpdateModal.vue";

//account check fetch
Utils.redirectToAuthIfNotAuthorized()

const token = ref(Utils.getCookie('_at'))
const parsedToken = ref(Utils.parseJwt(token.value))

const isSettings = ref(true)
const isPeopleManagement = ref(false)
const isDepartmentManagement = ref(false)
const isOfficeManagement = ref(false)

const isModalVisible = ref(false)

var prevTarget = isSettings

onMounted(() => {

  const $menuBtn = $(".menu-btn");
  $menuBtn.unbind()

  $menuBtn.on("click", (e) => {
    [...$menuBtn].forEach((el) => {
      el.className = "menu-btn"
    })

    switch (e.currentTarget.id) {
      case "settings": {
        prevTarget.value = false
        isSettings.value = true
        prevTarget = isSettings
        break
      }
      case "peopleManagement": {
        prevTarget.value = false
        isPeopleManagement.value = true
        prevTarget = isPeopleManagement
        break
      }
      case "departmentManagement": {
        prevTarget.value = false
        isDepartmentManagement.value = true
        prevTarget = isDepartmentManagement
        break
      }
      case "officeManagement": {
        prevTarget.value = false
        isOfficeManagement.value = true
        prevTarget = isOfficeManagement
        break
      }
    }

    e.currentTarget.className = "menu-btn select"
  })

})

function openModal() {
  isModalVisible.value = true
}

function closeModal() {
  isModalVisible.value = false
}
</script>

<template>

  <div class="inline-block">
    <div id="menu-bar" v-if="parsedToken.roles[0] === 'ROLE_USER'">
      <div><img src="@/assets/images/nauchsoft_logo.png" class="block-center"></div>
      <div style="font-family: 'codropsicons'">Текущий офис:</div>
      <a href="/" class="menu-btn">На главную</a>
      <a href="#" id="settings" class="menu-btn" :class="{select: isSettings}">Настройки</a>
      <a href="#" id="peopleManagement" class="menu-btn">Управление сотрудниками</a>
      <a href="#" id="departmentManagement" class="menu-btn">Управление отделами</a>
      <a href="#" id="officeManagement" class="menu-btn">Управление офисами</a>
      <a href="" @click="Utils.logout()" class="menu-btn">Выйти из аккаунта</a>
    </div>
    <div class="w-100">
      <SettingsView v-model:parsed-token="parsedToken" v-if="isSettings"/>
      <ManagePeople v-if="isPeopleManagement"/>
      <DepartmentManagement v-if="isDepartmentManagement"/>
      <OfficeManagement v-if="isOfficeManagement"/>
    </div>
  </div>

  <Modal v-show="isModalVisible"
         @close="closeModal"/>

  <button @click="openModal">OPEN MODAL</button>

</template>

<style scoped>

#menu-bar {
  background-color: #1b5a9a;
  height: 100vh;
}

img {
  width: 25vh;
  margin-top: 3vh;
  background-color: rgba(198, 223, 255, 0.6);
  padding: 5px;
  border-radius: 5px;
}

.menu-btn {
  font-size: 1em;
  text-align: left;
  background-color: #5e9dd6;
  padding: 5px 10px;
  cursor: pointer;
  text-decoration: none;
  color: #e3e3e3;
  margin: 5px 10px;
}

.menu-btn:hover {
  background-color: #3082bf;
  color: white;
}

.menu-btn.select {
  background-color: #3082bf;
  color: white;
}

a {
  display: block;
}

body {
  min-height: 0;
}
</style>