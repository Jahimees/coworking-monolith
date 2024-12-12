<script setup>

import {onMounted, ref} from "vue";
import DataTableUtils from "@/scripts/DataTableUtils.js";
import DataTable from "datatables.net-dt";
import InfoModal from "@/components/util/InfoModal.vue";

////////////////////////OFFICES//////////////////

const officeErrorLabel = "Название офиса должно содержать от 4 до 50 символов"
const isOfficeNameValidV = ref(true)

const selectedOffice = ref({
  id: null
})

const officesColumnDefs = [{visible: false}, {width: '50%'}, {visible: '50%'}]

let offices = ref([])

onMounted(() => {
  console.log("NALSF")
  initOfficesDataTable()
  initFloorsDataTable(-1)
})

async function initOfficesDataTable() {
  await fetch("http://localhost:8080/api/v1/offices")
      .then(data => data.json())
      .then(json => {
        offices = json
      })

  console.log(offices)

  DataTableUtils.initDataTable("offices", officesColumnDefs)

  const $dataTable = fillOfficesTable(offices)
  initCellClickOfficesEventListener($dataTable)
}

function fillOfficesTable(officesJson) {
  const $dataTable = new DataTable("#offices_table");

  officesJson.forEach((office) => {


    $dataTable.row.add([
      office.id,
      office.name,
      office.address,
    ]).draw(false)
  })

  return $dataTable
}

function initCellClickOfficesEventListener($dataTable) {
  $dataTable.off('click').on('click', 'tbody tr', function (e) {
    let rowData = $dataTable.row(this).data();
    selectedOffice.value.id = rowData[0];
    selectedOffice.value.name = rowData[1];
    selectedOffice.value.address = rowData[2];

    reloadFloorsTable()
  });
}

function isSelectedOfficeNull() {
  return selectedOffice.value.id == null
}

function setSelectedOfficeNull() {
  selectedOffice.value.id = null;
  selectedOffice.value.name = null;
  selectedOffice.value.address = null;
}

function isOfficeNameValid() {
  let isValid = selectedOffice.value.name != null
      && selectedOffice.value.name.length >= 4
      && selectedOffice.value.name.length <= 50


  isOfficeNameValidV.value = isValid;

  return isValid
}

const officesUrl = "http://localhost:8080/api/v1/offices"

async function createOffice() {
  if (!isOfficeNameValid()) {
    return
  }

  if (selectedOffice.value.id == null) {
    let officeJson = {
      name: selectedOffice.value.name,
      address: selectedOffice.value.address

    }

    let createdOffice

    await fetch(officesUrl, {
      method: "POST",
      body: JSON.stringify(officeJson),
      headers: {
        "Content-Type": "application/json"
      },
      crossDomain: true
    })
        .then(data => data.json())
        .then(json => createdOffice = json)
        .catch(e => {
          openErrorInfoModal()
        })

    reloadOfficesTable()

  } else {
    openErrorInfoModal()
  }
}

function reloadOfficesTable() {
  DataTableUtils.destroyDataTable("offices")
  initOfficesDataTable()
}

async function saveOfficeInfo() {
  const url = "http://localhost:8080/api/v1/offices"

  if (selectedOffice.value.id != null) {
    let officeJson = {
      id: selectedOffice.value.id,
      name: selectedOffice.value.name,
      address: selectedOffice.value.address
    }

    let savedOffice

    await fetch(url + "/" + selectedOffice.value.id, {
      method: "PUT",
      body: JSON.stringify(officeJson),
      headers: {
        "Content-Type": "application/json"
      },
      crossDomain: true
    })
        .then(data => data.json())
        .then(json => savedOffice = json)
        .catch(e => {
          openErrorInfoModal()
        })

    reloadOfficesTable()

  } else {
    openErrorInfoModal()
  }
}

function deleteOfficeInfo() {
  infoTitle.value = "Подтвердите действие";
  infoMessage.value = "Вы действительно хотите удалить офис '" + selectedOffice.value.name + "'? " +
      "Это приведет к удалению карты, объектов помещений и всех текущих броней.";
  openInfoModal()
  currentAction.value = () => confirmDeleteOffice()
}

function confirmDeleteOffice() {
  const id = selectedOffice.value.id

  fetch(officesUrl + "/" + id, {
    method: "DELETE"
  }).then(data => {
    closeInfoModal()
    openSuccessInfoModal()
    setSelectedOfficeNull()
    setSelectedFloorNull()
    reloadOfficesTable()
    reloadFloorsTable()

  }).catch(er => {
    closeInfoModal()
    openErrorInfoModal();
  })
}

