// Composables
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    component: () => import('@/layouts/default/Default.vue'),
    children: [
      {
        path: '/login',
        name: 'Login',
        component: () => import('@/views/Login.vue'),
      },

      {
        path: '/',
        name: 'Home',
        component: () => import('@/views/Home.vue'),
      },

      {
        path: '/new-teacher',
        name: 'New teacher',
        component: () => import('@/views/NewTeacher.vue'),
      },

      // else
      { 
        path: '/:pathMatch(.*)*', 
        name: 'NotFound', 
        component: () => import('@/views/404NotFound.vue')
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
})

export default router
