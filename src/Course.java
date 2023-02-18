import java.util.*;

public abstract class Course {
    private String name;
    private Teacher teacher;
    private SortedSet<Assistant> assistants;
    private ArrayList<Grade> grades;
    private TreeMap<String, Group> groups;
    private CourseStrategy courseStrategy;
    private int credit;

    public Course(CourseBuilder builder) {
        setName(builder.name);
        setTeacher(builder.teacher);
        setCourseStrategy(builder.courseStrategy);
        setCredit(builder.credit);

        groups = new TreeMap<String, Group>();
        grades = new ArrayList<Grade>();
    }

    // Set an assistant in a group with the given ID
    // If it doesn't exist yet, add the assistant in the assistant lot
    public void addAssistant(String ID, Assistant assistant) {
        Group group = groups.get(ID);
        group.setAssistant(assistant);

        assistants.add(assistant);
    }

    // Add the student in the group with this ID
    public void addStudent(String ID, Student student) {
        Group group = groups.get(ID);
        group.add(student);
    }

    //Add a group
    public void addGroup(Group group) {
        groups.put(group.getID(), group);
    }

    // Instantiate and add a group
    public void addGroup(String ID, Assistant assistant) {
        groups.put(ID, new Group(ID, assistant));
    }

    // Instantiate and add a group
    public void addGroup(String ID, Assistant assist, Comparator<Student> comp) {
        groups.put(ID, new Group(ID, assist, comp));
    }

    public Grade getGrade(Student student) {
        for(Grade gr : grades) {
            if(gr.getStudent().equals(student)) {
                return gr;
            }
        }
        return null;
    }

    public void addGrade(Grade grade) {
        grades.add(grade);
        Collections.sort(grades);
    }
    public ArrayList<Student> getAllStudents() {
        ArrayList<Student> students = new ArrayList<Student>();
        for(Group group: groups.values()) {
            students.addAll(group);
        }
        return students;
    }

    public HashMap<Student, Grade> gettAllStudentGrades() {
        HashMap<Student, Grade>studentGradeHashMap = new HashMap<Student, Grade>();
        for(Grade grade: grades) {
            studentGradeHashMap.put(grade.getStudent(), grade);
        }
        return studentGradeHashMap;
    }

    public Student getBestStudent() {
        return courseStrategy.getBestStudent(grades);
    }

    public abstract ArrayList<Student> getGraduatedStudents();
    public abstract String getCourseType();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public SortedSet<Assistant> getAssistants() {
        return assistants;
    }

    public void setAssistants(SortedSet<Assistant> assistants) {
        this.assistants = assistants;
    }

    public ArrayList<Grade> getGrades() {
        return grades;
    }

    public void setGrades(ArrayList<Grade> grades) {
        this.grades = grades;
    }

    public TreeMap<String, Group> getDict() {
        return groups;
    }

    public void setDict(TreeMap<String, Group> dict) {
        this.groups = dict;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public void setCourseStrategy(CourseStrategy courseStrategy) {
        this.courseStrategy = courseStrategy;
    }


    public abstract static class CourseBuilder {
        private String name;
        private Teacher teacher;
        //private SortedSet<Assistant> assistants;
        //private ArrayList<Grade> grades;
        //private TreeMap<String, Group> groups;
        private CourseStrategy courseStrategy;
        private int credit;

        UserFactory userFactory = UserFactory.getFactory();

        public CourseBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public CourseBuilder setTeacher(String firstName, String lastName) {
            this.teacher = (Teacher)userFactory.getUser(UserFactory.UserType.Teacher, firstName, lastName);
            return this;
        }

        public CourseBuilder setCourseStrategy(String courseStrategy) {
            if (courseStrategy.equals("BestExamScore")) {
                this.courseStrategy = new BestExamScore();
            } else if (courseStrategy.equals("BestPartialScore")) {
                this.courseStrategy = new BestPartialScore();
            } else if (courseStrategy.equals("BestTotalScore")) {
                this.courseStrategy = new BestTotalScore();
            }
            return this;
        }
        public CourseBuilder setCredit(int credit) {
            this.credit = credit;
            return this;
        }

        public abstract Course buildCourse();
    }
}
