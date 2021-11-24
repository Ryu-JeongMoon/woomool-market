import store from "@/store/index";
import { AxiosInstance, AxiosRequestConfig, AxiosResponse } from "axios";

export function setInterceptors(instance: AxiosInstance) {
  instance.interceptors.request.use(
    (config: AxiosRequestConfig) => {
      // config.headers.Authorization = store.state.token;
      return config;
    },
    function (error: any) {
      return Promise.reject(error);
    }
  );

  instance.interceptors.response.use(
    function (response: AxiosResponse) {
      return response;
    },
    function (error: Error) {
      return Promise.reject(error);
    }
  );

  return instance;
}
