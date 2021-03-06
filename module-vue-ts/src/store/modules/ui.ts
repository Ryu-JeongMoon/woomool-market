import { UIActionTypes } from "@/store/type/actionTypes";
import { actionsNormalTemplate } from "@/store/utils/actionsTemplate";
import { UIMutationTypes } from "@/store/type/mutationTypes";
import { changeThemeAndLoad, isDarkTheme } from "@/utils/theme";
import { UploadImageResponse } from "@/interfaces/common/common";
import commonApi from "@/api/CommonApi";
import {
  DefaultActionContext,
  UIActionContext,
} from "@/store/type/actionContextTypes";

export const state = {
  loading: false as boolean,
  isDarkTheme: isDarkTheme() as boolean,
  width: window.innerWidth as number,
};
export type UIState = typeof state;

export const mutations = {
  [UIMutationTypes.CHANGE_LOADING](state: UIState, value: boolean) {
    state.loading = value;
  },
  [UIMutationTypes.CHANGE_THEME](state: UIState) {
    changeThemeAndLoad();
    state.isDarkTheme = isDarkTheme();
  },
  [UIMutationTypes.CHANGE_WIDTH](state: UIState, width: number) {
    state.width = width;
  },
};
export type UIMutations = typeof mutations;

export const actions = {
  async [UIActionTypes.UPLOAD_TEMP_IMAGE](
    context: DefaultActionContext,
    formData: FormData
  ): Promise<UploadImageResponse> {
    return actionsNormalTemplate(async () => {
      const uploadImageResponse = await commonApi.postTempImage(formData);
      return { ...uploadImageResponse };
    });
  },
};
export type UIActions = typeof actions;
