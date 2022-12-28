package djrAccounting.repository;

import djrAccounting.entity.ClientVendor;
import djrAccounting.enums.ClientVendorType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientVendorRepository extends JpaRepository<ClientVendor,Long> {

}
