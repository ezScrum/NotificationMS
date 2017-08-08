package ntut.csie.service;


import ntut.csie.model.FilterModel;
import ntut.csie.repository.FilterRepository;

public class FilterServiceImpl implements FilterService{
    FilterRepository filterRepository;

    @Override
    public FilterModel findBySubscriberId(Long subscriberId){
        return filterRepository.findBySubscriberId(subscriberId);
    }

    @Override
    public void delete(Long id){filterRepository.delete(id);}

    @Override
    public FilterModel save(FilterModel filterModel){
        FilterModel fm = new FilterModel();
        fm.setSubscriberId(filterModel.getSubscriberId());
        fm.setFilter(filterModel.getFilter());

        return filterRepository.save(fm);
    }

    @Override
    public FilterModel update(FilterModel filterModel){
        return filterRepository.save(filterModel);
    }
}
