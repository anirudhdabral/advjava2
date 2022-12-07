package hibernateassignment;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
public class Main {
	private static void displayError(String error) {
		System.out.println(error + "\n");
	}

	private static void displayMessage(String message) {
		System.out.print(message);
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int sortType = 0;
		String color, size, gender, response;
		FileTask task = new FileTask();
		Timer t = new Timer();
		t.schedule(task, 0, 3000);

		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		SessionFactory factory = new Configuration().configure().buildSessionFactory();
		Session session = factory.openSession();

		while (true) {
			displayMessage("\nColor: ");
			color = sc.nextLine().toUpperCase();
			displayMessage("Size: ");
			size = sc.nextLine().toUpperCase();
			displayMessage("Gender (M/F): ");
			gender = sc.nextLine().toUpperCase();
			displayMessage("Sort by:\n" + "\t1. price\n" + "\t2. rating\n" + "\t3. both\n" + "\t4. none\n");
			try {
				sortType = sc.nextInt();
				sc.nextLine();
			} catch (InputMismatchException e) {
				displayError("Choose the option number only!");
				sc.nextLine();
				continue;
			}
			String query = "from Tshirts where color=:co and gender=:gen and size=:sz";

			switch (sortType) {
			case 1:
				query += " order by price";
				break;
			case 2:
				query += " order by rating desc";
				break;
			case 3:
				query += " order by rating desc, price";
				break;
			case 4:
				query += " order by id";
				break;
			default:
				displayError("Invalid option");
				continue;
			}
			Query q = session.createQuery(query);
			q.setParameter("co", color);
			q.setParameter("gen", gender);
			q.setParameter("sz", size);

			List<Tshirts> ls = q.list();
			if (ls.isEmpty()) {
				displayError("No match found");
			} else {
				for (Tshirts ts : ls) {
					System.out.println(ts);
				}
			}

			displayMessage("\nDo you want to search again (y/n):");
			response = sc.nextLine();
			if (response.toUpperCase().equals("N")) {
				break;
			}
		}
		session.close();
		sc.close();
		displayMessage("\nThank you for using our software!");
		System.exit(0);

	}

}
