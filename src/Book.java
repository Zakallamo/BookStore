import java.math.BigDecimal;

public class Book {
	private String title;
	private String author;
	private BigDecimal price;
	private int qty;
	
	public void SetTitle(String bookTitle) {
		this.title = bookTitle;
		return;
	}

	public String GetTitle() {
		return this.title;
	}

	public void SetAuthor(String author) {
		this.author = author;
		return;
	}

	public String GetAuthor() {
		return this.author;
	}

	public void SetPrice(BigDecimal price) {
		this.price = price;
		return;
	}

	public BigDecimal GetPrice() {
		return this.price;
	}

	public void SetQty(int temp) {
		this.qty = temp;
		return;
	}

	public int GetQty() {
		return this.qty;
	}

}