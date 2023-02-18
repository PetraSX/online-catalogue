import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

public class ScoreVisitor implements Visitor {
    private TreeMap<Teacher, ArrayList<Tuple<Student, String, Double>>> examScores;
    private TreeMap<Assistant, ArrayList<Tuple<Student, String, Double>>> partialScores;

    private static ScoreVisitor scoreVisitorInstance;

    public ScoreVisitor() {
        examScores = new TreeMap<>();
        partialScores = new TreeMap<>();
    }
    public static ScoreVisitor getScoreVisitorInstance() {
        if(scoreVisitorInstance == null) {
            synchronized (ScoreVisitor.class) {
                if(scoreVisitorInstance == null) {
                    scoreVisitorInstance = new ScoreVisitor();
                }
            }
        }
        return scoreVisitorInstance;
    }
    @Override
    public void visit(Assistant assistant) {
        Catalog catalog = Catalog.getInstance();
        ArrayList<Course> courses = catalog.getCourses();
        for(Tuple<Student, String, Double> tuple : partialScores.get(assistant)) {
            for(Course course : courses) {
                if(course.getName().equals(tuple.second)) {
                    Grade gr = course.getGrade(tuple.first);
                    if(gr != null) {
                        gr.setPartialScore(tuple.third);
                    }
                    else {
                        gr = new Grade();
                        gr.setPartialScore(tuple.third);
                        gr.setCourse(tuple.second);
                        gr.setStudent(tuple.first);
                        course.addGrade(gr);
                    }
                    catalog.notifyObserver(gr);
                }
            }
        }
    }

    @Override
    public void visit(Teacher teacher) {
        Catalog catalog = Catalog.getInstance();
        ArrayList<Course> courses = catalog.getCourses();
        for(Tuple<Student, String, Double> tuple : examScores.get(teacher)) {
            for(Course course : courses) {
                if(course.getName().equals(tuple.second)) {
                    Grade gr = course.getGrade(tuple.first);
                    if(gr != null) {
                        gr.setExamScore(tuple.third);
                    }
                    else {
                        gr = new Grade();
                        gr.setExamScore(tuple.third);
                        gr.setCourse(tuple.second);
                        gr.setStudent(tuple.first);
                        course.addGrade(gr);
                    }
                    catalog.notifyObserver(gr);
                }
            }
        }
    }

    public void addGrade(Teacher teacher, Student student, String course_name, Double grade) {
        if (!examScores.containsKey(teacher)) {
            examScores.put(teacher, new ArrayList<Tuple<Student, String, Double>>());
        }

        examScores.get(teacher).add(new Tuple<>(student, course_name, grade));
    }

    public void addGrade(Assistant assistant, Student student, String course_name, Double grade) {
        if (!partialScores.containsKey(assistant)) {
            partialScores.put(assistant, new ArrayList<Tuple<Student, String, Double>>());
        }

        partialScores.get(assistant).add(new Tuple<>(student, course_name, grade));
    }

    public TreeMap<Student, Double> getExamScoresFrom(Teacher teacher, String courseName) {
        TreeMap<Student, Double> teacherExamScores = new TreeMap<>();
        for(Tuple<Student, String, Double> tuple : examScores.get(teacher)) {
            if(tuple.second.equals(courseName)) {
                teacherExamScores.put(tuple.first, tuple.third);
            }
        }
        return teacherExamScores;
    }

    public TreeMap<Student, Double> getPartialScoresFrom(Assistant assistant, String courseName) {
        TreeMap<Student, Double> assistantPartialScores = new TreeMap<>();
        for(Tuple<Student, String, Double> tuple : partialScores.get(assistant)) {
            if(tuple.second.equals(courseName)) {
                assistantPartialScores.put(tuple.first, tuple.third);
            }
        }
        return assistantPartialScores;
    }

    public TreeMap<Assistant, ArrayList<Tuple<Student, String, Double>>> getPartialScores() {
        return partialScores;
    }

    private static class Tuple <T, U, V> implements Comparable<U>{
        private T first;
        private U second;
        private V third;

        public Tuple(T first, U second, V third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }

        public int compareTo(U param) {
            return this.second.toString().compareTo(param.toString());
        }
    }
}
