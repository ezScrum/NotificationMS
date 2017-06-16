package ntut.csie.service;

import ntut.csie.model.Subscriber;
import ntut.csie.repository.SubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriberServiceImpl implements SubscriberService{
    @Autowired
    private SubscriberRepository subscriberRepository;

    @Override
    public Subscriber findSubscriberByUsername(String username) {
        return subscriberRepository.findSubscriberByUsername(username);
    }

    @Override
    public List<Subscriber> getSubscriberList() {
        return subscriberRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        subscriberRepository.delete(id);
    }

    @Override
    public Subscriber save(Subscriber subscriber) {
        Subscriber sub = new Subscriber();
        sub.setUsername(subscriber.getUsername());

        return subscriberRepository.save(sub);
    }

    @Override
    public Subscriber findUserById(Long id) {
        return subscriberRepository.findOne(id);
    }
}
