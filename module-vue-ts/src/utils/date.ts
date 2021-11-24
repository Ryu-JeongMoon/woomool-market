export class DateUtils {
  static getLocalDate(localDateTime: string): string {
    const localDate = localDateTime.split(" ")[0];
    const token = localDate.split("-");

    if (token.length !== 3) {
      throw new Error(`Invalid localDateTime: ${localDateTime}`);
    }

    return `${token[0]}.${token[1]}.${token[2]}`;
  }
}
