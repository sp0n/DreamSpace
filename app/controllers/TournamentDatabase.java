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
//public class TournamentDatabase extends Controller {
//    
//    public static Result addTournament() {
//        Connection conn = null;
//        PreparedStatement preparedStatement = null;
//        
// 
//  Tournament tour = Form.form(Tournament.class).bindFromRequest().get();
//  String tourTournamentname = tour.tournamentname;
//  int tourParticipants = tour.participant_count;
//  int tourTournamentgameID = tour.tournamentgameID;
//  int tourTournamentcreator = tour.tournamentcreator;
//      
// 
//
//  try {
//   
//   conn = DB.getConnection();
//   String insertIntoDatabase = "INSERT INTO ETournament (tournamentname, participant_count, tournamentgameID, tournamentcreator) VALUES(?,?,?,?)";
//   preparedStatement = conn.prepareStatement(insertIntoDatabase);
//   preparedStatement.setString(1, tournamentname);
//   preparedStatement.setint(2, participant_count);
//   preparedStatement.setint(3, tournamentgameID);
//   preparedStatement.setint(4, tournamentcreator);
//   preparedStatement.executeUpdate();
//   session("connected", tourTournamentname);
//   return redirect(routes.Application.mainMethod());
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
