package ntut.csie.service;


import ntut.csie.model.Subscriber;

import java.util.List;

public interface SubscriberService {
    Subscriber findSubscriberByUsername(String username);

    List<Subscriber> getSubscriberList();

    void delete(Long id);

    Subscriber save(Subscriber subscriber);

    Subscriber findUserById(Long id);
}
