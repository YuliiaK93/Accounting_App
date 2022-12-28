package djrAccounting.repository;

import djrAccounting.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


   User findByUserId(String username);

    User findByUsername(String username);

    @Transactional
    void deleteUserName(String username);


}
