import store from "@/store/index";
import { AxiosInstance, AxiosRequestConfig, AxiosResponse } from "axios";

const TOKEN_TYPE = "Bearer ";

/**
 * config.headers!.Authorization, type assertion
 * setting Authorization for specific http requests
 */
export const setInterceptors = (instance: AxiosInstance) => {
  instance.interceptors.request.use(
    (config: AxiosRequestConfig) => {
      config.headers!.Authorization = TOKEN_TYPE + store.state.auth.accessToken;
      return config;
    },
    (error: Error) => Promise.reject(error)
  );

  instance.interceptors.response.use(
    (response: AxiosResponse) => response,
    (error: Error) => Promise.reject(error)
  );

  return instance;
};

/*
if (config.headers) {
    config.headers.Authorization = store.state.auth.accessToken;
}
이것도 사용 가능, 근디 headers.Authorization 은 null 일 수도 없고, null check 할 필요가 없을듯 하여
단언 !. 사용했듬, 추후 문제 발생 시 if 문으로 변경하면 될듯
 */
