export class DateUtils {
  static getLocalDatetime(localDateTime: string): string {
    const localDate = localDateTime.split("T")[0];
    const localTime = localDateTime.split("T")[1];

    const dateToken = localDate.split("-");
    const timeToken = localTime.split(":");

    if (dateToken.length !== 3) {
      throw new Error(`Invalid localDateTime: ${localDateTime}`);
    }

    return `${dateToken[0]}.${dateToken[1]}.${dateToken[2]} ${timeToken[0]}:${timeToken[1]}`;
  }

  // static convertLocalDateTime(date: string): string {}
}
