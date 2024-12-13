<script setup>

import SearchSelect from "@/components/util/SearchSelect.vue";
import Map5 from "@/components/views/map/Map5.vue";
import {onMounted, ref} from "vue";

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

const exampleJSON = ref(`
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
`);

//////////////////////SELECT FIELDS/////////////////////

///////////////////OFFICE//////////////////

onMounted(() => {
  loadOffices();
})

const offices = ref([])
const floors = ref([])

const officesSearchMap = []
let floorsSearchMap = ref([])

const selectedOfficeSearch = ref(null)
const selectedFloorSearch = ref(null)

const officesUrl = "http://localhost:8080/api/v1/offices"
const floorsUrl = "http://localhost:8080/api/v1/floors"

async function loadOffices() {
  // officesSearchMap.value = []

  await fetch(officesUrl, {
    headers: {
      "Content-Type": "application/json"
    }
  })
      .then(data => data.json())
      .then(json => {
        offices.value = json
      })

  officesSearchMap.push({id: -1, name: "Не выбран"})

  offices.value.forEach(office => {
    officesSearchMap.push({id: office.id, name: office.name})
  })
}

function onOfficeSearchChosen(officeId) {

  if (officeId === -1) {
    selectedOfficeSearch.value = null
    selectedFloorSearch.value = null;
    loadFloorsSearch()

    return
  }

  offices.value.forEach(office => {
    if (office.id === officeId) {
      selectedOfficeSearch.value = office
      floors.value = office.floors
      loadFloorsSearch()

      return
    }
  })
}

function loadFloorsSearch() {
  floorsSearchMap.value = []

  floors.value.forEach((floor) => {
    floorsSearchMap.value.push({id: floor.id, name: floor.name})
  })
}

const isOfficeChosen = ref(true)
const isFloorChosen = ref(true)

function loadMap() {
  validateOfficeAndFloor()

  if (selectedFloor.value.rooms.length === 0) {
    console.log("NULL")
  } else {

  }
}

function retrieveMapValues() {
  mapRef.value.getRooms();
}

function saveMap(rooms) {
  console.log(rooms)
}

// {
  //   "rooms": [
  //   {
  //     "id": 1,
  //     "name": "Room A",
  //     "x": 10,
  //     "y": 10,
  //     "width": 10,
  //     "height": 10,
  //     "color": "red",
  //     "workplaces": [
  //       { "id": 101, "name": "WP 1", "x": 1, "y": 1, "roomId": 1 },
  //       { "id": 102, "name": "WP 2", "x": 3, "y": 2, "roomId": 1 }
  //     ]
  //   }
  // ]
  // }

function validateOfficeAndFloor() {

  isOfficeChosen.value = typeof selectedOfficeSearch.value !== "undefined" &&
      selectedOfficeSearch.value !== null &&
      selectedOfficeSearch.value.id !== -1

  isFloorChosen.value = typeof selectedFloorSearch.value !== "undefined" &&
      selectedFloorSearch.value !== null &&
      selectedFloorSearch.value.id !== -1

  return isOfficeChosen.value && isFloorChosen.value
}

function onFloorSearchChosen(floorId) {
  floors.value.forEach(floor => {
    if (floor.id === floorId) {
      selectedFloorSearch.value = floor
      selectedFloor.value = floor
    }
  })
}

</script>

<template>
  <div class="office-select-menu">
    <div class="search-select-container" style="display: flex">
      <div class="search-select-item">
        <label>Выбрать офис</label>
        <div>
          <label class="err-label" v-if="!isOfficeChosen">Выберите офис</label>
        </div>
        <SearchSelect :options="officesSearchMap" @return-id="onOfficeSearchChosen"/>
      </div>
      <div class="search-select-item">
        <label>Выбрать этаж</label>
        <div>
          <label class="err-label" v-if="!isFloorChosen">Выберите этаж</label>
        </div>
        <SearchSelect :options="floorsSearchMap" @return-id="onFloorSearchChosen"/>
      </div>
      <button class="map-management-btn" @click="loadMap">Загрузить карту</button>
      <button class="map-management-btn" style="margin: auto 0 0 10px;" @click="retrieveMapValues">Сохранить карту</button>
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
            @return-rooms="saveMap"
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