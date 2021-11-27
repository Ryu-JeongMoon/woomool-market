const ACCESS_TOKEN_KEY = "accessToken";
const REFRESH_TOKEN_KEY = "refreshToken";

export class CookieUtils {
  static setAccessTokenToCookie(value: string): void {
    document.cookie = `${ACCESS_TOKEN_KEY}=${value}`;
  }

  static setRefreshTokenToCookie(value: string): void {
    document.cookie = `${REFRESH_TOKEN_KEY}=${value}`;
  }

  static getAccessTokenFromCookie(): string {
    return document.cookie.replace(
      /(?:(?:^|.*;\s*)accessToken\s*=\s*([^;]*).*$)|^.*$/,
      "$1"
    );
  }

  static getRefreshTokenFromCookie(): string {
    return document.cookie.replace(
      /(?:^|.*;\s*)refreshToken\s*=\s*([^;]*).*$|^.*$/,
      "$1"
    );
  }

  static deleteCookie(value: string): void {
    document.cookie = `${value}=; expires=Thu, 01 Jan 1970 00:00:01 GMT;`;
  }
}
