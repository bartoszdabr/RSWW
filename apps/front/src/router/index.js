import Vue from 'vue'
import Router from 'vue-router'
import TravelForm from '@/components/TravelForm'
import Preferences from '@/components/Preferences'
import Login from '@/components/Login'
import Offer from '@/components/Offer'
import OfferHistory from '@/components/OfferHistory'
import { isAuthenticated } from '@/components/utils.js';

Vue.use(Router)

const router =  new Router({
  routes: [
    {
      path: '/',
      name: 'TravelForm',
      component: TravelForm
    },
    {
      path: '/preferences',
      name: 'Preferences',
      component: Preferences
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/offer/:hotelId/:transportId',
      name: 'Offer',
      component: Offer
    },
    {
      path: '/history/:hotelId/:transportId',
      name: 'OfferHistory',
      component: OfferHistory
    }
  ]
})

router.beforeEach(async (to, from, next) => {
  if(to.name != 'Login' && !isAuthenticated()) {
    next({ name: 'Login' })
  } else {
    next();
  }
})

export default router