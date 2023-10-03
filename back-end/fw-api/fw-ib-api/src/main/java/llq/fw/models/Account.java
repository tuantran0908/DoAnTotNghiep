package llq.fw.models;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Account {
	private String acc;
	private String id; // true là đã được chọn , false là không được chọn
	public Account(String acc, String id) {
		super();
		this.acc = acc;
		this.id = id;
	}
	
}
