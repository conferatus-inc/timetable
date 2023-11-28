// Composables
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    component: () => import('@/layouts/default/Default.vue'),
    children: [
      {
        path: '/',
        name: 'Home',
        component: () => import('@/views/Home.vue'),
      },

      {
        path: '/teacher',
        name: 'Teachers',
        component: () => import('@/views/AllTeachers.vue'),
      },

      {
        path: '/teacher/:id',
        name: 'Table by teacher',
        component: () => import('@/views/TeacherTable.vue')
      },
      
      {
        path: '/audience',
        name: 'Audiences',
        component: () => import('@/views/AllAudiences.vue'),
      },

      {
        path: '/audience/:id',
        name: 'Table by audience',
        component: () => import('@/views/AudienceTable.vue')
      },

      {
        path: '/group',
        name: 'Groups',
        component: () => import('@/views/AllGroups.vue'),
      },

      {
        path: '/group/:id',
        name: 'Table by group',
        component: () => import('@/views/GroupTable.vue')
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
