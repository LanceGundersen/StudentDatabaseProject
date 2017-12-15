import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * StudentDatabase.java
 * Lance Gundersen
 * 15DEC17
 * @version 1.0
 *
 * This program is a student database management project.
 */

public class StudentDatabase extends JFrame {

  private static final int WINDOW_WIDTH = 400, WINDOW_HEIGHT = 200;
  private static final String[] LETTER_GRADE =
      {"A", "B", "C", "D", "F"};
  private static Integer[] CREDITS = {1, 3, 4, 6};
  private static HashMap<String, Student> studentDatabase = new HashMap<>();
  private static JTextField idTextField = new JTextField();
  private static JTextField nameTextField = new JTextField();
  private static JTextField majorTextField = new JTextField();
  private static JComboBox<String> selectionBox = new JComboBox<>();
  private static JButton processButton = new JButton("Process Request");
  private static JOptionPane frame = new JOptionPane();
  private static JLabel idLabel = new JLabel("Id: ");
  private static JLabel nameLabel = new JLabel("Name: ");
  private static JLabel majorLabel = new JLabel("Major: ");
  private static JLabel selectionBoxLabel = new JLabel("Choose Selection: ");

  private StudentDatabase() {
    super("Student Database Management");

    JPanel mainPanel = createMainFrame();
    addPanels(mainPanel);
    createFieldForChooseSelection(mainPanel);
    createProcessButton(mainPanel);
  }

  public static void main(String[] args) {
    StudentDatabase database = new StudentDatabase();
    database.display();
  }

  private void setFrame(int width, int height) {
    setSize(width, height);
    setLocationRelativeTo(null);
  }

  private void display() {
    setVisible(true);
  }

  private void addPanels(JPanel mainPanel) {
    JComponent[] components = {idLabel, idTextField,
        nameLabel, nameTextField, majorLabel,
        majorTextField,
        selectionBoxLabel};

    addToPanel(mainPanel, components);
  }

  private void addToPanel(JPanel p, JComponent[] c) {
    for (JComponent component : c) {
      p.add(component);
    }
  }

  private void addItemsToField(JComboBox<String> field, String[] items) {
    for (String item : items) {
      field.addItem(item);
    }
  }

  private void createFieldForChooseSelection(JPanel mainPanel) {
    mainPanel.add(selectionBox);

    String[] items = {"Insert", "Delete", "Find", "Update"};
    addItemsToField(selectionBox, items);
  }

  private void createProcessButton(JPanel mainPanel) {
    mainPanel.add(processButton);
    processButton
        .addActionListener(new
            buttonForProcessListener());
  }

  private JPanel createMainFrame() {
    setFrame(WINDOW_WIDTH, WINDOW_HEIGHT);
    setResizable(false);
    JPanel mainPanel = new JPanel();
    add(mainPanel);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainPanel.setLayout(new GridLayout(5, 5, 10, 10));
    return mainPanel;
  }

  class buttonForProcessListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {
      Object userSelection = selectionBox.getSelectedItem();

      assert userSelection != null;
      if (userSelection.equals("Insert")) {
        insertStudent();
      }

      if (userSelection.equals("Delete")) {
        deleteStudent();
      }

      if (userSelection.equals("Find")) {
        findStudent();
      }

      if (userSelection.equals("Update")) {
        updatePerformed();
      }
    }

    private void blankFieldCatch(JTextField[] requiredFields) {
      for (JTextField field : requiredFields) {
        blankFieldError(field);
      }
    }

    private void updatePerformed() {
      blankFieldCatch(new JTextField[]
          {idTextField, nameTextField,
              majorTextField});
      String id = idTextField.getText();
      Student student = studentDatabase.get(id);
      if (student == null) {
        showDialog("Student not found in database!");
        return;
      }

      if (!studentFound(student)) {
        JOptionPane.showMessageDialog(frame,"Student ID and/or information mismatch!");
        return;
      }

      String grade = queryGrade(LETTER_GRADE);
      Integer cred = queryCredit(CREDITS);

      if (grade == null || cred == null) {
        showDialog("Cancelled.");
      } else {
        Integer gradeAsNumber = gradeMap(grade);
        student.courseCompleted(cred, gradeAsNumber);
        showDialog("Success!");
      }
    }

    private boolean studentFound(Student student) {
      String name = nameTextField.getText();
      String major = majorTextField.getText();
      AtomicBoolean nameMatches = new AtomicBoolean(student.getName().equals(name));
      boolean majorMatches = student.getMajor().equals(major);
      boolean studentInfoMatchesInput;
      studentInfoMatchesInput = nameMatches.get() && majorMatches;
      return studentInfoMatchesInput;
    }

    private Integer queryCredit(Integer[] credits) {
      return (Integer) JOptionPane.showInputDialog(null,
          "Please Choose " + "Credit", "Credit",
          JOptionPane.QUESTION_MESSAGE, null,
          credits, credits[0]);
    }

    private String queryGrade(String[] letterGrade) {
      return (String) JOptionPane.showInputDialog(frame,
          "Please Choose Grade", "Grade",
          JOptionPane.QUESTION_MESSAGE, null, letterGrade,
          letterGrade[0]);
    }

    private Integer gradeMap(String grade) {
      return Arrays.asList("F" + "D", "C", "B", "A")
          .indexOf(grade) + 1;

    }

    private void findStudent() {
      blankFieldCatch(new JTextField[] {idTextField, nameTextField, majorTextField});
      String id = idTextField.getText();
      Student student = studentDatabase.get(id);
      if (student == null) {
        showDialog("Student not found in database!");
      } else if (!studentFound(student)) {
        JOptionPane.showMessageDialog(frame,
            "Student ID and/or information mismatch!");

      } else if (studentDatabase.containsKey(id)) {
        JOptionPane.showMessageDialog(null, student, "Find",
            JOptionPane.INFORMATION_MESSAGE);
      } else {
        showDialog("Student not found in database!");
      }

    }

    private void deleteStudent() {
      blankFieldCatch(new JTextField[]
          {idTextField, nameTextField,
              majorTextField});
      String id = idTextField.getText();
      Student student = studentDatabase.get(id);
      if (student == null) {
        showDialog("Student not found in database!");
      } else if (!studentFound(student)) {
        JOptionPane.showMessageDialog(frame,
            "Student ID and/or information mismatch!");
      } else if (studentDatabase.containsKey(idTextField.getText())) {
        studentDatabase.remove(idTextField.getText());
        showDialog("Success!");
      } else {
        showDialog("Student not found in database!");
      }
    }

    private void insertStudent() {
      String id = idTextField.getText();

      if (studentDatabase.containsKey(id)) {
        showDialog("Please enter a student ID that doesn't already exist.");
      } else {
        String name = nameTextField.getText();
        String major = majorTextField.getText();
        Student student = new Student(name, major);
        studentDatabase.put(id, student);
        showDialog("Success!");
      }
    }

    private void showDialog(String msg) {
      JOptionPane.showMessageDialog(frame, msg);
    }

    private boolean blankError(JTextField field) {
      return field.getText().equals("");
    }

    private void blankFieldError(JTextField requiredField) {
      if (blankError(requiredField)) {
        showDialog("Error: Student cannot be blank!");
      }
    }
  }
}
