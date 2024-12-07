<script setup>
import {computed, onMounted, ref, watch} from 'vue';

const $props = defineProps({
  options: Array,
  defaultValue: Object
})

const $emits = defineEmits(['returnId'])

// Исходные данные
const options = ref($props.options);

const searchQuery = ref('');
const isDropdownOpen = ref(false);
const selectedOption = ref($props.defaultValue);
const selectedId = ref(null);

watch(() => selectedOption, async (selectedOption1) => {
  searchQuery.value = selectedOption1.name
  selectedId.value = selectedOption1.id
})

// Фильтрация опций на основе поискового запроса
const filteredOptions = computed(() =>
    options.value.filter((option) =>
        option.name.toLowerCase().includes(searchQuery.value.toLowerCase())
    )
);

const selectOption = (option) => {

  selectedOption.value = option;
  searchQuery.value = option?.name
  selectedId.value = option?.id

  $emits('returnId', selectedId.value)
};

const closeDropDownAndSelect = () => {
  setTimeout(() => {
    searchQuery.value = selectedOption.value.name
    selectedId.value = selectedOption.value.id

    $emits('returnId', selectedId.value)

    isDropdownOpen.value = false;
  }, 100)
}

onMounted(() => {
  let observer = new MutationObserver(() => {
    console.log($props.defaultValue)
    console.log($props.options[0])
    if ($props.defaultValue != null && typeof $props.defaultValue !== "undefined"
        && $props.defaultValue.id !== '' && typeof $props.defaultValue.id !== "undefined") {
      selectOption($props.defaultValue)
    } else {
      selectOption($props.options[0])
    }
  })

  observer.observe($(".modal-backdrop")[0], {
    attributes: true
  })
})

</script>

<template>
  <div>
    <input class="select-input"
           type="text"
           v-model="searchQuery"
           placeholder="Поиск..."
           @focus="isDropdownOpen = true"
           @focusout="closeDropDownAndSelect()"
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