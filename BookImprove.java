
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// ==========================================
// FILE DATABASE
// ==========================================
class FileDatabase {

    public static final Path FILE_PATH = Paths.get("Book.txt");

    public static List<Book> loadBooks() {
        List<Book> inventory = new ArrayList<>();

        if (!Files.exists(FILE_PATH)) {
            try {
                List<String> defaultBooks = new ArrayList<>();

                defaultBooks.add("Clean Code|Robert C. Martin|true");
                defaultBooks.add("Java Basics|Herbert Schildt|true");
                defaultBooks.add("Java Hard Mode|Aaron|true");
                defaultBooks.add("Bible|Virador|true");
                defaultBooks.add("Java Hard|Jacob|true");

                Files.write(FILE_PATH, defaultBooks);

            } catch (IOException e) {
                JOptionPane.showMessageDialog(
                    null,
                    "Error creating Book.txt: " + e.getMessage()
                );
            }
        }

        try {
            List<String> lines = Files.readAllLines(FILE_PATH);

            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\|");

                if (parts.length == 3) {
                    String title = parts[0].trim();
                    String author = parts[1].trim();

                    boolean available =
                        Boolean.parseBoolean(parts[2].trim());

                    inventory.add(
                        new Book(title, author, available)
                    );
                }
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                null,
                "Error reading Book.txt: " + e.getMessage()
            );
        }

        return inventory;
    }

    public static boolean saveBooks(List<Book> inventory) {
        List<String> lines = new ArrayList<>();

        for (Book book : inventory) {
            lines.add(
                book.getTitle() + "|"
                + book.getAuthor() + "|"
                + book.isAvailable()
            );
        }

        try {
            Files.write(FILE_PATH, lines);
            return true;

        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                null,
                "Error saving Book.txt: " + e.getMessage()
            );
            return false;
        }
    }
}

// ==========================================
// BOOK DATA STRUCTURE
// ==========================================
class Book {

    private String title;
    private String author;
    private boolean isAvailable;

    public Book(String title, String author, boolean isAvailable) {
        this.title = title;
        this.author = author;
        this.isAvailable = isAvailable;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }
}

// ==========================================
// ABSTRACT LIBRARY ACTION
// ==========================================
abstract class LibraryAction {

    abstract void execute(List<Book> inventory);
}

// ==========================================
// BORROW ACTION
// ==========================================
class BorrowAction extends LibraryAction {

    @Override
    void execute(List<Book> inventory) {
        // GUI handles borrowing through LibraryGUI.
    }
}

// ==========================================
// RETURN ACTION
// ==========================================
class ReturnAction extends LibraryAction {

    @Override
    void execute(List<Book> inventory) {
        // GUI handles returning through LibraryGUI.
    }
}

// ==========================================
// SEARCH ACTION
// ==========================================
class SearchAction extends LibraryAction {

    @Override
    void execute(List<Book> inventory) {
        // GUI handles catalog display through LibraryGUI.
    }
}

// ==========================================
// GUI CLASS
// ==========================================
class LibraryGUI extends JFrame implements ActionListener {

    private JTextField userIdField;
    private JTextField titleField;

    private JTextArea catalogArea;

    private JButton borrowButton;
    private JButton returnButton;
    private JButton searchButton;
    private JButton clearButton;
    private JButton exitButton;

    private List<Book> inventory;

    public LibraryGUI() {

        inventory = FileDatabase.loadBooks();

        // JFrame settings
        setTitle("Community Library System");
        setSize(750, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // ==================================
        // HEADING
        // ==================================
        JLabel heading = new JLabel(
            "COMMUNITY LIBRARY SYSTEM",
            SwingConstants.CENTER
        );

        heading.setFont(new Font("Arial", Font.BOLD, 24));
        heading.setForeground(new Color(25, 60, 90));
        heading.setBorder(
            BorderFactory.createEmptyBorder(20, 10, 15, 10)
        );

        add(heading, BorderLayout.NORTH);

        // ==================================
        // INPUT FIELDS
        // ==================================
        JPanel inputPanel = new JPanel(
            new GridLayout(2, 2, 10, 10)
        );

        inputPanel.setBorder(
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        );

        JLabel userIdLabel = new JLabel("User ID:");
        JLabel titleLabel = new JLabel("Book Title:");

        userIdField = new JTextField();
        titleField = new JTextField();

        userIdLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 15));

        inputPanel.add(userIdLabel);
        inputPanel.add(userIdField);
        inputPanel.add(titleLabel);
        inputPanel.add(titleField);

        // ==================================
        // BUTTONS
        // ==================================
        borrowButton = new JButton("Borrow Book");
        returnButton = new JButton("Return Book");
        searchButton = new JButton("View Catalog");
        clearButton = new JButton("Clear Fields");
        exitButton = new JButton("Exit");

