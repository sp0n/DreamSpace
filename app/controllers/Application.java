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

	public static Result index() {
		return ok(index.render("Website"));
	}

	public static Result helloWeb() {

		ObjectNode result = Json.newObject();

		result.put("content", "Hello Web");

		return ok(result);
	}

	public static Result newUserPage() {
		return ok(NewUserPage.render());
	}
	
		public static Result loginUserPage() {
		return ok(LoginUserPage.render(""));
	}


// Create user and send to database
	public static Result addUser() {
	    
	    if (Form.form(User.class).bindFromRequest().hasErrors()){
		    return ok("Leave no form empty");
		}
	    
		User user = Form.form(User.class).bindFromRequest().get();
		ObjectNode result = Json.newObject();
		Connection conn = null;
		Statement stmt = null;
		String userUsername = user.username;
		String userPassword = user.password;

		try {
			conn = DB.getConnection();
			stmt = conn.createStatement();

			String insertIntoDatabase = "INSERT INTO user"
					+ "(USERNAME, PASSWORD) " + "VALUES" + "(" + "'" +userUsername + "'" + "," + "'" + userPassword + "'" +")";
			

			// execute insert SQL stetement
			stmt.executeUpdate(insertIntoDatabase);

			// user.save();
			return redirect(routes.Application.newUserPage());
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

	public static Result getUser() {

		
		Connection conn = null;
		Statement stmt = null;
		
		if (Form.form(User.class).bindFromRequest().hasErrors()){
		    return ok(LoginUserPage.render("Leave no form empty"));
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
				    return ok("You are logged in as " + userUsername);
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
