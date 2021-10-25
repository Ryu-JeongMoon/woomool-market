export function formatDate(value: Date): string {
  const date = new Date(value);
  const year = date.getFullYear();
  let month: number | string = date.getMonth() + 1;
  month = month > 9 ? month : `0${month}`;
  const day = date.getDate();
  let hours: number | string = date.getHours();
  hours = hours > 9 ? hours : `0${hours}`;
  const minutes = date.getMinutes();
  return `${year}-${month}-${day} ${hours}:${minutes}`;
}
