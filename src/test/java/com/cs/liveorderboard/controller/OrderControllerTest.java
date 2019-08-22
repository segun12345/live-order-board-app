package com.cs.liveorderboard.controller;

import com.cs.liveorderboard.domain.*;
import com.cs.liveorderboard.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import static com.cs.liveorderboard.domain.OrderType.SELL;
import static com.cs.liveorderboard.domain.Unit.kg;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    private JacksonTester<Order> personDTOJsonTester;

    @Test
    public void testDeleteOrderWhenIdIsSpecified() throws Exception {

        this.mvc.perform(delete("/orders/{id}", "Some_ID").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testCreateOrderWithValidRequestBody() throws Exception {

        Order order = Order.of(
                null,
                "userId",
                new Quantity(1.0, kg),
                new Price(BigDecimal.valueOf(11), Currency.GBP),
                SELL
        );

        given(this.orderService.createOrder(order))
                .willReturn(Order.of(
                        "3333333",
                        "userId",
                        new Quantity(1.0, kg),
                        new Price(BigDecimal.valueOf(11), Currency.GBP),
                        SELL
                ));


       this.mvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("LOCATION"))
                .andExpect(jsonPath("$.id", is("3333333")))
                .andExpect(jsonPath("$.userId", is("userId")));

    }


    @Test
    public void testCreateOrderWithInValidRequestBody() throws Exception {

        Order invalidOrder = Order.of(
                null,
                null,
                new Quantity(1.0, kg),
                new Price(BigDecimal.valueOf(11), Currency.GBP),
                SELL
        );

        this.mvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidOrder)))
                .andExpect(status().isBadRequest())
                .andExpect(header().doesNotExist("LOCATION"));

    }


    @Test
    public void testGetOrderWithNoDataSetup() throws Exception {

        this.mvc.perform(get("/orders")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)))
                .andExpect(content().json("[]"));

    }

    @Test
    public void testGetOrderWhenServiceReturnsEmptyList() throws Exception {

        given(this.orderService.getAllOrders())
                .willReturn(new ArrayList<>());

        this.mvc.perform(get("/orders")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)))
                .andExpect(content().json("[]"));

    }

    @Test
    public void testGetOrderReturnsOrders() throws Exception {

        given(this.orderService.getAllOrders())
                .willReturn(Arrays.asList(
                        OrderResponse.of(
                                OrderType.SELL,
                                Arrays.asList(
                                        OrderDetail.of(
                                                "1234",
                                                "userId",
                                                new Quantity(1.0, kg),
                                                new Price(BigDecimal.valueOf(11), Currency.GBP)
                                        ),
                                        OrderDetail.of(
                                                "3456",
                                                "userId",
                                                new Quantity(1.0, kg),
                                                new Price(BigDecimal.valueOf(11), Currency.GBP)
                                        )
                                )
                        ),
                        OrderResponse.of(
                                OrderType.BUY,
                                Arrays.asList(
                                        OrderDetail.of(
                                                "1234",
                                                "userId",
                                                new Quantity(1.0, kg),
                                                new Price(BigDecimal.valueOf(11), Currency.GBP)
                                        ),
                                        OrderDetail.of(
                                                "3456",
                                                "userId",
                                                new Quantity(1.0, kg),
                                                new Price(BigDecimal.valueOf(11), Currency.GBP)
                                        )
                                )
                        )

                ));

        this.mvc.perform(get("/orders")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

    }

}