import { CommitOptions, DispatchOptions, Store } from "vuex";
import { UIActions, UIMutations, UIState } from "@/store/modules/ui";
import {
  BoardActions,
  BoardMutations,
  BoardState,
} from "@/store/modules/board";
import {
  MemberActions,
  MemberGetters,
  MemberMutations,
  MemberState,
} from "@/store/modules/member";
import {
  AuthActions,
  AuthGetters,
  AuthMutations,
  AuthState,
} from "@/store/modules/auth";

export type RootState = {
  ui: UIState;
  board: BoardState;
  member: MemberState;
  auth: AuthState;
};

export type MergedGetters = AuthGetters & MemberGetters;

export type MergedMutations = UIMutations &
  BoardMutations &
  MemberMutations &
  AuthMutations & {
    [key: string]: any;
  };
export type MergedActions = UIActions &
  AuthActions &
  BoardActions &
  MemberActions & {
    [key: string]: any;
  };

type WoomoolGetters = {
  getters: {
    [K in keyof MergedGetters]: ReturnType<MergedGetters[K]>;
  };
};

type WoomoolMutations = {
  commit<
    K extends keyof MergedMutations,
    P extends Parameters<MergedMutations[K]>[1]
  >(
    key: K,
    payload?: P,
    options?: CommitOptions
  ): ReturnType<MergedMutations[K]>;
};

type WoomoolActions = {
  dispatch<K extends keyof MergedActions>(
    key: K,
    payload?: Parameters<MergedActions[K]>[1],
    options?: DispatchOptions
  ): ReturnType<MergedActions[K]>;
};

export type WoomoolStore = Omit<
  Store<RootState>,
  "getters" | "commit" | "dispatch"
> &
  WoomoolGetters &
  WoomoolMutations &
  WoomoolActions;
