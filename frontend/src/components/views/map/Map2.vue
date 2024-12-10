<script setup>
import { ref, watch } from 'vue';
import interact from 'interactjs';

const rooms = ref([]);
const selectedRoom = ref(null);
const selectedWorkplace = ref(null);
const scale = 20;

const emits = defineEmits(["updateRooms"])

const addRoom = () => {
  rooms.value.push({
    id: Date.now(),
    name: `Room ${rooms.value.length + 1}`,
    width: 5 * scale,
    height: 5 * scale,
    x: 10,
    y: 10,
    color: 'lightgray', // Цвет по умолчанию
    status: 'default',
    type: 'meeting',
  });
};

const emitChanges = (type, payload) => {
  // Эмитим изменения наружу
  emits(type, payload)
  console.log(`Change emitted: ${type}`, payload);
};

watch(rooms, (newRooms) => {
  emitChanges('updateRooms', newRooms);
});

// Драг-н-дроп логика
watch(
    () => rooms.value,
    (newRooms) => {
      newRooms.forEach((room) => {
        interact(`#room-${room.id}`)
            .draggable({
              listeners: {
                move(event) {
                  const target = event.target;
                  const index = rooms.value.findIndex((r) => r.id === room.id);
                  if (index !== -1) {
                    rooms.value[index].x += event.dx;
                    rooms.value[index].y += event.dy;
                    target.style.transform = `translate(${rooms.value[index].x}px, ${rooms.value[index].y}px)`;
                  }
                },
              },
            });
      });
    },
    { immediate: true }
);

const renderFromJSON = (json) => {
  const data = JSON.parse(json);
  if (Array.isArray(data.rooms)) {
    rooms.value = data.rooms.map((room) => ({
      ...room,
      x: room.x * scale,
      y: room.y * scale,
      width: room.width * scale,
      height: room.height * scale,
    }));
  }
};

const exampleJSON = `
{
  "rooms": [
    { "id": 1, "name": "Room A", "x": 10, "y": 10, "width": 5, "height": 5, "color": "red", "status": "repair" },
    { "id": 2, "name": "Room B", "x": 20, "y": 15, "width": 4, "height": 6, "color": "green", "status": "free" }
  ]
}
`;

// Пример вызова для загрузки карты
renderFromJSON(exampleJSON);
</script>

<template>
  <div id="map" class="map-container">
    <div
        v-for="room in rooms"
        :key="room.id"
        :id="'room-' + room.id"
        class="room"
        :style="{
        width: room.width + 'px',
        height: room.height + 'px',
        backgroundColor: room.color,
        transform: `translate(${room.x}px, ${room.y}px)`,
      }"
        @click="selectedRoom = room"
    >
      {{ room.name }}
    </div>
    {{selectedRoom}}
  </div>
</template>

<style scoped>
.map-container {
  position: relative;
  width: 100%;
  height: 500px;
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
}
</style>