////////////////INFO MODAL/////////////

const infoTitle = ref(null)
const infoMessage = ref(null)

const isInfoModalVisible = ref(false)

function openInfoModal() {
  isInfoModalVisible.value = true;
}

function closeInfoModal() {
  isInfoModalVisible.value = false;
}


function openSuccessInfoModal() {
  infoTitle.value = "Действие прошло успешно";
  infoMessage.value = "Объект успешно удален";
  currentAction.value = () => closeInfoModal()
  openInfoModal()
}

function openErrorInfoModal() {
  infoTitle.value = "Что-то пошло не так!";
  infoMessage.value = "Не удалось совершить действие с объектом";
  currentAction.value = () => closeInfoModal()
  openInfoModal()
}

const currentAction = ref(() => {
})

/////////////////////////////FLOORS////////////////////////////


const floorErrorLabel = "Название этажа должно содержать от 4 до 50 символов"
const isFloorNameValidV = ref(true)

const selectedFloor = ref({
  id: null
})

const floorsColumnDefs = [{visible: false}, {visible: false}, {width: '50%'}, {visible: '50%'}]

let floors = ref([])

async function initFloorsDataTable(officeId) {
  await fetch("http://localhost:8080/api/v1/offices/" + officeId + "/floors")
      .then(data => data.json())
      .then(json => {
        floors = json
      })

  DataTableUtils.initDataTable("floors", floorsColumnDefs)

  const $dataTable = fillFloorsTable(floors)
  initCellClickFloorsEventListener($dataTable)
}

function fillFloorsTable(floorsJson) {
  const $dataTable = new DataTable("#floors_table");

  floorsJson.forEach((floor) => {

    console.log(floor)

    $dataTable.row.add([
      floor.id,
      floor.office.id,
      floor.name,
      floor.description,
    ]).draw(false)
  })

  return $dataTable
}

function initCellClickFloorsEventListener($dataTable) {
  $dataTable.off('click').on('click', 'tbody tr', function (e) {
    let rowData = $dataTable.row(this).data();
    selectedFloor.value.id = rowData[0];
    selectedFloor.value.officeId = rowData[1];
    selectedFloor.value.name = rowData[2];
    selectedFloor.value.description = rowData[3];
  });
}

function isSelectedFloorNull() {
  return selectedFloor.value.id == null
}

function setSelectedFloorNull() {
  selectedFloor.value.id = null;
  selectedFloor.value.officeId = null;
  selectedFloor.value.name = null;
  selectedFloor.value.description = null;
}

function isFloorNameValid() {
  let isValid = selectedFloor.value.name != null
      && selectedFloor.value.name.length >= 4
      && selectedFloor.value.name.length <= 50

  isFloorNameValidV.value = isValid;

  return isValid
}

const floorsUrl = "http://localhost:8080/api/v1/floors"

async function createFloor() {
  if (!isFloorNameValid()) {
    return
  }

  if (selectedFloor.value.id == null) {
    let floorJson = {
      name: selectedFloor.value.name,
      description: selectedFloor.value.description,
      office: {
        id: selectedOffice.value.id
      }
    }

    let createdFloor

    await fetch(floorsUrl, {
      method: "POST",
      body: JSON.stringify(floorJson),
      headers: {
        "Content-Type": "application/json"
      },
      crossDomain: true
    })
        .then(data => data.json())
        .then(json => createdFloor = json)
        .catch(e => {
          openErrorInfoModal()
        })

    reloadFloorsTable()

  } else {
    openErrorInfoModal()
  }
}


function reloadFloorsTable() {
  DataTableUtils.destroyDataTable("floors")
  initFloorsDataTable(selectedOffice.value.id)
}

async function saveFloorInfo() {

  if (selectedFloor.value.id != null) {

    let floorJson = {
      id: selectedFloor.value.id,
      name: selectedFloor.value.name,
      description: selectedFloor.value.description,
      office: {
        id: selectedFloor.value.officeId
      }
    }

    let savedFloor

    await fetch(floorsUrl + "/" + selectedFloor.value.id, {
      method: "PUT",
      body: JSON.stringify(floorJson),
      headers: {
        "Content-Type": "application/json"
      },
      crossDomain: true
    })
        .then(data => data.json())
        .then(json => savedFloor = json)
        .catch(e => {
          openErrorInfoModal()
        })

    reloadFloorsTable()

  } else {
    openErrorInfoModal()
  }
}

