export class LocalStorageUtils {
  static setUsernameToLocalStorage(value: string): void {
    localStorage.setItem("username", value);
  }

  static getUsernameFromLocalStorage(): string | undefined {
    return localStorage.getItem("username")?.toString();
  }

  static removeUsernameFromLocalStorage(): void {
    localStorage.removeItem("username");
  }
}
