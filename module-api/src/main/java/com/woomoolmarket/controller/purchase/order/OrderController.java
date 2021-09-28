package com.woomoolmarket.controller.purchase.order;


import static java.util.stream.Collectors.toList;
import static org.springframework.beans.support.PagedListHolder.DEFAULT_MAX_LINKED_PAGES;
import static org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE;

import com.woomoolmarket.common.util.ExceptionUtil;
import com.woomoolmarket.domain.purchase.order.entity.Order;
import com.woomoolmarket.domain.purchase.order.repository.OrderRepository;
import com.woomoolmarket.service.order.dto.request.CreateOrderRequest;
import com.woomoolmarket.service.order.dto.request.ModifyOrderRequest;
import com.woomoolmarket.service.order.dto.response.OrderResponse;
import com.woomoolmarket.service.order.mapper.ModifyOrderRequestMapper;
import com.woomoolmarket.service.order.mapper.OrderRequestMapper;
import com.woomoolmarket.service.order.mapper.OrderResponseMapper;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.MediaTypes;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Transactional(readOnly = true)
@RequestMapping(path = "/api/orders", produces = MediaTypes.HAL_JSON_VALUE)
public class OrderController {


}

/*
음 도메인 별로 형태가 넘 비슷하당
 */