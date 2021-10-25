import axios from "axios";
import { setInterceptors } from "@/api/common/interceptors";

function createInstance() {
  return axios.create({
    baseURL: process.env.VUE_BASE_URL,
  });
}

function createInstanceWithAuth(url: string) {
  const instance = axios.create({
    baseURL: `${process.env.VUE_BASE_URL}${url}`,
  });
  return setInterceptors(instance);
}

export const publicInstance = createInstance();
export const privateInstance = createInstanceWithAuth("");

export {};
