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

      {
        path: '/new-group',
        name: 'New group',
        component: () => import('@/views/NewGroup.vue'),
      },

      {
        path: '/new-audience',
        name: 'New audience',
        component: () => import('@/views/NewAudience.vue'),
      },

      {
        path: '/new-subject',
        name: 'New subject',
        component: () => import('@/views/NewSubject.vue'),
      },

      {
        path: '/link-teacher-subject',
        name: 'Link teacher subject',
        component: () => import('@/views/LinkTeacherSubject.vue'),
      },

      {
        path: '/generate-table',
        name: 'Generate table',
        component: () => import('@/views/GenerateTable.vue'),
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
