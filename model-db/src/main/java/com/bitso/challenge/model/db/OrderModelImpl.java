package com.bitso.challenge.model.db;

import com.bitso.challenge.entity.Currency;
import com.bitso.challenge.entity.Order;
import com.bitso.challenge.entity.Status;
import com.bitso.challenge.model.OrderModel;
import com.bitso.challenge.model.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderModelImpl implements OrderModel {

  @Autowired
  private OrderRepository orderRepository;

  @Override
  @Transactional
  public long submit(Order order) {
    order.setCreated(new Date());
    order.setStatus(Status.active);

    return insert(order);
  }

  @Override
  public Optional<Order> get(long id) {
    return orderRepository.findById(id);
  }

  @Override
  public List<Order> findOrdersForUser(long userId, Status status, Currency major, Currency minor) {
    return orderRepository.findByUserIdAndStatusAndMajorAndMinor(userId, status, major, minor);
  }

  @Override
  public List<Order> findOrdersForBook(Currency major, Currency minor) {
    return orderRepository.findByMajorAndMinor(major, minor);
  }

  @Override
  public long insert(Order order) {
    return orderRepository.save(order).getId();
  }
}
