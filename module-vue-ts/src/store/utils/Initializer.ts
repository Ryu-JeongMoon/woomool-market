import { BoardResponse } from "@/interfaces/board";
import { MemberModelResponse } from "@/interfaces/member";
import { BoardResponseLinks, Links } from "@/interfaces/common/links";
import { Address } from "@/interfaces/common/address";
import { Page } from "@/interfaces/common/page";

export default class StateInitializer {
  static board(): BoardResponse {
    return {
      id: 0,
      title: "",
      content: "",
      hit: 0,
      boardCategory: "",
      startDateTime: "",
      endDateTime: "",
      createdDateTime: "",
      memberResponse: this.memberResponse(),
      _links: {
        self: {
          href: "",
        },
        "modify-board": {
          href: "",
        },
        "delete-board": {
          href: "",
        },
      },
    };
  }

  static memberResponse(): MemberModelResponse {
    return {
      id: 0,
      email: "",
      nickname: "",
      profileImage: "",
      phone: "",
      license: "",
      createdDateTime: "",
      lastModifiedDateTime: "",
      leaveDateTime: "",
      authority: "",
      address: this.address(),
      authProvider: "",
      status: "",
      _links: {} as Links,
    };
  }

  static address(): Address {
    return {
      city: "",
      street: "",
      zipcode: "",
    };
  }

  static link(): Links {
    return {
      self: {
        href: "",
      },
    };
  }

  static page(): Page {
    return {
      size: 0,
      number: 0,
      totalPages: 0,
      totalElements: 0,
    };
  }
}
