<script setup>
import {onMounted, ref} from "vue";
import initFunc from "@/scripts/Neurolinks.js";
import Utils from "@/scripts/Utils.js";

const isAuthenticated = ref(Utils.getCookie('_at') !== '')
console.log(isAuthenticated.value)

onMounted(() => {
  initFunc.initAll()
})

function logout() {
  Utils.logout();
  isAuthenticated.value = !isAuthenticated.value
}
</script>

<template>
  <header id="large-header" class="large-header">
    <canvas id="demo-canvas"></canvas>
    <div class="center-header-block">
      <h1>NAUCHSOFT Office</h1>
      <div>Управление офисным пространством</div>
    </div>
    <div id="logo">
    <img src="@/assets/images/nauchsoft_logo.png">
    </div>

    <div class="right-header-block" v-if="isAuthenticated">
      <a class="header-btn" href="/account">
        Личный кабинет
      </a>
      <a class="header-btn" href="/#" @click.prevent="logout">
        Выйти из аккаунта
      </a>
    </div>
    <div class="right-header-block" v-if="!isAuthenticated">
      <a class="header-btn" href="/auth">
        Авторизация
      </a>
      <a class="header-btn" href="/reg">
        Регистрация
      </a>
    </div>
  </header>
</template>

<style scoped>
 #logo {
   width: 40vh;
   position: absolute;
   margin: 0;
   padding: 0;
   color: #f9f1e9;
   text-align: center;
   top: 50%;
   right: 10%;
   -webkit-transform: translate3d(-50%, -50%, 0);
   transform: translate3d(-50%, -50%, 0);
 }

 img {
   width: 40vh;
   background-color: #ffffff;
   padding: 1.5vh;
   border-radius: 10px;
   transition: 0.3s;
 }

 img:hover {
   width: 45vh;
   transition: 0.3s;
 }
</style>