public class Parent extends User implements Observer {
    public Parent(String firstName, String lastName) {
        super(firstName, lastName);
    }

    @Override
    public void Update(Notification notification) {
        System.out.println(super.toString() + " received notification: " + notification.toString());
    }
}