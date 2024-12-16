<script setup>

import SearchSelect from "@/components/util/SearchSelect.vue";
import {onMounted, ref} from "vue";
import BookingMap from "@/components/views/map/BookingMap.vue";
import FullCalendar from '@fullcalendar/vue3'
import dayGridPlugin from '@fullcalendar/daygrid'
import interactionPlugin from '@fullcalendar/interaction'
import timeGridPlugin from '@fullcalendar/timegrid'
import InfoModal from "@/components/util/InfoModal.vue";
import Utils from "@/scripts/Utils.js";

const offices = ref([])
const floors = ref([])

const officesSearchMap = []
let floorsSearchMap = ref([])

const selectedOfficeSearch = ref(null)
const selectedFloorSearch = ref(null)

const officesUrl = "http://localhost:8080/api/v1/offices"
const floorsUrl = "http://localhost:8080/api/v1/floors"

const props = defineProps({
  parsedToken: Object
})

// Обработчик клика по дате
function handleDateClick(arg) {
  alert('date click! ' + arg.dateStr);
}

// Исходные события для календаря
const calendarEvents = ref([
]);

// Опции календаря
const calendarOptions = ref({
  plugins: [dayGridPlugin, interactionPlugin, timeGridPlugin],
  initialView: 'timeGridDay',
  dateClick: handleDateClick,
  // eventClick: handleEventClick,
  events: calendarEvents.value,
  eventTimeFormat: { // Формат отображения времени
    hour: '2-digit',
    minute: '2-digit',
    hour12: false,
  },
  headerToolbar: {
    left: 'prev,next',
    center: 'title',
    right: 'today'
  },
  locale: 'ru'
});

const currentUser = ref(null)

onMounted(() => {
  loadOffices();
  findUser()
})

async function loadOffices() {
  // officesSearchMap.value = []

  await fetch(officesUrl, {
    headers: {
      "Content-Type": "application/json"
    }
  })
      .then(data => data.json())
      .then(json => {
        offices.value = json
      })

  officesSearchMap.push({id: -1, name: "Не выбран"})

  offices.value.forEach(office => {
    officesSearchMap.push({id: office.id, name: office.name})
  })
}

function onOfficeSearchChosen(officeId) {

  if (officeId === -1) {
    selectedOfficeSearch.value = null
    selectedFloorSearch.value = null;
    loadFloorsSearch()

    return
  }

  offices.value.forEach(office => {
    if (office.id === officeId) {
      selectedOfficeSearch.value = office
      floors.value = office.floors
      loadFloorsSearch()

      return
    }
  })
}

function loadFloorsSearch() {
  floorsSearchMap.value = []

  floors.value.forEach((floor) => {
    floorsSearchMap.value.push({id: floor.id, name: floor.name})
  })
}

const isOfficeChosen = ref(true)
const isFloorChosen = ref(true)

const mapData = ref("")

function validateOfficeAndFloor() {

  isOfficeChosen.value = typeof selectedOfficeSearch.value !== "undefined" &&
      selectedOfficeSearch.value !== null &&
      selectedOfficeSearch.value.id !== -1

  isFloorChosen.value = typeof selectedFloorSearch.value !== "undefined" &&
      selectedFloorSearch.value !== null &&
      selectedFloorSearch.value.id !== -1

  return isOfficeChosen.value && isFloorChosen.value
}

function onFloorSearchChosen(floorId) {
  floors.value.forEach(floor => {
    if (floor.id === floorId) {
      selectedFloorSearch.value = floor
      selectedFloor.value = floor
    }
  })
}

async function loadMap() {
  if (!validateOfficeAndFloor()) {
    return
  }

  await fetch("http://localhost:8080/api/v1/floors/" + selectedFloorSearch.value.id, {
    headers: {
      "Content-Type": "application/json"
    }
  })
      .then(data => data.json())
      .then(json => mapData.value = JSON.stringify(json))

}

///////////////

// Массив времени с шагом 30 минут
const times = ref([]);

// Генерация времени от 00:00 до 24:00 с шагом 30 минут
function generateTimes() {
  const timeList = [];
  for (let hour = 0; hour < 24; hour++) {
    for (let minute = 0; minute < 60; minute += 30) {
      // Форматируем числа в виде HH:MM
      const formattedTime = `${String(hour).padStart(2, '0')}:${String(minute).padStart(2, '0')}`;
      timeList.push(formattedTime);
    }
  }
  return timeList;
}

// Заполняем массив times
times.value = generateTimes();

// Переменная для выбранного значения
const selectedTime = ref('');

