import { ActionContext, CommitOptions, DispatchOptions } from "vuex";
import { MergedActions, MergedMutations, RootState } from "@/store/type/types";
import { BoardState } from "@/store/modules/board";
import { UIState } from "@/store/modules/ui";
import { MemberState } from "@/store/modules/member";
import { AuthState } from "@/store/modules/auth";

export type DefaultActionContext = {
  commit<
    K extends keyof MergedMutations,
    P extends Parameters<MergedMutations[K]>[1]
  >(
    key: K,
    payload?: P,
    options?: CommitOptions
  ): ReturnType<MergedMutations[K]>;
  dispatch<K extends keyof MergedActions>(
    key: K,
    payload?: Parameters<MergedActions[K]>[1],
    options?: DispatchOptions
  ): ReturnType<MergedActions[K]>;
} & Omit<ActionContext<RootState, RootState>, "commit" | "dispatch">;

export type UIActionContext = {
  commit<
    K extends keyof MergedMutations,
    P extends Parameters<MergedMutations[K]>[1]
  >(
    key: K,
    payload?: P,
    options?: CommitOptions
  ): ReturnType<MergedMutations[K]>;
  dispatch<K extends keyof MergedActions>(
    key: K,
    payload?: Parameters<MergedActions[K]>[1],
    options?: DispatchOptions
  ): ReturnType<MergedActions[K]>;
} & Omit<ActionContext<UIState, RootState>, "commit" | "dispatch">;

export type AuthActionContext = {
  commit<
    K extends keyof MergedMutations,
    P extends Parameters<MergedMutations[K]>[1]
  >(
    key: K,
    payload?: P,
    options?: CommitOptions
  ): ReturnType<MergedMutations[K]>;
  dispatch<K extends keyof MergedActions>(
    key: K,
    payload?: Parameters<MergedActions[K]>[1],
    options?: DispatchOptions
  ): ReturnType<MergedActions[K]>;
} & Omit<ActionContext<AuthState, RootState>, "commit" | "dispatch">;

export type BoardActionContext = {
  commit<
    K extends keyof MergedMutations,
    P extends Parameters<MergedMutations[K]>[1]
  >(
    key: K,
    payload?: P,
    options?: CommitOptions
  ): ReturnType<MergedMutations[K]>;
  dispatch<K extends keyof MergedActions>(
    key: K,
    payload?: Parameters<MergedActions[K]>[1],
    options?: DispatchOptions
  ): ReturnType<MergedActions[K]>;
} & Omit<ActionContext<BoardState, RootState>, "commit" | "dispatch">;

export type MemberActionContext = {
  commit<
    K extends keyof MergedMutations,
    P extends Parameters<MergedMutations[K]>[1]
  >(
    key: K,
    payload?: P,
    options?: CommitOptions
  ): ReturnType<MergedMutations[K]>;
  dispatch<K extends keyof MergedActions>(
    key: K,
    payload?: Parameters<MergedActions[K]>[1],
    options?: DispatchOptions
  ): ReturnType<MergedActions[K]>;
} & Omit<ActionContext<MemberState, RootState>, "commit" | "dispatch">;
