//package controllers;
//
//import java.security.SecureRandom;
//import java.sql.*;
//
//import com.fasterxml.jackson.databind.JsonNode;
//
//import views.html.*;
//import models.FacebookUser;
//import play.data.Form;
//import play.db.DB;
//import play.mvc.*;
//import play.db.*;
//
//
//public class TournamentDatabase {
//
//	public static Result addFacebookUser() {
//		Connection conn = null;
//		PreparedStatement preparedStatement;
//		
//		JsonNode json = request().body().asJson();
//		
//		String username = json.findPath("username").textValue();
//		
//		String id = json.findPath("id").textValue();
//		long idLong = Long.parseLong(id);
//		
//		String name = json.findPath("name").textValue();
//
//		String email = json.findPath("email").textValue();
//	
//		try {
//			
//			conn = DB.getConnection();
//			String insertIntoDatabase = "INSERT INTO FacebookUser (USERNAME, USERID, NAME, EMAIL) VALUES(?,?,?,?)";
//			preparedStatement = conn.prepareStatement(insertIntoDatabase);
//			
//			preparedStatement.setString(1, username);
//			preparedStatement.setLong(2, idLong);
//			preparedStatement.setString(3, name);
//			preparedStatement.setString(4, email);
//			preparedStatement.executeUpdate();
//			
//			session("connected", username);
//			return redirect(routes.Application.mainMethod());
//			
//		} 	catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException ice){
//		    return badRequest(LoginUserPage
//					.render("User with that name already exists")); 
//		} catch (SQLException se) {
//            // Handle sql errors
//			return internalServerError(se.toString());
//		} catch (Exception e) {
//			// Handle errors for Class.forName
//			return internalServerError(e.toString());
//		} finally {
//			 //finally block used to close resources
////			try {
////				if (preparedStatement != null)
////					conn.close();
////			} catch (SQLException se) {
////			} //do nothing
//			try {
//				if (conn != null)
//					conn.close();
//			} catch (SQLException se) {
//				return internalServerError(se.toString());
//			} //end finally try
//		} //end try
//
//	}
//	
//}
//	
//	
//}
