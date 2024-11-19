<script setup>

import {onMounted, ref} from "vue";
import DataTableUtils from "@/scripts/DataTableUtils.js";
import DataTable from "datatables.net-dt";
import SearchSelect from "@/components/util/SearchSelect.vue";

const departments = ref([])
const clickedRowData = ref([])
const users = ref([])
let usersMap = [];
const columnDefs = [{visible: false}, {width: '25%'}, {visible: false}, {width: '20%'}]

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
    usersMap.push({id: user.id, name: getUserFullName(user)})
  })

  initUserSelect();
}

function initUserSelect() {

}

function reloadTable() {
  DataTableUtils.destroyDataTable("users")
  initDataTable()
}

async function initDataTable() {
  await fetch("http://localhost:8080/api/v1/departments")
      .then(data => data.json())
      .then(json => {
        departments.value = json
      })

  DataTableUtils.initDataTable("departments", columnDefs)

  const $dataTable = fillTable(departments.value)
  initCellClickEventListener($dataTable)
}

function initCellClickEventListener($dataTable) {
  $dataTable.on('click', 'tbody tr', function () {
    clickedRowData.value = $dataTable.row(this).data();

    openEditUserModal()
  });
}

function fillTable(departmentsJson) {
  const $dataTable = new DataTable("#users_table");


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
      bossId,
      bossFullName,
      departmentId,
      departmentName,
    ]).draw(false)
  })

  return $dataTable
}

function getUserFullName(user) {
  if (user != null && typeof user !== "undefined") {
    let fullName =
        (user.lastName != null ? user.lastName + " " : "") +
        (user.firstName != null ? user.firstName + " " : "") +
        (user.middleName != null ? user.middleName : "")

    if (fullName == null || fullName.trim() === "") {
      return user.username;
    } else {
      return fullName
    }
  }

  return ""
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
        <SearchSelect :options="usersMap"/>
        <label>Название отдела</label>
        <input>
        <button style="display: block; margin: 0.5em auto">Создать</button>
      </div>
    </div>
  </div>
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
}
</style>