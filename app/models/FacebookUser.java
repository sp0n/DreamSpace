package models;

import java.util.*;

import javax.persistence.*;

import play.db.ebean.*;
import play.data.validation.*;
import play.db.ebean.Model;

@Entity
public class FacebookUser extends Model{

@Id @Constraints.Required
public long id;
	
@Constraints.Required
public String name;

@Constraints.Required 
public String email;




//public static Model.Finder<String, User> find = new Model.Finder<String, User>(String.class, User.class);
}
