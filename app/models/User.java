package models;

import java.util.*;

import javax.persistence.*;

import play.db.ebean.*;
import play.data.validation.*;
import play.db.ebean.Model;

@Entity
public class User extends Model{

@Id @Constraints.Required
public String username;

@Constraints.Required 
public String password;

@Constraints.Required 
public String email;

@Constraints.Required 
public String confirmPass;

//public static Model.Finder<String, User> find = new Model.Finder<String, User>(String.class, User.class);
}
