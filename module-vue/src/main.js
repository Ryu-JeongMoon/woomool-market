import Vue from 'vue'
import App from './App.vue'

Vue.config.productionTip = false
// 사용자 정의 페이지 함수 사용 설정

new Vue({
    render: h => h(App),
}).$mount('#app')