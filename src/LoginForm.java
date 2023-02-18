import java.awt.*;
import java.awt.event.*;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class LoginForm extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox studentBox;
    private JCheckBox parentBox;
    private JCheckBox teacherBox;
    private JCheckBox assistantBox;
    private JCheckBox adminBox;
    private JButton loginButton;
    private JButton loadJSONButton;
    private boolean isJSONLoaded;
    private ButtonGroup buttonGroup;
    private JPanel loginPanel;
    private JPanel studentPanel;
    private JPanel parentPanel;
    private JPanel teacherPanel;
    private JPanel assistantPanel;
    private JPanel adminPanel;

    private Student currentStudent;
    private Assistant currentAssistant;
    private Teacher currentTeacher;
    private Parent currentParent;
    private boolean userFound;
    private Catalog catalog;
    private JButton logoutStudentButton;
    private JButton logoutParentButton;
    private JButton logoutTeacherButton;
    private JButton logoutAssistantButton;
    private JButton logoutAdminButton;
    private JComboBox courseSelecter;
    private ImageIcon image;

    private TreeSet<Student> subscribedStudents;

    private DefaultTableModel generalCourseModel;
    private JTable generalCourseTable;
    private JScrollPane generalCoursePane;
    private DefaultTableModel privateCourseModel;
    private JTable privateCourseTable;
    private JScrollPane privateCoursePane;
    private ScoreVisitor scoreVisitor;
    private JComboBox teacherCourseSelecter;
    private DefaultTableModel teacherCourseModel;
    private JTable teacherCourseTable;
    private JScrollPane teacherCoursePane;
    private JButton validateExamScores;
    private JComboBox assistantCourseSelecter;
    private DefaultTableModel assistantCourseModel;
    private JTable assistantCourseTable;
    private JScrollPane assistantCoursePane;
    private JButton validatePartialScores;

    public LoginForm() {
        super("Catalog");

        //TestInterface.LoadJSON();

        image = new ImageIcon("logo-ro-web.jpg");
        this.setIconImage(image.getImage());

        catalog = Catalog.getInstance();
        subscribedStudents = new TreeSet<Student>();
        getAllSubscribedStudents();
        scoreVisitor = ScoreVisitor.getScoreVisitorInstance();
        //acceptScores();


        isJSONLoaded = false;

        // create the text fields for the username and password
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);

        // create the checkboxes and add them to a ButtonGroup
        studentBox = new JCheckBox("Student");
        parentBox = new JCheckBox("Parent");
        teacherBox = new JCheckBox("Teacher");
        assistantBox = new JCheckBox("Assistant");
        adminBox = new JCheckBox("Admin");
        buttonGroup = new ButtonGroup();
        buttonGroup.add(studentBox);
        buttonGroup.add(parentBox);
        buttonGroup.add(teacherBox);
        buttonGroup.add(assistantBox);
        buttonGroup.add(adminBox);

        // create the login button
        loginButton = new JButton("Login");
        loginButton.addActionListener(this);


        // create the login panel with the form
        loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(5, 5, 5, 5);
        loginPanel.add(new JLabel("Username:"), constraints);
        constraints.gridx = 1;
        loginPanel.add(usernameField, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        loginPanel.add(new JLabel("Password:"), constraints);
        constraints.gridx = 1;
        loginPanel.add(passwordField, constraints);
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        loginPanel.add(studentBox, constraints);
        constraints.gridy = 3;
        loginPanel.add(parentBox, constraints);
        constraints.gridy = 4;
        loginPanel.add(teacherBox, constraints);
        constraints.gridy = 5;
        loginPanel.add(assistantBox, constraints);
        constraints.gridy = 6;
        loginPanel.add(adminBox, constraints);
        constraints.gridx = 0;
        constraints.gridy = 7;
        constraints.gridwidth = 2;
        loginPanel.add(loginButton, constraints);
        this.add(loginPanel);


        // create the student panel with a logout button and selecter
        courseSelecter = new JComboBox();
        generalCourseModel = new DefaultTableModel();
        generalCourseModel.addColumn("Course Name");
        generalCourseModel.addColumn("Teacher");
        generalCourseModel.addColumn("No. Credit Points");
        generalCourseModel.addColumn("Assistant 1");
        generalCourseModel.addColumn("Assistant 2");
        generalCourseModel.setNumRows(1);
        generalCourseTable = new JTable(generalCourseModel);
        generalCourseTable.setFillsViewportHeight(false);
        generalCoursePane = new JScrollPane(generalCourseTable);
        generalCoursePane.setPreferredSize(new Dimension(400, 150));

        privateCourseModel = new DefaultTableModel();
        privateCourseModel.addColumn("Group");
        privateCourseModel.addColumn("Assistant");
        privateCourseModel.addColumn("Partial Score");
        privateCourseModel.addColumn("Exam Score");
        privateCourseModel.addColumn("Total Score");
        privateCourseModel.setNumRows(1);
        privateCourseTable = new JTable(privateCourseModel);
        privateCourseTable.setFillsViewportHeight(false);
        privateCoursePane = new JScrollPane(privateCourseTable);
        privateCoursePane.setPreferredSize(new Dimension(400, 150));

        studentPanel = new JPanel();
        studentPanel.setLayout(new FlowLayout());
        logoutStudentButton = new JButton("Logout");
        logoutStudentButton.addActionListener(this);
        studentPanel.add(courseSelecter);
        studentPanel.add(generalCoursePane);
        studentPanel.add(privateCoursePane);
        studentPanel.add(logoutStudentButton);


        // create the parent panel
        parentPanel = new JPanel();
        logoutParentButton = new JButton("Logout");
        logoutParentButton.addActionListener(this);
        parentPanel.add(logoutParentButton);


        // create the teacher panel with a logout button
        teacherCourseSelecter = new JComboBox();
        teacherCourseModel = new DefaultTableModel();
        teacherCourseModel.addColumn("Student");
        teacherCourseModel.addColumn("Group");
        teacherCourseModel.addColumn("Assistant");
        teacherCourseModel.addColumn("Exam Score");
        teacherCourseTable = new JTable(teacherCourseModel);
        teacherCourseTable.setFillsViewportHeight(false);
        teacherCoursePane = new JScrollPane(teacherCourseTable);
        teacherCoursePane.setPreferredSize(new Dimension(400,250));

        teacherPanel = new JPanel();
        validateExamScores = new JButton("Validate Exam Scores");
        validateExamScores.setPreferredSize(new Dimension(400,30));
        validateExamScores.addActionListener(this);
        teacherPanel.setLayout(new FlowLayout());
        logoutTeacherButton = new JButton("Logout");
        logoutTeacherButton.addActionListener(this);
        teacherPanel.add(teacherCourseSelecter);
        teacherPanel.add(teacherCoursePane);
        teacherPanel.add(validateExamScores);
        teacherPanel.add(logoutTeacherButton);


        // create the assistant panel
        assistantCourseSelecter = new JComboBox();
        assistantCourseModel = new DefaultTableModel();
        assistantCourseModel.addColumn("Student");
        assistantCourseModel.addColumn("Group");
        assistantCourseModel.addColumn("Teacher");
        assistantCourseModel.addColumn("Partial Score");
        assistantCourseTable = new JTable(assistantCourseModel);
        assistantCourseTable.setFillsViewportHeight(false);
        assistantCoursePane = new JScrollPane(assistantCourseTable);

        assistantCoursePane.setPreferredSize(new Dimension(400,250));
        assistantPanel = new JPanel();
        validatePartialScores = new JButton("Validate Partial Scores");
        validatePartialScores.setPreferredSize(new Dimension(400, 30));
        validatePartialScores.addActionListener(this);
        assistantPanel.setLayout(new FlowLayout());
        logoutAssistantButton = new JButton("Logout");
        logoutAssistantButton.addActionListener(this);
        assistantPanel.add(assistantCourseSelecter);
        assistantPanel.add(assistantCoursePane);
        assistantPanel.add(validatePartialScores);
        assistantPanel.add(logoutAssistantButton);


        // create the admin panel
        adminPanel = new JPanel();
        logoutAdminButton = new JButton("Logout");
        logoutAdminButton.addActionListener(this);
        loadJSONButton = new JButton("Load JSON");
        loadJSONButton.addActionListener(this);
        adminPanel.add(logoutAdminButton);
        adminPanel.add(loadJSONButton);

        // add the panels to the frame and show the login panel
        this.setLayout(new CardLayout(0,0));
        CardLayout cardLayout = new CardLayout();
        this.setLayout(cardLayout);
        this.add(loginPanel, "login");
        this.add(studentPanel, "student");
        this.add(parentPanel, "parent");
        this.add(teacherPanel, "teacher");
        this.add(assistantPanel, "assistant");
        this.add(adminPanel, "admin");
        cardLayout.show(this.getContentPane(), "login");

        // set the size of the frame and display it
        this.setSize(450, 450);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    // helper method to show a specific panel
    private void showPanel(String panel) {
        CardLayout cardLayout = (CardLayout) this.getContentPane().getLayout();
        cardLayout.show(this.getContentPane(), panel);
    }

    public void acceptScores() {
        for(Course curs : catalog.getCourses()) {
            curs.getTeacher().accept(scoreVisitor);
            for (Assistant assistant : curs.getAssistants()) {
                assistant.accept(scoreVisitor);
            }
        }
    }

    public void updateStudentPanel() {
        courseSelecter.addItem("Please select a course");
        for(Course course : catalog.getCourses()) {
            for(Student student : course.getAllStudents()) {
                if(student.equals(currentStudent)) {
                    courseSelecter.addItem(course.getName());
                }
            }
        }
        courseSelecter.addActionListener(this);
    }

    public void clearStudentPanel() {
        courseSelecter.removeActionListener(this);
        courseSelecter.removeAllItems();
    }

    public void updateStudentCourseInfo() {
        int assistantIndex = 3;
        int courseIndex = courseSelecter.getSelectedIndex();
        String courseName = (String) courseSelecter.getSelectedItem();

        if(courseIndex > 0) {
            courseIndex = 0;
            for(Course course : catalog.getCourses()) {
                if(course.getName().equals(courseName)) {
                    generalCourseModel.setValueAt(catalog.getCourses().get(courseIndex).getName(), 0,0);
                    generalCourseModel.setValueAt(catalog.getCourses().get(courseIndex).getTeacher(), 0,1);
                    generalCourseModel.setValueAt(catalog.getCourses().get(courseIndex).getCredit(), 0,2);
                    for(Assistant assistant : catalog.getCourses().get(courseIndex).getAssistants()) {
                        generalCourseModel.setValueAt(assistant.toString(), 0, assistantIndex++);
                    }

                    privateCourseModel.setValueAt(currentStudent.toString(), 0, 0);
                    for(Group group : course.getDict().values()) {
                        for(Student student : group) {
                            if(student.equals(currentStudent)) {
                                privateCourseModel.setValueAt(group.getID(), 0, 0);
                                privateCourseModel.setValueAt(group.getAssistant().toString(), 0, 1);
                                for(Grade grade : course.getGrades()) {
                                    if(grade.getStudent().equals(currentStudent)) {
                                        privateCourseModel.setValueAt(grade.getPartialScore(), 0, 2);
                                        privateCourseModel.setValueAt(grade.getExamScore(), 0, 3);
                                        privateCourseModel.setValueAt(grade.getTotal(), 0, 4);
                                        //return;
                                    }
                                }

                            }
                        }
                    }
                }
                courseIndex++;
            }
        }
    }

    public void clearStudentCourseInfo() {
        for(int i = 0; i < 5; i++) {
            generalCourseModel.setValueAt(null, 0, i);
            privateCourseModel.setValueAt(null, 0, i);
        }
    }

    private void updateTeacherPanel() {
        teacherCourseSelecter.addItem("Please select a course");
        if(!currentTeacher.getExamScoresValidated()) {
            for(Course course : catalog.getCourses()) {
                if(course.getTeacher().equals(currentTeacher)) {
                    teacherCourseSelecter.addItem(course.getName());
                }
            }
        }
        teacherCourseSelecter.addActionListener(this);
    }

    private void clearTeacherPanel() {
        teacherCourseSelecter.removeActionListener(this);
        teacherCourseSelecter.removeAllItems();
    }

    private void updateTeacherCourseInfo() {
        int courseIndex = teacherCourseSelecter.getSelectedIndex();
        int tableIndex = 0;
        String courseName = (String) teacherCourseSelecter.getSelectedItem();
        TreeMap<Student, Double> examScores = null;

        if(courseIndex > 0) {
            examScores = scoreVisitor.getExamScoresFrom(currentTeacher, courseName);
            for(Course course : catalog.getCourses()) {
                if(course.getName().equals(courseName)) {
                    teacherCourseModel.setNumRows(examScores.size());
                    for(Group group : course.getDict().values()) {
                        for(Student student : group) {
                            teacherCourseModel.setValueAt(student.toString(), tableIndex, 0);
                            teacherCourseModel.setValueAt(group.getID(), tableIndex, 1);
                            teacherCourseModel.setValueAt(group.getAssistant().toString(), tableIndex, 2);
                            teacherCourseModel.setValueAt(examScores.get(student), tableIndex, 3);
                            tableIndex++;
                        }
                    }
                }
            }
        }
    }

    private void clearTeacherCourseInfo() {
        for(int i = 0; i < teacherCourseModel.getRowCount(); i++) {
            for(int j = 0; j < 4; j++) {
                teacherCourseModel.setValueAt(null, i, j);
            }
        }
        teacherCourseModel.setNumRows(0);
    }

    public void updateAssistantPanel() {
        assistantCourseSelecter.addItem("Please Select a course");
        if(!currentAssistant.getpartialScoresValidated()) {
            for(Course course : catalog.getCourses()) {
                for(Assistant assistant : course.getAssistants()) {
                    if(assistant.equals(currentAssistant)) {
                        assistantCourseSelecter.addItem(course.getName());
                    }
                }
            }
        }
        assistantCourseSelecter.addActionListener(this);
    }

    public void clearAssistantPanel() {
        assistantCourseSelecter.removeActionListener(this);
        assistantCourseSelecter.removeAllItems();
    }


    private void updateAssistantCourseInfo() {
        int courseIndex = assistantCourseSelecter.getSelectedIndex();
        int tableIndex = 0;
        String courseName = (String) assistantCourseSelecter.getSelectedItem();
        TreeMap<Student, Double> partialScores = null;

        if(courseIndex > 0) {
            partialScores = scoreVisitor.getPartialScoresFrom(currentAssistant, courseName);
            for(Course course : catalog.getCourses()) {
                if(course.getName().equals(courseName)) {
                    assistantCourseModel.setNumRows(partialScores.size());
                    for(Group group : course.getDict().values()) {
                        if(group.getAssistant().equals(currentAssistant)) {
                            for(Student student : group) {
                                assistantCourseModel.setValueAt(student.toString(), tableIndex, 0);
                                assistantCourseModel.setValueAt(group.getID(), tableIndex, 1);
                                assistantCourseModel.setValueAt(course.getTeacher().toString(), tableIndex, 2);
                                assistantCourseModel.setValueAt(partialScores.get(student), tableIndex, 3);
                                tableIndex++;
                            }
                        }
                    }
                }
            }
        }
    }

    private void clearAssistantCourseInfo() {
        for(int i = 0; i < assistantCourseModel.getRowCount(); i++) {
            for(int j = 0; j < 4; j++) {
                assistantCourseModel.setValueAt(null, i, j);
            }
        }
        assistantCourseModel.setNumRows(0);
    }

    public void getAllSubscribedStudents() {
        for(Course course : catalog.getCourses()) {
            subscribedStudents.addAll(course.getAllStudents());
        }
    }

    public void clearLoginInformation() {
        passwordField.setText("");
        usernameField.setText("");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == loginButton) {
            String username = usernameField.getText();
            char[] password = passwordField.getPassword();
            String passwordStr = new String(password);
            userFound = false;
            currentStudent = null;
            currentParent = null;
            currentAssistant = null;
            currentTeacher = null;

            if (studentBox.isSelected()) {
                for(Student student : subscribedStudents) {
                        if(username.equals(student.toString()) && passwordStr.equals("student")){
                            currentStudent = student;
                            userFound = true;
                            updateStudentPanel();
                            showPanel("student");
                        }
                    }
                if(!userFound) {
                    JOptionPane.showMessageDialog(null, "Invalid credentials");
                }
            } else if(parentBox.isSelected()) {
                for(Student student : subscribedStudents) {
                        if(student.getMother() != null) {
                            if(username.equals(student.getMother().toString()) && passwordStr.equals("mother")){
                                currentParent = student.getMother();
                                userFound = true;
                                showPanel("parent");
                            }
                        }
                        if(student.getFather() != null) {
                            if(username.equals(student.getFather().toString()) && passwordStr.equals("father")){
                                currentParent = student.getFather();
                                userFound = true;
                                showPanel("parent");
                            }
                        }
                    }
                if(!userFound) {
                    JOptionPane.showMessageDialog(null, "Invalid credentials");
                }
            } else if (teacherBox.isSelected()) {
                for(Course course : catalog.getCourses()) {
                    if(username.equals(course.getTeacher().toString()) && passwordStr.equals("teacher")) {
                        currentTeacher = course.getTeacher();
                        userFound = true;
                        updateTeacherPanel();
                        showPanel("teacher");
                    }
                }
                if(!userFound) {
                    JOptionPane.showMessageDialog(null, "Invalid credentials");
                }
            } else if(assistantBox.isSelected()) {
                for(Course course : catalog.getCourses()) {
                    for (Assistant assistant : course.getAssistants()) {
                        if (username.equals(assistant.toString()) && passwordStr.equals("assistant")) {
                            currentAssistant = assistant;
                            userFound = true;
                            updateAssistantPanel();
                            showPanel("assistant");
                        }
                    }
                }
                if(!userFound) {
                    JOptionPane.showMessageDialog(null, "Invalid credentials");
                }
            } else if (adminBox.isSelected()) {
                if (username.equals("admin") && passwordStr.equals("admin")) {
                    showPanel("admin");
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid credentials!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a user type!");
            }
        } else if(e.getSource() == loadJSONButton) {
            if(!isJSONLoaded) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter(".json", "json"));
                int response = fileChooser.showOpenDialog(null);

                if(response == JFileChooser.APPROVE_OPTION) {
                    TestInterface.LoadJSON(fileChooser.getSelectedFile().getAbsolutePath());
                    JOptionPane.showMessageDialog(null, "JSON loaded successfully.");
                    isJSONLoaded = true;
                    getAllSubscribedStudents();
                }
            } else {
                JOptionPane.showMessageDialog(null, "JSON already loaded.");
            }
        } else if(e.getSource() == logoutStudentButton) {
            clearStudentPanel();
            clearStudentCourseInfo();
            clearLoginInformation();
            showPanel("login");
        } else if(e.getSource() == logoutParentButton) {
            clearLoginInformation();
            showPanel("login");
        } else if(e.getSource() == validateExamScores) {
            if(!currentTeacher.getExamScoresValidated()) {
                currentTeacher.accept(scoreVisitor);
                currentTeacher.setExamScoresValidated();
                clearTeacherCourseInfo();
                JOptionPane.showMessageDialog(null, "Exam Scores validated successfully");
            } else {
                JOptionPane.showMessageDialog(null, "No Exam Scores to validate");
            }
        } else if(e.getSource() == logoutTeacherButton) {
            clearTeacherPanel();
            clearTeacherCourseInfo();
            clearLoginInformation();
            showPanel("login");
        } else if(e.getSource() == validatePartialScores) {
            if(!currentAssistant.getpartialScoresValidated()) {
                currentAssistant.accept(scoreVisitor);
                currentAssistant.setpartialScoresValidated();
                clearAssistantCourseInfo();
                JOptionPane.showMessageDialog(null, "Partial Scores validated successfully");
            } else {
                JOptionPane.showMessageDialog(null, "No Partial Scores to validate");
            }
        } else if(e.getSource() == logoutAssistantButton) {
            clearAssistantPanel();
            clearAssistantCourseInfo();
            clearLoginInformation();
            showPanel("login");
        } else if(e.getSource() == logoutAdminButton) {
            clearLoginInformation();
            showPanel("login");
        } else if(e.getSource() == courseSelecter) {
            updateStudentCourseInfo();
        } else if(e.getSource() == teacherCourseSelecter) {
            updateTeacherCourseInfo();
        } else if(e.getSource() == assistantCourseSelecter) {
            updateAssistantCourseInfo();
        }
    }


    public static void main(String[] args) {
        new LoginForm();
    }
}

