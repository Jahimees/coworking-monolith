<script setup>

import SearchSelect from "@/components/util/SearchSelect.vue";
import {onMounted, ref} from "vue";
import Map5 from "@/components/views/map/MapManager.vue";
import BookingMap from "@/components/views/map/BookingMap.vue";


const offices = ref([])
const floors = ref([])

const officesSearchMap = []
let floorsSearchMap = ref([])

const selectedOfficeSearch = ref(null)
const selectedFloorSearch = ref(null)

const officesUrl = "http://localhost:8080/api/v1/offices"
const floorsUrl = "http://localhost:8080/api/v1/floors"

onMounted(() => {
  loadOffices();
})

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

const mapData = ref("")

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

async function loadMap() {
  if (!validateOfficeAndFloor()) {
    return
  }

  await fetch("http://localhost:8080/api/v1/floors/" + selectedFloorSearch.value.id, {
    headers: {
      "Content-Type": "application/json"
    }
  })
      .then(data => data.json())
      .then(json => mapData.value = JSON.stringify(json))

}

////////////

function onRoomSelected(room) {
  console.log(room)
}

function onWorkspaceSelected(workspace) {
  console.log(workspace)
}

function onDeselectAll() {
  console.log("deselect")
}

</script>

<template>

  <div class="view-header">
    Бронирование переговорных
  </div>

  <div class="body-container">
    <div class="content-container">
      <div id="interactive-map-tab-container">

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
            <button class="map-management-btn" style="margin: auto 0 0 10px;" @click="retrieveMapValues">Сохранить карту
            </button>
          </div>
        </div>

      </div>
      <div class="work-flow row">
        <div style="height: 77vh" class="content-container col-2">

        </div>
        <div class="content-container col">
          <BookingMap ref="mapRef"
                      :map-data="mapData"
                      @room-selected="onRoomSelected"
                      @workspace-selected="onWorkspaceSelected"
                      @deselect-all="onDeselectAll"
          />
        </div>
      </div>

    </div>

  </div>

</template>

<style scoped>
.office-select-menu {
  display: flex;
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

.body-container {
  margin: 0 auto;
  width: 98%;
}

.content-container {
  padding: 10px;
  border-radius: 10px;
  border: 2px solid #b1c5d8;
}

.work-flow {
  margin-top: 1em;
  display: flex;
}
</style>