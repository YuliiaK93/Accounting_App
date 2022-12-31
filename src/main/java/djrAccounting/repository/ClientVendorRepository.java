package djrAccounting.repository;

import djrAccounting.entity.ClientVendor;
import djrAccounting.enums.ClientVendorType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientVendorRepository extends JpaRepository<ClientVendor,Long> {
  List<ClientVendor> findAllByCompanyId(Long id);
  List<ClientVendor>  findAllByCompanyIdAndClientVendorTypeOrderByClientVendorName(Long id, ClientVendorType clientVendorType);
  boolean existsByClientVendorName(String name);

}
