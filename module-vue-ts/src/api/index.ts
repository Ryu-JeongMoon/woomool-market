import axios, { AxiosInstance } from "axios";
import { setInterceptors } from "@/api/common/interceptors";

const createInstance = (): AxiosInstance => {
  return axios.create({ baseURL: process.env.VUE_APP_LOCAL_URL });
};

const createPrivateInstance = (): AxiosInstance => setInterceptors(publicAxios);

export const publicAxios = createInstance();
export const privateAxios = createPrivateInstance();
