import java.util.ArrayList;
import java.util.HashMap;

public class PartialCourse extends Course{
    public PartialCourse(PartialCourseBuilder builder) {
        super(builder);
    }

    @Override
    public ArrayList<Student> getGraduatedStudents() {
        ArrayList<Student> graduatedStudents = new ArrayList<Student>();
        for(Grade grade: gettAllStudentGrades().values()) {
            if(grade.getTotal() >= 5.0) {
                graduatedStudents.add(grade.getStudent());
            }
        }
        return graduatedStudents;
    }

    public String getCourseType() {
        return "Partial Course";
    }

    public static class PartialCourseBuilder extends Course.CourseBuilder {
        @Override
        public Course buildCourse() {
            return new PartialCourse(this);
        }
    }
}
