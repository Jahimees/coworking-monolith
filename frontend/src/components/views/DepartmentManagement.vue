<script setup>

import {onMounted, ref} from "vue";
import DataTableUtils from "@/scripts/DataTableUtils.js";
import DataTable from "datatables.net-dt";
import SearchSelect from "@/components/util/SearchSelect.vue";
import InfoModal from "@/components/util/InfoModal.vue";
import Utils from "@/scripts/Utils.js";
import DepartmentUpdateModal from "@/components/util/DepartmentUpdateModal.vue";

let departments = ref([])
const clickedRowData = ref([])
const users = ref([])
const columnDefs = [{visible: false}, {width: '25%'}, {visible: false}, {width: '20%'}]

const selectedUserId = ref(-1)
const newDepartmentName = ref("")

const isDepartmentValid = ref(true)
const departmentErrorLabel = "Название департамента должно содержать от 1 до 50 символов"

let usersMap = [];

onMounted(() => {
  initDataTable()
  loadUsers()
})

async function loadUsers() {
  await fetch("http://localhost:8080/api/v1/users")
      .then(data => data.json())
      .then(json => users.value = json)

  usersMap.push({id: -1, name: "Не выбран"})
  users.value.forEach(user => {
    usersMap.push({id: user.id, name: Utils.getUserFullName(user)})
  })

}

async function initDataTable() {
  await fetch("http://localhost:8080/api/v1/departments")
      .then(data => data.json())
      .then(json => {
        departments = json
      })

  DataTableUtils.initDataTable("departments", columnDefs)

  const $dataTable = fillTable(departments)
  initCellClickEventListener($dataTable)
}

function initCellClickEventListener($dataTable) {
  $dataTable.on('click', 'tbody tr', function () {
    clickedRowData.value = $dataTable.row(this).data();
    openEditDepartmentModal()
  });
}

function fillTable(departmentsJson) {
  const $dataTable = new DataTable("#departments_table");

  departmentsJson.forEach(department => {
    let bossFullName = ""
    let bossId
    if (department.user != null && typeof department.user !== "undefined") {
      let user = department.user;
      bossFullName = ((user.lastName != null ? user.lastName : " ") + " " +
          (user.firstName != null ? user.firstName : " ") + " " +
          (user.middleName != null ? user.middleName : ""));
      bossId = user.id
    }

    bossFullName = bossFullName != null ? bossFullName.trim() : ""

    const departmentName = department.name != null ? department.name : "";
    const departmentId = department.id

    $dataTable.row.add([
      departmentId,
      departmentName,
      bossId,
      bossFullName,
    ]).draw(false)
  })

  return $dataTable
}

function returnIdCallback(id) {
  selectedUserId.value = id
}

async function createDepartment() {
  if (!validateFields()) {
    return
  }

  let json = {
    name: newDepartmentName.value,
    user: {
      id: selectedUserId.value
    }
  }

  await fetch("http://localhost:8080/api/v1/departments", {
    method: "POST",
    body: JSON.stringify(json),
    headers: {
      "Content-Type": "application/json"
    },
    crossDomain: true
  })
      .then(data => data.json())
      .then(json => console.log(json))

  DataTableUtils.reloadTable(departments)
}

function validateFields() {
  let newDepartment = newDepartmentName.value;
  isDepartmentValid.value = newDepartment.trim().length > 0 && newDepartment.trim().length < 51;

  return isDepartmentValid.value
}

////MODAL

const isEditDepartmentModalVisible = ref(false)
const isInfoModalVisible = ref(false)
const infoTitle = ref("")
const infoMessage = ref("")

function openEditDepartmentModal() {
  isEditDepartmentModalVisible.value = true
}

function closeEditDepartmentModal() {
  isEditDepartmentModalVisible.value = false
}

function onSuccessDepartmentUpdate() {
  openInfoModal()
}

function onFailDepartmentUpdate() {
  infoTitle.value = "Ошибка"
  infoMessage.value = "Произошла ошибка сохранения"
  openInfoModal()
}

function openInfoModal() {
  isInfoModalVisible.value = true;
}

function closeInfoModal() {
  isInfoModalVisible.value = false;
}

</script>

<template>
  <div class="view-header">
    Управление отделами
  </div>
  <div class="body-container">
    <div class="data-table-container">
      <table id="departments_table" class="display">
        <thead>
        <tr>
          <th style="display: none">userid</th>
          <th>Название отдела</th>
          <th style="display: none">id</th>
          <th>Начальник отдела</th>
        </tr>
        </thead>
      </table>
    </div>
    <div>
      <div class="department-creation-box">
        <h3>Создать новый отдел</h3>
        <label>Начальник отдела</label>
        <SearchSelect :options="usersMap" @return-id="returnIdCallback"/>
        <div> {{ selectedUserId }}</div>
        <label>Название отдела</label>
        <br>
        <label class="err-label" v-if="!isDepartmentValid">
          {{ departmentErrorLabel }}
        </label>
        <input v-model="newDepartmentName">
        <button style="display: block; margin: 0.5em auto" @click="createDepartment">Создать</button>
      </div>
    </div>
  </div>

  <DepartmentUpdateModal v-show="isEditDepartmentModalVisible"
                         :data="clickedRowData"
                         :title="'Изменение отдела'"
                         :action="'update'"
                         :users-map="usersMap"
                         @close="closeEditDepartmentModal"
                         @success="onSuccessDepartmentUpdate"
                         @fail="onFailDepartmentUpdate"
                         @reload-table="DataTableUtils.reloadTable('departments')"/>

  <InfoModal v-show="isInfoModalVisible"
             :title="infoTitle"
             :message="infoMessage"
             @close="closeInfoModal"/>
</template>

<style scoped>

.data-table-container {
  margin: 0 2em 0 1em;
  width: 50%;
  padding: 10px;
  border-radius: 10px;
  border: 2px solid #b1c5d8;
}

.body-container {
  display: flex;
}

select, input {
  display: block;
  width: 100%;
}

.department-creation-box {
  margin: 0 auto;
  width: 100%;
}
</style>