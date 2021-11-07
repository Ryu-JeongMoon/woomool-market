import axios from "axios";
import { setInterceptors } from "@/api/common/interceptors";

function createInstance() {
  return axios.create({
    baseURL: process.env.VUE_APP_LOCAL_URL,
  });
}

function createInstanceWithAuth(url: string) {
  const instance = axios.create({
    baseURL: `${process.env.VUE_APP_LOCAL_URL}${url}`,
  });
  return setInterceptors(instance);
}

const publicInstance = createInstance();
const privateInstance = createInstanceWithAuth("");

export { publicInstance, privateInstance };
