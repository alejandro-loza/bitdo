package com.bitso.challenge.model.repository;

import com.bitso.challenge.entity.Currency;
import com.bitso.challenge.entity.Order;
import com.bitso.challenge.entity.Status;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends CrudRepository<Order, Long> {

  Order save(Order order);
  Optional<Order> findById(long id);
  List<Order> findByUserIdAndStatusAndMajorAndMinor(long userId, Status status, Currency major, Currency minor);
  List<Order> findByMajorAndMinor(Currency major, Currency minor);
}
