package yyl.mvc.core.util.page.impl;

import java.util.ArrayList;
import java.util.List;

import yyl.mvc.core.util.page.Page;

/**
 * 分页查询的结果数据.<br>
 */
@SuppressWarnings("serial")
public class SimplePage<T> implements Page<T> {

	// =================================Constants=============================================
	public static final int DEFAULT_LIMIT = 20;

	// =================================Fields================================================
	/** 开始查询 的数据索引号 (从0开始) */
	private int start = 0;
	/** 每页条数 */
	private int limit = DEFAULT_LIMIT;
	/** 总记录数 */
	private int total = 0;
	/** 当前页数据 */
	private List<T> records;

	// =================================Constructors===========================================
	/**
	 * 构造函数
	 */
	public SimplePage() {
		this.records = new ArrayList<T>();
	}

	/**
	 * 构造函数
	 * @param records 当前页数据
	 */
	public SimplePage(List<T> records) {
		this(0, records.size(), records, records.size());
	}

	/**
	 * 构造函数
	 * @param start 记录开始索引号
	 * @param limit 页面最大记录数
	 * @param records 当前页数据
	 * @param total 总记录数
	 */
	public SimplePage(int start, int limit, List<T> records, int total) {
		this.start = start;
		this.limit = limit;
		this.records = records;
		this.total = total;
	}

	// =================================Methods================================================
	/** 获取从第几条数据开始查询 */
	public int getStart() {
		return start;
	}

	/** 设置从第几条数据开始查询 */
	public void setStart(int start) {
		this.start = start;
	}

	/** 获取每页显示条数 */
	public int getLimit() {
		return limit;
	}

	/** 设置每页显示条数 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/** 设置总条数 */
	public void setTotal(int total) {
		this.total = total;
	}

	/** 获取总条数 */
	public int getTotal() {
		return total;
	}

	/** 获取当前页数据 */
	public List<T> getRecords() {
		return records;
	}

	/** 设置当前页数据 */
	public void setRecords(List<T> records) {
		this.records = records;
	}
}
