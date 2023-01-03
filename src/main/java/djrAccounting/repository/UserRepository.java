package djrAccounting.repository;

import djrAccounting.entity.Company;
import djrAccounting.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Query("Select u from User u where u.isDeleted=?1 order by u.company.title, u.role.description ")
    List<User> findAllOrderByCompanyAndRole(Boolean deleted);


    //User findUserById(Long id);

    //User findByUsernameAndIsDeleted(Long id, boolean a);

    List<User> findAllByRole_Description(String admin);


    List<User> findAllByCompany_Title(Object currentUserCompanyTitle);

    List<User> findAllByRoleDescriptionOrderByCompanyTitle(String role);
    List<User> findAllByCompanyOrderByRoleDescription(Company company);

    List<User> findAllByRoleDescriptionAndCompanyOrderByCompanyTitleAscRoleDescription(String admin, Company company);

    List <User> findAllByCompanyId(Long id);

}
