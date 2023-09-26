package yyl.mvc.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.relucent.base.common.page.Pagination;
import com.github.relucent.base.common.page.SimplePage;

import yyl.mvc.plugin.mybatis.MybatisHelper;
import yyl.mvc.test.mapper.DemoMapper;
import yyl.mvc.test.model.Demo;

@Service
public class DemoService {

    @Autowired
    private DemoMapper demoMapper;

    public SimplePage<Demo> getByPage(Pagination pagination, Demo condition) {
        return MybatisHelper.selectPage(pagination, () -> demoMapper.find(condition));
    }
}
