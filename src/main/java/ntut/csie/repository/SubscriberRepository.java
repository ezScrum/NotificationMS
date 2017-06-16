package ntut.csie.repository;

import ntut.csie.model.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriberRepository extends JpaRepository<Subscriber, Long>{
    Subscriber findSubscriberByUsername(String username);
}
