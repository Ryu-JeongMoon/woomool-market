import { ModuleTree } from "vuex";

function isObject(value: any) {
  return value && typeof value === "object" && value.constructor === Object;
}

function getModuleName(fileName: string) {
  return fileName.replace(/^\.\//, "").replace(/\.\w+$/, "");
}

function makeModules(): ModuleTree<any> {
  const modules: ModuleTree<any> = {};
  const requireModule = require.context(
    ".",
    true,
    /^((?!index|init).)*\.[jt]s$/
  );
  requireModule.keys().forEach((fileName: string) => {
    const definitions =
      requireModule(fileName).default || requireModule(fileName);
    if (!definitions || !isObject(definitions)) {
      if (process.env.NODE_ENV === "test") {
        return;
      }
      console.error(
        `[vuex module] export default not defined or not Object type. fileName: ${fileName}, definition: ${definitions}`
      );
      return;
    }

    const moduleName: string = getModuleName(fileName);
    modules[moduleName] = {
      namespaced: false,
      ...definitions,
    };
  });
  return modules;
}

const modules = makeModules();
export default modules;