        styleButton(borrowButton, new Color(35, 122, 87));
        styleButton(returnButton, new Color(40, 101, 165));
        styleButton(searchButton, new Color(107, 92, 165));
        styleButton(clearButton, new Color(100, 116, 139));
        styleButton(exitButton, new Color(180, 55, 55));

        // Event listeners
        borrowButton.addActionListener(this);
        returnButton.addActionListener(this);
        searchButton.addActionListener(this);
        clearButton.addActionListener(this);
        exitButton.addActionListener(this);

        JPanel buttonPanel = new JPanel(
            new GridLayout(1, 5, 8, 8)
        );

        buttonPanel.setBorder(
            BorderFactory.createEmptyBorder(0, 20, 15, 20)
        );

        buttonPanel.add(borrowButton);
        buttonPanel.add(returnButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(exitButton);

        // ==================================
        // CATALOG AREA
        // ==================================
        catalogArea = new JTextArea();
        catalogArea.setEditable(false);
        catalogArea.setFont(
            new Font("Monospaced", Font.PLAIN, 13)
        );
        catalogArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(catalogArea);

        scrollPane.setBorder(
            BorderFactory.createTitledBorder("Library Inventory")
        );

        // ==================================
        // LAYOUT
        // ==================================
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.CENTER);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.setBorder(
            BorderFactory.createEmptyBorder(0, 15, 15, 15)
        );

        add(mainPanel, BorderLayout.CENTER);

        // Display catalog on startup
        displayCatalog();
    }

    // ==================================
    // BUTTON DESIGN
    // ==================================
    private void styleButton(JButton button, Color color) {

        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
    }

    // ==================================
    // EVENT HANDLING
    // ==================================
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == borrowButton) {
            borrowBook();

        } else if (e.getSource() == returnButton) {
            returnBook();

        } else if (e.getSource() == searchButton) {
            displayCatalog();

        } else if (e.getSource() == clearButton) {
            userIdField.setText("");
            titleField.setText("");
            userIdField.requestFocus();

        } else if (e.getSource() == exitButton) {

            int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit?",
                "Exit System",
                JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                dispose();
            }
        }
    }

    // ==================================
    // FIND BOOK
    // ==================================
    private Book findBook(String title) {

        String cleanInput = title.replaceAll("\\s+", "");

        for (Book book : inventory) {

            String cleanCatalogTitle =
                book.getTitle().replaceAll("\\s+", "");

            if (cleanCatalogTitle.equalsIgnoreCase(cleanInput)) {
                return book;
            }
        }

        return null;
    }

    // ==================================
    // BORROW BOOK
    // ==================================
    private void borrowBook() {

        String userId = userIdField.getText().trim();
        String title = titleField.getText().trim();

        if (userId.isEmpty() || title.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Please enter your User ID and book title.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Book book = findBook(title);

        if (book == null) {

            JOptionPane.showMessageDialog(
                this,
                "Book not found in our catalog.",
                "Book Not Found",
                JOptionPane.ERROR_MESSAGE
            );

        } else if (!book.isAvailable()) {

            JOptionPane.showMessageDialog(
                this,
                "\"" + book.getTitle() + "\" is already borrowed.",
                "Book Unavailable",
                JOptionPane.WARNING_MESSAGE
            );

        } else {

            book.setAvailable(false);

            if (!FileDatabase.saveBooks(inventory)) {
                book.setAvailable(true);
                return;
            }

            JOptionPane.showMessageDialog(
                this,
                "Successfully borrowed the book!\n\n"
                + "Book: " + book.getTitle() + "\n"
                + "Author: " + book.getAuthor() + "\n"
                + "Borrower ID: " + userId,
                "Borrow Successful",
                JOptionPane.INFORMATION_MESSAGE
            );

            displayCatalog();

            userIdField.setText("");
            titleField.setText("");
        }
    }

    // ==================================
    // RETURN BOOK
    // ==================================
    private void returnBook() {

        String title = titleField.getText().trim();

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Please enter the title of the book to return.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Book book = findBook(title);

        if (book == null) {

            JOptionPane.showMessageDialog(
                this,
                "This book does not belong to our library.",
                "Book Not Found",
                JOptionPane.ERROR_MESSAGE
            );

        } else if (book.isAvailable()) {

            JOptionPane.showMessageDialog(
                this,
                "This book is already available.",
                "Return Notice",
                JOptionPane.INFORMATION_MESSAGE
            );

        } else {

            book.setAvailable(true);

            if (!FileDatabase.saveBooks(inventory)) {
                book.setAvailable(false);
                return;
            }

            JOptionPane.showMessageDialog(
                this,
                "Thank you for returning \""
                + book.getTitle() + "\"!",
                "Return Successful",
                JOptionPane.INFORMATION_MESSAGE
            );

            displayCatalog();
            titleField.setText("");
        }
    }

    // ==================================
    // SEARCH AND DISPLAY CATALOG
    // ==================================
    private void displayCatalog() {

        StringBuilder output = new StringBuilder();

        output.append(
            String.format(
                "%-25s %-22s %-12s%n",
                "TITLE", "AUTHOR", "STATUS"
            )
        );

        output.append(
            "------------------------------------------------------------\n"
        );

        if (inventory.isEmpty()) {

            output.append("The library catalog is empty.\n");

        } else {

            for (Book book : inventory) {

                String status = book.isAvailable()
                    ? "Available"
                    : "Borrowed";

                output.append(
                    String.format(
                        "%-25s %-22s %-12s%n",
                        book.getTitle(),
                        book.getAuthor(),
                        status
                    )
                );
            }
        }

        catalogArea.setText(output.toString());
        catalogArea.setCaretPosition(0);
    }
}

