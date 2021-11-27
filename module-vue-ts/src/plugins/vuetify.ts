import Vue from "vue";
import Vuetify from "vuetify";
import "material-design-icons-iconfont/dist/material-design-icons.css";
import "vuetify/dist/vuetify.min.css";

Vue.use(Vuetify);

export const vuetify = new Vuetify({
  icons: {
    iconfont: "md",
  },
  theme: {
    themes: {
      dark: {},
    },
  },
});
