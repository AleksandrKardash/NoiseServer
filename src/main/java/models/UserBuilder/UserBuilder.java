package models.UserBuilder;


interface UserBuilder{
    public User.Builder setName(String n);
    public User.Builder setCity(String c);
    public User.Builder setAdress(String a);
    public User.Builder setMail(String m);
    public User.Builder setPhone(String p);
    public User.Builder setLogin(String l);
    public User.Builder setPassword(String p);
    public User.Builder setSeller(String s);

    public User build();
}

