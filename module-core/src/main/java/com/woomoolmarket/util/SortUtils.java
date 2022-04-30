package com.woomoolmarket.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.bouncycastle.util.Arrays;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SortUtils {

  /**
   * sorts란 정렬 대상 컬럼을 의미하고 orders란 정렬 방향을 의미한다<br/>
   * 리스트는 정렬 대상 컬럼을 기준으로 한다<br/>
   * 즉 sorts.length를 이용해 <정렬 대상 컬럼, 정렬 방향> 쌍을 리스트로 만들어 반환 한다
   */
  public static List<Pair<String, Direction>> getPairs(String[] sorts, String[] orders) {
    String[] computedOrders = getOrAppendedOrders(sorts, orders);

    return IntStream.range(0, sorts.length)
      .mapToObj(i -> {
        String eachOrder = computedOrders[i];
        Sort.Direction direction = StringUtils.equalsIgnoreCase(eachOrder, Sort.Direction.ASC.name())
          ? Sort.Direction.ASC
          : Sort.Direction.DESC;

        return Pair.of(sorts[i], direction);
      })
      .collect(Collectors.toList());
  }

  /**
   * 정렬 대상과 정렬 방향의 쌍을 맞추기 위해 입력 값의 길이가 다른 경우 강제적으로 빈 값으로 맞춰준다<br/>
   * 정렬 컬럼 기준으로 리스트를 생성하기 때문에 정렬 방향의 배열이 더 긴 경우는 고려하지 않는다
   */
  private static String[] getOrAppendedOrders(String[] sorts, String[] orders) {
    int differenceOfLength = ArrayUtils.getLength(sorts) - ArrayUtils.getLength(orders);
    if (differenceOfLength > 0)
      for (int i = 0; i < differenceOfLength; i++)
        orders = Arrays.append(orders, "");

    return orders;
  }
}
