package cashtracker.pojo;

import com.fasterxml.jackson.annotation.JsonRootName;


@JsonRootName("meta")
public class Meta {

	public long total = 0;
	public int limit = 20;
	public int offset = 0;

	public Meta(long total, int limit, int offset) {
		this.total = total;
		this.limit = limit;
		this.offset = offset;
	}
}
