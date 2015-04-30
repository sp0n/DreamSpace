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
	
	
	
	public static Result addTournament(){
		
		Connection conn = null;
        PreparedStatement preparedStatement = null;
		JsonNode json = request().body().asJson();
		
		JsonNode jsonparent = json.findParent("tournamentInfo");
		String tournamentData = jsonparent.toString();
		String dataOnly = tournamentData.substring(tournamentData.lastIndexOf(":"));
        String tournamentName = json.findPath("name").textValue();
        String tournamentAmount= json.findPath("amount").textValue();
        String currentUser = session("connected");
		 try {
		
			conn = DB.getConnection();
    
             int teamAmount = Integer.parseInt(tournamentAmount);
            String insertIntoDatabase = "INSERT INTO ETournament (admin, tournamentData, tournamentName, teamAmount) VALUES(?,?,?,?)";
            preparedStatement = conn.prepareStatement(insertIntoDatabase);
  
            preparedStatement.setString(1,currentUser);
            preparedStatement.setString(2,tournamentData);
            preparedStatement.setString(3,tournamentName);
            preparedStatement.setInt(4, teamAmount);
    
                preparedStatement.executeUpdate();
                return redirect(routes.Application.mainMethod());
		    } 	catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException ice){
			    return badRequest(ice.toString()); 
		    } catch (NumberFormatException nfe){
                return 	badRequest(nfe.toString()); 	    
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
	
	
}
	
	

