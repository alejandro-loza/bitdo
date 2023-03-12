package com.bitso.challenge.model.db

import com.bitso.challenge.entity.Currency
import com.bitso.challenge.entity.Order
import com.bitso.challenge.entity.Status
import com.bitso.challenge.model.OrderModel
import com.bitso.challenge.model.TestApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

@DataJpaTest
@ContextConfiguration(classes = [TestApplication.class])
@ActiveProfiles("test")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class OrderModelImplSpec extends Specification {

  @Autowired
  OrderModel model

  void "insert, then query"() {
    long order = model.submit(new Order(userId: 1, major: 'btc', minor: 'mxn',
        amount:1.0, price: 350_000.00))
    long order2 = model.submit(new Order(userId: 2, major: 'btc', minor: 'mxn',
        amount:2.0, price: 330_000.00))
    long order3 = model.submit(new Order(userId: 3, major: 'eth', minor: 'mxn',
        amount:0.00001, price: 12_000.00))
    expect: "orders by user work"
    model.findOrdersForUser(1, Status.active, Currency.btc, Currency.mxn).size() == 1
    model.findOrdersForUser(2, Status.active, Currency.btc, Currency.mxn).size() == 1
    model.findOrdersForUser(3, Status.active, Currency.eth, Currency.mxn).size() == 1
    model.findOrdersForUser(3, Status.active, Currency.ltc, Currency.mxn).empty
    model.findOrdersForUser(3, Status.complete, Currency.eth, Currency.mxn).empty
    and: "orders by book work"
    model.findOrdersForBook(Currency.btc, Currency.mxn).size() == 2
    model.findOrdersForBook(Currency.eth, Currency.mxn).size() == 1
    model.findOrdersForBook(Currency.ltc, Currency.mxn).empty
  }
}