///////////////////////


// Массив для хранения продолжительности
const durations = ref([]);

// Генерация значений времени до 3 часов с шагом 30 минут
function generateDurations() {
  const durationList = [];
  let totalMinutes = 30; // Начинаем с 30 минут

  while (totalMinutes <= 180) { // 3 часа = 180 минут
    const hours = Math.floor(totalMinutes / 60);
    const minutes = totalMinutes % 60;

    // Формируем текстовую метку
    let label = '';
    if (hours > 0) {
      label += `${hours} час${hours > 1 ? 'а' : ''}`;
    }
    if (minutes > 0) {
      label += ` ${minutes} минут${minutes > 1 ? 'ы' : 'а'}`;
    }

    // Добавляем в массив
    durationList.push({value: totalMinutes, label: label.trim()});
    totalMinutes += 30; // Увеличиваем на 30 минут
  }

  return durationList;
}

// Заполняем массив durations
durations.value = generateDurations();

// Переменная для выбранного значения
const selectedDuration = ref('');

const selectedRoom = ref(null)

async function onRoomSelected(room) {

  await fetchBookInfoByRoom(room)
  selectedRoom.value = room
}

async function fetchBookInfoByRoom(room) {

  let books

  await fetch("http://localhost:8080/api/v1/books/rooms/" + room.id, {
    method: "GET",
    headers: {
      "Content-Type": "application/json"
    }
  })
      .then(data => data.json())
      .then(json => books = json)

  calendarEvents.value = []
  books.forEach(book => {

    calendarEvents.value.push({
      title: book.theme,
      start: new Date(book.startDate),
      end: new Date(book.endDate)
    })
  })

  calendarOptions.value.events = calendarEvents.value;
}

function onWorkspaceSelected(workspace) {
}

function onDeselectAll() {
}

const selectedTheme = ref(null)
const selectedDate = ref(null)

const isStartDateValid = ref(true)
const isSelectedRoomValid = ref(true)
const isSelectedDurationValid = ref(true)
const isSelectedTimeValid = ref(true)
const isSelectedThemeValid = ref(true)

function clearRoomBookingErrors() {
  isStartDateValid.value = true;
  isSelectedRoomValid.value = true;
  isSelectedDurationValid.value = true;
  isSelectedTimeValid.value = true;
  isSelectedThemeValid.value = true;
}

function validateRoomBookingValues() {
  if (selectedDate.value == null) {
    isStartDateValid.value = false
  }

  if (selectedRoom.value == null) {
    isSelectedRoomValid.value = false;
  }

  if (selectedDuration.value == null || typeof selectedDuration.value === "undefined"
      || selectedDuration.value === "") {
    isSelectedDurationValid.value = false
  }

  if (selectedTime.value == null || typeof selectedTime.value === "undefined"
      || selectedTime.value.trim() === "") {
    isSelectedTimeValid.value = false
  }

  if (selectedTheme.value == null || typeof selectedTheme.value === "undefined"
      || selectedTheme.value.trim().length < 3 || selectedTheme.value.trim().length > 20) {
    isSelectedThemeValid.value = false
  }

  return isStartDateValid.value && isSelectedRoomValid.value && isSelectedDurationValid.value
      && isSelectedTimeValid.value && isSelectedThemeValid.value
}

async function onRoomBooking() {
  clearRoomBookingErrors()

  if (!validateRoomBookingValues()) {
    return
  }

  let startDate = new Date(selectedDate.value + 'T' + selectedTime.value);
  let endDate = startDate.getTime() + selectedDuration.value * 60 * 1000;

  if (Utils.isOverlapping(startDate, endDate, calendarOptions.value.events)) {
    onFailBookModal()
    return
  }

  let theme = selectedTheme.value;
  let room = selectedRoom.value

  let jsonForSend = {
    user: currentUser.value,
    startDate: startDate,
    endDate: endDate,
    theme: theme,
    room: room
  }

  fetch("http://localhost:8080/api/v1/books", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(jsonForSend)
  })
      .then(data => data.json())
      .then(json => {
            onSuccessModal()
            fetchBookInfoByRoom(room)
          }
      )
      .catch(e => onFailModal())



  // (new Date("2025-12-12T11:00:00") - new Date("2025-12-12T09:00:00"))/1000/60
// new Date(new Date("2025-12-12T11:00:00").getTime() + 120*60*1000)
}

async function findUser() {

  await fetch("http://localhost:8080/api/v1/users/" + props.parsedToken.sub, {
    headers: {
      "Content-Type": "application/json"
    }
  })
      .then(data => data.json())
      .then(json => {
        currentUser.value = json
      })
      .catch(e => {
        onFailModal()
      })
}

