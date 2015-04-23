package controllers;

import models.*;
import play.*;
import play.mvc.*;

import java.sql.*;

import views.html.*;
import play.data.Form;
import play.db.*;
import views.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;

public class Application extends Controller {

	public static Result mainMethod() {
		  String user = session("connected");
        if(user != null) {
             return ok(main.render("You are logged in as " + user));
        } else {
        return unauthorized(LoginUserPage.render("Welcome, login to explore the website"));
        }
	}

	public static Result helloWeb() {

		ObjectNode result = Json.newObject();

		result.put("content", "Hello Web");

		return ok(result);
	}

	public static Result newUserPage() {
	    String currentUser = session("connected");
        if(currentUser != null) {
             return ok(main.render("You are already logged in as " + currentUser + " Please log out if you wish to create another account"));
        } 
	    
		return ok(NewUserPage.render(""));
	}
	
	public static Result loginUserPage() {
	    
	    String currentUser = session("connected");
        if(currentUser != null) {
             return ok(main.render("You are already logged in as " + currentUser));
        } 
	    
		return ok(LoginUserPage.render(""));
	}
	

	public static Result logout() {
	    session().clear();
		return redirect(routes.Application.loginUserPage());
	}
	
	
	


// Create user and send to database
	public static Result addUser() {
	    
	    if (Form.form(User.class).bindFromRequest().hasErrors()){
		    return badRequest(NewUserPage.render("Leave no form empty"));
		}
	    
		User user = Form.form(User.class).bindFromRequest().get();
		ObjectNode result = Json.newObject();
		Connection conn = null;
		Statement stmt = null;
		String userUsername = user.username;
		String userPassword = user.password;
		
		if (userUsername.matches("^.*[^a-zA-Z0-9].*$")){
		    return badRequest(NewUserPage.render("Please only use letters and numbers for the username"));
		}

		try {
			conn = DB.getConnection();
			stmt = conn.createStatement();

			String insertIntoDatabase = "INSERT INTO user"
					+ "(USERNAME, PASSWORD) " + "VALUES" + "(" + "'" +userUsername + "'" + "," + "'" + userPassword + "'" +")";
			
            
			// execute insert SQL stetement
			stmt.executeUpdate(insertIntoDatabase);

			// user.save();
			session("connected", userUsername);
			return redirect(routes.Application.mainMethod());
		} catch (SQLException se) {
			// Handle errors for JDBC
			return internalServerError(se.toString());
		} catch (Exception e) {
			// Handle errors for Class.forName
			return internalServerError(e.toString());
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}// do nothing
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				return internalServerError(se.toString());
			}// end finally try
		}// end try

	}

	
	// Method for user login. Requests database reply for entered username, reacts accordingly.
	public static Result getUser() {
        
       
		
		Connection conn = null;
		Statement stmt = null;
		
		if (Form.form(User.class).bindFromRequest().hasErrors()){
		    return badRequest(LoginUserPage.render("Leave no form empty"));
		}
		
	    User user = Form.form(User.class).bindFromRequest().get();
        
        
        
        String userUsername = user.username;
		String userPassword = user.password;
        
		try {

			conn = DB.getConnection();
			stmt = conn.createStatement();

			String sql = "SELECT * FROM `user` WHERE `username` = " + "'" + userUsername + "'";

			ResultSet rs = stmt.executeQuery(sql);
			if(rs.isBeforeFirst()){
			rs.next();
			
			String username = rs.getString("username");
			String password = rs.getString("password");
				
				if (userUsername.equals(username) && userPassword.equals(password)){
				    rs.close();
				    session("connected", userUsername);
				    return redirect(routes.Application.mainMethod());
				}
			} 
			
			rs.close();
			return ok(LoginUserPage.render("Wrong user/pass"));
			
		} catch (SQLException se) {
			// Handle errors for JDBC
			return internalServerError(se.toString());
		} catch (Exception e) {
			// Handle errors for Class.forName
			return internalServerError(e.toString());
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}// do nothing
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				return internalServerError(se.toString());
			}// end finally try
		}// end try

	}
}
