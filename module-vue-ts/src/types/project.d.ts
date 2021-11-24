import Vue from "vue";
import { WoomoolStore } from "@/store/type/types";

declare module "vue/types/options" {
  interface ComponentOptions<V extends Vue> {
    store?: WoomoolStore;
  }
}

declare module "vue/types/vue" {
  interface Vue {
    $store: WoomoolStore;
  }
}
