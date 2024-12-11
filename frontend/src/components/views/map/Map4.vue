<script setup>
import { reactive, ref } from "vue";

const rooms = ref([]); // Список помещений
const currentRoom = reactive({ id: null, name: "", width: 10, height: 10, x: 0, y: 0, status: "default", workplaces: [] });
const currentWorkplace = reactive({ id: null, name: "", x: 0, y: 0 });
const selectedRoom = ref(null);
const selectedWorkplace = ref(null);

const scale = 20; // Коэффициент перевода метров в пиксели

// Вычисляемый стиль для комнаты
const getRoomStyle = (room) => ({
  width: `${room.width * scale}px`,
  height: `${room.height * scale}px`,
  left: `${room.x * scale}px`,
  top: `${room.y * scale}px`,
  position: "absolute",
  backgroundColor: room.status === "repair" ? "red" : "lightgray",
  cursor: "move",
});

// Вычисляемый стиль для рабочего места
const getWorkplaceStyle = (workplace) => ({
  width: "10px",
  height: "10px",
  left: `${workplace.x * scale}px`,
  top: `${workplace.y * scale}px`,
  position: "absolute",
  backgroundColor: "blue",
});

// Добавление или обновление помещения
const addRoom = () => {
  if (currentRoom.id === null) {
    const newRoom = { ...currentRoom, id: Date.now(), workplaces: [] };
    rooms.value.push(newRoom);
  } else {
    const index = rooms.value.findIndex((r) => r.id === currentRoom.id);
    if (index !== -1) {
      rooms.value[index] = { ...currentRoom };
    }
  }
  resetCurrentRoom();
};

// Удаление помещения
const deleteRoom = () => {
  if (selectedRoom.value) {
    rooms.value = rooms.value.filter((r) => r.id !== selectedRoom.value.id);
    selectedRoom.value = null;
    resetCurrentRoom();
  }
};

// Добавление или обновление рабочего места
const addWorkplace = () => {
  if (selectedRoom.value) {
    if (currentWorkplace.id === null) {
      const newWorkplace = { ...currentWorkplace, id: Date.now() };
      selectedRoom.value.workplaces.push(newWorkplace);
    } else {
      const index = selectedRoom.value.workplaces.findIndex((w) => w.id === currentWorkplace.id);
      if (index !== -1) {
        selectedRoom.value.workplaces[index] = { ...currentWorkplace };
      }
    }
    resetCurrentWorkplace();
  }
};

// Удаление рабочего места
const deleteWorkplace = () => {
  if (selectedRoom.value && selectedWorkplace.value) {
    selectedRoom.value.workplaces = selectedRoom.value.workplaces.filter((w) => w.id !== selectedWorkplace.value.id);
    selectedWorkplace.value = null;
    resetCurrentWorkplace();
  }
};

// Выбор помещения
const selectRoom = (room) => {
  Object.assign(currentRoom, room);
  selectedRoom.value = room;
  selectedWorkplace.value = null;
};

// Выбор рабочего места
const selectWorkplace = (workplace, room) => {
  Object.assign(currentWorkplace, workplace);
  selectedRoom.value = room;
  selectedWorkplace.value = workplace;
};

// Сброс текущего редактируемого помещения
const resetCurrentRoom = () => {
  Object.assign(currentRoom, { id: null, name: "", width: 10, height: 10, x: 0, y: 0, status: "default", workplaces: [] });
};

// Сброс текущего редактируемого рабочего места
const resetCurrentWorkplace = () => {
  Object.assign(currentWorkplace, { id: null, name: "", x: 0, y: 0 });
};

// Логика Drag-and-Drop
let draggedRoom = null;
let draggedWorkplace = null;

const onDragStart = (room) => {
  draggedRoom = room;
};

const onDrop = (event) => {
  if (draggedRoom) {
    const mapRect = event.currentTarget.getBoundingClientRect();
    const x = (event.clientX - mapRect.left) / scale;
    const y = (event.clientY - mapRect.top) / scale;
    draggedRoom.x = Math.max(0, x);
    draggedRoom.y = Math.max(0, y);
    draggedRoom = null;
  }
};

const onWorkplaceDragStart = (workplace, room) => {
  draggedWorkplace = { workplace, room };
};

const onDropWorkplace = (event) => {
  if (draggedWorkplace) {
    const mapRect = event.currentTarget.getBoundingClientRect();
    const x = (event.clientX - mapRect.left) / scale;
    const y = (event.clientY - mapRect.top) / scale;
    draggedWorkplace.workplace.x = Math.max(0, x);
    draggedWorkplace.workplace.y = Math.max(0, y);
    draggedWorkplace = null;
  }
};
</script>

<template>
  <div class="office-management">
    <div class="sidebar">
      <h3>Создать/редактировать помещение</h3>
      <div>
        <label>Название:</label>
        <input v-model="currentRoom.name" placeholder="Название" />
      </div>
      <div>
        <label>Ширина:</label>
        <input v-model.number="currentRoom.width" type="number" placeholder="Ширина (м)" />
      </div>
      <div>
        <label>Длина:</label>
        <input v-model.number="currentRoom.height" type="number" placeholder="Длина (м)" />
      </div>
      <div>
        <label>Статус:</label>
        <select v-model="currentRoom.status">
          <option value="default">Светло-серый</option>
          <option value="repair">На ремонте</option>
        </select>
      </div>
      <button @click="addRoom">Сохранить</button>
      <button @click="deleteRoom" :disabled="!selectedRoom">Удалить</button>

      <h3>Создать/редактировать рабочее место</h3>
      <div>
        <label>Название:</label>
        <input v-model="currentWorkplace.name" placeholder="Название рабочего места" />
      </div>
      <div>
        <label>Позиция (X):</label>
        <input v-model.number="currentWorkplace.x" type="number" placeholder="X (м)" />
      </div>
      <div>
        <label>Позиция (Y):</label>
        <input v-model.number="currentWorkplace.y" type="number" placeholder="Y (м)" />
      </div>
      <button @click="addWorkplace" :disabled="!selectedRoom">Добавить рабочее место</button>
      <button @click="deleteWorkplace" :disabled="!selectedWorkplace">Удалить</button>
    </div>

    <div class="map-container">
      <div
          class="map"
          @drop="onDrop"
          @dragover.prevent
      >
        <div
            v-for="room in rooms"
            :key="room.id"
            class="room"
            :style="getRoomStyle(room)"
            @click="selectRoom(room)"
            :class="{ active: selectedRoom && selectedRoom.id === room.id }"
            :draggable="true"
            @dragstart="onDragStart(room)"
        >
          {{ room.name }}
          <div
              v-for="workplace in room.workplaces"
              :key="workplace.id"
              class="workplace"
              :style="getWorkplaceStyle(workplace)"
              @click.stop="selectWorkplace(workplace, room)"
              :class="{ active: selectedWorkplace && selectedWorkplace.id === workplace.id }"
              :draggable="true"
              @dragstart="onWorkplaceDragStart(workplace, room)"
          >
            {{ workplace.name }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.office-management {
  display: flex;
  flex-direction: row;
  gap: 20px;
}

.sidebar {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.map-container {
  flex: 2;
  position: relative;
}

.map {
  position: relative;
  width: 800px;
  height: 600px;
  background-color: #f5f5f5;
  border: 1px solid #ccc;
  overflow: hidden;
}

.room {
  display: flex;
  align-items: center;
  justify-content: center;
  border: 2px solid transparent;
  box-sizing: border-box;
}

.room.active {
  border: 2px solid yellow;
}
</style>