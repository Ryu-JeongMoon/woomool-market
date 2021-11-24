export interface Link {
  self: {
    href: string;
  };
}

export interface BoardResponseLink {
  self: {
    href: string;
  };
  "modify-board": {
    href: string;
  };
  "delete-board": {
    href: string;
  };
}

export interface ProductResponseLink {
  self: {
    href: string;
  };
  "modify-product": {
    href: string;
  };
  "delete-product": {
    href: string;
  };
}

export interface CartResponseLink {
  self: {
    href: string;
  };
  "cart-list": {
    href: string;
  };
}
