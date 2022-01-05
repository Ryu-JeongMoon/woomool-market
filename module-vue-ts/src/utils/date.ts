import moment from "moment";

export class DateUtils {
  static getLocalDatetime(localDateTime: string): string {
    const localDate = localDateTime.split(" ")[0];
    const localTime = localDateTime.split(" ")[1];

    const dateToken = localDate.split("-");
    const timeToken = localTime.split(":");

    if (dateToken.length !== 3) {
      throw new Error(`Invalid localDateTime: ${localDateTime}`);
    }

    return `${dateToken[0]}.${dateToken[1]}.${dateToken[2]} ${timeToken[0]}:${timeToken[1]}`;
  }

  static convertDateFormat(date: string, time: string): string {
    return moment(date + " " + time)
      .format()
      .substr(0, 19);
  }
}
