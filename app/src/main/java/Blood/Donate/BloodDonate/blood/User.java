package Blood.Donate.BloodDonate.blood;

public class User {

    public String BloodGrp;
    public String City;
    public String Gender;
    public String Mobile;
    public String Name;
    public String Password;



    public User(String bloodGrp, String city, String gender, String mobile, String name, String password) {
        BloodGrp = bloodGrp;
        City = city;
        Gender = gender;
        Mobile = mobile;
        Name = name;
        Password = password;
    }
    public User(){

    }
}
