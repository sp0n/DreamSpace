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


public class TournamentDatabase extends Controller {
	
//	public static Result addSpike(){
//		JsonNode json = request().body().asJson();
//		JsonNode jsonparent = json.findParent("teams");
////		JsonNode jsonparent2 = jsonparent.findParent("0");
//		JsonNode nameDirty = jsonparent.get(0);
//		
////		jsonparent2.get("0");
////		if(jsonparent2.isContainerNode()){
////		return ok("true");
////		}
////		else
////////		String tournamentData = json.findPath("teams").textValue();
////		
//		
//		return ok(json.toString()  + "");
//		
//	}
	
	
	public static Result addSpike(){
		
		Connection conn = null;
        PreparedStatement preparedStatement = null;
		JsonNode json = request().body().asJson();
		String jsonString = json.toString();
		String currentUser = session("connected");
		 try {
		
			 conn = DB.getConnection();
			   String insertIntoDatabase = "INSERT INTO TournamentTest (admin, tournamentData, teamAmount) VALUES(?,?,?)";
			   preparedStatement = conn.prepareStatement(insertIntoDatabase);
		
				preparedStatement.setString(1,currentUser);
				preparedStatement.setString(2,jsonString);
				preparedStatement.setInt(3, 5);
				
					preparedStatement.executeUpdate();
					return redirect(routes.Application.mainMethod());
		 } 	catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException ice){
			    return badRequest(ice.toString()); 
			} catch (SQLException se) {
	            // Handle sql errors
				return internalServerError(se.toString());
			} catch (Exception e) {
				// Handle errors for Class.forName
				return internalServerError(e.toString() );
			} finally {
				 //finally block used to close resources
//				try {
//					if (preparedStatement != null)
//						conn.close();
//				} catch (SQLException se) {
//				} //do nothing
				try {
					if (conn != null)
						conn.close();
				} catch (SQLException se) {
					return internalServerError(se.toString());
				} //end finally try
			} //end try
	}
	
    public static Result addTournament() {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
            
 

  try {
   
   conn = DB.getConnection();
   String insertIntoDatabase = "INSERT INTO ETournament (tournamentname, participant_count, tournamentgameID, tournamentcreator) VALUES(?,?,?,?)";
   preparedStatement = conn.prepareStatement(insertIntoDatabase);

   
   preparedStatement.executeUpdate();
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
	
	

