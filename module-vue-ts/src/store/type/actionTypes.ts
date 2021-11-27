export enum UIActionTypes {
  UPLOAD_TEMP_IMAGE = "common/uploadTempImage",
}

export enum AuthActionTypes {
  REQUEST_LOGIN = "/api/",
}

export enum BoardActionTypes {
  REQUEST_BOARDS = "api/boards",
  REQUEST_BOARD = "api/boards/:boardId",
}

export enum ClubActionTypes {
  REQUEST_CLUB_INFO_AND_USER_INFO = "club/requestClubInfoAndUserInfo",
  REQUEST_CLUB_CREATE = "club/requestClubCreate",
  REQUEST_CLUB_EDIT = "club/requestClubEdit",
  REQUEST_CLUB_JOIN = "club/requestClubJoin",
  REQUEST_CHANGE_USER_ROLE = "club/requestChangeUserRole",
  REQUEST_KICK_USER = "club/requestKickUser",
  REQUEST_CLUB_WITHDRAW = "club/requestClubWithdraw",
  REQUEST_CLUB_DELETE = "club/requestClubDelete",
}

export enum ClubListActionTypes {
  REQUEST_FIRST_CLUB_LIST = "clubList/requestFirstClubList",
  REQUEST_NEXT_CLUB_LIST = "clubList/requestNextClubList",
  REQUEST_FIRST_MY_CLUB_LIST = "clubList/requestFirstMyClubList",
  REQUEST_NEXT_MY_CLUB_LIST = "clubList/requestNextMyClubList",
}
