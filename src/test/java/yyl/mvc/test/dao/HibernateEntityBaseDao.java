package yyl.mvc.test.dao;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.relucent.base.common.collection.Mapx;
import com.github.relucent.base.common.page.Pagination;
import com.github.relucent.base.common.page.SimplePage;

import yyl.mvc.plugin.hibernate.HibernateSimpleEntityDao;
import yyl.mvc.plugin.hibernate.query.CriterionBuildWalker;

public abstract class HibernateEntityBaseDao<T> extends HibernateSimpleEntityDao<T> {

    protected final Logger logger = Logger.getLogger(getClass());

    @Autowired
    protected CriterionBuildWalker criterionBuildWalker;

    /**
     * 默认的分页查询
     * @param pagination 分页条件
     * @param filters 过滤条件
     * @return 分页对象，包含该页数据
     */
    public SimplePage<T> pagedQuery(Pagination pagination, Mapx filters) {
        Criteria criteria = super.createCriteria(super.entityClass);
        long offset = pagination.getOffset();
        long limit = pagination.getLimit();
        buildCriterions(criteria, filters, super.entityClass);
        return super.pagedQuery(criteria, (int) offset, (int) limit);
    }

    protected void buildCriterions(Criteria criteria, Mapx filters, Class<T> entityClass) {
        criterionBuildWalker.addCriterions(criteria, filters, entityClass);
    }
}
