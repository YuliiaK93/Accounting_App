package djrAccounting.repository;

import djrAccounting.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    User findByUsername(String username);
    @Query("Select u from User u where u.isDeleted=?1 order by u.company.title, u.role.description ")
    List<User> findAllOrderByCompanyAndRole(Boolean deleted);




}
