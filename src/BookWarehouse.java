import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/* Known problems/holes:
 * 1.	To choose a book from inventory. that is if there are multiple with either the same book title or author (I'm looking at you, Cunning Bastard and Rich Bloke)
 * 		the system takes first one it finds in the search and list.
 * 	Possible solution: 	Step through the whole inventory and then check if there are several with the same book title or author and put those
 * 						in a different list, give the user a choice over which book the user wants and return that.
 * 
 * 2.	Checking out the cart full of items and comparing if there is enough in storage to allow the checkout. Don't really know how to solve the checks. Same problem with removing books from cart.
 * 
 * 3.	Not sure how to make checks for BOTH author and book title to make the searches better.
 * 
 * 4.	Reading in special tokens like åäö.
 *  Possible solution: 	More time and google for tips and tricks.
 *  
 * 5.	Not really sure how to implement the tests from scratch.
 */

/**
 * 
 * @author Johny Löfgren
 *
 */
public class BookWarehouse implements BookList {
	
	enum status { OK, NOT_IN_STOCK, DOES_NOT_EXIST }
	List<Book> cart = new ArrayList<Book>();
		
	public Book[] list(String searchString) {
		Scanner fileScanner = null;
		int i = 0;
		Book[] tempBook = new Book[7];
		
		try {
			URL url = new URL("http://www.contribe.se/bookstoredata/bookstoredata.txt");
			fileScanner = new Scanner(url.openStream());
		}
		
		catch (IOException ex) {
			ex.printStackTrace();
		}
		
		System.out.print("Searching inventory for book...\n");
		
		do {
			String[] temp = fileScanner.nextLine().split(";");
			
			NumberFormat fmt = NumberFormat.getNumberInstance(Locale.ENGLISH);
			Number num = null;
			
			try {
				num = fmt.parse(temp[2]);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			BigDecimal tempPrice = BigDecimal.valueOf(num.doubleValue());
			
			tempBook[i] = new Book();
			tempBook[i].SetTitle(temp[0]);
			tempBook[i].SetAuthor(temp[1]);
			tempBook[i].SetPrice(tempPrice);
			tempBook[i].SetQty(Integer.parseInt(temp[3]));
			
			i++;
		} while (fileScanner.hasNextLine());
		
		fileScanner.close();
		return tempBook;
	}
	
	public boolean add (Book book, int quantity) {
		
		for (int i = 0; i< quantity; i++) {
			cart.add(book);
		}
		return true;
	}
	
	public int[] buy(Book... books) {
		int[] cost = new int[books.length];
		for (int i = 0; i<books.length; i++) {
			cost[i] = books[i].GetPrice().intValueExact();
		}
		return cost;
	}
	
	public void addToCart(BookWarehouse warehouse, List<Book> bookList, String searchString, Scanner s) {
		
		String input = "";
		
		// searching for a book match...
		for (int j = 0; j<bookList.size(); j++) {
			if (searchString.equals(bookList.get(j).GetTitle()) || searchString.equals(bookList.get(j).GetAuthor())) {
				System.out.println("Found Book!");
				System.out.println("How many of said book do you want to add to cart?");
				input = s.nextLine();
				
				warehouse.add(bookList.get(j) , Integer.parseInt(input));
				// printing out the list of books in cart...
				for (int i = 0; i<warehouse.cart.size(); i++) {
					System.out.println(warehouse.cart.get(i).GetTitle() + ", " + warehouse.cart.get(i).GetAuthor());
				}
			 break;
			}
		}
		
		return;
	}
	
	public void checkOut(BookWarehouse warehouse, List<Book> bookList) {
		
		for (int e = 0; e<bookList.size(); e++) {
			for (int j = 0; j<warehouse.cart.size(); j++) {
				if (bookList.get(e).GetTitle().equals(warehouse.cart.get(j).GetTitle())) {
					if (bookList.get(e).GetQty() == warehouse.cart.get(j).GetQty()) {
						System.out.println("Checking if there is enough in storage... " + status.OK + "\n");
					} else if (bookList.get(e).GetQty() < warehouse.cart.get(j).GetQty()) {
						System.out.print("Checking if there is enough in storage of " + bookList.get(e).GetTitle() + bookList.get(e).GetAuthor()
								+ " ...\n" + status.NOT_IN_STOCK + ". Not enough in stock\n");
						break;
					}
				}
			}
		}
		
		System.out.println("Checking out book(s) in cart...");
		// calculate the sum of all books in cart...
		
		int[] cartCost = warehouse.buy(warehouse.cart.toArray(new Book[warehouse.cart.size()]));
		int sum = 0;
		for (int j = 0; j<cartCost.length; j++) {
			sum += cartCost[j];
		}
		
		System.out.println(sum + " is the price for " + cartCost.length + " books.");
		
		return;
	}
	
	public void removeFromCart (BookWarehouse warehouse, Scanner s) {		
		System.out.print("Which book do you want removed?\n");
		
		// printing out the list of books in cart...
		for (int i = 0; i<warehouse.cart.size(); i++) {
			System.out.println(warehouse.cart.get(i).GetTitle() + ", " + warehouse.cart.get(i).GetAuthor());
		}
		// Choose book...
		String searchString = s.nextLine();
		
		for (int i = 0; i<warehouse.cart.size(); i++) {
			if (searchString.equals(warehouse.cart.get(i).GetAuthor()) || searchString.equals(warehouse.cart.get(i).GetTitle())) {
				warehouse.cart.remove(i);
				System.out.println(searchString + " removed");
				break;
			}
		}
		return;
	}
	
	public void addToInventory(List<Book> bookList, Scanner s) {
		Book temp = new Book();
		System.out.println("What is the name of the book?");
		String searchString = s.nextLine();
		temp.SetTitle(searchString);
		System.out.println("What is the of the author of the book?");
		searchString = s.nextLine();
		temp.SetAuthor(searchString);
		// needs to be ',' and not '.' for decimals
		System.out.println("What is the the price of the book?");
		searchString = s.nextLine();
		
		NumberFormat fmt = NumberFormat.getNumberInstance(Locale.ENGLISH);
		Number num = null;
		
		try {
			num = fmt.parse(searchString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BigDecimal tempPrice = BigDecimal.valueOf(num.doubleValue());
		
		temp.SetPrice(tempPrice);
		System.out.println("How many of the book is added to inventory?");
		searchString = s.nextLine();
		temp.SetQty(Integer.parseInt(searchString));
		
		bookList.add(temp);
	}
	
	public static void main (String [] arguments) {
		Scanner s = new Scanner(System.in);
		int userChoice = 0;
		boolean quit = false;
		Book[] tempArray = new Book[7];
		List<Book> bookList = new ArrayList<Book>();
		BookWarehouse warehouse = new BookWarehouse();
		
		for (int i = 0; i<tempArray.length; i++)
			tempArray[i] = new Book();
		
		do {
			System.out.println();
			System.out.println("1) Add book(s) to cart");
			System.out.println("2) Buy books");
			System.out.println("3) Remove items from cart");
			System.out.println("4) Add new books to inventory");
			System.out.println("5) Quit");
			System.out.println();
			System.out.print("Enter choice: ");
			
			try {
				userChoice = Integer.parseInt(s.nextLine());
			}
			catch (NumberFormatException e) {
				e.printStackTrace();
			}
			
			switch(userChoice) {
			case 1: // adding book(s) to cart...
				
				System.out.println("Enter book title or author: ");
				String searchString = s.nextLine();
				
				tempArray = warehouse.list(searchString);
				for (int i = 0; i<tempArray.length; i++) {
					bookList.add(tempArray[i]);
				}
				warehouse.addToCart(warehouse, bookList, searchString, s);
				
				break;
				
			case 2: // checking out the cart...
				if (warehouse.cart.isEmpty()) {
					System.out.println("The cart is empty");
					break;
				} else {
					warehouse.checkOut(warehouse, bookList);
				}
				break;
			
			case 3: // remove books from cart...
				
				if (warehouse.cart.isEmpty()) {
					System.out.println("The cart is empty");
					break;
				} else {
					warehouse.removeFromCart(warehouse, s);
				}
				break;
				
			case 4: // Add new books to inventory...
				warehouse.addToInventory(bookList, s);
				break;
				
			case 5: // Finished with the program...
				
				System.out.println("Shutting down, see you later!");
				quit = true;
				break;
				
			default:
				
				System.out.println("\nInvalid choice");
				break;	
			}
		} while (!quit);
		s.close();
	}
}