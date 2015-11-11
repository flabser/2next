package task.helper;

public class PageRequest {

	private int offset;
	private int limit;
	private String sort;

	public PageRequest(int offset, int limit, String sort) {
		this.offset = offset;
		this.limit = (limit < 1 || limit > 100) ? 20 : limit;
		this.sort = sort;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
}
