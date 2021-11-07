import Vue from "vue";
import Vuetify from "vuetify";
import { ICONS } from "@/utils/common/icons";

Vue.use(Vuetify);

export const vuetify = new Vuetify({
  icons: {
    iconfont: "mdiSvg",
    values: ICONS,
  },
  theme: {
    themes: {
      dark: {},
    },
  },
});
