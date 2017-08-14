package ntut.csie.service;

import ntut.csie.model.FilterModel;
import ntut.csie.repository.FilterRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class FilterServiceTest {
    @Autowired
    private FilterService filterService;

    @Autowired
    private FilterRepository filterRepository;

    private String initFilterString = "";
    @Before
    public void setup(){
        FilterModel filterModel = new FilterModel();
        filterModel.setId(Integer.toUnsignedLong(1));
        filterModel.setSubscriberId(Integer.toUnsignedLong(1));
        JSONObject filter = new JSONObject();
        try{
            JSONArray projects = new JSONArray();
            JSONObject projectFilter = new JSONObject();
            projectFilter.put("Id","project1");
            projectFilter.put("Subscribe",true);
            projectFilter.put("event",new JSONObject());
            projects.put(projectFilter);
            filter.put("ezScrum",projects);
        }catch(JSONException e){
        }
        initFilterString = filter.toString();
        filterModel.setFilter(initFilterString);
        filterRepository.save(filterModel);
    }

    @After
    public void teardown(){filterRepository.deleteAll();}

    @Test
    public void TestFindBySubscriberId(){
        FilterModel filterModel = filterService.findBySubscriberId(Integer.toUnsignedLong(1));

        Assert.assertEquals(Integer.toUnsignedLong(1),filterModel.getSubscriberId().longValue());
        Assert.assertEquals(initFilterString,filterModel.getFilter());
    }

    @Test
    public void TestSave(){
        FilterModel filterModel = new FilterModel();
        filterModel.setSubscriberId(Integer.toUnsignedLong(2));
        JSONObject filter = new JSONObject();
        try{
            JSONArray projects = new JSONArray();
            JSONObject projectFilter = new JSONObject();
            projectFilter.put("Id","project2");
            projectFilter.put("Subscribe",true);
            projectFilter.put("event",new JSONObject());
            projects.put(projectFilter);
            filter.put("ezScrum",projects);
        }catch(JSONException e){
        }
        filterModel.setFilter(filter.toString());
        filterService.save(filterModel);

        FilterModel _filterModel = filterService.findBySubscriberId(Integer.toUnsignedLong(2));
        Assert.assertEquals(Integer.toUnsignedLong(2),_filterModel.getSubscriberId().longValue());
        Assert.assertEquals(filter.toString(),_filterModel.getFilter());
    }

    @Test
    public void TestUpdate(){
        FilterModel filterModel = filterService.findBySubscriberId(Integer.toUnsignedLong(1));
        JSONObject filter = new JSONObject();
        try{
            JSONArray projects = new JSONArray();
            JSONObject projectFilter1 = new JSONObject();
            projectFilter1.put("Id","project1");
            projectFilter1.put("Subscribe",true);
            projectFilter1.put("event",new JSONObject());
            projects.put(projectFilter1);
            JSONObject projectFilter2 = new JSONObject();
            projectFilter2.put("Id","project2");
            projectFilter2.put("Subscribe",true);
            projectFilter2.put("event",new JSONObject());
            projects.put(projectFilter2);
            filter.put("ezScrum",projects);
        }catch(JSONException e){
        }
        filterModel.setFilter(filter.toString());
        filterService.update(filterModel);

        FilterModel _filterModel = filterService.findBySubscriberId(Integer.toUnsignedLong(1));
        Assert.assertEquals(Integer.toUnsignedLong(1),_filterModel.getSubscriberId().longValue());
        Assert.assertEquals(filter.toString(),_filterModel.getFilter());
    }

    @Test
    public void TestDelete(){
        FilterModel filterModel = filterService.findBySubscriberId(Integer.toUnsignedLong(1));
        filterService.delete(filterModel.getId());
        filterModel = filterService.findBySubscriberId(Integer.toUnsignedLong(1));
        Assert.assertNull(filterModel);
        Assert.assertEquals(0,filterRepository.count());
    }
}