// ==========================================
// MAIN / RUNNABLE CLASS
// ==========================================
public class BookImprove {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            LibraryGUI gui = new LibraryGUI();
            gui.setVisible(true);
        });
    }
}









// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Scanner;

// // Helper class to handle file operations
// class FileDatabase {
//     public static final Path FILE_PATH = Paths.get("Book.txt");

//     // Loads books from the text file into your program's memory
//     public static List<Book> loadBooks() {
//         List<Book> inventory = new ArrayList<>();
        
//         // If the file doesn't exist yet, create a default one with the book data
//         if (!Files.exists(FILE_PATH)) {
//             try {
//                 List<String> defaultBooks = new ArrayList<>();
//                 defaultBooks.add("Clean Code|Robert C. Martin|true");
//                 defaultBooks.add("Java Basics|Herbert Schildt|true");
//                 defaultBooks.add("Java Hard Mode|Aaron|true");
//                 defaultBooks.add("Bible | Virador | true");
//                 defaultBooks.add("Java Hard | Jacob | True");
//                 Files.write(FILE_PATH, defaultBooks);
//                 System.out.println(" Created a fresh 'books.txt' file with your catalog.");
//             } catch (IOException e) {
//                 System.out.println(" Error creating database file.");
//             }
//         }

//         try {
//             List<String> lines = Files.readAllLines(FILE_PATH);
//             for (String line : lines) {
//                 if (line.trim().isEmpty()) continue;
                
//                 // Split the text line by the '|' character
//                 String[] parts = line.split("\\|");
//                 if (parts.length == 3) {
//                     // Stripping spaces around the data to ensure accuracy
//                     String title = parts[0].trim();       
//                     String author = parts[1].trim();      
//                     boolean isAvailable = Boolean.parseBoolean(parts[2].trim()); 
                    
//                     inventory.add(new Book(title, author, isAvailable));
//                 }
//             }
//         } catch (IOException e) {
//             System.out.println("Error reading from books.txt: " + e.getMessage());
//         }
//         return inventory;
//     }

//     // Overwrites the text file with the updated status of all books
//     public static void saveBooks(List<Book> inventory) {
//         List<String> linesToSave = new ArrayList<>();
//         for (Book b : inventory) {
//             linesToSave.add(b.getTitle() + "|" + b.getAuthor() + "|" + b.isAvailable());
//         }
//         try {
//             Files.write(FILE_PATH, linesToSave);
//         } catch (IOException e) {
//             System.out.println(" Error saving changes to books.txt: " + e.getMessage());
//         }
//     }
// }

// // Base Class
// abstract class LibraryAction {
//     protected Scanner scanner = new Scanner(System.in);
//     abstract void execute(List<Book> inventory);
// }

// // 1. BORROW ACTION
// class BorrowAction extends LibraryAction {
//     @Override
//     void execute(List<Book> inventory) {
//         System.out.println("\n--- BORROW A BOOK ---");
//         System.out.print("Enter your User ID: ");
//         String userId = scanner.nextLine();
//         System.out.print("Enter the title of the book you want to borrow: ");
//         String title = scanner.nextLine();

//         // Strip ALL spaces from what the user typed to prevent spacing errors
//         String cleanInput = title.replace(" ", "").trim();

//         Book foundBook = null;
//         for (Book b : inventory) {
//             // Strip ALL spaces from the catalog title before comparing
//             String cleanCatalogTitle = b.getTitle().replace(" ", "").trim();
            
//             if (cleanCatalogTitle.equalsIgnoreCase(cleanInput)) {
//                 foundBook = b;
//                 break;
//             }
//         }

