import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    user: JSON.parse(localStorage.getItem('user') || '{}'),
    userType: localStorage.getItem('userType') || ''
  },
  mutations: {
    SET_USER(state, { user, type }) {
      state.user = user
      state.userType = type
      localStorage.setItem('user', JSON.stringify(user))
      localStorage.setItem('userType', type)
    },
    CLEAR_USER(state) {
      state.user = {}
      state.userType = ''
      localStorage.removeItem('user')
      localStorage.removeItem('userType')
    }
  },
  actions: {
    login({ commit }, data) {
      commit('SET_USER', data)
    },
    logout({ commit }) {
      commit('CLEAR_USER')
    }
  }
})
