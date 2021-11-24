import Vue from "vue";
import Vuex from "vuex";
import modules from "@/store/modules";
import { WoomoolStore } from "@/store/type/types";

Vue.use(Vuex);

const store: WoomoolStore = new Vuex.Store({
  modules,
});

export default store;
