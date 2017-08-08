package ntut.csie.service;


import ntut.csie.model.SubscriberModel;

import java.util.List;

public interface SubscriberService {
    SubscriberModel findSubscriberByUsername(String username);

    List<SubscriberModel> getSubscriberList();

    void delete(Long id);

    SubscriberModel save(SubscriberModel subscriberModel);

    SubscriberModel findUserById(Long id);
}
