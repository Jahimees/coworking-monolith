<script setup>

import {onMounted, ref} from "vue";
import DataTableUtils from "@/scripts/DataTableUtils.js";
import DataTable from "datatables.net-dt";
import InfoModal from "@/components/util/InfoModal.vue";

let books = ref([])
const clickedRowData = ref([])
const columnDefs = [{visible: false}, {width: '25%'}, {width: '25%'}, {width: '20%'}, {width: '30%'}]
const currentUser = ref({})

const props = defineProps({
  parsedToken: Object
})


onMounted(async () => {
  await findCurrentUser()
  await initDataTable()
})

async function findCurrentUser() {
  await fetch("http://localhost:8080/api/v1/users/" + props.parsedToken.sub, {
    headers: {
      "Content-Type": "application/json"
    }
  })
      .then(data => data.json())
      .then(json => {
        currentUser.value = json
      })
}

async function initDataTable() {
  await fetch("http://localhost:8080/api/v1/books/users/" + currentUser.value.id)
      .then(data => data.json())
      .then(json => {
        books.value = json
      })

  console.log(books.value)

  DataTableUtils.initDataTable("books", columnDefs)

  const $dataTable = fillTable(books.value)
  initCellClickEventListener($dataTable)
}

function initCellClickEventListener($dataTable) {
  $dataTable.off('click').on('click', 'tbody tr', function () {
    clickedRowData.value = $dataTable.row(this).data();
    openInfoModal()
  });
}

function fillTable(booksJson) {
  const $dataTable = new DataTable("#books_table");

  booksJson.forEach(book => {

    let roomName = book.room.name
    let startTime = new Date(book.startDate).toLocaleString()
    startTime = startTime.slice(0, startTime.length - 3)

    let endTime = new Date(book.endDate).toLocaleString()
    endTime = endTime.slice(0, endTime.length - 3)
    let theme = book.theme

    $dataTable.row.add([
      book.id,
      roomName,
      startTime,
      endTime,
      theme
    ]).draw(false)
  })

  return $dataTable
}

function reloadTable() {
  DataTableUtils.destroyDataTable("books")
  initDataTable()
}

const isInfoModalVisible = ref(false)
const infoTitle = ref("")
const infoMessage = ref("")

function openInfoModal() {
  infoTitle.value = "Удаление"
  infoMessage.value = "Вы действительно хотите удалить бронирование '" + clickedRowData.value[4] + "'?"
  isInfoModalVisible.value = true;
}

function closeInfoModal() {
  isInfoModalVisible.value = false;
}

function confirmDeleteBook() {
  let bookId = clickedRowData.value[0];

  fetch("http://localhost:8080/api/v1/books/" + bookId, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json"
    }
  })
      .then(data => {

      })
      .then(() => {
        closeInfoModal()
        reloadTable()
      })
      .catch(er => {
        closeInfoModal()
        infoTitle.value = "Ошибка"
        infoMessage.value = "Что-то пошло не так"
        openInfoModal()
      })
}

</script>

<template>
  <div class="view-header">
    Мои брони
  </div>

  <div class="body-container">
    <div class="data-table-container">
      <h4>Для удаления - нажмите на строку</h4>

      <table id="books_table" class="display">
        <thead>
        <tr>
          <th>bookId</th>
          <th>Переговорная</th>
          <th>Начало встречи</th>
          <th>Конец встречи</th>
          <th>Описание</th>
        </tr>
        </thead>
      </table>
    </div>
  </div>

  <InfoModal v-if="isInfoModalVisible" :title="infoTitle" :message="infoMessage" @confirm="confirmDeleteBook" @close="closeInfoModal"/>
</template>

<style scoped>
.data-table-container {
  margin: 0 2em 0 1em;
  width: 98%;
  padding: 10px;
  border-radius: 10px;
  border: 2px solid #b1c5d8;
}

tr td {
  cursor: pointer;
}
</style>