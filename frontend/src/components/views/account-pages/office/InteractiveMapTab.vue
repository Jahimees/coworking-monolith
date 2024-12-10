<script setup>

import SearchSelect from "@/components/util/SearchSelect.vue";
import Map3 from "@/components/views/map/Map3.vue";
import Map2 from "@/components/views/map/Map2.vue";
</script>

<template>
  <div class="office-select-menu">
    <div class="search-select-container" style="display: flex">
      <div class="search-select-item">
        <label>Выбрать офис</label>
        <SearchSelect/>
      </div>
      <div class="search-select-item">
        <label>Выбрать этаж</label>
        <SearchSelect/>
      </div>
    </div>
  </div>
  <div class="work-flow row">
    <div class="content-container col-2">
      <h3>Создать/редактировать помещение</h3>
      <div class="input-field">
        <label>Название</label>
        <input id="lastname-input" v-model="lastNameValue">
      </div>
      <div class="input-field">
        <label>Ширина</label>
        <input id="lastname-input" v-model="lastNameValue">
      </div>
      <div class="input-field">
        <label>Длина</label>
        <input id="lastname-input" v-model="lastNameValue">
      </div>
      <div class="input-field">
        <label>Статус</label>
        <SearchSelect/>
      </div>
      <div class="input-field">
        <label>Тип</label>
        <SearchSelect/>
      </div>
      <button class="manage-btn">Сохранить</button>
      <button class="manage-btn">Удалить</button>
      <hr>

      <h3>Создать/редактировать рабочее место</h3>
      <div class="input-field">
        <label>Название</label>
        <input id="lastname-input" v-model="lastNameValue">
      </div>
      <div class="input-field">
        <label>Помещение</label>
        <SearchSelect/>
      </div>
      <div class="input-field">
        <label>Сотрудник</label>
        <SearchSelect/>
      </div>
      <button class="manage-btn">Сохранить</button>
      <button class="manage-btn">Удалить</button>
    </div>
    <div class="content-container col">
      <Map2/>
    </div>
  </div>
</template>

<style scoped>
.office-select-menu {
  display: flex;
}

.work-flow {
  margin-top: 1em;
  display: flex;
}

.content-container {
  padding: 10px;
  border-radius: 10px;
  border: 2px solid #b1c5d8;
  margin: 0 0.5em;
  display: block;
}

h3 {
  text-align: center;
  font-size: 1.2em;
}

label {
  font-size: 1em;
}

.input-field > input {
  width: 100%;
}

.input-field {
  padding: 0;
  margin: 0;
}

.manage-btn {
  margin-right: 0.2em;
}

.search-select-container {
  display: flex;
}

.search-select-item {
  margin-right: 0.5em;
}

</style>