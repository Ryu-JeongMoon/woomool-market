import axios, { AxiosInstance } from "axios";
import { setInterceptors } from "@/api/common/interceptors";

function createAxiosInstance(): AxiosInstance {
  return axios.create({
    baseURL: process.env.VUE_APP_LOCAL_URL,
  });
}

function createAxiosInstanceWithAuth(url: string): AxiosInstance {
  const instance = axios.create({
    baseURL: `${process.env.VUE_APP_LOCAL_URL}${url}`,
  });
  return setInterceptors(instance);
}

const publicAxios = createAxiosInstance();
const privateAxios = createAxiosInstanceWithAuth("");

export { publicAxios, privateAxios };
