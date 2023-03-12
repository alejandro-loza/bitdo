package com.bitso.challenge.service;

import com.bitso.challenge.entity.Currency;
import com.bitso.challenge.entity.Order;
import com.bitso.challenge.entity.Status;
import com.bitso.challenge.model.OrderModel;
import com.bitso.challenge.model.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * REST endpoint to submit and query orders.
 */
@RestController("orders")
public class OrderController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    private OrderModel orderModel;
    @Inject
    private UserModel userModel;

    @RequestMapping("/get/{id}")
    @ResponseBody
    public Optional<Order> get(@PathVariable long id) {
        Optional<Order> order = orderModel.get(id);
        order.ifPresentOrElse(
                o -> log.debug("get {}: {}", id, o),
                () -> log.debug("get {}: null", id));
        return order;
    }

    @RequestMapping("/submit") @PostMapping
    public Order submit(Order order) {
        log.debug("Submitting order {}", order);
        orderModel.submit(order);
        return order;
    }

    @RequestMapping("/book/{major}/{minor}")
    public List<Order> findOrdersForBook(@PathVariable String major,
                                         @PathVariable String minor) {
        //TODO validate currencies
        var maj = Currency.valueOf(major);
        var min = Currency.valueOf(minor);
        return orderModel.findOrdersForBook(maj, min);
    }

    @RequestMapping("/query/{userId}/{status}/{major}/{minor}")
    public List<Order> getBy(@PathVariable long userId,
                             @PathVariable String status,
                             @PathVariable String major,
                             @PathVariable String minor) {
        Status st = status == null || status.isEmpty() ? null : Status.valueOf(status);
        Currency ma = major == null || major.isEmpty() ? null : Currency.valueOf(major);
        Currency mi = minor == null || minor.isEmpty() ? null : Currency.valueOf(minor);
        List<Order> r = orderModel.findOrdersForUser(userId, st, ma, mi);
        log.debug("Query {}/{}/{}/{} returns {} orders", userId, st, ma, mi, r.size());
        return r;
    }
}
