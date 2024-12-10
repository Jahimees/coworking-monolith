<script setup>
import {ref} from "vue";

// Принимаем `props` через Composition API
const props = defineProps({
  mapData: {
    type: Object,
    required: true,
  },
});

// Локальные состояния
const rooms = ref(props.mapData.rooms);
const workplaces = ref(props.mapData.workplaces);
const selectedRoom = ref(null);
const selectedWorkplace = ref(null);

// Методы
const getRoomStyle = (room) => ({
  position: "absolute",
  left: `${room.x * 20}px`,
  top: `${room.y * 20}px`,
  width: `${room.width * 20}px`,
  height: `${room.height * 20}px`,
  backgroundColor: room.color || "lightgray",
  border: selectedRoom.value?.id === room.id ? "2px solid yellow" : "1px solid black",
});

const getWorkplaceStyle = (workplace) => {
  const room = rooms.value.find((r) => r.id === workplace.roomId);
  return {
    position: "absolute",
    left: `${(room.x + workplace.x) * 20}px`,
    top: `${(room.y + workplace.y) * 20}px`,
    width: "20px",
    height: "20px",
    backgroundColor: "blue",
    border: selectedWorkplace.value?.id === workplace.id ? "2px solid yellow" : "1px solid black",
  };
};

const selectRoom = (room) => {
  selectedRoom.value = room;
  emit("room-selected", room); // Используем `emit` для события
};

const selectWorkplace = (workplace) => {
  selectedWorkplace.value = workplace;
  emit("workplace-selected", workplace);
};

// Эмит событий для взаимодействия с родительским компонентом
const emit = defineEmits(["room-selected", "workplace-selected"]);
</script>

<template>
  <div class="interactive-map">
    <div
        v-for="room in rooms"
        :key="room.id"
        class="room"
        :style="getRoomStyle(room)"
        @click="selectRoom(room)"
    >
      <div class="room-label">{{ room.name }}</div>
    </div>

    <div
        v-for="workplace in workplaces"
        :key="workplace.id"
        class="workplace"
        :style="getWorkplaceStyle(workplace)"
        @click="selectWorkplace(workplace)"
    >
      <div class="workplace-label">{{ workplace.name }}</div>
    </div>
  </div>
</template>

<style scoped>
.interactive-map {
  position: relative;
  width: 900px;
  height: 600px;
  border: 1px solid #ccc;
  overflow: hidden;
  background-color: #f9f9f9;
}

.room {
  position: absolute;
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
  color: white;
  font-weight: bold;
}

.workplace {
  position: absolute;
  cursor: pointer;
  background-color: #007bff;
  display: flex;
  justify-content: center;
  align-items: center;
  color: white;
  font-size: 12px;
}

.room-label {
  pointer-events: none;
}

.workplace-label {
  pointer-events: none;
}
</style>