<script setup>

import {onMounted, ref} from "vue";
import SearchSelect from "@/components/util/SearchSelect.vue";

const startDate = ref(null)
const endDate = ref(null)
const offices = ref([])
let officesSearchSelect = []

onMounted(() => {
  loadOffices()
})

async function loadOffices() {

  await fetch("http://localhost:8080/api/v1/offices", {
    headers: {
      "Content-Type": "application/json"
    }
  })
      .then(data => data.json())
      .then(json => {
        offices.value = json
      })


  offices.value.forEach(office => {
    officesSearchSelect.push({
      id: office.id, name: office.name
    })
  })
}

const selectedLeftOffice = ref(-1)

function onLeftOfficeSelect(id) {
  selectedLeftOffice.value = id
}

function findBookAnalytics() {
  console.log(startDate)
  console.log(endDate)
  console.log(selectedLeftOffice.value)
}


</script>

<template>
  <div class="view-header">
    Аналитика бронирования
  </div>

  <div class="flex-container">
    <div class="content-container">
      <h3>Аналитика бронирования переговорных комнат</h3>
      <div>
        <div>Выберите промежуток дат</div>
        <div class="flex-container">
          <input type="date" v-model="startDate" style="width: 100%">
          <input type="date" v-model="endDate" style="width: 100%" v-if="startDate != null"
                 :min="startDate">
        </div>
      </div>
      <div>
        <div>Офис</div>
        <div class="flex-container">
          <SearchSelect
              :options="officesSearchSelect" @return-id="onLeftOfficeSelect"
          />
          <button @click="findBookAnalytics">Поиск</button>
        </div>
      </div>

      <hr>
      <h6>Список наиболее популярных переговорных</h6>

      <hr>
      <h6>Частота бронирования по часам</h6>
    </div>
    <div class="content-container">
      <h3>Аналитика паспределения рабочих мест</h3>
    </div>
  </div>
</template>

<style scoped>
.content-container {
  margin: 0 2em 0 1em;
  width: 45%;
  padding: 10px;
  border-radius: 10px;
  border: 2px solid #b1c5d8;
}

.flex-container {
  display: flex;
}

h3 {
  text-align: center;
}
</style>