package com.bitso.challenge.model.db

import com.bitso.challenge.entity.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import com.bitso.challenge.model.*


@DataJpaTest
@ContextConfiguration(classes = [TestApplication.class])
@ActiveProfiles("test")
class UserModelImplSpec extends Specification {

  @Autowired
  UserModel userModel

  def "should add user and query it"() {
    setup:
    User user = new User()
    user.setPassword("1perrito.1")
    user.setEmail("test@test.com")
    and:
    User user2 = new User()
    user.setPassword("1perrito.1")
    user.setEmail("test2@test.com")
    when:
    ((UserModelImpl)userModel).add(user)
    ((UserModelImpl)userModel).add(user2)
    then:
    userModel.get(1l).get() != null
    and:
    userModel.get(2l).get() != null
  }
}
