import Vue from "vue";
import Vuetify from "vuetify";
import "material-design-icons-iconfont/dist/material-design-icons.css";
import "@mdi/font/css/materialdesignicons.css";
import "vuetify/dist/vuetify.min.css";

Vue.use(Vuetify);

export const vuetify = new Vuetify({
  icons: {
    iconfont: "mdi",
  },
  theme: {
    themes: {
      dark: {},
    },
  },
});
