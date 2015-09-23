package cashtracker.pojo;

import com.fasterxml.jackson.annotation.JsonRootName;


@JsonRootName("meta")
public class Meta {

	public int total = 0;
	public int total_pages = 0;
	public int page = 0;
	public int limit = 20;
	public int offset = 0;

	public Meta(int total, int limit, int offset, int page) {
		this.total = total;
		this.limit = limit;
		this.offset = offset;
		this.total_pages = calculatePageCount(total, limit);
		this.page = page;
	}

	public static int calculatePageCount(int count, int limit) {
		return count > limit ? (int) Math.ceil((double) count / limit) : 1;
	}
}
