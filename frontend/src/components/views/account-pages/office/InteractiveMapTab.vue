<script setup>

import SearchSelect from "@/components/util/SearchSelect.vue";
import Map5 from "@/components/views/map/MapManager.vue";
import {onMounted, ref} from "vue";
import Utils from "@/scripts/Utils.js";
import InfoModal from "@/components/util/InfoModal.vue";

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
  length: null,
  status: null,
  type: null,
  color: null
})

const workspaceInputs = ref({
  name: null,
  user: null,
})

const addRoom = () => {
  if (!validateFields()) {
    return
  }

  mapRef.value.addRoom();
}

////ROOM VALIDATION
const isNameCorrect = ref(true)
const isWidthCorrect = ref(true)
const isLengthCorrect = ref(true)
const isStatusCorrect = ref(true)
const isDepartmentCorrect = ref(true)
const isTypeCorrect = ref(true)

function clearErrors() {
  isNameCorrect.value = true
  isWidthCorrect.value = true
  isLengthCorrect.value = true
  isStatusCorrect.value = true
  isDepartmentCorrect.value = true
  isTypeCorrect.value = true
}

function validateFields() {
  let name = roomInputs.value.name
  let width = roomInputs.value.width
  let length = roomInputs.value.length
  let status = roomInputs.value.status
  let department = roomInputs.value.department
  let type = roomInputs.value.type

  isNameCorrect.value = !(name == null || typeof name === "undefined"
      || name.length > 40 || name.length < 2)

  isWidthCorrect.value = !(width == null || typeof width === "undefined"
      || width > 25 || width < 1)

  isLengthCorrect.value = !(length == null || typeof length === "undefined"
      || length > 25 || length < 1);

  isStatusCorrect.value = !(status == null || typeof status === "undefined")

  // isDepartmentCorrect.value = !(department == null || typeof department === "undefined")

  isTypeCorrect.value = !(type == null || typeof type === "undefined")

  return isNameCorrect.value && isWidthCorrect && isLengthCorrect && isStatusCorrect && isDepartmentCorrect
      && isTypeCorrect;
}


function onRoomSelected(room) {
  clearErrors()

  selectedRoom.value = room
  roomInputs.value = selectedRoom.value

  if (selectedRoom.value != null) {
    if (selectedRoom.value.status != null) {
      statusSelectRef.value.selectOption({id: selectedRoom.value.status.id, name: selectedRoom.value.status.name})
    }

    if (selectedRoom.value.type != null) {
      typeSelectRef.value.selectOption({id: selectedRoom.value.type.id, name: selectedRoom.value.type.name})
    }

    if (selectedRoom.value.department != null) {
      departmentSelectRef.value.selectOption({
        id: selectedRoom.value.department.id,
        name: selectedRoom.value.department.name
      })
    } else {
      departmentSelectRef.value.selectOption({
        id: -1,
        name: "Не выбрано"
      })
    }
  }
}

function onWorkspaceSelected(workspace) {
  selectedWorkspace.value = workspace
  workspaceInputs.value = selectedWorkspace.value;

  if (selectedWorkspace.value != null) {
    if (selectedWorkspace.value.user != null) {
      userSelectRef.value.selectOption({
        id: selectedWorkspace.value.user.id,
        name: Utils.getUserFullName(selectedWorkspace.value.user)
      })
    } else {
      userSelectRef.value.selectOption({id: -1, name: "Не выбрано"})
    }
  }
}

function onDeselectAll() {
  clearErrors()
  statusSelectRef.value.selectOption(null)
  typeSelectRef.value.selectOption(null)
  departmentSelectRef.value.selectOption(null)
  userSelectRef.value.selectOption(null)
  roomInputs.value = ref({})
  workspaceInputs.value = ref({
    name: null
  })

}

//////////////////////SELECT FIELDS/////////////////////

///////////////////OFFICE//////////////////

