<script setup>
import {onMounted, ref, watch} from 'vue';
import interact from 'interactjs';
import Utils from "@/scripts/Utils.js";

const scale = 27; // Коэффициент масштабирования
const rooms = ref([]); // Список помещений
const selectedRoom = ref(null); // Выбранное помещение
const selectedWorkspace = ref(null); // Выбранное рабочее место

const props = defineProps({
  mapData: {
    type: String
  },
  roomInputs: {
    type: Object
  },
  workspaceInputs: {
    type: Object
  }
})

const emits = defineEmits(["updateRooms", "roomSelected", "workspaceSelected", "deselectAll", "returnRooms",
  "noRoomSelected", "notEnoughPlace"]);

// Добавление нового помещения
function addRoom() {
  const newRoom = {
    id: Utils.UUID(),
    name: props.roomInputs.name,
    width: props.roomInputs.width,
    length: props.roomInputs.length,
    status: props.roomInputs.status,
    type: props.roomInputs.type,
    department: props.roomInputs.department,
    x: 10,
    y: 10,
    workspaces: [], // Рабочие места внутри помещения
  };
  rooms.value.push(newRoom);
  initializeRoomDrag(newRoom);
}

function getRooms() {
  emits('returnRooms', rooms)
}

// Добавление рабочего места
const addWorkspace = () => {
  if (selectedRoom.value == null || typeof selectedRoom.value === "undefined") {
    emits("noRoomSelected")
  return
  }

  let length = selectedRoom.value.length
  let width = selectedRoom.value.width;
  let square = length * width;

  let workspaceSquareRequired = (selectedRoom.value.workspaces.length + 1) * 4.5;

  if (square < workspaceSquareRequired) {
    emits("notEnoughPlace")
    return;
  }

  const newWorkspace = {
    id: Date.now(),
    name: props.workspaceInputs.name,
    user: props.workspaceInputs.user,
    x: scale,
    y: scale,
    roomId: selectedRoom.value.id
  };
  selectedRoom.value.workspaces.push(newWorkspace);
  initializeWorkspaceDrag(selectedRoom.value, newWorkspace);
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


defineExpose({
  addRoom,
  getRooms,
  addWorkspace,
  deleteRoom,
  deleteWorkspace
  // `WP ${selectedRoom.value.workspaces.length + 1}`
})

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
          roomData.y = Math.max(0, Math.min(newY, mapBounds.height - roomData.length));

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
          wp.y = Math.max(-room.length * scale / 2, Math.min(newY, room.length * scale / 2));

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
        length: room.length,
        status: room.status,
        type: room.type,
        department: room.department,
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

watch(() => props.mapData, () => {
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
  <div id="map" class="map-container">
    <!-- Комнаты -->
    <div
        v-for="room in rooms"
        :key="room.id"
        :id="'room-' + room.id"
        class="room"
        :style="{
        width: room.width * scale + 'px',
        height: room.length * scale + 'px',
        backgroundColor: room.type.color,
        borderWidth: 5 + 'px',
        borderColor: room === selectedRoom ? 'yellow' : room.status.color,
        transform: `translate(${room.x}px, ${room.y}px)`,
      }"
        @click="selectRoom(room)"
    >
      {{ room.name }}. {{room.department?.name}}

      <!-- Рабочие места внутри комнаты -->
      <div
          v-for="wp in room.workspaces"
          :key="wp.id"
          :id="'wp-' + wp.id"
          class="workspace"
          :style="{
            fontSize: '0.9em',
          width: '35px',
          height: '35px',
          backgroundColor: wp.user ? '#fa2363' : '#5ec463',
          border: '1px solid',
          borderColor: wp === selectedWorkspace ? 'yellow' : 'black',
          position: 'absolute',
          transform: `translate(${wp.x}px, ${wp.y}px)`,
        }"
          @click.stop="selectWorkspace(wp)"
      >
        {{ wp.name }} <div style="color: white; text-align: center">{{wp.user?.lastName}} </div>
      </div>
    </div>

    <!-- Панель управления -->
    <div class="controls">
      <button @click="deselectAll">Убрать выделение</button>
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
  margin: 10px 0 0 10px;
}
</style>
