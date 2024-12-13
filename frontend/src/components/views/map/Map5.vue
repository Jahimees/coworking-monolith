<script setup>
import {onMounted, ref} from 'vue';
import interact from 'interactjs';
import Utils from "@/scripts/Utils.js";

const scale = 20; // Коэффициент масштабирования
const rooms = ref([]); // Список помещений
const selectedRoom = ref(null); // Выбранное помещение
const selectedWorkspace = ref(null); // Выбранное рабочее место

const props = defineProps({
  mapData: {
    type: String
  },
  roomInputs: {
    type: Object
  }
})

const emits = defineEmits(["updateRooms", "roomSelected", "workspaceSelected", "deselectAll", "returnRooms"]);

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
    workspaces: [], // Рабочие места внутри помещения
  };
  rooms.value.push(newRoom);
  initializeRoomDrag(newRoom);
}

function getRooms() {
  emits('returnRooms', rooms)
}

defineExpose({
  addRoom,
  getRooms
})

// Добавление рабочего места
const addWorkspace = () => {
  if (selectedRoom.value) {
    const newWorkspace = {
      id: Date.now(),
      name: `WP ${selectedRoom.value.workspaces.length + 1}`,
      x: scale,
      y: scale,
      roomId: selectedRoom.value.id
    };
    selectedRoom.value.workspaces.push(newWorkspace);
    initializeWorkspaceDrag(selectedRoom.value, newWorkspace);
  }
};

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
const initializeWorkspaceDrag = (room, workspace) => {
  interact(`#wp-${workspace.id}`).draggable({
    listeners: {
      move(event) {
        const wpIndex = room.workspaces.findIndex((wp) => wp.id === workspace.id);
        if (wpIndex !== -1) {
          const wp = room.workspaces[wpIndex];

          // Новые координаты рабочего места
          const newX = wp.x + event.dx;
          const newY = wp.y + event.dy;

          //10 - половина размера раб. места (20пх) 20пх - размер раб места.
          // Ограничение движения внутри комнаты
          wp.x = Math.max(-room.width * scale / 2, Math.min(newX, room.width * scale / 2));
          wp.y = Math.max(-room.height * scale / 2, Math.min(newY, room.height * scale / 2));

          const wpElement = document.getElementById(`wp-${workspace.id}`);
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
        workspaces: room.workspaces || [],
      }));

      // Применяем интерактивность для всех элементов
      rooms.value.forEach((room) => {
        initializeRoomDrag(room);
        room.workspaces.forEach((wp) => initializeWorkspaceDrag(room, wp));
      });
    }
  }
};

// Загрузка примера карты
renderFromJSON();

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
          v-for="wp in room.workspaces"
          :key="wp.id"
          :id="'wp-' + wp.id"
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

    <!-- Панель управления -->
    <div class="controls">
      ROOM: {{ selectedRoom }}
      WORKSPACE: {{ selectedWorkspace }}
      <button @click="addRoom">Добавить помещение</button>
      <button @click="addWorkspace" :disabled="!selectedRoom">Добавить рабочее место</button>
      <button @click="deleteWorkspace" :disabled="!selectedWorkspace">Удалить рабочее место</button>
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

.workspace {
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
