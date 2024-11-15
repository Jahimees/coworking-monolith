<script setup>
import {onMounted, ref} from "vue";
import DataTableUtils from "@/scripts/DataTableUtils.js"

let users = ref([])

onMounted(async () => {
  await fetch("http://localhost:8080/api/v1/users")
      .then(data => data.json())
      .then(json => {
        users = json
        console.log(users)

      })


  DataTableUtils.initDataTable("users")
  fillTable(users)
})

function fillTable(usersJson) {
  const tableName = "users"
  const $dataTable = $("#users_table");

  DataTableUtils.destroyAndInitDataTable(tableName, $dataTable)

  let timeOptions = {
    hour: 'numeric',
    minute: 'numeric'
  }

  usersJson.forEach(function (usersJson) {

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

      const rowNode = $dataTable.DataTable().row.add([
        fullName,
        username,
        email,
        department,
        role,
        workSpace,
      ])
          .draw()
          .node();
    })
  })
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
      <tbody>
      <tr>
        <td>Row 1 Data 1</td>
        <td>Row 1 Data 2</td>
      </tr>
      <tr>
        <td>Row 2 Data 1</td>
        <td>Row 2 Data 2</td>
      </tr>
      </tbody>
    </table>
  </div>
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