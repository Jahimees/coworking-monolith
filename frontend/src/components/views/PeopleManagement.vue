<script setup>
import {onMounted, ref} from "vue";
import DataTableUtils from "@/scripts/DataTableUtils.js"
import DataTable from "datatables.net-dt";
import Modal from "@/components/util/Modal.vue";

let users = ref([])

onMounted(async () => {
  await fetch("http://localhost:8080/api/v1/users")
      .then(data => data.json())
      .then(json => {
        users = json
      })

  DataTableUtils.initDataTable("users")

  const $dataTable = fillTable(users)
  initEventListener($dataTable)
})

function fillTable(usersJson) {
  const $dataTable = new DataTable("#users_table");

  let timeOptions = {
    hour: 'numeric',
    minute: 'numeric'
  }

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

      $dataTable.row.add([
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

function initEventListener($dataTable) {
  $dataTable.on('click', 'tbody tr', function () {
    let data = $dataTable.row(this).data();

    // alert('You clicked on ' + data[0] + "'s row");
    openModal()
  });
}

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

  <Modal v-show="isModalVisible"
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