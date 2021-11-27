export interface Links {
  self: {
    href: string;
  };
}

export interface BoardResponseLinks {
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

export interface ProductResponseLinks {
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

export interface CartResponseLinks {
  self: {
    href: string;
  };
  "cart-list": {
    href: string;
  };
}