function deleteFloorInfo() {
  infoTitle.value = "Подтвердите действие";
  infoMessage.value = "Вы действительно хотите удалить этаж '" + selectedFloor.value.name + "'? " +
      "Это приведет к удалению карты, объектов помещений и всех текущих броней.";
  openInfoModal()
  currentAction.value = () => confirmDeleteFloor()
}

function confirmDeleteFloor() {
  const id = selectedFloor.value.id

  fetch(floorsUrl + "/" + id, {
    method: "DELETE"
  }).then(data => {
    closeInfoModal()
    openSuccessInfoModal()
    reloadFloorsTable()

  }).catch(er => {
    closeInfoModal()
    openErrorInfoModal();
  })
}

</script>

<template>
  <div class="body-container">
    <div class="row">
      <div class="data-table-container col-7">
        <table id="offices_table" class="display">
          <thead>
          <tr>
            <th>officeId</th>
            <th>Название офиса</th>
            <th>Адрес офиса</th>
          </tr>
          </thead>
        </table>
      </div>
      <div class="field-form-v col-4">

        <div class="creation-box">

          <h3 v-if="isSelectedOfficeNull()">Создать новый офис</h3>
          <h3 v-else>Редактировать офис</h3>
          <button class="save-btn" style="font-size: 1em" v-if="!isSelectedOfficeNull()"
                  @click="setSelectedOfficeNull()">
            Переключить на создание
          </button>
          <input v-model="selectedOffice.id" hidden>
          <label>Название офиса</label>
          <div>
            <label class="err-label" v-if="!isOfficeNameValidV">
              {{ officeErrorLabel }}
            </label>
          </div>
          <input v-model="selectedOffice.name">
          <label>Адрес офиса</label>
          <input v-model="selectedOffice.address">
          <button v-if="isSelectedOfficeNull()" class="save-btn" @click="createOffice">Создать</button>
          <div v-else style="display: flex;">
            <button class="save-btn" @click="saveOfficeInfo">Сохранить</button>
            <button class="save-btn" @click="deleteOfficeInfo">Удалить</button>
          </div>
        </div>

      </div>
    </div>
    <div class="row" style="margin-top: 2em">
      <div class="data-table-container col-7">
        <table id="floors_table" class="display">
          <thead>
          <tr>
            <th>floorId</th>
            <th>officeId</th>
            <th>Название этажа</th>
            <th>Описание</th>
          </tr>
          </thead>
        </table>
      </div>
      <div class="field-form-h col-4">

        <div class="creation-box" v-if="!isSelectedOfficeNull()">

          <h3 v-if="isSelectedFloorNull()">Создать новый этаж</h3>
          <h3 v-else>Редактировать этаж</h3>
          <button class="save-btn" style="font-size: 1em" v-if="!isSelectedFloorNull()" @click="setSelectedFloorNull()">
            Переключить на создание
          </button>
          <input v-model="selectedFloor.id" hidden>
          <input v-model="selectedFloor.officeId" hidden>
          <label>Название этажа</label>
          <div>
            <label class="err-label" v-if="!isFloorNameValidV">
              {{ officeErrorLabel }}
            </label>
          </div>
          <input v-model="selectedFloor.name">
          <label>Описание</label>

          <input v-model="selectedFloor.description">
          <button v-if="isSelectedFloorNull()" class="save-btn" @click="createFloor">Создать</button>
          <div v-else style="display: flex;">
            <button class="save-btn" @click="saveFloorInfo">Сохранить</button>
            <button class="save-btn" @click="deleteFloorInfo">Удалить</button>
          </div>
        </div>
      </div>
    </div>
  </div>

  <InfoModal v-show="isInfoModalVisible"
             :title="infoTitle"
             :message="infoMessage"
             @close="closeInfoModal"
             @confirm="currentAction"/>
</template>

<style scoped>

.body-container {
  margin: 0 auto;
  width: 98%;
}

.content-container {
  padding: 10px;
  border-radius: 10px;
  border: 2px solid #b1c5d8;
}

.data-table-container {
  margin: 0 auto;
  //width: 95%;
  padding: 10px;
  border-radius: 10px;
  border: 2px solid #b1c5d8;
}

.field-form-v {

}

input {
  width: 20em;
  display: block;
}

.creation-box {
  margin: 0 auto;
}

.save-btn {
  display: block;
  margin: 10px 10px 0 0
}
</style>