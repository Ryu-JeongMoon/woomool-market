import store from "@/store";
import { MutationTypes, UIMutationTypes } from "@/store/type/mutationTypes.ts";
import { AxiosError } from "axios";
import { AuthUtils } from "@/utils/auth";
import routerHelper from "@/router/RouterHelper";

export const actionsLoadingTemplate = async <T>(
  callback: () => Promise<T>,
  mutationTypes: MutationTypes = UIMutationTypes.CHANGE_LOADING,
  failCallback?: any
): Promise<T> => {
  try {
    store.commit(mutationTypes, true);
    return await callback();
  } catch (e) {
    handleException(e, failCallback);
    return Promise.reject();
  } finally {
    store.commit(mutationTypes, false);
  }
};

export const actionsNormalTemplate = async <T>(
  callback: () => Promise<T>,
  failCallback?: () => any
): Promise<T> => {
  try {
    return await callback();
  } catch (e) {
    handleException(e, failCallback);
    return Promise.reject();
  }
};

function handleException(e: any, failCallback?: () => any) {
  if (AuthUtils.isUnauthorizedError(e)) {
    handleForUnauthorized();
  } else {
    handleForOthers(e, failCallback);
  }
  throw e;
}

function handleForUnauthorized(): void {
  AuthUtils.removeAppToken();
  routerHelper.pushWhenUnauthorizedError().then((r) => console.log(r));
}

function extractMessage(e: AxiosError) {
  if (e && e.response && e.response.data) {
    return e.response.data;
  }
  return null;
}

function handleForOthers(e: any, failCallback?: () => any) {
  const errorMessageFromServer = extractMessage(e);

  if (failCallback) {
    failCallback();
  }
}
