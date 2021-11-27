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
      if (store.state.auth.accessToken !== "") {
        config.headers!.Authorization =
          TOKEN_TYPE + store.state.auth.accessToken;
      }
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

Token Type 넣어줄 때, if 문 확인 없이 넣어주면 기본적으로 Bearer 붙어서 들어가므로 서버에서 장애남
따라서 여기서 확인해줘서 accessToken 있는 경우에만 Bearer 붙여주기
 */
