import java.util.ArrayList;

public class FullCourse extends Course{
    public FullCourse(FullCourseBuilder builder) {
        super(builder);
    }

    @Override
    public ArrayList<Student> getGraduatedStudents() {
        ArrayList<Student> graduatedStudents = new ArrayList<Student>();
        for(Grade grade: gettAllStudentGrades().values()) {
            if(grade.getPartialScore() >= 3.0 && grade.getExamScore() >= 2.0) {
                graduatedStudents.add(grade.getStudent());
            }
        }
        return graduatedStudents;
    }

    public String getCourseType() {
        return "Full Course";
    }

    public static class FullCourseBuilder extends Course.CourseBuilder {
        @Override
        public Course buildCourse() {
            return new FullCourse(this);
        }
    }
}
