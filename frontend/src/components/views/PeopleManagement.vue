<script setup>
import {onMounted, ref} from "vue";
import DataTableUtils from "@/scripts/DataTableUtils.js"
import DataTable from "datatables.net-dt";
import UserUpdateModal from "@/components/util/UserUpdateModal.vue";

let users = ref([])
const data = ref([])
const columnDefs = [{visible: false}, {width: '25%'}, {width: '15%'}, {width: '20%'}, {width: '10%'}, {width: '10%'}, {width: '10%'}]


onMounted(async () => {
  await fetch("http://localhost:8080/api/v1/users")
      .then(data => data.json())
      .then(json => {
        users = json
      })

  DataTableUtils.initDataTable("users", columnDefs)

  const $dataTable = fillTable(users)
  initCellClickEventListener($dataTable)
})

function initCellClickEventListener($dataTable) {
  $dataTable.on('click', 'tbody tr', function () {
    data.value = $dataTable.row(this).data();

    openModal()
  });
}

function fillTable(usersJson) {
  const $dataTable = new DataTable("#users_table");

  // let timeOptions = {
  //   hour: 'numeric',
  //   minute: 'numeric'
  // }

  Array.of(usersJson).forEach(function (usersJson) {

    usersJson.forEach(user => {
      let fullName = (user.lastName != null ? user.lastName : " " +
      user.firstName != null ? user.firstName : " " +
      user.middleName != null ? user.middleName : "");

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
  })

  return $dataTable
}

//MODAL
const isModalVisible = ref(false)

function openModal() {
  isModalVisible.value = true
}

function closeModal() {
  isModalVisible.value = false
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

  <UserUpdateModal v-show="isModalVisible" :data="data"
                   :title="'Изменение пользователя'"
                   :action="'update'"
                   @close="closeModal"/>
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