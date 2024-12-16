import './assets/main.css'
import './assets/common.css'
import './assets/header.css'
import './assets/normalize.css'
import './assets/base.css'
import './assets/account.css'
import './assets/datatable-custom.css'
import './assets/modal.css'

import './scripts/TweenLite.min.js'
import './scripts/EasePack.min.js'
import "./scripts/Utils.js"
import "./scripts/DataTableUtils.js"


// import "http://html5shiv.googlecode.com/svn/trunk/html5.js"
import {createApp} from 'vue'
import App from './App.vue'
import AuthView from "@/components/views/auth/AuthView.vue";
import RegView from "@/components/views/auth/RegView.vue";
import HomeView from "@/components/views/HomeView.vue";
import DrawPanelView from "@/components/views/DrawPanelView.vue";
import PersonalAccountView from "@/components/views/PersonalAccountView.vue"
import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css'

import {createRouter, createWebHistory} from "vue-router";

const router = createRouter({
    routes: [
        {
            path: "/",
            component: HomeView
        },
        {
            path: "/auth",
            component: AuthView
        },
        {
            path: "/draw",
            component: DrawPanelView
        },
        {
            path: "/reg",
            component: RegView
        },
        {
            path: "/account",
            component: PersonalAccountView
        }
    ],
    history: createWebHistory()
})

createApp(App)
    .use(router)
    .component('VueDatePicker', VueDatePicker)
    .mount('#app')
