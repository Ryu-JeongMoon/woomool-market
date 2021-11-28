import _ from "lodash";

export function validateEmail(email: string): boolean {
  const re =
    /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  return re.test(_.toLower(email));
}

export function validateWithRules(target: any, rules: any[]): boolean {
  const validationMessages = getValidationMessage(target, rules);
  if (_.isEmpty(validationMessages)) {
    return true;
  }
  // store.commit(UIMutationTypes.OPEN_SNACK_BAR, validationMessages[0]);
  return false;
}

function getValidationMessage(target: any, rules: any[]) {
  return rules
    .map((rule) => {
      const value = rule(target);
      if (typeof value === "string") {
        return value;
      }
      if (typeof value === "boolean") {
        return value ? "" : "잘못된 값입니다.";
      }
      console.warn("invalid rule function result. value: ", value);
      return "";
    })
    .filter((value) => !_.isEmpty(value));
}
