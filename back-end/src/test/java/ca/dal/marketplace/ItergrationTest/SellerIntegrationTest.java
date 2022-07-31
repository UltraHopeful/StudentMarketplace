package ca.dal.marketplace.ItergrationTest;

import ca.dal.marketplace.dao.*;
import ca.dal.marketplace.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SellerIntegrationTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void UserRegistrationSuccessful() throws ParseException
    {
        User user = new User();
        String sDate1="1998-01-29";
        Date date=new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);
        user.setId(1000L);
        user.setEmail("test@gmail.com");
        user.setPassword("asdasdasd");
        user.setEnabled(true);
        user.setDateOfBirth(date);
        user.setFirstName("kaasdasdasdvya");
        user.setLastName("Kasasdasdasdaraneni");
        user.setPhoneNumber("902-412-8559");
        user.setLocked(false);
        user.setFailedAttempts(0);

        user = testEntityManager.merge(user);
        testEntityManager.flush();
        userRepository.save(user);
        assertThat(userRepository.findByEmailIgnoreCase(user.getEmail())).isNotNull();
    }

    @Test
    public void UserLoginSuccessful(){
        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword("asdasdasd");

    }

    @Test
    public void ShouldReturnInterestedBuyersOfPost(){


    }

}
