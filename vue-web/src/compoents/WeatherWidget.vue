<template>
  <div class="weather-widget">
    <div class="widget-title">🌍 实时天气</div>

    <div v-if="loading" class="weather-center">
      <div class="loading-spinner"></div>
      <span class="hint-text">获取位置中...</span>
    </div>

    <div v-else-if="error" class="weather-center">
      <div style="font-size: 2.2rem;">🌐</div>
      <span class="hint-text">{{ error }}</span>
    </div>

    <template v-else>
      <div class="weather-city">📍 {{ city }}</div>
      <div class="weather-icon">{{ weatherIcon }}</div>
      <div class="weather-temp">
        {{ weather.temperature }}<span class="temp-unit">°C</span>
      </div>
      <div class="weather-desc">{{ weatherDesc }}</div>

      <div class="weather-divider"></div>

      <div class="weather-details">
        <div class="detail-item">
          <span class="detail-icon">💧</span>
          <span class="detail-label">湿度</span>
          <span class="detail-value">{{ weather.humidity }}%</span>
        </div>
        <div class="detail-item">
          <span class="detail-icon">💨</span>
          <span class="detail-label">风速</span>
          <span class="detail-value">{{ weather.windSpeed }}<small> km/h</small></span>
        </div>
      </div>

      <div class="update-time">更新于 {{ updateTime }}</div>
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const loading = ref(true)
const error = ref('')
const city = ref('')
const updateTime = ref('')
const weatherIcon = ref('🌡️')
const weatherDesc = ref('')
const weather = ref({ temperature: '--', humidity: '--', windSpeed: '--' })

// WMO Weather interpretation codes
const WMO_MAP = {
  0:  ['☀️',  '晴天'],
  1:  ['🌤️', '晴间少云'],
  2:  ['⛅',  '晴间多云'],
  3:  ['☁️',  '阴天'],
  45: ['🌫️', '雾'],
  48: ['🌫️', '霜雾'],
  51: ['🌦️', '轻毛毛雨'],
  53: ['🌦️', '毛毛雨'],
  55: ['🌧️', '重毛毛雨'],
  61: ['🌧️', '小雨'],
  63: ['🌧️', '中雨'],
  65: ['🌧️', '大雨'],
  71: ['🌨️', '小雪'],
  73: ['🌨️', '中雪'],
  75: ['🌨️', '大雪'],
  77: ['🌨️', '雪粒'],
  80: ['🌦️', '阵雨'],
  81: ['🌦️', '中阵雨'],
  82: ['🌧️', '强阵雨'],
  85: ['🌨️', '阵雪'],
  86: ['🌨️', '强阵雪'],
  95: ['⛈️',  '雷阵雨'],
  96: ['⛈️',  '雷暴冰雹'],
  99: ['⛈️',  '强雷暴'],
}

const getWeatherInfo = (code) => {
  return WMO_MAP[code] ?? WMO_MAP[Math.floor(code / 10) * 10] ?? ['🌡️', '未知天气']
}

const fetchWeather = async (lat, lon) => {
  try {
    const [weatherRes, geoRes] = await Promise.all([
      fetch(
        `https://api.open-meteo.com/v1/forecast?latitude=${lat}&longitude=${lon}` +
        `&current=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m&timezone=auto`
      ),
      fetch(
        `https://nominatim.openstreetmap.org/reverse?format=json&lat=${lat}&lon=${lon}&accept-language=zh-CN`
      ),
    ])

    const weatherData = await weatherRes.json()
    const geoData = await geoRes.json()

    const cur = weatherData.current
    weather.value = {
      temperature: Math.round(cur.temperature_2m),
      humidity: cur.relative_humidity_2m,
      windSpeed: Math.round(cur.wind_speed_10m),
    }
    const [icon, desc] = getWeatherInfo(cur.weather_code)
    weatherIcon.value = icon
    weatherDesc.value = desc

    const addr = geoData.address ?? {}
    city.value = addr.city ?? addr.town ?? addr.county ?? addr.state ?? '未知城市'

    const now = new Date()
    updateTime.value = `${now.getHours().toString().padStart(2, '0')}:${now.getMinutes().toString().padStart(2, '0')}`
  } catch {
    error.value = '天气获取失败'
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
      (pos) => fetchWeather(pos.coords.latitude, pos.coords.longitude),
      () => fetchWeather(39.9042, 116.4074) // 默认北京
    )
  } else {
    fetchWeather(39.9042, 116.4074)
  }
})
</script>

<style scoped>
.weather-widget {
  padding: 18px 14px;
  text-align: center;
  color: #2c3e50;
  height: 100%;
  box-sizing: border-box;
}

.widget-title {
  font-size: 13px;
  font-weight: 700;
  color: #3a7bd5;
  margin-bottom: 14px;
  letter-spacing: 1px;
  text-transform: uppercase;
}

.weather-center {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  padding: 24px 0;
}

.loading-spinner {
  width: 28px;
  height: 28px;
  border: 3px solid #dce7f5;
  border-top-color: #3a7bd5;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.hint-text {
  font-size: 11px;
  color: #999;
}

.weather-city {
  font-size: 12px;
  color: #5a7a9a;
  margin-bottom: 6px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.weather-icon {
  font-size: 3.2rem;
  line-height: 1.1;
  margin: 6px 0;
  filter: drop-shadow(0 2px 4px rgba(0,0,0,0.15));
}

.weather-temp {
  font-size: 2.2rem;
  font-weight: 800;
  color: #2c3e50;
  line-height: 1;
}

.temp-unit {
  font-size: 1.1rem;
  font-weight: 400;
  color: #666;
}

.weather-desc {
  font-size: 12px;
  color: #7a9ab8;
  margin: 6px 0 10px;
}

.weather-divider {
  height: 1px;
  background: linear-gradient(90deg, transparent, rgba(58, 123, 213, 0.25), transparent);
  margin: 8px 0;
}

.weather-details {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-top: 4px;
}

.detail-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  padding: 4px 6px;
  background: rgba(255, 255, 255, 0.5);
  border-radius: 8px;
}

.detail-icon {
  font-size: 14px;
}

.detail-label {
  color: #888;
  flex: 1;
  text-align: left;
}

.detail-value {
  font-weight: 700;
  color: #2c3e50;
}

.update-time {
  margin-top: 12px;
  font-size: 10px;
  color: #bbb;
}
</style>
