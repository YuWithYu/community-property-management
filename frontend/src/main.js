import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import axios from 'axios'

Vue.use(ElementUI)

axios.defaults.baseURL = '/api'
axios.defaults.timeout = 10000

axios.interceptors.request.use(
  config => {
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

axios.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 200) {
      ElementUI.Message.error(res.msg || '请求失败')
      if (res.code === 401) {
        store.dispatch('logout')
        router.push('/login')
      }
      return Promise.reject(new Error(res.msg || '请求失败'))
    }
    return res
  },
  error => {
    ElementUI.Message.error(error.message || '网络错误')
    return Promise.reject(error)
  }
)

Vue.prototype.$axios = axios
Vue.prototype.$http = axios

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
