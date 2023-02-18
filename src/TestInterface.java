import java.io.FileReader;
import java.util.SortedSet;
import java.util.TreeSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class TestInterface {
    public static void LoadJSON(String path) {
        Catalog catalog = Catalog.getInstance();
        UserFactory userFactory = UserFactory.getFactory();
        ScoreVisitor scoreVisitor = ScoreVisitor.getScoreVisitorInstance();
        int index = 0;

        try {
            //parse the JSON file
            //Object obj = new JSONParser().parse(new FileReader("src/catalog.json"));
            Object obj = new JSONParser().parse(new FileReader(path));

            JSONObject catalogObject = (JSONObject) obj;
            JSONArray catalogCourses = (JSONArray) catalogObject.get("courses");
            for(Object curs : catalogCourses) {
                JSONObject jsonObject = (JSONObject)curs;

                // get the values of the fields of the main object
                String type = (String) jsonObject.get("type");
                String strategy = (String) jsonObject.get("strategy");
                String name = (String) jsonObject.get("name");

                // get the values of the fields of the teacher object
                JSONObject teacher = (JSONObject) jsonObject.get("teacher");
                String firstName = (String) teacher.get("firstName");
                String lastName = (String) teacher.get("lastName");

                //Create the course
                if(type.equals("FullCourse")) {
                    catalog.addCourse((FullCourse) new FullCourse.FullCourseBuilder()
                            .setName(name)
                            .setCredit(5)
                            .setTeacher(firstName, lastName)
                            .setCourseStrategy(strategy)
                            .buildCourse());
                } else if (type.equals("PartialCourse")) {
                    catalog.addCourse((PartialCourse) new PartialCourse.PartialCourseBuilder()
                            .setName(name)
                            .setCredit(2)
                            .setTeacher(firstName, lastName)
                            .setCourseStrategy(strategy)
                            .buildCourse());
                }

                // get the values of the fields of the assistants array
                SortedSet<Assistant> assistans = new TreeSet<Assistant>();
                JSONArray assistantsJSON = (JSONArray) jsonObject.get("assistants");
                for (Object assistant : assistantsJSON) {
                    JSONObject assistantJSON = (JSONObject) assistant;
                    firstName = (String) assistantJSON.get("firstName");
                    lastName = (String) assistantJSON.get("lastName");

                    assistans.add((Assistant)userFactory
                            .getUser(UserFactory.UserType.Assistant, firstName, lastName));
                }
                catalog.getCourses().get(index).setAssistants(assistans);

                // get the values of the fields of the groups array
                JSONArray groupsJSON = (JSONArray) jsonObject.get("groups");
                for (Object group : groupsJSON) {
                    JSONObject groupJSON = (JSONObject) group;
                    String ID = (String) groupJSON.get("ID");

                    JSONObject assistantJSON = (JSONObject) groupJSON.get("assistant");
                    firstName = (String) assistantJSON.get("firstName");
                    lastName = (String) assistantJSON.get("lastName");
                    String assistantFullName = firstName + " " + lastName;
                    Assistant assistant = null;

                    for(Assistant aux : catalog.getCourses().get(index).getAssistants()) {
                        if(assistantFullName.compareTo(aux.toString()) == 0) {
                            assistant = aux;
                            break;
                        }
                    }

                    catalog.getCourses().get(index).addGroup(ID
                            ,assistant
                            ,new Student.SortbyFullName());

                    JSONArray studentsJSON = (JSONArray) groupJSON.get("students");
                    for (Object student : studentsJSON) {
                        JSONObject studentJSON = (JSONObject) student;
                        firstName = (String) studentJSON.get("firstName");
                        lastName = (String) studentJSON.get("lastName");
                        Student stud = (Student) userFactory.getUser(UserFactory.UserType.Student, firstName, lastName);

                        boolean ok = false;
                        if(firstName.equals("Camelia") && lastName.equals("Tunaru")){
                            ok = true;
                        }

                        JSONObject motherJSON = (JSONObject) studentJSON.get("mother");
                        if(motherJSON != null){
                            firstName = (String) motherJSON.get("firstName");
                            lastName = (String) motherJSON.get("lastName");
                            Parent parent = (Parent) userFactory.getUser(UserFactory.UserType.Parent, firstName, lastName);
                            stud.setMother(parent);

//                            if(ok) {
//                                catalog.addObserver(parent);
//                                catalog.removeObserver(parent);
//                            }
                        }

                        JSONObject fatherJSON = (JSONObject) studentJSON.get("father");
                        if(fatherJSON != null){
                            firstName = (String) fatherJSON.get("firstName");
                            lastName = (String) fatherJSON.get("lastName");
                            Parent parent = (Parent) userFactory.getUser(UserFactory.UserType.Parent, firstName, lastName);
                            stud.setFather(parent);

//                            if(ok) {
//                                catalog.addObserver(parent);
//                            }
                        }
                        catalog.getCourses().get(index).addStudent(ID, stud);
                    }
                }
                index++;
            }

            JSONArray examScoresJSON = (JSONArray) catalogObject.get("examScores");
            for (Object examJSON : examScoresJSON) {
                JSONObject exam = (JSONObject) examJSON;

                JSONObject teacher = (JSONObject) exam.get("teacher");
                String firstNameTeacher = (String) teacher.get("firstName");
                String lastNameTeacher = (String) teacher.get("lastName");

                JSONObject student = (JSONObject) exam.get("student");
                String firstNameStudent = (String) student.get("firstName");
                String lastNameStudent = (String) student.get("lastName");

                String course = (String) exam.get("course");

                double grade = Double.parseDouble(exam.get("grade").toString());


                Teacher teach = null;
                Student stud = null;

                for(Course courses : catalog.getCourses()) {
                    String fullNameStudent = firstNameStudent + " " + lastNameStudent;
                    String fullNameTeacher = firstNameTeacher + " " + lastNameTeacher;

                    for(Student aux : courses.getAllStudents()) {
                        if(fullNameStudent.compareTo(aux.toString()) == 0) {
                            stud = aux;
                            break;
                        }
                    }

                    if(fullNameTeacher.compareTo(courses.getTeacher().toString()) == 0) {
                        teach = courses.getTeacher();
                    }
                }
                scoreVisitor.addGrade(teach, stud, course, grade);
            }


            JSONArray partialScoresJSON = (JSONArray) catalogObject.get("partialScores");
            for (Object partialJSON : partialScoresJSON) {
                JSONObject partial = (JSONObject) partialJSON;

                JSONObject assistant = (JSONObject) partial.get("assistant");
                String firstNameAssistant = (String) assistant.get("firstName");
                String lastNameAssistant = (String) assistant.get("lastName");

                JSONObject student = (JSONObject) partial.get("student");
                String firstNameStudent = (String) student.get("firstName");
                String lastNameStudent = (String) student.get("lastName");

                String course = (String) partial.get("course");

                double grade = Double.parseDouble(partial.get("grade").toString());

                Assistant assist = null;
                Student stud = null;

                for(Course courses : catalog.getCourses()) {
                    String fullNameStudent = firstNameStudent + " " + lastNameStudent;
                    String fullnameAssistant = firstNameAssistant + " " + lastNameAssistant;

                    for(Student aux : courses.getAllStudents()) {
                        if(fullNameStudent.compareTo(aux.toString()) == 0) {
                            stud = aux;
                            break;
                        }
                    }

                    for(Assistant aux : courses.getAssistants()) {
                        if(fullnameAssistant.compareTo(aux.toString()) == 0) {
                            assist = aux;
                            break;
                        }
                    }
                }
                scoreVisitor.addGrade(assist, stud, course, grade);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
