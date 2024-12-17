<script setup>
import { ref, watch, defineProps } from "vue";
import {BarChart} from "vue-chart-3";
import { Chart, registerables } from "chart.js";

// Регистрация компонентов Chart.js
Chart.register(...registerables);

// Получаем пропсы от родительского компонента
const props = defineProps({
  inputData: {
    type: Object,
    required: true,
  },
});

// Реактивные данные для графика
const chartData = ref(null);

// Опции графика
const chartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  scales: {
    x: {
      title: { display: true, text: "Время суток" },
      grid: { display: false },
    },
    y: {
      title: { display: true, text: "Частота бронирования" },
      beginAtZero: true,
      ticks: { stepSize: 1 },
    },
  },
  plugins: { legend: { display: false } },
};

// Функция для пересчета chartData из входных данных
const updateChartData = () => {
  const labels = Object.keys(props.inputData);
  const data = Object.values(props.inputData);

  chartData.value = {
    labels,
    datasets: [
      {
        label: "Количество бронирований",
        data,
        backgroundColor: "rgba(169, 217, 132, 0.6)",
        borderColor: "#A9D984",
        borderWidth: 1,
      },
    ],
  };
};

// Следим за изменениями inputData
watch(() => props.inputData, updateChartData, { immediate: true });
</script>

<template>
  <div ref="chartContainer" class="chart-container">
    <h3>Частота бронирования по часам</h3>
    <BarChart
        v-if="chartData"
        :chart-data="chartData"
        :chart-options="chartOptions"
    />
  </div>
</template>

<style scoped>
h3 {
  text-align: center;
  margin-bottom: 20px;
}
div {
  height: 400px;
}
</style>
