import { BoardResponse } from "@/interfaces/board/board";
import { MemberResponse } from "@/interfaces/member/member";
import { BoardResponseLink, Link } from "@/interfaces/common/link";
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

  static memberResponse(): MemberResponse {
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
    };
  }

  static address(): Address {
    return {
      city: "",
      street: "",
      zipcode: "",
    };
  }

  static link(): Link {
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
