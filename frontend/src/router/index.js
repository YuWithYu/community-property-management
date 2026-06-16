import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '../store'

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/login/Login.vue'),
    meta: { public: true }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/login/Register.vue'),
    meta: { public: true }
  },
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/admin',
    name: 'Admin',
    component: () => import('../views/admin/Layout.vue'),
    children: [
      { path: 'dashboard', name: 'Dashboard', component: () => import('../views/admin/Dashboard.vue') },
      { path: 'owner', name: 'OwnerManage', component: () => import('../views/admin/OwnerManage.vue') },
      { path: 'property', name: 'PropertyManage', component: () => import('../views/admin/PropertyManage.vue') },
      { path: 'parking', name: 'ParkingManage', component: () => import('../views/admin/ParkingManage.vue') },
      { path: 'activity', name: 'ActivityManage', component: () => import('../views/admin/ActivityManage.vue') },
      { path: 'repair', name: 'RepairManage', component: () => import('../views/admin/RepairManage.vue') },
      { path: 'complaint', name: 'ComplaintManage', component: () => import('../views/admin/ComplaintManage.vue') },
      { path: 'fee', name: 'FeeManage', component: () => import('../views/admin/FeeManage.vue') },
      { path: 'visitor', name: 'VisitorManage', component: () => import('../views/admin/VisitorManage.vue') }
    ]
  },
  {
    path: '/home',
    name: 'UserLayout',
    component: () => import('../views/user/Layout.vue'),
    children: [
      { path: '', name: 'Home', component: () => import('../views/user/Home.vue') },
      { path: 'profile', name: 'UserProfile', component: () => import('../views/user/Profile.vue') },
      { path: 'fee', name: 'UserFee', component: () => import('../views/user/Fee.vue') },
      { path: 'parking', name: 'UserParking', component: () => import('../views/user/Parking.vue') },
      { path: 'repair', name: 'UserRepair', component: () => import('../views/user/Repair.vue') },
      { path: 'activity', name: 'UserActivity', component: () => import('../views/user/Activity.vue') },
      { path: 'complaint', name: 'UserComplaint', component: () => import('../views/user/Complaint.vue') },
      { path: 'visitor', name: 'UserVisitor', component: () => import('../views/user/Visitor.vue') }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  routes
})

router.beforeEach((to, from, next) => {
  if (to.matched.length === 0) {
    next('/')
    return
  }
  
  if (to.meta && to.meta.public) {
    next()
    return
  }
  
  const user = store.state.user
  if (!user || !user.id) {
    next('/login')
    return
  }
  
  next()
})

const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

export default router
