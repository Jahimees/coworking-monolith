<script setup>
  import {onMounted} from "vue";
  import DataTableUtils from "@/scripts/DataTableUtils.js"

  onMounted(() => {
    DataTableUtils.initDataTable("default")
  })
</script>

<template>
  <div class="view-header">
    Управление сотрудниками
  </div>
  <div class="data-table-container">
  <table id="default_table" class="display">
    <thead>
    <tr>
      <th>Column 1</th>
      <th>Column 2</th>
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