import java.util.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LibrarySystem {

    static Scanner sc = new Scanner(System.in);

    static Map<String, Book> books = new HashMap<>();
    static Map<String, Member> members = new HashMap<>();

    static final double DAILY_FINE = 10.0;

    static class Book {
        private String title;
        private String author;
        private String isbn;
        private boolean available;
        private LocalDate issueDate;
        private LocalDate dueDate;
        private String borrowerId;

        public Book(String title, String author, String isbn) {
            this.title = title;
            this.author = author;
            this.isbn = isbn;
            this.available = true;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public String getIsbn() {
            return isbn;
        }

        public boolean isAvailable() {
            return available;
        }

        public LocalDate getDueDate() {
            return dueDate;
        }

        public void checkout(String memberId) {
            available = false;
            borrowerId = memberId;
            issueDate = LocalDate.now();
            dueDate = issueDate.plusDays(14);
        }

        public void returnBook() {
            available = true;
            borrowerId = null;
            issueDate = null;
            dueDate = null;
        }
    }

    static class Member {
        private String memberId;
        private String name;
        private List<String> borrowedBooks;

        public Member(String memberId, String name) {
            this.memberId = memberId;
            this.name = name;
            borrowedBooks = new ArrayList<>();
        }

        public void borrowBook(String isbn) {
            borrowedBooks.add(isbn);
        }

        public void returnBook(String isbn) {
            borrowedBooks.remove(isbn);
        }

        public String getName() {
            return name;
        }
    }

    public static void main(String[] args) {

        loadSampleBooks();

        int choice;

        do {
            System.out.println("\n========== DIGITAL LIBRARY MANAGEMENT ==========");
            System.out.println("1. Add Book");
            System.out.println("2. Register Member");
            System.out.println("3. Search Book");
            System.out.println("4. Checkout Book");
            System.out.println("5. Return Book");
            System.out.println("6. Display Inventory");
            System.out.println("7. Exit");
            System.out.print("Enter Choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    registerMember();
                    break;
                case 3:
                    searchBook();
                    break;
                case 4:
                    checkoutBook();
                    break;
                case 5:
                    returnBook();
                    break;
                case 6:
                    displayInventory();
                    break;
                case 7:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid Choice!");
            }

        } while (choice != 7);
    }

    static void loadSampleBooks() {
        books.put("ISBN101",
                new Book("Java Programming", "James Gosling", "ISBN101"));

        books.put("ISBN102",
                new Book("Database Management", "Elmasri", "ISBN102"));

        books.put("ISBN103",
                new Book("Data Structures", "Mark Allen", "ISBN103"));
    }

    static void addBook() {

        System.out.print("Enter Title: ");
        String title = sc.nextLine();

        System.out.print("Enter Author: ");
        String author = sc.nextLine();

        System.out.print("Enter ISBN: ");
        String isbn = sc.nextLine();

        if (books.containsKey(isbn)) {
            System.out.println("ISBN already exists!");
            return;
        }

        books.put(isbn, new Book(title, author, isbn));

        System.out.println("Book Added Successfully.");
    }

    static void registerMember() {

        System.out.print("Enter Member ID: ");
        String id = sc.nextLine();

        System.out.print("Enter Member Name: ");
        String name = sc.nextLine();

        members.put(id, new Member(id, name));

        System.out.println("Member Registered Successfully.");
    }

    static void searchBook() {

        System.out.println("\n1. Search by Title");
        System.out.println("2. Search by Author");
        System.out.println("3. Show Available Books");
        System.out.print("Choose: ");

        int option = sc.nextInt();
        sc.nextLine();

        switch (option) {

            case 1:
                System.out.print("Enter Title Keyword: ");
                String title = sc.nextLine().toLowerCase();

                for (Book b : books.values()) {
                    if (b.getTitle().toLowerCase().contains(title)) {
                        printBook(b);
                    }
                }
                break;

            case 2:
                System.out.print("Enter Author Name: ");
                String author = sc.nextLine().toLowerCase();

                for (Book b : books.values()) {
                    if (b.getAuthor().toLowerCase().contains(author)) {
                        printBook(b);
                    }
                }
                break;

            case 3:
                for (Book b : books.values()) {
                    if (b.isAvailable()) {
                        printBook(b);
                    }
                }
                break;

            default:
                System.out.println("Invalid Option");
        }
    }

    static void checkoutBook() {

        System.out.print("Enter Member ID: ");
        String memberId = sc.nextLine();

        Member member = members.get(memberId);

        if (member == null) {
            System.out.println("Member Not Found!");
            return;
        }

        System.out.print("Enter ISBN: ");
        String isbn = sc.nextLine();

        Book book = books.get(isbn);

        if (book == null) {
            System.out.println("Book Not Found!");
            return;
        }

        if (!book.isAvailable()) {
            System.out.println("Book Already Issued!");
            return;
        }

        book.checkout(memberId);
        member.borrowBook(isbn);

        System.out.println("Book Issued Successfully.");
        System.out.println("Due Date: " + book.getDueDate());
    }

    static void returnBook() {

        System.out.print("Enter Member ID: ");
        String memberId = sc.nextLine();

        Member member = members.get(memberId);

        if (member == null) {
            System.out.println("Member Not Found!");
            return;
        }

        System.out.print("Enter ISBN: ");
        String isbn = sc.nextLine();

        Book book = books.get(isbn);

        if (book == null) {
            System.out.println("Book Not Found!");
            return;
        }

        long overdueDays = 0;

        if (book.getDueDate() != null &&
            LocalDate.now().isAfter(book.getDueDate())) {

            overdueDays = ChronoUnit.DAYS.between(
                    book.getDueDate(),
                    LocalDate.now());
        }

        double fine = overdueDays * DAILY_FINE;

        book.returnBook();
        member.returnBook(isbn);

        System.out.println("\nBook Returned Successfully");
        System.out.println("Overdue Days : " + overdueDays);
        System.out.println("Fine Amount  : Rs. " + fine);
    }

    static void displayInventory() {

        System.out.println("\n----------------------------------------------------------------");
        System.out.printf("%-12s %-25s %-20s %-12s%n",
                "ISBN", "TITLE", "AUTHOR", "STATUS");
        System.out.println("----------------------------------------------------------------");

        for (Book b : books.values()) {

            String status =
                    b.isAvailable() ? "Available" : "Issued";

            System.out.printf("%-12s %-25s %-20s %-12s%n",
                    b.getIsbn(),
                    b.getTitle(),
                    b.getAuthor(),
                    status);
        }
    }

    static void printBook(Book b) {

        System.out.println("\n---------------------------");
        System.out.println("Title  : " + b.getTitle());
        System.out.println("Author : " + b.getAuthor());
        System.out.println("ISBN   : " + b.getIsbn());
        System.out.println("Status : " +
                (b.isAvailable() ? "Available" : "Issued"));
    }
}