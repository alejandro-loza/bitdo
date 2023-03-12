package com.bitso.challenge.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Represents a buy or sell order.
 */
@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long userId;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Date created;
    @Enumerated(EnumType.STRING)
    private Currency major;
    @Enumerated(EnumType.STRING)
    private Currency minor;
    private BigDecimal amount;
    private BigDecimal price;
    private boolean buy;
}
