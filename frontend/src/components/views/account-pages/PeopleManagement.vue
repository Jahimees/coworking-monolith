<script setup>
import {onMounted, ref} from "vue";
import DataTableUtils from "@/scripts/DataTableUtils.js"
import DataTable from "datatables.net-dt";
import UserUpdateModal from "@/components/util/UserUpdateModal.vue";
import InfoModal from "@/components/util/InfoModal.vue";

let users = ref([])
let roleMap = []
let departmentMap = []
let workspaceMap = []

const clickedRowData = ref([])
const columnDefs = [{visible: false}, {width: '25%'}, {width: '15%'}, {width: '20%'}, {visible: false}, {width: '10%'},
  {visible: false}, {width: '10%'}, {visible: false}, {width: '10%'}]

onMounted(() => {
  initDataTable()
  loadDepartments()
  loadRoles()
})

function reloadTable() {
  DataTableUtils.destroyDataTable("users")
  initDataTable()
}

async function initDataTable() {
  await fetch("http://localhost:8080/api/v1/users?without_workspace=false")
      .then(data => data.json())
      .then(json => {
        users = json
      })
  //
  if (!DataTable.isDataTable('#users')) {
    let $dataTable = new DataTable('#users_table');
    $dataTable.data().clear();
    $dataTable.destroy();
    console.log("DESTRYO")
  }
  DataTableUtils.initDataTable("users", columnDefs)

  const $dataTable = fillTable(users)
  initCellClickEventListener($dataTable)
}

function initCellClickEventListener($dataTable) {
  $dataTable.off('click').on('click', 'tbody tr', function () {
    clickedRowData.value = $dataTable.row(this).data();
    console.log(clickedRowData.value)

    openEditUserModal()
  });
}

function fillTable(usersJson) {
  const $dataTable = new DataTable("#users_table");

  usersJson.forEach(user => {
    let fullName = ((user.lastName != null ? user.lastName : " ") + " " +
        (user.firstName != null ? user.firstName : " ") + " " +
        (user.middleName != null ? user.middleName : ""));

    fullName = fullName != null ? fullName.trim() : ""

    const username = user.username.trim();
    const email = user.email.trim();
    const department = user.department != null ? user.department.name : " ";
    const departmentId = user.department != null ? user.department.id : " "
    const role = user.roles != null ? user.roles[0].name : " "
    const roleId = user.roles != null ? user.roles[0].id : " "
    const workSpace = user.workSpace != null ? user.workSpace : " "
    const id = user.id;

    $dataTable.row.add([
      id,
      fullName,
      username,
      email,
      departmentId,
      department,
      roleId,
      role,
      "WID ",
      workSpace,
    ]).draw(false)
  })

  return $dataTable
}

//MODAL

const isEditUserModalVisible = ref(false)
const isInfoModalVisible = ref(false)
const infoTitle = ref("")
const infoMessage = ref("")

const modalAction = ref('update')

function openEditUserModal() {
  isEditUserModalVisible.value = true
  modalAction.value = 'update'
}

function openCreateUserModal() {
  isEditUserModalVisible.value = true
  modalAction.value = 'create'
  console.log("modalAction")
  clickedRowData.value = ["", "", "", "", "", "", "", "", ""]
}

function closeEditUserModal() {
  isEditUserModalVisible.value = false
  clickedRowData.value = []
}

function onSuccessUserUpdate(text) {
  infoTitle.value = "Успех"
  if (typeof text === "undefined" || text === null || text === "") {
    infoMessage.value = "Действие выполнено успешно"
  } else {
    infoMessage.value = text
  }

  openInfoModal()
}

function onFailUserUpdate(text) {
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

/////////////SEARCH SELECTS////////////////

async function loadDepartments() {
  const departmentsJson = ref([])
  await fetch("http://localhost:8080/api/v1/departments")
      .then(data => data.json())
      .then(json => departmentsJson.value = json)

  departmentMap.push({id: -1, name: "Не выбран"})
  departmentsJson.value.forEach(department => {
    departmentMap.push({id: department.id, name: department.name})
  })
}

async function loadRoles() {
  const rolesJson = ref([])
  await fetch("http://localhost:8080/api/v1/roles")
      .then(data => data.json())
      .then(json => rolesJson.value = json)

  rolesJson.value.forEach(role => {
    roleMap.push({id: role.id, name: role.name})
  })
}

function loadWorkspaces() {
}
</script>

<template>
  <div class="view-header">
    Управление сотрудниками
  </div>
  <div class="data-table-container">
    <table id="users_table" class="display">
      <thead>
      <tr>
        <th>id</th>
        <th>ФИО</th>
        <th>Имя пользователя</th>
        <th>Email</th>
        <th>departmentId</th>
        <th>Отдел</th>
        <th>roleId</th>
        <th>Роль</th>
        <th>workspaceId</th>
        <th>Рабочее место</th>
      </tr>
      </thead>
      <tbody></tbody>
    </table>
  </div>
  <button style="margin: 1em 0 0 2.5em" @click="openCreateUserModal">Создать пользователя</button>

  <UserUpdateModal v-show="isEditUserModalVisible"
                   :data="clickedRowData"
                   :title="'Изменение пользователя'"
                   :action="modalAction"
                   :department-map="departmentMap"
                   :role-map="roleMap"
                   @close="closeEditUserModal"
                   @success="onSuccessUserUpdate"
                   @fail="onFailUserUpdate"
                   @reload-table="reloadTable"/>

  <InfoModal v-show="isInfoModalVisible"
             :title="infoTitle"
             :message="infoMessage"
             @close="closeInfoModal"
             @confirm="closeInfoModal"/>
</template>

<style scoped>
.data-table-container {
  margin: 0 auto;
  width: 95%;
  padding: 10px;
  border-radius: 10px;
  border: 2px solid #b1c5d8;
}
</style>