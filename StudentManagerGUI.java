import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentManagerGUI extends JFrame {

    static final String DB_URL = "jdbc:mysql://localhost:3306/school";
    static final String USER = "root";
    static final String PASS = "";

    JTextField tfName, tfAge, tfMarks, tfID, tfUpdateMarks;
    JTextArea textArea;

    public StudentManagerGUI() {
        setTitle("Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        container.setBackground(new Color(245, 248, 250));
        add(container);

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 20, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add Student"));
        formPanel.setBackground(Color.white);

        JLabel lblName = new JLabel("Name:");
        lblName.setFont(labelFont);
        tfName = new JTextField();
        tfName.setFont(inputFont);

        JLabel lblAge = new JLabel("Age:");
        lblAge.setFont(labelFont);
        tfAge = new JTextField();
        tfAge.setFont(inputFont);

        JLabel lblMarks = new JLabel("Marks:");
        lblMarks.setFont(labelFont);
        tfMarks = new JTextField();
        tfMarks.setFont(inputFont);

        formPanel.add(lblName); formPanel.add(tfName);
        formPanel.add(lblAge); formPanel.add(tfAge);
        formPanel.add(lblMarks); formPanel.add(tfMarks);

        container.add(formPanel);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(245, 248, 250));

        JButton btnAdd = new JButton("Add Student");
        JButton btnView = new JButton("View Students");
        JButton btnUpdate = new JButton("Update Marks");
        JButton btnDelete = new JButton("Delete Student");

        styleButton(btnAdd, new Color(200, 230, 201));
        styleButton(btnView, new Color(187, 222, 251));
        styleButton(btnUpdate, new Color(255, 224, 178));
        styleButton(btnDelete, new Color(255, 205, 210));

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnView);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

        container.add(buttonPanel);

        // Edit Panel
        JPanel editPanel = new JPanel(new GridLayout(2, 2, 20, 10));
        editPanel.setBorder(BorderFactory.createTitledBorder("Edit Student"));
        editPanel.setBackground(Color.white);

        JLabel lblID = new JLabel("Student ID:");
        lblID.setFont(labelFont);
        tfID = new JTextField();
        tfID.setFont(inputFont);

        JLabel lblNewMarks = new JLabel("New Marks:");
        lblNewMarks.setFont(labelFont);
        tfUpdateMarks = new JTextField();
        tfUpdateMarks.setFont(inputFont);

        editPanel.add(lblID); editPanel.add(tfID);
        editPanel.add(lblNewMarks); editPanel.add(tfUpdateMarks);

        container.add(editPanel);

        // Output Area
        textArea = new JTextArea(10, 50);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Output"));

        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(scrollPane);

        // Action Listeners
        btnAdd.addActionListener(e -> addStudent());
        btnView.addActionListener(e -> viewStudents());
        btnUpdate.addActionListener(e -> updateMarks());
        btnDelete.addActionListener(e -> deleteStudent());

        setVisible(true);
    }

    private void styleButton(JButton btn, Color bgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(150, 35));
        btn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(bgColor.darker());
            }

            public void mouseExited(MouseEvent evt) {
                btn.setBackground(bgColor);
            }
        });
    }

    private void addStudent() {
        try {
            String name = tfName.getText().trim();
            int age = Integer.parseInt(tfAge.getText().trim());
            double marks = Double.parseDouble(tfMarks.getText().trim());

            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                String sql = "INSERT INTO students (name, age, marks) VALUES (?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, name);
                ps.setInt(2, age);
                ps.setDouble(3, marks);
                ps.executeUpdate();
                textArea.setText("Student added successfully.\n");
                tfName.setText(""); tfAge.setText(""); tfMarks.setText("");
            }
        } catch (Exception ex) {
            textArea.setText("Error adding student: " + ex.getMessage());
        }
    }

    private void viewStudents() {
        textArea.setText("Student Records:\n\n");
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "SELECT * FROM students";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            textArea.append(String.format("%-5s %-20s %-5s %-6s\n", "ID", "Name", "Age", "Marks"));
            textArea.append("--------------------------------------------------------\n");

            while (rs.next()) {
                textArea.append(String.format("%-5d %-20s %-5d %-6.2f\n",
                        rs.getInt("id"), rs.getString("name"), rs.getInt("age"), rs.getDouble("marks")));
            }
        } catch (Exception ex) {
            textArea.setText("Error retrieving students: " + ex.getMessage());
        }
    }

    private void updateMarks() {
        try {
            int id = Integer.parseInt(tfID.getText().trim());
            double newMarks = Double.parseDouble(tfUpdateMarks.getText().trim());

            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                String sql = "UPDATE students SET marks = ? WHERE id = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setDouble(1, newMarks);
                ps.setInt(2, id);
                int rows = ps.executeUpdate();

                if (rows > 0) {
                    textArea.setText("Marks updated for student ID: " + id);
                } else {
                    textArea.setText("No student found with ID: " + id);
                }
            }
        } catch (Exception ex) {
            textArea.setText("Error updating marks: " + ex.getMessage());
        }
    }

    private void deleteStudent() {
        try {
            int id = Integer.parseInt(tfID.getText().trim());

            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                String sql = "DELETE FROM students WHERE id = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                int rows = ps.executeUpdate();

                if (rows > 0) {
                    textArea.setText("Student with ID " + id + " deleted.");
                } else {
                    textArea.setText("No student found with ID: " + id);
                }
            }
        } catch (Exception ex) {
            textArea.setText("Error deleting student: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentManagerGUI::new);
    }
}