//         System.out.println("\n========================");
//         if (foundBook == null) {
//             System.out.println(" Error: Book not found in our catalog.");
//         } else if (!foundBook.isAvailable()) {
//             System.out.println(" Status: \"" + foundBook.getTitle() + "\" is already borrowed.");
//         } else {
//             foundBook.setAvailable(false);
            
//             // Save the update directly to the text file
//             FileDatabase.saveBooks(inventory);
            
//             System.out.println(" Success! You have successfully borrowed the book.");
//             System.out.println(" Book: " + foundBook.getTitle());
//             System.out.println(" Author: " + foundBook.getAuthor());
//             System.out.println(" Borrower ID: " + userId);
//         }
//         System.out.println("========================");
//     }
// }

// // 2. RETURN ACTION
// class ReturnAction extends LibraryAction {
//     @Override
//     void execute(List<Book> inventory) {
//         System.out.println("\n--- RETURN A BOOK ---");
//         System.out.print("Enter the title of the book you are returning: ");
//         String title = scanner.nextLine();

//         String cleanInput = title.replace(" ", "").trim();

//         Book foundBook = null;
//         for (Book b : inventory) {
//             String cleanCatalogTitle = b.getTitle().replace(" ", "").trim();
//             if (cleanCatalogTitle.equalsIgnoreCase(cleanInput)) {
//                 foundBook = b;
//                 break;
//             }
//         }

//         System.out.println("\n========================");
//         if (foundBook == null) {
//             System.out.println(" Error: This book does not belong to our library.");
//         } else if (foundBook.isAvailable()) {
//             System.out.println("Notice: This book is already marked as available.");
//         } else {
//             foundBook.setAvailable(true);
            
//             FileDatabase.saveBooks(inventory);
            
//             System.out.println(" Success! Thank you for returning \"" + foundBook.getTitle() + "\".");
//         }
//         System.out.println("========================");
//     }
// }

// // 3. SEARCH & DISPLAY ACTION
// class SearchAction extends LibraryAction {
//     @Override
//     void execute(List<Book> inventory) {
//         System.out.println("\n--- LIBRARY INVENTORY ---");
//         System.out.printf("%-25s %-20s %-15s\n", "TITLE", "AUTHOR", "STATUS");
//         System.out.println("------------------------------------------------------------");
        
//         if (inventory.isEmpty()) {
//             System.out.println(" [!] The catalog is empty. No books loaded from books.txt.");
//         } else {
//             for (Book b : inventory) {
//                 String statusText = b.isAvailable() ? " Available" : " Borrowed";
//                 System.out.printf("%-25s %-20s %-15s\n", b.getTitle(), b.getAuthor(), statusText);
//             }
//         }
//         System.out.println("------------------------------------------------------------");
//     }
// }

// // BOOK DATA STRUCTURE
// class Book {
//     private String title;
//     private String author;
//     private boolean isAvailable;

//     public Book(String title, String author, boolean isAvailable) {
//         this.title = title;
//         this.author = author;
//         this.isAvailable = isAvailable; 
//     }

//     public String getTitle() { return title; }
//     public String getAuthor() { return author; }
//     public boolean isAvailable() { return isAvailable; }
//     public void setAvailable(boolean available) { this.isAvailable = available; }
// }

// // MAIN CONTROLLER
// public class BookImprove {
//     public static void main(String[] args) {
//         Scanner scanner = new Scanner(System.in);
        
//         // Load inventory from text file
//         List<Book> inventory = FileDatabase.loadBooks();

//         LibraryAction[] action = new LibraryAction[] {
//             new BorrowAction(),
//             new ReturnAction(),
//             new SearchAction()
//         };

//         boolean running = true;
//         System.out.println(" WELCOME TO THE COMMUNITY LIBRARY SYSTEM ");

//         while (running) {
//             System.out.println("\nMAIN MENU:");
//             System.out.println("[1] Borrow a Book");
//             System.out.println("[2] Return a Book");
//             System.out.println("[3] View Catalog & Search");
//             System.out.println("[4] Exit System");
//             System.out.print("Please select an option (1-4): ");
            
//             if (!scanner.hasNextInt()) {
//                 System.out.println(" Invalid input! Please enter a number between 1 and 4.");
//                 scanner.next(); 
//                 continue;
//             }
            
//             int choice = scanner.nextInt();
//             scanner.nextLine(); // Clear scanner buffer newline character

//             switch (choice) {
//                 case 1:
//                     action[0].execute(inventory); break;
//                 case 2:
//                    action[1].execute(inventory);
//                     break;
//                 case 3:
//                    action[2].execute(inventory);
//                     break;
//                 case 4:
//                     System.out.println("\nThank you for using the Library System. Goodbye!");
//                     running = false;
//                     break;
//                 default:
//                     System.out.println(" Invalid choice. Please select from options 1 to 4.");
//             }
//         }
//         scanner.close();
//     }
// }
