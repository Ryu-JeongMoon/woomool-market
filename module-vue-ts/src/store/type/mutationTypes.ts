export type MutationTypes = UIMutationTypes | BoardMutationTypes;

export enum BoardMutationTypes {}

export enum UIMutationTypes {
  CHANGE_LOADING = "ui/changeLoading",
  CHANGE_THEME = "ui/changeTheme",
  CHANGE_WIDTH = "ui/changeWidth",
}
