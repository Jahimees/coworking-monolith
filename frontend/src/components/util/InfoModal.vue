<script setup>

const $emit = defineEmits(['close'])

defineProps({
  title: {
    type: String,
    required: true
  },
  message: {
    type: String,
    required: true
  }
})

function close() {
  $emit('close')
}

</script>

<template>
  <transition name="modal-fade">
    <div class="modal-backdrop">
      <div class="modal"
           role="dialog"
           aria-labelledby="modalTitle"
           aria-describedby="modalDescription">
        <header
            class="modal-header"
            id="modalTitle">
          <slot name="header">
            {{ title }}

            <button
                type="button"
                class="btn-close"
                @click="close"
                aria-label="Close modal">
            </button>
          </slot>
        </header>
        <section
            class="modal-body"
            id="modalDescription">
          <slot name="body">
            <div id="modal-body">
              {{ message }}
            </div>
          </slot>
        </section>
        <footer class="modal-footer">
          <slot name="footer">
            <button
                type="button"
                @click="close"
                aria-label="Close modal">
              Закрыть
            </button>
          </slot>
        </footer>
      </div>
    </div>
  </transition>
</template>

<style scoped>
.modal-backdrop {
  position: fixed;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.3);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal {
  background: #FFFFFF;
  box-shadow: 2px 2px 20px 1px;
  overflow-x: auto;
  display: flex;
  flex-direction: column;
  width: 35%;
  top: 100px;
  left: auto;
  height: fit-content;
}

.modal-header,
.modal-footer {
  padding: 15px;
  display: flex;
}

.modal-header {
  font-size: 1.5em;
  border-bottom: 1px solid #eeeeee;
  color: #3082bf;
  justify-content: space-between;
}

.modal-footer {
  border-top: 1px solid #eeeeee;
  justify-content: flex-end;
}

.modal-body {
  position: relative;
  padding: 20px 10px;
}

#modal-body {
  display: flex;
  margin: 0 0 0 0;
  font-size: 1.2em;
}

</style>