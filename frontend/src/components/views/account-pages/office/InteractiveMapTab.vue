<script setup>

import SearchSelect from "@/components/util/SearchSelect.vue";
import Map5 from "@/components/views/map/Map5.vue";
import {ref} from "vue";
import {map} from "@jsplumb/browser-ui";

const selectedRoom = ref(null)
const selectedWorkspace = ref(null)
const selectedOffice = ref(null)
const selectedFloor = ref(null)

const mapRef = ref(null)

const roomInputs = ref({
  id: null,
  tempId: null,
  name: null,
  width: null,
  height: null,
  status: null,
  type: null,
  color: null
})

const workspaceInputs = ref({
  name: null
})

const addRoom = () => {
  mapRef.value.addRoom();
}

function onRoomSelected(room) {
  selectedRoom.value = room
  roomInputs.value = selectedRoom.value
}

function onWorkspaceSelected(workspace) {
  selectedWorkspace.value = workspace
  workspaceInputs.value = selectedWorkspace.value
}

function onDeselectAll() {
  // selectedRoom.value = null
  //id - uid
  // name
  //width
  //height
  //status - id
  //type - id
  roomInputs.value = ref({
    id: null,
    name: null,
    width: null,
    height: null,
    status: null,
    type: null,
  })
  workspaceInputs.value = ref({
    name: null
  })

}

const exampleJSON = `
{
  "rooms": [
    {
      "id": 1,
      "name": "Room A",
      "x": 10,
      "y": 10,
      "width": 10,
      "height": 10,
      "color": "red",
      "workplaces": [
        { "id": 101, "name": "WP 1", "x": 1, "y": 1, "roomId": 1 },
        { "id": 102, "name": "WP 2", "x": 3, "y": 2, "roomId": 1 }
      ]
    }
  ]
}
`;
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
      <button class="map-management-btn">Загрузить карту</button>
      <button class="map-management-btn" style="margin: auto 0 0 10px;">Сохранить карту</button>
    </div>
  </div>
  <div class="work-flow row">
    <div class="content-container col-2">
      <h3>Создать/редактировать помещение</h3>
      <div class="input-field">
        <label>Название {{ roomInputs.name }}</label>
        <input id="room-name-input" v-model="roomInputs.name">
      </div>
      <div class="input-field">
        <label>Ширина</label>
        <input id="width-input" v-model="roomInputs.width">
      </div>
      <div class="input-field">
        <label>Длина</label>
        <input id="length-input" v-model="roomInputs.height">
      </div>
      <div class="input-field">
        <label>Статус</label>
        <SearchSelect/>
      </div>
      <div class="input-field">
        <label>Тип</label>
        <SearchSelect/>
      </div>
      <button class="manage-btn" @click="addRoom">Создать</button>
      <button class="manage-btn">Удалить</button>
      <hr>

      <h3>Создать/редактировать рабочее место</h3>
      <div class="input-field">
        <label>Название</label>
        <input id="workspace-name-input" v-model="workspaceInputs.name">
      </div>
      <div class="input-field">
        <label>Помещение</label>
        <SearchSelect/>
      </div>
      <div class="input-field">
        <label>Сотрудник</label>
        <SearchSelect/>
      </div>
      <button class="manage-btn" @click="addRoom">Создать</button>
      <button class="manage-btn">Удалить</button>
    </div>
    <div class="content-container col">
      <Map5 ref="mapRef"
          :map-data="exampleJSON"
          :room-inputs="roomInputs"
          @room-selected="onRoomSelected"
          @workspace-selected="onWorkspaceSelected"
          @deselect-all="onDeselectAll"
      />
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

.map-management-btn {
  font-size: 1em;
  padding: 10px;
  height: fit-content;
  margin-top: auto;
}

</style>