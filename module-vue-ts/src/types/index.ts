import Vue, { VueConstructor } from "vue";

export type WoomoolVue<T> = VueConstructor<Vue & T>;
export type WoomoolVueRefs<T> = VueConstructor<Vue & { $refs: T }>;

// 스크립트 상에서 일일이 변수를 as HTMLElement 타입 선언하지 말고
// WoomoolVueRefs<{refsName : refsType}> 으로 설정해주면 this.$refs.refsName 으로 간단히 사용 가능
