<script setup>

import {onMounted, ref} from "vue";
import DataTableUtils from "@/scripts/DataTableUtils.js";
import DataTable from "datatables.net-dt";

////////////////////////OFFICES//////////////////

const selectedOffice = ref({
  id: null
})

const officesColumnDefs = [{visible: false}, {width: '50%'}, {visible: '50%'}]

let offices = ref([])

onMounted(() => {
  initOfficesDataTable()
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
  $dataTable.on('click', 'tbody tr', function () {
    let rowData = $dataTable.row(this).data();
    selectedOffice.value.id = rowData[0];
    selectedOffice.value.name = rowData[1];
    selectedOffice.value.address = rowData[2];
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

async function createOffice() {
  const url = "http://localhost:8080/api/v1/offices"

  if (selectedOffice.value.id == null) {
    let officeJson = {
      name: selectedOffice.value.name,
      address: selectedOffice.value.address

    }

    let createdOffice

    await fetch(url, {
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
          //TODO ERROR
        })

    reloadOfficesTable()

  } else {
    //TODO ERROR
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
          //TODO ERROR
        })

    reloadOfficesTable()

  } else {
    //TODO ERROR
  }
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
          <br>
          <input v-model="selectedOffice.name">
          <label>Адрес офиса</label>
          <label class="err-label" v-if="!isDepartmentValid">
            {{ departmentErrorLabel }}
          </label>
          <input v-model="selectedOffice.address">
          <button v-if="isSelectedOfficeNull()" class="save-btn" @click="createOffice">Создать</button>
          <button v-else class="save-btn" @click="saveOfficeInfo">Сохранить</button>
        </div>

      </div>
    </div>
    <div class="row">
      <div class="data-table-container col-7">
        <table id="floors_table" class="display">
          <thead>
          <tr>
            <th>офис айди</th>
            <th>Название офиса</th>
            <th>Адрес офиса</th>
          </tr>
          </thead>
        </table>
      </div>
      <div class="field-form-h col-5">

      </div>
    </div>
  </div>
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
  margin: 10px 0 0 0
}
</style>