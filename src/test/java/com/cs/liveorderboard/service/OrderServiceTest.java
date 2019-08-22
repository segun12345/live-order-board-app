package com.cs.liveorderboard.service;

import com.cs.liveorderboard.domain.*;
import com.cs.liveorderboard.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static com.cs.liveorderboard.domain.OrderType.BUY;
import static com.cs.liveorderboard.domain.OrderType.SELL;
import static com.cs.liveorderboard.domain.Unit.kg;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTest {

    @MockBean
    private OrderRepository orderRepository;

    @Autowired
    OrderService orderService;

    @Test
    public void testOrderIsGroupedByPrice() {

        given(this.orderRepository.findAll()).willReturn(Arrays.asList(
                Order.of(
                        "13343434",
                        "userId",
                        new Quantity(1.0, kg),
                        new Price(BigDecimal.valueOf(11), Currency.GBP),
                        SELL
                ),
                Order.of(
                        "13343434",
                        "userId",
                        new Quantity(1.0, kg),
                        new Price(BigDecimal.valueOf(11), Currency.GBP),
                        SELL
                )
        ));


        List<OrderResponse> orderResponse = orderService.getAllOrders();
        assertThat(orderResponse.get(0).getOrders().size()).isEqualTo(1);
        assertThat(orderResponse.get(0).getOrders().get(0).getQuantity().getValue()).isEqualTo(2.0);
    }

    @Test
    public void testSellOrderTypeSortsByLowestPrice() {

        given(this.orderRepository.findAll()).willReturn(Arrays.asList(
                Order.of(
                        "1111",
                        "userId",
                        new Quantity(1.0, kg),
                        new Price(BigDecimal.valueOf(50), Currency.GBP),
                        SELL
                ),
                Order.of(
                        "2222",
                        "userId",
                        new Quantity(1.0, kg),
                        new Price(BigDecimal.valueOf(11), Currency.GBP),
                        SELL
                ),
                Order.of(
                        "3333",
                        "userId",
                        new Quantity(1.0, kg),
                        new Price(BigDecimal.valueOf(22), Currency.GBP),
                        SELL
                ),
                Order.of(
                        "4444",
                        "userId",
                        new Quantity(1.0, kg),
                        new Price(BigDecimal.valueOf(9), Currency.GBP),
                        SELL
                )
        ));

        List<OrderDetail> orderDetails = Arrays.asList(
                OrderDetail.of(
                        "4444",
                        "userId",
                        new Quantity(1.0, kg),
                        new Price(BigDecimal.valueOf(9), Currency.GBP)
                ),
                OrderDetail.of(
                        "2222",
                        "userId",
                        new Quantity(1.0, kg),
                        new Price(BigDecimal.valueOf(11), Currency.GBP)
                ),
                OrderDetail.of(
                        "3333",
                        "userId",
                        new Quantity(1.0, kg),
                        new Price(BigDecimal.valueOf(22), Currency.GBP)
                ),
                OrderDetail.of(
                        "1111",
                        "userId",
                        new Quantity(1.0, kg),
                        new Price(BigDecimal.valueOf(50), Currency.GBP)
                )
        );

        List<OrderResponse> orderResponse = orderService.getAllOrders();
        assertThat(orderResponse.get(0).getOrders().size()).isEqualTo(4);
        assertThat(orderResponse.get(0).getOrders()).isEqualTo(orderDetails);
    }


    @Test
    public void testBuyOrderTypeSortsByHighestPrice() {

        given(this.orderRepository.findAll()).willReturn(Arrays.asList(
                Order.of(
                        "1111",
                        "userId",
                        new Quantity(1.0, kg),
                        new Price(BigDecimal.valueOf(50), Currency.GBP),
                        BUY
                ),
                Order.of(
                        "2222",
                        "userId",
                        new Quantity(1.0, kg),
                        new Price(BigDecimal.valueOf(11), Currency.GBP),
                        BUY
                ),
                Order.of(
                        "3333",
                        "userId",
                        new Quantity(1.0, kg),
                        new Price(BigDecimal.valueOf(22), Currency.GBP),
                        BUY
                ),
                Order.of(
                        "4444",
                        "userId",
                        new Quantity(1.0, kg),
                        new Price(BigDecimal.valueOf(9), Currency.GBP),
                        BUY
                )
        ));

        List<OrderDetail> orderDetails = Arrays.asList(
                OrderDetail.of(
                        "1111",
                        "userId",
                        new Quantity(1.0, kg),
                        new Price(BigDecimal.valueOf(50), Currency.GBP)
                ),
                OrderDetail.of(
                        "3333",
                        "userId",
                        new Quantity(1.0, kg),
                        new Price(BigDecimal.valueOf(22), Currency.GBP)
                ),
                OrderDetail.of(
                        "2222",
                        "userId",
                        new Quantity(1.0, kg),
                        new Price(BigDecimal.valueOf(11), Currency.GBP)
                ),
                OrderDetail.of(
                        "4444",
                        "userId",
                        new Quantity(1.0, kg),
                        new Price(BigDecimal.valueOf(9), Currency.GBP)
                )
        );

        List<OrderResponse> orderResponse = orderService.getAllOrders();
        assertThat(orderResponse.get(0).getOrders().size()).isEqualTo(4);
        assertThat(orderResponse.get(0).getOrders()).isEqualTo(orderDetails);
    }

    @Test
    public void testBuyOrderTypeSortsByHighestPriceAndSellOrderSortsByLowest() {

        given(this.orderRepository.findAll()).willReturn(Arrays.asList(
                Order.of(
                        "1111",
                        "userId",
                        new Quantity(1.0, kg),
                        new Price(BigDecimal.valueOf(50), Currency.GBP),
                        BUY
                ),
                Order.of(
                        "9999",
                        "userId",
                        new Quantity(1.0, kg),
                        new Price(BigDecimal.valueOf(50), Currency.GBP),
                        SELL
                ),
                Order.of(
                        "2222",
                        "userId",
                        new Quantity(1.0, kg),
                        new Price(BigDecimal.valueOf(11), Currency.GBP),
                        BUY
                ),
                Order.of(
                        "3333",
                        "userId",
                        new Quantity(1.0, kg),
                        new Price(BigDecimal.valueOf(22), Currency.GBP),
                        BUY
                ),
                Order.of(
                        "4444",
                        "userId",
                        new Quantity(1.0, kg),
                        new Price(BigDecimal.valueOf(9), Currency.GBP),
                        BUY
                ),
                Order.of(
                        "8888",
                        "userId",
                        new Quantity(1.0, kg),
                        new Price(BigDecimal.valueOf(22), Currency.GBP),
                        SELL
                )
        ));

        List<OrderDetail> buyOrderDetails = Arrays.asList(
                OrderDetail.of(
                        "1111",
                        "userId",
                        new Quantity(1.0, kg),
                        new Price(BigDecimal.valueOf(50), Currency.GBP)
                ),
                OrderDetail.of(
                        "3333",
                        "userId",
                        new Quantity(1.0, kg),
                        new Price(BigDecimal.valueOf(22), Currency.GBP)
                ),
                OrderDetail.of(
                        "2222",
                        "userId",
                        new Quantity(1.0, kg),
                        new Price(BigDecimal.valueOf(11), Currency.GBP)
                ),
                OrderDetail.of(
                        "4444",
                        "userId",
                        new Quantity(1.0, kg),
                        new Price(BigDecimal.valueOf(9), Currency.GBP)
                )
        );

        List<OrderDetail> sellOrderDetails = Arrays.asList(
                OrderDetail.of(
                        "8888",
                        "userId",
                        new Quantity(1.0, kg),
                        new Price(BigDecimal.valueOf(22), Currency.GBP)
                ),
                OrderDetail.of(
                        "9999",
                        "userId",
                        new Quantity(1.0, kg),
                        new Price(BigDecimal.valueOf(50), Currency.GBP)
                )
        );

        List<OrderResponse> orderResponse = orderService.getAllOrders();
        assertThat(orderResponse.get(0).getOrders().size()).isEqualTo(2);
        assertThat(orderResponse.get(0).getOrders()).isEqualTo(sellOrderDetails);

        assertThat(orderResponse.get(1).getOrders().size()).isEqualTo(4);
        assertThat(orderResponse.get(1).getOrders()).isEqualTo(buyOrderDetails);
    }

}
