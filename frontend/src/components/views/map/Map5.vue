<script setup>
import {onMounted, ref} from 'vue';
import interact from 'interactjs';
import Utils from "@/scripts/Utils.js";

const scale = 20; // Коэффициент масштабирования
const rooms = ref([]); // Список помещений
const selectedRoom = ref(null); // Выбранное помещение
const selectedWorkplace = ref(null); // Выбранное рабочее место

const props = defineProps({
  mapData: {
    type: String
  },
  roomInputs: {
    type: Object
  }
})

const emits = defineEmits(["updateRooms", "roomSelected", "workspaceSelected", "deselectAll"]);

// Добавление нового помещения
function addRoom() {
  console.log(props.roomInputs)
  const newRoom = {
    id: Utils.UUID(),
    name: props.roomInputs.name,
    width: props.roomInputs.width,
    height: props.roomInputs.height,
    x: 10,
    y: 10,
    color: 'lightgray',
    workplaces: [], // Рабочие места внутри помещения
  };
  rooms.value.push(newRoom);
  initializeRoomDrag(newRoom);
}

defineExpose({
  addRoom
})

// Добавление рабочего места
const addWorkplace = () => {
  if (selectedRoom.value) {
    const newWorkplace = {
      id: Date.now(),
      name: `WP ${selectedRoom.value.workplaces.length + 1}`,
      x: scale,
      y: scale,
      roomId: selectedRoom.value.id
    };
    selectedRoom.value.workplaces.push(newWorkplace);
    initializeWorkplaceDrag(selectedRoom.value, newWorkplace);
  }
};

// Удаление выбранного рабочего места
const deleteWorkplace = () => {
  if (selectedRoom.value && selectedWorkplace.value) {
    selectedRoom.value.workplaces = selectedRoom.value.workplaces.filter(
        (wp) => wp.id !== selectedWorkplace.value.id
    );
    selectedWorkplace.value = null;
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
    width: $("#map").width(),
    height: $("#map").height()
  }
})


// Инициализация драг-н-дропа для помещения
const initializeRoomDrag = (room) => {
  interact(`#room-${room.id}`).draggable({
    listeners: {
      move(event) {
        const index = rooms.value.findIndex((r) => r.id === room.id);
        if (index !== -1) {


          const roomData = rooms.value[index];
          // Новые координаты
          const newX = roomData.x + event.dx;
          const newY = roomData.y + event.dy;

          // Ограничение движения в пределах карты
          roomData.x = Math.max(0, Math.min(newX, mapBounds.width - roomData.width));
          roomData.y = Math.max(0, Math.min(newY, mapBounds.height - roomData.height));

          const target = event.target;
          target.style.transform = `translate(${roomData.x}px, ${roomData.y}px)`;
        }
      },
    },
  });
};

// Инициализация драг-н-дропа для рабочего места
const initializeWorkplaceDrag = (room, workplace) => {
  interact(`#wp-${workplace.id}`).draggable({
    listeners: {
      move(event) {
        const wpIndex = room.workplaces.findIndex((wp) => wp.id === workplace.id);
        if (wpIndex !== -1) {
          const wp = room.workplaces[wpIndex];

          // Новые координаты рабочего места
          const newX = wp.x + event.dx;
          const newY = wp.y + event.dy;

          //10 - половина размера раб. места (20пх) 20пх - размер раб места.
          // Ограничение движения внутри комнаты
          wp.x = Math.max(-room.width * scale / 2, Math.min(newX, room.width * scale / 2));
          wp.y = Math.max(-room.height * scale / 2, Math.min(newY, room.height * scale / 2));

          const wpElement = document.getElementById(`wp-${workplace.id}`);
          wpElement.style.transform = `translate(${wp.x}px, ${wp.y}px)`;
        }
      },
    },
  });
};

// Рендеринг из JSON
const renderFromJSON = (json) => {
  if (typeof props.mapData != "undefined") {
    const data = JSON.parse(props.mapData);
    if (Array.isArray(data.rooms)) {
      rooms.value = data.rooms.map((room) => ({
        ...room,
        x: room.x,
        y: room.y,
        width: room.width,
        height: room.height,
        workplaces: room.workplaces || [],
      }));

      // Применяем интерактивность для всех элементов
      rooms.value.forEach((room) => {
        initializeRoomDrag(room);
        room.workplaces.forEach((wp) => initializeWorkplaceDrag(room, wp));
      });
    }
  }
};

// Загрузка примера карты
renderFromJSON();

const selectRoom = (room) => {
  if (selectedWorkplace.value != null &&
      room.id !== selectedWorkplace.value.roomId) {
    deselectAll()
  }

  selectedRoom.value = room
  emits("roomSelected", room)
}

const selectWorkspace = (workspace) => {
  selectedWorkplace.value = workspace
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
  <div id="map" class="map-container">
    <!-- Комнаты -->
    <div
        v-for="room in rooms"
        :key="room.id"
        :id="'room-' + room.id"
        class="room"
        :style="{
        width: room.width * scale + 'px',
        height: room.height * scale + 'px',
        backgroundColor: room.status === 1 ? 'red' : '#a5a5a5',
        borderColor: room === selectedRoom ? 'yellow' : room.color,
        transform: `translate(${room.x}px, ${room.y}px)`,
      }"
        @click="selectRoom(room)"
    >
      {{ room.name }}

      <!-- Рабочие места внутри комнаты -->
      <div
          v-for="wp in room.workplaces"
          :key="wp.id"
          :id="'wp-' + wp.id"
          class="workplace"
          :style="{
          width: '30px',
          height: '30px',
          backgroundColor: 'blue',
          border: '1px solid',
          borderColor: wp === selectedWorkplace ? 'yellow' : 'black',
          position: 'absolute',
          transform: `translate(${wp.x}px, ${wp.y}px)`,
        }"
          @click.stop="selectWorkspace(wp)"
      >
        {{ wp.name }}
      </div>
    </div>

    <!-- Панель управления -->
    <div class="controls">
      ROOM: {{ selectedRoom }}
      WORKSPACE: {{ selectedWorkplace }}
      <button @click="addRoom">Добавить помещение</button>
      <button @click="addWorkplace" :disabled="!selectedRoom">Добавить рабочее место</button>
      <button @click="deleteWorkplace" :disabled="!selectedWorkplace">Удалить рабочее место</button>
      <button @click="deleteRoom">Удалить Комнату</button>
      <button @click="deselectAll">Убрать выделение</button>
    </div>
  </div>
</template>

<style scoped>
.map-container {
  position: relative;
  width: 100%;
  height: 100%;
  background-color: #f0f0f0;
  overflow: hidden;
  border: 1px solid #ccc;
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

.workplace {
  border-radius: 50%;
  cursor: move;
  color: white;
  text-align: center;
  font-size: 10px;
  user-select: none;
}

.controls {
  margin-top: 10px;
}
</style>