////////////MODAL

const isInfoModalVisible = ref(false)
const infoTitle = ref("")
const infoMessage = ref("")

function onSuccessModal() {
  infoTitle.value = "Успех"
  infoMessage.value = "Действие выполнено успешно"
  openInfoModal()
}

function onFailModal() {
  infoTitle.value = "Ошибка"
  infoMessage.value = "Произошла ошибка сохранения"
  openInfoModal()
}

function onFailBookModal() {
  infoTitle.value = "Ошибка"
  infoMessage.value = "Выбранное время налагается на другую запись"
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
    Бронирование переговорных
  </div>

  <div class="body-container">
    <div class="">
      <div id="interactive-map-tab-container">

        <div class="office-select-menu">
          <div class="search-select-container" style="display: flex">
            <div class="search-select-item">
              <label>Выбрать офис</label>
              <div>
                <label class="err-label" v-if="!isOfficeChosen">Выберите офис</label>
              </div>
              <SearchSelect :options="officesSearchMap" @return-id="onOfficeSearchChosen"/>
            </div>
            <div class="search-select-item">
              <label>Выбрать этаж</label>
              <div>
                <label class="err-label" v-if="!isFloorChosen">Выберите этаж</label>
              </div>
              <SearchSelect :options="floorsSearchMap" @return-id="onFloorSearchChosen"/>
            </div>
            <button class="map-management-btn" @click="loadMap">Загрузить карту</button>
          </div>
        </div>

      </div>
      <div class="booking-panel row">
        <label style="margin-left: 5px" class="err-label" v-if="!isSelectedRoomValid">Выберите комнату</label>
        <div class="col">
          <label>Дата</label>
          <label style="margin-left: 5px" class="err-label" v-if="!isStartDateValid">Выберите дату</label>
          <input type="date" v-model="selectedDate" style="width: 100%" :min="new Date().toISOString().split('T')[0]">
        </div>
        <div class="col">
          <label>Время</label>
          <label style="margin-left: 5px" class="err-label" v-if="!isSelectedTimeValid">Выберите время</label>
          <select v-model="selectedTime" style="width: 100%">
            <option v-for="time in times" :key="time" :value="time">
              {{ time }}
            </option>
          </select>
        </div>
        <div class="col">
          <label>Срок</label>
          <label style="margin-left: 5px" class="err-label" v-if="!isSelectedDurationValid">Выберите значение</label>
          <select v-model="selectedDuration" style="width: 100%">
            <option v-for="duration in durations" :key="duration.value" :value="duration.value">
              {{ duration.label }}
            </option>
          </select>
        </div>
        <div class="col">
          <label>Тема</label>
          <label style="margin-left: 5px" class="err-label" v-if="!isSelectedThemeValid">Название от 3 до 20
            символов</label>
          <input v-model="selectedTheme">
        </div>
        <button class="col" @click="onRoomBooking">Забронировать</button>
      </div>

      <div class="work-flow row">

        <div class="col">
          <BookingMap ref="mapRef"
                      :map-data="mapData"
                      @room-selected="onRoomSelected"
                      @workspace-selected="onWorkspaceSelected"
                      @deselect-all="onDeselectAll"
          />
        </div>
        <div class="col-2">
          <FullCalendar :options="calendarOptions"/>
          <div id='calendar'></div>
        </div>

      </div>
    </div>

  </div>

  <InfoModal v-if="isInfoModalVisible" :title="infoTitle" :message="infoMessage" @confirm="closeInfoModal"
             @close="closeInfoModal"/>

</template>

<style scoped>
.office-select-menu {
  display: flex;
}

.search-select-container {
  display: flex;
}

.search-select-item {
  margin-right: 0.5em;
}

.map-management-btn {
  font-size: 1em;
  padding: 10px;
  height: fit-content;
  margin-top: auto;
}

.body-container {
  margin: 0 auto;
  width: 98%;
}

.content-container {
  padding: 10px;
  border-radius: 10px;
  border: 2px solid #b1c5d8;
}

.work-flow {
  margin-top: 1em;
  display: flex;
}

.fc, .fc-media-screen {
  height: 73vh;
}

.fc-col-header-cell-cushion {
  font-size: 1em !important;
}

.fc-timegrid-slot-label-frame .fc-scrollgrid-shrink-frame {
  font-size: 0.2em !important;
}

.booking-panel {
  width: 75%;
  margin-top: 1em
}

</style>