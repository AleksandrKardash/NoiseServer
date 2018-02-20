package models.UserBuilder;

import java.io.Serializable;

public class User  implements Serializable {

    private String name;
    private String city;
    private String adress;
    private String mail;
    private String phone;
    private String login;
    private String password;

    public static boolean reg=false;

    private User(){};

    @Override
    public String toString()
    {
        return "Name: " + this.name + " " + "City: " +this.city + " " +  "Adress: " + this.adress + " " +  "Mail: " +this.mail + " " +  "Phone: " +this.phone;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getAdress() {
        return adress;
    }

    public String getMail() {
        return mail;
    }

    public String getPhone() {
        return phone;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public static class Builder implements UserBuilder{


    private static User user = new User();

    @Override
    public Builder setName(String n) {
        user.name = n;
        return this;
    }

    @Override
    public Builder setCity(String c) {
        user.city = c;
        return this;
    }

    @Override
    public Builder setAdress(String a) {
        user.adress = a;
        return this;
    }

    @Override
    public Builder setMail(String m) {
        user.mail = m;
        return this;
    }

    @Override
    public Builder setPhone(String p) {
        user.phone = p;
        return this;
    }

    @Override
    public Builder setLogin(String l) {
        user.login = l;
        return this;
    }

    @Override
    public Builder setPassword(String p) {
        user.password = p;
        return this;
    }


    @Override
    public User build() {
        if (user.name.length()>=1&& user.city.length()>=2&&user.adress.length()>=5 &&user.mail.length()>=5 &&user.phone.length()>=6&& user.login.length()>=1&&user.password.length()>=1){
            reg=true;
            return user;
        } else{
            return null;
        }
    }
  }
}