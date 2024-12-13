<script setup>

import InteractiveMapTab from "@/components/views/account-pages/office/InteractiveMapTab.vue";
import OfficeManagementTab from "@/components/views/account-pages/office/OfficeManagementTab.vue";
import {onMounted, ref} from "vue";
import StatusesAndTypesManagementTab from "@/components/views/account-pages/office/StatusesAndTypesManagementTab.vue";

const isInteractiveMap = ref(true)
const isOfficeManagement = ref(false)
const isStatusesAndTypes = ref(false)

onMounted(() => {

  const $menuBtn = $(".tab-button");
  $menuBtn.unbind()

  $menuBtn.on("click", (e) => {
    [...$menuBtn].forEach((el) => {
      el.className = "tab-button"
    })

    switch (e.currentTarget.id) {
      case "interactive-map-tab": {
        isInteractiveMap.value = true
        isOfficeManagement.value = false
        isStatusesAndTypes.value = false
        break
      }
      case "office-management-tab": {
        isInteractiveMap.value = false
        isOfficeManagement.value = true
        isStatusesAndTypes.value = false
        break
      }
      case "statuses-types-tab": {
        isInteractiveMap.value = false
        isOfficeManagement.value = false
        isStatusesAndTypes.value = true
        break
      }
    }

    e.currentTarget.className = "tab-button select"
  })
})
</script>

<template>
  <div class="view-header">
    Управление офисами
  </div>

  <div class="body-container">
    <div class="tab-menu">
      <button id="interactive-map-tab" class="tab-button select">Интерактивная карта</button>
      <button id="office-management-tab" class="tab-button">Управление офисами</button>
      <button id="statuses-types-tab" class="tab-button">Управление типами и статусами</button>
    </div>
    <div class="content-container">
      <div id="interactive-map-tab-container" v-if="isInteractiveMap">
        <InteractiveMapTab/>
      </div>
      <div id="office-management-tab-container" v-if="isOfficeManagement">
        <OfficeManagementTab/>
      </div>
      <div id="office-management-tab-container" v-if="isStatusesAndTypes">
        <StatusesAndTypesManagementTab/>
      </div>
    </div>
  </div>
</template>

<style scoped>

.body-container {
  margin: 0 auto;
  width: 98%;
}

.content-container {
  padding: 10px;
  border-radius: 0 10px 10px 10px;
  border: 2px solid #b1c5d8;
}

.tab-menu {
  display: flex;
  font-size: 1em;
}

.tab-menu > div {
  padding: 3px 10px;
  border-radius: 5px;
  border: 2px solid #b1c5d8;
  cursor: pointer;
}

.tab-menu > div {
  padding: 3px 10px;
  border-radius: 3px;
  border: 1px solid #b1c5d8;
  cursor: pointer;
  font-weight: bold;
  color: white;
  background-color: #a1c9f8;
}

.tab-menu .select {
  background-color: #3082bf;
}

button {
  font-size: 1em;
  border-bottom-left-radius: 0;
  border-bottom-right-radius: 0;
}
</style>