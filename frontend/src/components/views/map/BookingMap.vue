<script setup>
import {onMounted, ref, watch} from 'vue';
import interact from "interactjs";
import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css'

const scale = 27; // Коэффициент масштабирования
const rooms = ref([]); // Список помещений
const selectedRoom = ref(null); // Выбранное помещение
const selectedWorkspace = ref(null); // Выбранное рабочее место

const props = defineProps({
  mapData: {
    type: String
  },
})

const emits = defineEmits(["updateRooms", "roomSelected", "workspaceSelected", "deselectAll", "returnRooms"]);

function getRooms() {
  emits('returnRooms', rooms)
}

defineExpose({
  getRooms,
  // `WP ${selectedRoom.value.workspaces.length + 1}`
})

// Удаление выбранного рабочего места
const deleteWorkspace = () => {
  if (selectedRoom.value && selectedWorkspace.value) {
    selectedRoom.value.workspaces = selectedRoom.value.workspaces.filter(
        (wp) => wp.id !== selectedWorkspace.value.id
    );
    selectedWorkspace.value = null;
  }
};

// Удаление помещения
const deleteRoom = () => {
  if (selectedRoom.value) {
    rooms.value = rooms.value.filter((r) => r.id !== selectedRoom.value.id);
    selectedRoom.value = null;
    deselectAll()
  }
};

// Эмитим изменения наружу
const emitChanges = (type, payload) => {
  emits(type, payload);
  console.log(`Change emitted: ${type}`, payload);
};

// Границы карты
let mapBounds = {
  width: 800, // Ширина карты
  height: 500, // Высота карты
};

onMounted(() => {
  mapBounds = {
    width: $("#booking-map").width(),
    height: $("#booking-map").height()
  }

  interact.stop()
})


// Рендеринг из JSON
const renderFromJSON = (json) => {
  console.log("PARARSARAS")
  if (typeof props.mapData != "undefined") {
    const data = JSON.parse(props.mapData);
    if (Array.isArray(data.rooms)) {
      rooms.value = data.rooms.map((room) => ({
        ...room,
        x: room.x,
        y: room.y,
        width: room.width,
        length: room.length,
        status: room.status,
        type: room.type,
        department: room.department,
        workspaces: room.workspaces || [],
      }));
    }
  }
};

watch(() => props.mapData, () => {
  console.log(props.mapData)
  renderFromJSON(props.mapData);
})

const selectRoom = (room) => {
  if (selectedWorkspace.value != null &&
      room.id !== selectedWorkspace.value.roomId) {
    deselectAll()
  }

  selectedRoom.value = room
  emits("roomSelected", room)
}

const selectWorkspace = (workspace) => {
  selectedWorkspace.value = workspace
  if (workspace != null) {
    rooms.value.forEach(room => {
      if (room.id === workspace.roomId) {
        selectRoom(room)
      }
    })
  }
  emits("workspaceSelected", workspace)
}

const deselectAll = () => {
  selectWorkspace(null);
  selectRoom(null);
  emits("deselectAll")
}

</script>

<template>
  <div id="booking-map" class="map-container">
    <!-- Панель управления -->
<!--    <div class="controls">-->
<!--      <button @click="deselectAll">Убрать выделение</button>-->
<!--    </div>-->
    <hr>
    <!-- Комнаты -->
    <div
        v-for="room in rooms"
        :key="room.id"
        :id="'room-i-' + room.id"
        class="room"
        :style="{
        width: room.width * scale + 'px',
        height: room.length * scale + 'px',
        backgroundColor: room.type.name === 'Переговорная' ? '#aeaeff' : '#cccccc',
        borderWidth:  room.type.name === 'Переговорная' ? 5 + 'px' : 0 + 'px',
        borderColor: room === selectedRoom ? 'yellow' : room.status.color,
        transform: `translate(${room.x}px, ${room.y}px)`,
      }"
        @click="selectRoom(room)"
    >
      {{ room.name }}

      <!-- Рабочие места внутри комнаты -->
      <div
          v-for="wp in room.workspaces"
          :key="wp.id"
          :id="'wp-i-' + wp.id"
          class="workspace"
          :style="{
          width: '30px',
          height: '30px',
          backgroundColor: 'blue',
          border: '1px solid',
          borderColor: wp === selectedWorkspace ? 'yellow' : 'black',
          position: 'absolute',
          transform: `translate(${wp.x}px, ${wp.y}px)`,
        }"
          @click.stop="selectWorkspace(wp)"
      >
        {{ wp.name }}
      </div>
    </div>


  </div>
</template>

<style scoped>
.map-container {
  //position: relative;
  width: 100%;
  height: 100%;
  background-color: #f0f0f0;
  overflow: hidden;
}

.room {
  position: absolute;
  border: 2px solid black;
  cursor: move;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 14px;
  user-select: none;
}

.workspace {
  border-radius: 50%;
  cursor: move;
  color: white;
  text-align: center;
  font-size: 10px;
  user-select: none;
}

.controls {
  margin: 10px 0 0 10px;
}
</style>
