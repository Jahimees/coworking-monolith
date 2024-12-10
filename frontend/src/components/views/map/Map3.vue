<script setup>
import { reactive, ref, computed } from "vue";

const rooms = ref([]); // Список помещений
const currentRoom = reactive({ id: null, name: "", width: 10, height: 10, x: 0, y: 0, status: "default" });
const selectedRoom = ref(null);

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

// Добавление или обновление помещения
const addRoom = () => {
  if (currentRoom.id === null) {
    // Добавление нового помещения
    const newRoom = { ...currentRoom, id: Date.now() };
    rooms.value.push(newRoom);
  } else {
    // Обновление существующего
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

// Сброс текущего редактируемого помещения
const resetCurrentRoom = () => {
  Object.assign(currentRoom, { id: null, name: "", width: 10, height: 10, x: 0, y: 0, status: "default" });
};

// Выбор помещения для редактирования
const selectRoom = (room) => {
  Object.assign(currentRoom, room);
  selectedRoom.value = room;
};

// Логика Drag-and-Drop
let draggedRoom = null;

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

// Метод для рендеринга карты из JSON
const renderMapFromJSON = (json) => {
  rooms.value = json.map((room) => ({
    ...room,
    x: room.x || 0,
    y: room.y || 0,
  }));
};

// Пример JSON для теста
const exampleJSON = [
  { id: 1, name: "Комната 1", width: 10, height: 10, x: 5, y: 5, status: "repair" },
  { id: 2, name: "Комната 2", width: 15, height: 10, x: 20, y: 10, status: "default" },
];
renderMapFromJSON(exampleJSON);
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