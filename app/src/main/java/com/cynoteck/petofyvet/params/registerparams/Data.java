package com.cynoteck.petofyvet.params.registerparams;

public class Data
{
    private String Email;

    private String FirstName;

    private String PhoneNumber;

    private String LastName;

    private String Password;

    private String ConfirmPassword;

    public String getEmail ()
    {
        return Email;
    }

    public void setEmail (String Email)
    {
        this.Email = Email;
    }

    public String getFirstName ()
    {
        return FirstName;
    }

    public void setFirstName (String FirstName)
    {
        this.FirstName = FirstName;
    }

    public String getPhoneNumber ()
    {
        return PhoneNumber;
    }

    public void setPhoneNumber (String PhoneNumber)
    {
        this.PhoneNumber = PhoneNumber;
    }

    public String getLastName ()
    {
        return LastName;
    }

    public void setLastName (String LastName)
    {
        this.LastName = LastName;
    }

    public String getPassword ()
    {
        return Password;
    }

    public void setPassword (String Password)
    {
        this.Password = Password;
    }

    public String getConfirmPassword ()
    {
        return ConfirmPassword;
    }

    public void setConfirmPassword (String ConfirmPassword)
    {
        this.ConfirmPassword = ConfirmPassword;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Email = "+Email+", FirstName = "+FirstName+", PhoneNumber = "+PhoneNumber+", LastName = "+LastName+", Password = "+Password+", ConfirmPassword = "+ConfirmPassword+"]";
    }
}