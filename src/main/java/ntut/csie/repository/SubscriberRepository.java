package ntut.csie.repository;

import ntut.csie.model.SubscriberModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriberRepository extends JpaRepository<SubscriberModel, Long>{
    SubscriberModel findSubscriberByUsername(String username);
}
