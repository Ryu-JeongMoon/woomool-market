function sum(a: number, b: number): number {
  return a + b;
}

describe("sum", () => {
  test("10 + 20 = 30", () => {
    const result = sum(10, 20);
    expect(result).toBe(30);
  });
});
