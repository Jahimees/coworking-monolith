<script setup>
import {onMounted, ref} from "vue";
import DataTableUtils from "@/scripts/DataTableUtils.js"
import DataTable from "datatables.net-dt";
import UserUpdateModal from "@/components/util/UserUpdateModal.vue";
import InfoModal from "@/components/util/InfoModal.vue";

let users = ref([])
const clickedRowData = ref([])
const columnDefs = [{visible: false}, {width: '25%'}, {width: '15%'}, {width: '20%'}, {width: '10%'}, {width: '10%'}, {width: '10%'}]

onMounted(() => {
  initDataTable()
})

function reloadTable() {
  DataTableUtils.reloadTable("users")
}

async function initDataTable() {
  await fetch("http://localhost:8080/api/v1/users")
      .then(data => data.json())
      .then(json => {
        users = json
      })

  DataTableUtils.initDataTable("users", columnDefs)

  const $dataTable = fillTable(users)
  initCellClickEventListener($dataTable)
}

function initCellClickEventListener($dataTable) {
  $dataTable.on('click', 'tbody tr', function () {
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
    const department = user.department != null ? user.department.name : "";
    const role = user.roles != null ? user.roles[0].name : ""
    const workSpace = user.workSpace != null ? user.workSpace : ""
    const id = user.id;

    $dataTable.row.add([
      id,
      fullName,
      username,
      email,
      department,
      role,
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

function openEditUserModal() {
  isEditUserModalVisible.value = true
}

function closeEditUserModal() {
  isEditUserModalVisible.value = false
}

function onSuccessUserUpdate() {
  openInfoModal()
}

function onFailUserUpdate() {
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
    Управление сотрудниками
  </div>
  <div class="data-table-container">
    <table id="users_table" class="display">
      <thead>
      <tr>
        <th style="display: none">id</th>
        <th>ФИО</th>
        <th>Имя пользователя</th>
        <th>Email</th>
        <th>Отдел</th>
        <th>Роль</th>
        <th>Рабочее место</th>
      </tr>
      </thead>
    </table>
  </div>

  <UserUpdateModal v-show="isEditUserModalVisible" :data="clickedRowData"
                   :title="'Изменение пользователя'"
                   :action="'update'"
                   @close="closeEditUserModal"
                   @success="onSuccessUserUpdate"
                   @fail="onFailUserUpdate"
                   @reload-table="reloadTable"/>

  <InfoModal v-show="isInfoModalVisible"
             :title="infoTitle"
             :message="infoMessage"
             @close="closeInfoModal"/>
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