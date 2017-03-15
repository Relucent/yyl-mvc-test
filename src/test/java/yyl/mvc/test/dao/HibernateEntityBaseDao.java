package yyl.mvc.test.dao;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;

import yyl.mvc.core.plug.hibernate.HibernateSimpleEntityDao;
import yyl.mvc.core.plug.hibernate.query.CriterionBuildWalker;
import yyl.mvc.core.util.collect.Mapx;
import yyl.mvc.core.util.page.Page;
import yyl.mvc.core.util.page.Pagination;

public abstract class HibernateEntityBaseDao<T> extends HibernateSimpleEntityDao<T> {

	protected final Logger logger = Logger.getLogger(getClass());

	@Autowired
	protected CriterionBuildWalker criterionBuildWalker;

	/**
	 * 默认的分页查询
	 * @param pageQuery 查询条件
	 * @return 分页对象，包含该页数据
	 */
	public Page<T> pagedQuery(Pagination pagination) {
		Criteria criteria = super.createCriteria(super.entityClass);
		int start = pagination.getStart();
		int limit = pagination.getLimit();
		Mapx filters = pagination.getFilters();
		buildCriterions(criteria, filters, super.entityClass);
		return super.pagedQuery(criteria, start, limit);
	}

	protected void buildCriterions(Criteria criteria, Mapx filters, Class<T> entityClass) {
		criterionBuildWalker.addCriterions(criteria, filters, entityClass);
	}
}