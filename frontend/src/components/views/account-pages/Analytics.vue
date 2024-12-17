<script setup>

import {onMounted, ref} from "vue";
import SearchSelect from "@/components/util/SearchSelect.vue";
import HourlyChart from "@/components/util/HourlyChart.vue";
import html2canvas from "html2canvas";

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

const loadedAnalytics = ref({})

const mostUsedRooms = ref({})
const mostCommonBookingTime = ref({})

const calculatedMostUsedRooms = ref([])
const calculatedMostCommonBookingTime = ref({})

async function findBookAnalytics() {
  console.log(startDate)
  console.log(endDate)
  console.log(selectedLeftOffice.value)

  await fetch("http://localhost:8080/api/v1/books/offices/" + selectedLeftOffice.value +
      "?start_date=" + startDate.value + "&end_date=" + endDate.value, {
    headers: {
      "Content-Type": "application/json"
    }
  })
      .then(data => data.json())
      .then(json => loadedAnalytics.value = json)

  mostUsedRooms.value = loadedAnalytics.value.mostUsedRooms;
  mostCommonBookingTime.value = loadedAnalytics.value.mostCommonBookingTime;

  calculateAnalytics()
}

function calculateAnalytics() {
  calculatedMostUsedRooms.value = []
  Object.entries(mostUsedRooms.value).forEach(([key, value]) => {
    const match = key.match(/name=([^,]+)/); // Ищем name=
    const roomName = match ? match[1] : 'Не найдено';
    calculatedMostUsedRooms.value.push({name: roomName, count: value})
  })

  console.log(mostCommonBookingTime.value)
  Object.entries(mostCommonBookingTime.value).forEach(([key, value]) => {
    console.log(key)
  })

  calculatedMostCommonBookingTime.value = Object.entries(mostCommonBookingTime.value)
      .map(([time, value]) => [addHoursToTime(time, 3), value]) // Прибавляем 3 часа
      .sort(([timeA], [timeB]) => { // Сортируем по времени
        const [hourA, minuteA] = timeA.split(':').map(Number);
        const [hourB, minuteB] = timeB.split(':').map(Number);
        return hourA - hourB || minuteA - minuteB;
      })
      .reduce((acc, [time, value]) => { // Собираем обратно в объект
        acc[time] = value;
        return acc;
      }, {});

  console.log(calculatedMostCommonBookingTime.value)
}

// Функция для увеличения времени на 3 часа
function addHoursToTime(time, hoursToAdd) {
  const [hour, minute] = time.split(':').map(Number); // Разделяем на часы и минуты
  let newHour = (hour + hoursToAdd) % 24; // Увеличиваем часы и обрабатываем границу суток
  return `${newHour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}`; // Форматируем строку
}

const printSection = async () => {
  const printContent = document.getElementById("book-print-section");

  $(".find-btn").hide()
  // Ожидаем, чтобы отрисовалась диаграмма
  const canvas = await html2canvas(printContent);
  const image = canvas.toDataURL(); // Получаем изображение диаграммы

  // Создаем новый элемент для печати
  const printWindow = window.open("", "_blank");
  printWindow.document.write("<img src='" + image + "' />"); // Вставляем график как изображение
  printWindow.document.close(); // Закрываем документ, чтобы он был готов для печати

  setTimeout(() => {
    printWindow.print();
  }, 500); // Задержка в 500 мс

  $(".find-btn").show()
};

</script>

<template>
  <div class="view-header">
    Аналитика бронирования
  </div>

  <div class="flex-container">
    <div class="content-container" id="book-print-section">
      <div>
        <h3>Аналитика бронирования переговорных комнат</h3>
        <div>
          <div>Выберите промежуток дат</div>
          <div class="flex-container">
            <input class="date-input" type="date" v-model="startDate" style="margin-right: 2em">
            <input class="date-input" type="date" v-model="endDate" v-if="startDate != null"
                   :min="startDate">
          </div>
          <div>Офис</div>
          <div class="flex-container">
            <SearchSelect class="office-select"
                          :options="officesSearchSelect" @return-id="onLeftOfficeSelect"
            />
            <button class="find-btn" @click="findBookAnalytics">Поиск</button>
            <button class="find-btn" @click="printSection">Печать</button>

          </div>
        </div>


        <hr>
        <h6>Список наиболее популярных переговорных</h6>
        <ol>
          <li v-for="room in calculatedMostUsedRooms" :key="room.name">
            '{{ room.name }}': <b>{{ room.count }}</b>
          </li>
        </ol>
        <hr>
        <h6>Частота бронирования по часам</h6>
        <div>
          <HourlyChart :inputData="calculatedMostCommonBookingTime"/>
        </div>
        <br>
        <br>
      </div>
    </div>
  </div>
</template>

<style scoped>
.content-container {
  margin: 0 2em 0 1em;
  width: 100%;
  padding: 10px;
  border-radius: 5px;
  height: max-content;
  border: 2px solid #b1c5d8;
  border-bottom: 0;
}

.flex-container {
  display: flex;
}

h3 {
  text-align: center;
}

.date-input {
}

.office-select {

}

.find-btn {
  font-size: 1.1em;
  margin: auto 0 0 0.5em;
}
</style>