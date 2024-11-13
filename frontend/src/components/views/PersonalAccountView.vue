<script setup>
import Utils from "@/scripts/Utils.js";
import {onMounted, ref} from "vue";
import SettingsView from "@/components/views/SettingsView.vue";
import CurrentOffice from "@/components/views/CurrentOffice.vue";
import OfficePeople from "@/components/views/OfficePeople.vue";
import OfficeManagement from "@/components/views/OfficeManagement.vue";

//account check fetch
Utils.redirectToAuthIfNotAuthorized()

const token = ref(Utils.getCookie('_at'))
const parsedToken = ref(Utils.parseJwt(token.value))

const isSettings = ref(true)
const isCurrentOffice = ref(false)
const isOfficePeople = ref(false)
const isOfficeManagement = ref(false)

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
      case "currentOffice": {
        prevTarget.value = false
        isCurrentOffice.value = true
        prevTarget = isCurrentOffice
        break
      }
      case "officePeople": {
        prevTarget.value = false
        isOfficePeople.value = true
        prevTarget = isOfficePeople
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
</script>

<template>

  <div class="inline-block">
    <div style="background-color: #3389DFFF; height: 100vh" id="menu-bar" v-if="parsedToken.roles[0] === 'ROLE_USER'">
      <div><img src="@/assets/images/nauchsoft_logo.png" class="block-center"></div>
      <div style="font-family: 'codropsicons'">Текущий офис:</div>
      <a href="/" class="menu-btn">На главную</a>
      <a href="#" id="settings" class="menu-btn" :class="{select: isSettings}">Настройки</a>
      <a href="#" id="currentOffice" class="menu-btn">Текущий офис</a>
      <a href="#" id="officePeople" class="menu-btn">Люди в офисе</a>
      <a href="#" id="officeManagement" class="menu-btn">Управление офисами</a>
      <a href="" @click="Utils.logout()" class="menu-btn">Выйти из аккаунта</a>
    </div>
    <div class="w-100">
      <SettingsView v-model:parsed-token="parsedToken" v-if="isSettings" />
      <CurrentOffice v-if="isCurrentOffice"/>
      <OfficePeople v-if="isOfficePeople" />
      <OfficeManagement v-if="isOfficeManagement" />
    </div>
  </div>

</template>

<style scoped>

img {
  width: 25vh;
  margin-top: 3vh;
  background-color: rgba(198, 223, 255, 0.6);
  padding: 5px;
  border-radius: 5px;
}

.menu-btn {
  //font-family: 'codropsicons';
  font-size: 1.5em;
  background-color: #7ab2e7;
  padding: 5px 0;
  width: 275px;
  text-align: center;
  cursor: pointer;
  text-decoration: none;
  color: rgb(36, 114, 189);
  margin: 5px 10px;
  font-weight: bold;
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