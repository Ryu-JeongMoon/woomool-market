export interface Page {
  size: number;
  number: number;
  totalPages: number;
  totalElements: number;
}

export interface Pageable {
  page: number;
  size: number;
}