onMounted(() => {
  loadOffices();
  loadStatuses();
  loadTypes();
  loadDepartments();
  loadUsers()
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

const mapData = ref("")

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

function retrieveMapValues() {
  mapRef.value.getRooms();
}

const saveMapUrl = "http://localhost:8080/api/v1/maps"

async function saveMap(rooms) {
  if (!validateOfficeAndFloor()) {
    return
  }

  if (!validateWorkspaces(rooms.value)) {
    return
  }

  let jsonForSend = {
    officeId: selectedOfficeSearch.value.id,
    floorId: selectedFloorSearch.value.id,
    rooms: rooms.value
  }

  await fetch(saveMapUrl, {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(jsonForSend)
  })
      .then(data => data.json())
      .then(json => onSuccessMapUpdate("Карта сохранена успешно"))
      .catch(er => {
        onFailMapUpdate("Не удалось сохранить карту")
      })

}

function validateWorkspaces(rooms) {
  let roomNames = []

  rooms.forEach(room => {

    let length = room.length
    let width = room.width;
    let square = length * width;

    let workspaceSquareRequired = (room.workspaces.length) * 4.5;

    if (square < workspaceSquareRequired) {
      roomNames.push(room.name)
    }
  })

  if (roomNames.length != 0) {
    infoTitle.value = "Ошибка"
    infoMessage.value = "Площадь помещений: " + roomNames.map(name => "'" + name + "'") + " не расчитана на добавление еще одного рабочего места"

    openInfoModal()
    return false;
  }

  return true
}

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

/////////////////SELECT FIELDS STATUS TYPE///////////////////

const statuses = ref([])
let statusesSearchMap = []

const statusesUrl = "http://localhost:8080/api/v1/room-statuses"

async function loadStatuses() {
  await fetch(statusesUrl, {
    headers: {
      "Content-Type": "application/json"
    }
  })
      .then(data => data.json())
      .then(json => {
        statuses.value = json
      })

  if (typeof statuses.value !== "undefined") {
    statuses.value.forEach(status => {
      statusesSearchMap.push({id: status.id, name: status.name})
    })
  }
}

function onStatusSelected(statusId) {
  let chosenStatus
  statuses.value.forEach(status => {
    if (statusId === status.id) {
      chosenStatus = status
    }
  })

  if (roomInputs.value == null) {
    return;
  }

  if (chosenStatus == null || typeof chosenStatus === "undefined") {
    roomInputs.value.status = null
    return
  }

  roomInputs.value.status = chosenStatus
}


/////////////////SELECT FIELDS TYPE///////////////////

const types = ref([])
let typesSearchMap = []

const typesUrl = "http://localhost:8080/api/v1/room-types"

async function loadTypes() {
  await fetch(typesUrl, {
    headers: {
      "Content-Type": "application/json"
    }
  })
      .then(data => data.json())
      .then(json => {
        types.value = json
      })

  if (typeof types.value !== "undefined") {
    types.value.forEach(type => {
      typesSearchMap.push({id: type.id, name: type.name})
    })
  }
}

function onTypeSelected(typeId) {
  let chosenType
  types.value.forEach(type => {
    if (typeId === type.id) {
      chosenType = type
    }
  })

  if (roomInputs.value == null) {
    return;
  }

  if (chosenType == null || typeof chosenType === "undefined") {
    roomInputs.value.type = null
    return
  }

  roomInputs.value.type = chosenType
}

//////////////////////DEPARTMENTS

const departments = ref([])
let departmentsSearchMap = []

const departmentsUrl = "http://localhost:8080/api/v1/departments"

async function loadDepartments() {
  await fetch(departmentsUrl, {
    headers: {
      "Content-Type": "application/json"
    }
  })
      .then(data => data.json())
      .then(json => {
        departments.value = json
      })

  departmentsSearchMap.push({id: -1, name: "Не выбрано"})
  if (typeof departments.value !== "undefined") {
    departments.value.forEach(department => {
      departmentsSearchMap.push({id: department.id, name: department.name})
    })
  }

}

function onDepartmentSelected(departmentId) {
  let chosenDepartment
  departments.value.forEach(department => {
    if (departmentId === department.id) {
      chosenDepartment = department
    }
  })

  if (roomInputs.value == null) {
    return;
  }

  if (chosenDepartment == null || typeof chosenDepartment === "undefined") {
    roomInputs.value.department = null
    return
  }

  roomInputs.value.department = chosenDepartment
}

const statusSelectRef = ref({})
const typeSelectRef = ref({})
const departmentSelectRef = ref({})
const userSelectRef = ref({})

const isRoomSelected = ref(true)
const isWorkspaceNameCorrect = ref(true)

function onNoRoomSelected() {
  isRoomSelected.value = false
}

function addWorkspace() {
  isRoomSelected.value = true

  if (workspaceInputs.value.name == null ||
      typeof workspaceInputs.value.name === "undefined" ||
      workspaceInputs.value.name.trim().length > 20
      || workspaceInputs.value.name.trim().length < 2) {
    isWorkspaceNameCorrect.value = false
    return
  }

  isWorkspaceNameCorrect.value = true
  mapRef.value.addWorkspace()
}


/////////////////////LOAD USERS


const users = ref([])
let usersSearchMap = []

const usersUrl = "http://localhost:8080/api/v1/users?without_workspace=false"

async function loadUsers() {
  await fetch(usersUrl, {
    headers: {
      "Content-Type": "application/json"
    }
  })
      .then(data => data.json())
      .then(json => {
        users.value = json
      })

  usersSearchMap.push({id: -1, name: "Не выбрано"})
  if (typeof users.value !== "undefined") {
    users.value.forEach(user => {
      let userFullName = Utils.getUserFullName(user)

      usersSearchMap.push({id: user.id, name: userFullName})
    })
  }
}

function onUserSelected(userId) {
  let chosenUser
  users.value.forEach(user => {
    if (userId === user.id) {
      chosenUser = user
    }
  })

  if (workspaceInputs.value == null) {
    return;
  }

  if (chosenUser == null || typeof chosenUser === "undefined") {
    workspaceInputs.value.user = null
    return
  }

  workspaceInputs.value.user = chosenUser
}

//MODAL

const isInfoModalVisible = ref(false)
const infoTitle = ref("")
const infoMessage = ref("")

function onSuccessMapUpdate(text) {
  infoTitle.value = "Успех"
  if (typeof text === "undefined" || text === null || text === "") {
    infoMessage.value = "Действие выполнено успешно"
  } else {
    infoMessage.value = text
  }

  openInfoModal()
}

function onFailMapUpdate(text) {
  infoTitle.value = "Ошибка"
  if (typeof text === "undefined" || text === "" || text === null) {
    infoMessage.value = "Произошла ошибка сохранения"
  } else {
    infoMessage.value = text;
  }
  openInfoModal()
}

function openInfoModal() {
  isInfoModalVisible.value = true;
}

function closeInfoModal() {
  isInfoModalVisible.value = false;
}

function onDeleteRoom() {
  mapRef.value.deleteRoom()
}

function onDeleteWorkspace() {
  mapRef.value.deleteWorkspace()
}

function onNotEnoughPlace() {
  infoTitle.value = "Ошибка"
  if (typeof text === "undefined" || text === "" || text === null) {
    infoMessage.value = "Площадь помещения не расчитана на добавление еще одного рабочего места"
  } else {
    infoMessage.value = text;
  }
  openInfoModal()
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
      <button class="map-management-btn" style="margin: auto 0 0 10px;" @click="retrieveMapValues">Сохранить карту
      </button>
    </div>
  </div>
  <div class="work-flow row">
    <div class="content-container col-2">
      <h3>Создать/редактировать помещение</h3>
      <div class="input-field">
        <label>Название</label>
        <label v-if="!isNameCorrect" class="err-label">От 2 до 40 символов</label>
        <input id="room-name-input" v-model="roomInputs.name">
      </div>
      <div class="input-field">
        <label>Ширина</label>
        <label v-if="!isWidthCorrect" class="err-label">1 < x < 25</label>
        <input id="width-input" v-model="roomInputs.width" type="number">
      </div>
      <div class="input-field">
        <label>Длина</label>
        <label v-if="!isLengthCorrect" class="err-label">1 < x < 25</label>
        <input id="length-input" v-model="roomInputs.length" type="number">
      </div>
      <div class="input-field">
        <label>Статус</label>
        <label v-if="!isStatusCorrect" class="err-label">Выберите статус</label>
        <SearchSelect ref="statusSelectRef" :options="statusesSearchMap" @return-id="onStatusSelected"/>
      </div>
      <div class="input-field">
        <label>Отдел</label>
        <label v-if="!isDepartmentCorrect" class="err-label">Выберите отдел</label>
        <SearchSelect ref="departmentSelectRef" :options="departmentsSearchMap" @return-id="onDepartmentSelected"
                      :default-value="{id: -1, name:'Не выбрано'}"/>
      </div>
      <div class="input-field">
        <label>Тип</label>
        <label v-if="!isTypeCorrect" class="err-label">Выберите тип</label>
        <SearchSelect ref="typeSelectRef" :options="typesSearchMap" @return-id="onTypeSelected"/>
      </div>
      <button class="manage-btn" @click="addRoom">Создать</button>
      <button class="manage-btn" @click="onDeleteRoom">Удалить</button>
      <hr>

      <h3>Создать/редактировать рабочее место</h3>
      <label class="err-label" v-if="!isRoomSelected">Выберите помещение</label>
      <div class="input-field">
        <label>Название</label>
        <label class="err-label" v-if="!isWorkspaceNameCorrect">От 2 до 20 символов</label>
        <input id="workspace-name-input" v-model="workspaceInputs.name">
      </div>
      <div class="input-field">
        <label>Сотрудник</label>
        <SearchSelect ref="userSelectRef" :options="usersSearchMap" @return-id="onUserSelected"
                      :default-value="{id: -1, name: 'Не выбрано'}"/>
      </div>
      <button class="manage-btn" @click="addWorkspace">Создать</button>
      <button class="manage-btn" @click="onDeleteWorkspace">Удалить</button>
    </div>
    <div class="content-container col">
      <Map5 ref="mapRef"
            :map-data="mapData"
            :room-inputs="roomInputs"
            :workspace-inputs="workspaceInputs"
            @room-selected="onRoomSelected"
            @workspace-selected="onWorkspaceSelected"
            @deselect-all="onDeselectAll"
            @return-rooms="saveMap"
            @no-room-selected="onNoRoomSelected"
            @not-enough-place="onNotEnoughPlace"
      />
    </div>
  </div>

  <InfoModal :title="infoTitle" :message="infoMessage" v-show="isInfoModalVisible"
             @close="closeInfoModal" @confirm="closeInfoModal"/>
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

.err-label {
  font-size: 0.6em;
  margin-left: 5px;
}

</style>