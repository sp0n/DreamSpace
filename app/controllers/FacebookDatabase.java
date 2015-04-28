package controllers;

import java.security.SecureRandom;
import java.sql.*;

import com.fasterxml.jackson.databind.JsonNode;

import views.html.*;
import models.FacebookUser;
import play.data.Form;
import play.db.DB;
import play.mvc.*;
import play.db.*;

public class FacebookDatabase extends Controller {
	
	
	
	public static Result addFacebookUserSpike(){
		JsonNode json = request().body().asJson();
		try{
	String idDirty = json.findPath("id").textValue();
		long idLong = Long.parseLong(idDirty);
		
		String nameDirty = json.findPath("name").textValue();
		
		String emailDirty = json.findPath("email").textValue();
		
	return ok("" + idDirty + " " + nameDirty + " " + emailDirty +"");
	}
	catch(NumberFormatException nfe){
		return ok(nfe.toString());
	}
	}
	
	 
	public static Result checkIfExistsFacebook() {
		
	    Connection conn = null;
		PreparedStatement preparedStatement = null;
		
		JsonNode json = request().body().asJson();
		
		String id = json.findPath("id").textValue();
		long idLong = Long.parseLong(id);
		String fbUser = null;
	
	
	
		try {
		    
		    conn = DB.getConnection();
			String insertIntoDatabase = "SELECT * FROM FacebookUser WHERE USERID=?";
			preparedStatement = conn.prepareStatement(insertIntoDatabase);
			preparedStatement.setLong(1, idLong);
			
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.isBeforeFirst()) {
				rs.next();
				fbUser = rs.getString("username");
		}
			
			session("connected", fbUser);
			return ok("@routes.Application.mainMethod()");
		} catch (SQLException se) {
			return ok("null");
		} catch (Exception e) {
			// Handle errors for Class.forName
			return ok("null");
		} finally {
			// finally block used to close resources
			try {
				if (preparedStatement != null)
					conn.close();
			} catch (SQLException se) {
			}// do nothing
		}

	}
	


	public static Result addFacebookUser() {
		Connection conn = null;
		PreparedStatement preparedStatement;
		
		JsonNode json = request().body().asJson();
		
		String id = json.findPath("id").textValue();
		long idLong = Long.parseLong(id);
		
		String name = json.findPath("name").textValue();

		String email = json.findPath("email").textValue();
	
		try {
			
			conn = DB.getConnection();
			String insertIntoDatabase = "INSERT INTO FacebookUser (USERID, USERNAME, EMAIL) VALUES(?,?,?)";
			preparedStatement = conn.prepareStatement(insertIntoDatabase);
			
			preparedStatement.setLong(1, idLong);
			preparedStatement.setString(2, name);
			preparedStatement.setString(3, email);
			preparedStatement.executeUpdate();
			
			session("connected", name);
			return redirect(routes.Application.mainMethod());
			
		} 	catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException ice){
		    return badRequest(LoginUserPage
					.render("User with that name already exists")); 
		} catch (SQLException se) {
            // Handle sql errors
			return internalServerError(se.toString());
		} catch (Exception e) {
			// Handle errors for Class.forName
			return internalServerError(e.toString());
		} finally {
			 //finally block used to close resources
//			try {
//				if (preparedStatement != null)
//					conn.close();
//			} catch (SQLException se) {
//			} //do nothing
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				return internalServerError(se.toString());
			} //end finally try
		} //end try

	}
	
}
