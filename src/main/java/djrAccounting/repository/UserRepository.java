package djrAccounting.repository;

import djrAccounting.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    User findByUsername(String username);
    List<User> findByCompanyId(Long id);
    List<User> findByRoleId(Long id);




}
