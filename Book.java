import java.util.*;
import java.text.SimpleDateFormat;

class Book {
    private int bookId;
    private String title;
    private String author;
    private boolean available;

    public Book(int bookId, String title, String author) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.available = true;
    }

    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}

class Member {
    private int memberId;
    private String name;
    private String email;

    public Member(int memberId, String name, String email) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
    }

    public int getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}

class Transaction {
    private int transactionId;
    private Member member;
    private Book book;
    private Date issueDate;
    private Date returnDate;
    private double fineAmount;

    public Transaction(int transactionId, Member member, Book book) {
        this.transactionId = transactionId;
        this.member = member;
        this.book = book;
        this.issueDate = new Date();
        this.returnDate = null;
        this.fineAmount = 0.0;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public Member getMember() {
        return member;
    }

    public Book getBook() {
        return book;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }
}

class Library {
    private List<Book> books;
    private List<Member> members;
    private List<Transaction> transactions;
    private int bookIdCounter;
    private int memberIdCounter;
    private int transactionIdCounter;

    public Library() {
        books = new ArrayList<>();
        members = new ArrayList<>();
        transactions = new ArrayList<>();
        bookIdCounter = 1;
        memberIdCounter = 1;
        transactionIdCounter = 1;
    }

    public void addBook(String title, String author) {
        Book book = new Book(bookIdCounter++, title, author);
        books.add(book);
    }

    public void addMember(String name, String email) {
        Member member = new Member(memberIdCounter++, name, email);
        members.add(member);
    }

    public Book findBookById(int bookId) {
        for (Book book : books) {
            if (book.getBookId() == bookId) {
                return book;
            }
        }
        return null;
    }

    public Member findMemberById(int memberId) {
        for (Member member : members) {
            if (member.getMemberId() == memberId) {
                return member;
            }
        }
        return null;
    }

    public void issueBook(int memberId, int bookId) {
        Member member = findMemberById(memberId);
        Book book = findBookById(bookId);

        if (member == null || book == null || !book.isAvailable()) {
            System.out.println("Invalid member or book, or the book is not available.");
            return;
        }

        Transaction transaction = new Transaction(transactionIdCounter++, member, book);
        transactions.add(transaction);
        book.setAvailable(false);

        System.out.println("Book issued successfully.");
    }

    public void returnBook(int memberId, int bookId) {
        Member member = findMemberById(memberId);
        Book book = findBookById(bookId);

        if (member == null || book == null) {
            System.out.println("Invalid member or book.");
            return;
        }

        Transaction transaction = findTransaction(memberId, bookId);

        if (transaction == null || transaction.getReturnDate() != null) {
            System.out.println("Transaction not found or book already returned.");
            return;
        }

        Date currentDate = new Date();
        transaction.setReturnDate(currentDate);
        long daysLate = calculateDaysLate(transaction.getIssueDate(), currentDate);

        if (daysLate > 0) {
            double fine = calculateFine(daysLate);
            transaction.setFineAmount(fine);
            System.out.println("Book returned successfully with a fine of $" + fine);
        } else {
            System.out.println("Book returned successfully.");
        }

        book.setAvailable(true);
    }

    public void listBooks() {
        System.out.println("Library Books:");
        for (Book book : books) {
            System.out.println("Book ID: " + book.getBookId() + ", Title: " + book.getTitle() + ", Author: " + book.getAuthor() + ", Available: " + (book.isAvailable() ? "Yes" : "No"));
        }
    }

    public void listMembers() {
        System.out.println("Library Members:");
        for (Member member : members) {
            System.out.println("Member ID: " + member.getMemberId() + ", Name: " + member.getName() + ", Email: " + member.getEmail());
        }
    }

    public void listTransactions() {
        System.out.println("Library Transactions:");
        for (Transaction transaction : transactions) {
            String returnStatus = (transaction.getReturnDate() == null) ? "Not Returned" : "Returned";
            System.out.println("Transaction ID: " + transaction.getTransactionId() + ", Member: " + transaction.getMember().getName() + ", Book: " + transaction.getBook().getTitle() + ", Issue Date: " + formatDate(transaction.getIssueDate()) + ", Return Date: " + formatDate(transaction.getReturnDate()) + ", Fine Amount: $" + transaction.getFineAmount() + ", Status: " + returnStatus);
        }
    }

    private Transaction findTransaction(int memberId, int bookId) {
        for (Transaction transaction : transactions) {
            if (transaction.getMember().getMemberId() == memberId && transaction.getBook().getBookId() == bookId) {
                return transaction;
            }
        }
        return null;
    }

    private long calculateDaysLate(Date issueDate, Date returnDate) {
        long diff = returnDate.getTime() - issueDate.getTime();
        return Math.max(0, diff / (24 * 60 * 60 * 1000));
    }

    private double calculateFine(long daysLate) {
        // You can implement your own fine calculation logic here
        // For example, charge $1 per day late
        return daysLate;
    }

    private String formatDate(Date date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return (date != null) ? sdf.format(date) : "N/A";
        }
    
        public static void main(String[] args) {
            Library library = new Library();
    
            // Adding some books and members
            library.addBook("Book 1", "Author 1");
            library.addBook("Book 2", "Author 2");
            library.addMember("Member 1", "member1@example.com");
            library.addMember("Member 2", "member2@example.com");
    
            // Issuing and returning books
            library.issueBook(1, 1);
            library.returnBook(1, 1);
    
            // Listing books, members, and transactions
            library.listBooks();
            library.listMembers();
            library.listTransactions();
        }
    }
    
