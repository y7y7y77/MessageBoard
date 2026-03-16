import { createRouter, createWebHistory } from 'vue-router'
import UserIndex from '@/pages/user/UserIndex.vue'
import AdminIndex from '@/pages/admin/AdminIndex.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: UserIndex,
    },
    {
      path: '/admin',
      component: AdminIndex,
    },
  ],
})

export default router
