package ntut.csie.service;


import ntut.csie.model.FilterModel;

public interface FilterService {
    FilterModel findBySubscriberId(Long subscriberId);

    void delete(Long id);

    FilterModel save(FilterModel filterModel);

    FilterModel update(FilterModel filterModel);
}
