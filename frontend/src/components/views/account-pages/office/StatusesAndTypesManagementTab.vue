<script setup>

import {onMounted, ref} from "vue";
import DataTableUtils from "@/scripts/DataTableUtils.js";
import DataTable from "datatables.net-dt";
import InfoModal from "@/components/util/InfoModal.vue";

////////////////////////TYPES//////////////////

const typeNameErrorLabel = "Название офиса должно содержать от 4 до 50 символов"
const typeColorErrorLabel = "Цвет записывается в формате #AAAAAA"
const isTypeNameValidV = ref(true)
const isTypeColorValidV = ref(true)

const selectedType = ref({
  id: null,
  color: null,
  name: null
})

const typesColumnDefs = [{visible: false}, {width: '40%'}, {visible: '50%'}, {visible: '10%'}]

let types = ref([])

onMounted(() => {
  initTypesDataTable()
  // initFloorsDataTable(-1)
})

async function initTypesDataTable() {
  await fetch("http://localhost:8080/api/v1/room-types")
      .then(data => data.json())
      .then(json => {
        types = json
      })


  DataTableUtils.initDataTable("types", typesColumnDefs)

  const $dataTable = fillTypesTable(types)
  initCellClickTypesEventListener($dataTable)
}

function fillTypesTable(typesJson) {
  const $dataTable = new DataTable("#types_table");

  typesJson.forEach((type) => {

    $dataTable.row.add([
      type.id,
      type.name,
      type.color,
      `<div style="background-color: ` + type.color + `; width: 20px; height: 20px; border: 1px solid #000"></div>`
    ]).draw(false)
  })

  return $dataTable
}

function initCellClickTypesEventListener($dataTable) {
  $dataTable.off('click').on('click', 'tbody tr', function (e) {
    let rowData = $dataTable.row(this).data();
    selectedType.value.id = rowData[0];
    selectedType.value.name = rowData[1];
    selectedType.value.color = rowData[2];
  });
}

function isSelectedTypeNull() {
  return selectedType.value.id == null
}

function setSelectedTypeNull() {
  selectedType.value.id = null;
  selectedType.value.name = null;
  selectedType.value.color = null;
}

function isTypeNameValid() {
  let isValid = selectedType.value.name != null
      && selectedType.value.name.length >= 4
      && selectedType.value.name.length <= 50


  isTypeNameValidV.value = isValid;

  return isValid
}

function isTypeColorValid() {
  let regexp = /#[a-f0-9]{6}\b/gi;


  console.log(selectedType.value.color)
  console.log(selectedType.value.color.match(regexp))

  let isValid = typeof selectedType.value.color !== "undefined"
      && selectedType.value.color.match(regexp) != null;

  isTypeColorValidV.value = isValid;

  return isValid;
}

const typesUrl = "http://localhost:8080/api/v1/room-types"

async function createType() {
  if (!isTypeNameValid() | !isTypeColorValid()) {
    return
  }

  if (selectedType.value.id == null) {
    let typeJson = {
      name: selectedType.value.name,
      color: selectedType.value.color
    }

    let createdType

    await fetch(typesUrl, {
      method: "POST",
      body: JSON.stringify(typeJson),
      headers: {
        "Content-Type": "application/json"
      },
      crossDomain: true
    })
        .then(data => data.json())
        .then(json => createdType = json)
        .catch(e => {
          openErrorInfoModal()
        })

    reloadTypesTable()

  } else {
    openErrorInfoModal()
  }
}

function reloadTypesTable() {
  DataTableUtils.destroyDataTable("types")
  initTypesDataTable()
}

async function saveTypeInfo() {
  if (selectedType.value.id != null) {
    let typeJson = {
      id: selectedType.value.id,
      name: selectedType.value.name,
      color: selectedType.value.color
    }

    let savedType

    await fetch(typesUrl + "/" + selectedType.value.id, {
      method: "PUT",
      body: JSON.stringify(typeJson),
      headers: {
        "Content-Type": "application/json"
      },
      crossDomain: true
    })
        .then(data => data.json())
        .then(json => savedType = json)
        .catch(e => {
          openErrorInfoModal()
        })

    reloadTypesTable()

  } else {
    openErrorInfoModal()
  }
}

