package ntut.csie.repository;

import ntut.csie.model.FilterModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilterRepository extends JpaRepository<FilterModel,Long>{
    FilterModel findBySubscriberId(Long subscriberId);
}
