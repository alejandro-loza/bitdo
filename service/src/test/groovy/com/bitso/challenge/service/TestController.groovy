package com.bitso.challenge.service

import com.bitso.challenge.entity.Currency
import com.bitso.challenge.entity.Order
import com.bitso.challenge.entity.Status
import com.bitso.challenge.entity.User
import com.bitso.challenge.model.OrderModel
import com.bitso.challenge.model.UserModel
import com.bitso.challenge.model.db.UserModelImpl
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

import javax.inject.Inject
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.LongStream

/** 
 * Test the OrderController.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestController extends Specification {

    @Inject
    OrderModel orderModel

    @Inject
    UserModel userModel

    def setup() {
        LongStream.rangeClosed(1, 10).forEach(id -> {
            User u = new User();
            u.setId(id);
            u.setEmail("user" + id + "@bitso.com");
            u.setPassword("password" + id);
            ((UserModelImpl)userModel).add(u);
        })

        Files.lines(Paths.get(getClass().getResource("/orders.csv").toURI())).map(line -> {
            Order order = new Order();
            String[] parts = line.split(",");
            order.setUserId(Long.parseLong(parts[0]));
            order.setStatus(Status.valueOf(parts[1]));
            order.setCreated(new Date(Long.parseLong(parts[2])));
            order.setMajor(Currency.valueOf(parts[3]));
            order.setMinor(Currency.valueOf(parts[4]));
            order.setAmount(new BigDecimal(parts[5]));
            order.setPrice(new BigDecimal(parts[6]));
            order.setBuy("buy".equals(parts[7]));
            return order;
        }).forEach(orderModel::insert);
    }


    @Inject
    OrderModel model
    @Value('${local.server.port}')
    int port
    RestTemplate rest = new RestTemplate()

    Map<String, Object> submit(long userId, String major, String minor, BigDecimal amount, BigDecimal price, boolean buy) {
        HttpHeaders headers = new HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED)
        MultiValueMap<String, String> req = new LinkedMultiValueMap<>()
        req.add('userId', Long.toString(userId))
        req.add('major', major)
        req.add('minor', minor)
        req.add('amount', amount.toPlainString())
        req.add('price', price.toPlainString())
        req.add('buy', Boolean.toString(buy))
        return rest.postForObject("http://localhost:${ port }/submit", new HttpEntity(req, headers), Map)
    }

    void "submit and get"() {
        given:
            long now = System.currentTimeMillis()
        when:
            Map<String, Object> resp = submit(1, 'btc', 'mxn', 1.0, 380_000.00, true)
        then:
            resp != null
            resp.id != null
            Long.parseLong(resp.id.toString()) > 0
            resp.status == 'active'
            resp.created != null
            //resp.created.time > now
        when:
            Map<String, Object> resp2 = rest.getForEntity("http://localhost:${ port }/get/${ resp.id }", Map).body
        then:
            resp2 != null
            resp2 == resp
    }

    void books() {
        when:
            List<Map<String, Object>> resp = rest.getForEntity("http://localhost:${ port }/book/btc/mxn", List).body
        then:
            resp != null
            resp.size() >= 2
    }

    void "other queries"() {
        when:
            List<Map<String, Object>> resp = rest.getForEntity("http://localhost:${ port }/query/1/active/btc/mxn", List).body
        then:
            resp != null
            resp.size() >= 2
    }

}
