package elixer.com.bloodbank.models;

public class User {

    private String name;
    private String phoneNumber;
    private String bloodGroup;
    private String city;
    private int level;
    private int age;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public User(String phoneNumber, String bloodGroup, String name, String city, int level, int age) {
        this.name=name;
        this.phoneNumber=phoneNumber;
        this.bloodGroup=bloodGroup;
        this.city=city;
        this.level=level;
        this.age = age;
    }

    public String getName() {
        return name;
    }
    public void setLevel(int level) {
        this.level = level;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", bloodGroup='" + bloodGroup + '\'' +
                ", city='" + city + '\'' +
                ", level=" + level +
                '}';
    }
}
