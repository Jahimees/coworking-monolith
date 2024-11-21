<script setup>
import {computed, ref} from 'vue';

const $props = defineProps({
  options: Map,
})

const $emits = defineEmits(['returnId'])

// Исходные данные
const options = ref($props.options);

const searchQuery = ref("");
const isDropdownOpen = ref(false);
const selectedOption = ref(null);
const selectedId = ref(null);

// Фильтрация опций на основе поискового запроса
const filteredOptions = computed(() =>
    options.value.filter((option) =>
        option.name.toLowerCase().includes(searchQuery.value.toLowerCase())
    )
);

const selectOption = (option) => {
  selectedOption.value = option;
  searchQuery.value = option.name
  selectedId.value = option.id

  $emits('returnId', selectedId.value)

  isDropdownOpen.value = false;
};

// onMounted(() => {
//   $(".select-input").on("click", (e) => {
//     console.log("clicked")
//     e.target.value = ""
//   })
// })

</script>

<template>
  <div>
    <input class="select-input"
           type="text"
           v-model="searchQuery"
           placeholder="Поиск..."
           @focus="isDropdownOpen = true"
    />
    <div v-if="isDropdownOpen" class="dropdown">
      <div
          v-for="option in filteredOptions"
          :key="option.id"
          @click="selectOption(option)"
          class="dropdown-item"
      >
        {{ option.name }}
      </div>
    </div>
  </div>
</template>

<style>
.select-input {
  width: 100%;
}

.dropdown {
  border: 1px solid #ccc;
  max-height: 150px;
  overflow-y: auto;
  background-color: #d8e7f8;
  border-radius: 5px;
  position: absolute;
}

.dropdown-item {
  padding: 8px;
  cursor: pointer;
}

.dropdown-item:hover {
  background-color: #f0f0f0;
}
</style>