function deleteTypeInfo() {
  infoTitle.value = "Подтвердите действие";
  infoMessage.value = "Вы действительно хотите удалить тип '" + selectedType.value.name + "'? " +
      "Это приведет к назначению помещениям с этим типом - случайного типа.";
  openInfoModal()
  currentAction.value = () => confirmDeleteType()
}

function confirmDeleteType() {
  const id = selectedType.value.id

  fetch(typesUrl + "/" + id, {
    method: "DELETE"
  }).then(data => {
    closeInfoModal()
    openSuccessInfoModal()
    setSelectedTypeNull()
    reloadTypesTable()

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

// /////////////////////////////STATUSES////////////////////////////

const statusNameErrorLabel = "Название офиса должно содержать от 4 до 50 символов"
const statusColorErrorLabel = "Цвет записывается в формате #AAAAAA"
const isStatusNameValidV = ref(true)
const isStatusColorValidV = ref(true)

const selectedStatus = ref({
  id: null,
  color: null,
  name: null
})

const statusesColumnDefs = [{visible: false}, {width: '40%'}, {visible: '50%'}, {visible: '10%'}]

let statuses = ref([])

onMounted(() => {
  initStatusesDataTable()
  // initFloorsDataTable(-1)
})

async function initStatusesDataTable() {
  await fetch("http://localhost:8080/api/v1/room-statuses")
      .then(data => data.json())
      .then(json => {
        statuses = json
      })


  DataTableUtils.initDataTable("statuses", statusesColumnDefs)

  const $dataTable = fillStatusesTable(statuses)
  initCellClickStatusesEventListener($dataTable)
}

function fillStatusesTable(statusesJson) {
  const $dataTable = new DataTable("#statuses_table");

  statusesJson.forEach((status) => {

    $dataTable.row.add([
      status.id,
      status.name,
      status.color,
      `<div style="background-color: ` + status.color + `; width: 20px; height: 20px; border: 1px solid #000"></div>`
    ]).draw(false)
  })

  return $dataTable
}

function initCellClickStatusesEventListener($dataTable) {
  $dataTable.off('click').on('click', 'tbody tr', function (e) {
    let rowData = $dataTable.row(this).data();
    selectedStatus.value.id = rowData[0];
    selectedStatus.value.name = rowData[1];
    selectedStatus.value.color = rowData[2];
  });
}

function isSelectedStatusNull() {
  return selectedStatus.value.id == null
}

function setSelectedStatusNull() {
  selectedStatus.value.id = null;
  selectedStatus.value.name = null;
  selectedStatus.value.color = null;
}

function isStatusNameValid() {
  let isValid = selectedStatus.value.name != null
      && selectedStatus.value.name.length >= 4
      && selectedStatus.value.name.length <= 50


  isStatusNameValidV.value = isValid;

  return isValid
}

function isStatusColorValid() {
  let regexp = /#[a-f0-9]{6}\b/gi;

  let isValid = typeof selectedStatus.value.color !== "undefined"
      && selectedStatus.value.color.match(regexp) != null;

  isStatusColorValidV.value = isValid;

  return isValid;
}

const statusesUrl = "http://localhost:8080/api/v1/room-statuses"

async function createStatus() {
  if (!isStatusNameValid() | !isStatusColorValid()) {
    return
  }

  if (selectedStatus.value.id == null) {
    let statusJson = {
      name: selectedStatus.value.name,
      color: selectedStatus.value.color
    }

    let createdStatus

    await fetch(statusesUrl, {
      method: "POST",
      body: JSON.stringify(statusJson),
      headers: {
        "Content-Type": "application/json"
      },
      crossDomain: true
    })
        .then(data => data.json())
        .then(json => createdStatus = json)
        .catch(e => {
          openErrorInfoModal()
        })

    reloadStatusesTable()

  } else {
    openErrorInfoModal()
  }
}

function reloadStatusesTable() {
  DataTableUtils.destroyDataTable("statuses")
  initStatusesDataTable()
}

async function saveStatusInfo() {
  if (selectedStatus.value.id != null) {
    let statusJson = {
      id: selectedStatus.value.id,
      name: selectedStatus.value.name,
      color: selectedStatus.value.color
    }

    let savedStatus

    await fetch(statusesUrl + "/" + selectedStatus.value.id, {
      method: "PUT",
      body: JSON.stringify(statusJson),
      headers: {
        "Content-Type": "application/json"
      },
      crossDomain: true
    })
        .then(data => data.json())
        .then(json => savedStatus = json)
        .catch(e => {
          openErrorInfoModal()
        })

    reloadStatusesTable()

  } else {
    openErrorInfoModal()
  }
}

function deleteStatusInfo() {
  infoTitle.value = "Подтвердите действие";
  infoMessage.value = "Вы действительно хотите удалить статус '" + selectedStatus.value.name + "'? " +
      "Это приведет к назначению помещениям с этим типом - типом по умолчанию.";
  openInfoModal()
  currentAction.value = () => confirmDeleteStatus()
}

function confirmDeleteStatus() {
  const id = selectedStatus.value.id

  fetch(statusesUrl + "/" + id, {
    method: "DELETE"
  }).then(data => {
    closeInfoModal()
    openSuccessInfoModal()
    setSelectedStatusNull()
    reloadStatusesTable()

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
        <table id="types_table" class="display">
          <thead>
          <tr>
            <th>typeId</th>
            <th>Название типа помещения</th>
            <th>Код цвета</th>
            <th>Отображение</th>
          </tr>
          </thead>
        </table>
      </div>
      <div class="field-form-v col-4">

        <div class="creation-box">

          <h3 v-if="isSelectedTypeNull()">Создать новый тип</h3>
          <h3 v-else>Редактировать тип помещения</h3>
          <button class="save-btn" style="font-size: 1em" v-if="!isSelectedTypeNull()"
                  @click="setSelectedTypeNull()">
            Переключить на создание
          </button>
          <input v-model="selectedType.id" hidden>
          <label>Название типа</label>
          <div>
            <label class="err-label" v-if="!isTypeNameValidV">
              {{ typeNameErrorLabel }}
            </label>
          </div>
          <input v-model="selectedType.name">
          <label>Цвет типа формата #AAAAAA</label>
          <div>
            <label class="err-label" v-if="!isTypeColorValidV">
              {{ typeColorErrorLabel }}
            </label>
          </div>
          <input v-model="selectedType.color">
          <button v-if="isSelectedTypeNull()" class="save-btn" @click="createType">Создать</button>
          <div v-else style="display: flex;">
            <button class="save-btn" @click="saveTypeInfo">Сохранить</button>
            <button class="save-btn" @click="deleteTypeInfo">Удалить</button>
          </div>
        </div>

      </div>
    </div>
    <div class="row" style="margin-top: 2em">
      <div class="data-table-container col-7">
        <table id="statuses_table" class="display">
          <thead>
          <tr>
            <th>statusId</th>
            <th>Название типа помещения</th>
            <th>Код цвета</th>
            <th>Отображение</th>
          </tr>
          </thead>
        </table>
      </div>
      <div class="field-form-h col-4">

        <div class="creation-box">

          <h3 v-if="isSelectedStatusNull()">Создать новый статус</h3>
          <h3 v-else>Редактировать статус помещения</h3>
          <button class="save-btn" style="font-size: 1em" v-if="!isSelectedStatusNull()"
                  @click="setSelectedStatusNull()">
            Переключить на создание
          </button>
          <input v-model="selectedStatus.id" hidden>
          <label>Название статуса</label>
          <div>
            <label class="err-label" v-if="!isStatusNameValidV">
              {{ statusNameErrorLabel }}
            </label>
          </div>
          <input v-model="selectedStatus.name">
          <label>Цвет типа формата #AAAAAA</label>
          <div>
            <label class="err-label" v-if="!isStatusColorValidV">
              {{ statusColorErrorLabel }}
            </label>
          </div>
          <input v-model="selectedStatus.color">
          <button v-if="isSelectedStatusNull()" class="save-btn" @click="createStatus">Создать</button>
          <div v-else style="display: flex;">
            <button class="save-btn" @click="saveStatusInfo">Сохранить</button>
            <button class="save-btn" @click="deleteStatusInfo">Удалить</button>
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