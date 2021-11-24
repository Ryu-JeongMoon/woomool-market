import { UploadImageResponse } from "@/interfaces/common/common";
import axios from "axios";
import ResponseConverter from "@/api/converter/ResponseConverter";

const commonApi = {
  postTempImage(formData: FormData): Promise<UploadImageResponse> {
    return axios
      .post("/api/common/temp/file", formData)
      .then(ResponseConverter.extractData);
  },
};

export default commonApi;
