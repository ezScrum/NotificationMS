package ntut.csie.service;

import ntut.csie.model.SubscriberModel;
import ntut.csie.repository.SubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriberServiceImpl implements SubscriberService{
    @Autowired
    private SubscriberRepository subscriberRepository;

    @Override
    public SubscriberModel findSubscriberByUsername(String username) {
        return subscriberRepository.findSubscriberByUsername(username);
    }

    @Override
    public List<SubscriberModel> getSubscriberList() {
        return subscriberRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        subscriberRepository.delete(id);
    }

    @Override
    public SubscriberModel save(SubscriberModel subscriberModel) {
        SubscriberModel sub = new SubscriberModel();
        sub.setUsername(subscriberModel.getUsername());

        return subscriberRepository.save(sub);
    }

    @Override
    public SubscriberModel findUserById(Long id) {
        return subscriberRepository.findOne(id);
    }
}
