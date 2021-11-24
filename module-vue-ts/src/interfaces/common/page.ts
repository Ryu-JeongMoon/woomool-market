export interface Page {
  size: number;
  number: number;
  totalPages: number;
  totalElements: number;
}

export interface PageRequest {
  page: number;
  size: number;
}
