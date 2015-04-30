package controllers;
import models.*;

import java.security.SecureRandom;
import java.sql.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import views.html.*;
import models.FacebookUser;
import play.data.Form;
import play.db.DB;
import play.libs.Json;
import play.mvc.*;
import play.db.*;
import play.libs.Json;

public class TournamentDatabase extends Controller {

	
		public static JsonNode getTournament(Integer id) {
		
	    Connection conn = null;
		PreparedStatement preparedStatement = null;
		
	    Tournament t = new Tournament();
	    
    	try {
		    
		    conn = DB.getConnection();
			String insertIntoDatabase = "SELECT * FROM ETournament WHERE tournamentID=?;";
			preparedStatement = conn.prepareStatement(insertIntoDatabase);
			preparedStatement.setInt(1, id);
			
			
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.isBeforeFirst()) {
				rs.next();
				t.tournamentname = rs.getString("tournamentName");
				t.participant_count = rs.getInt("teamAmount");
				t.tournamentcreator = rs.getString("admin");
				t.tournamentdata = rs.getString("tournamentData");
				
			    
		    }
		    
			if (t.tournamentdata == null){
			    return null;
			}
			JsonNode tournamentJson = Json.toJson(t);
			return tournamentJson;
			
		} catch (SQLException se) {
			return null;
		} catch (Exception e) {
			// Handle errors for Class.forName
			return null;
		} finally {
			// finally block used to close resources
			try {
				if (preparedStatement != null)
					conn.close();
			} catch (SQLException se) {
			}// do nothing
		}

	}

	public static Result getTournaments() {
		String currentUser = session("connected");
		if (currentUser == null) {
			return unauthorized(LoginUserPage
					.render("You have to login to access this page!"));
		} else {
			ObjectNode result = Json.newObject();
			Connection conn = null;
			PreparedStatement preparedStatement = null;

			try {

				conn = DB.getConnection();

				String insertIntoDatabase = "SELECT tournamentName, teamAmount FROM ETournament et WHERE admin=?";
				preparedStatement = conn.prepareStatement(insertIntoDatabase);
				preparedStatement.setString(1, currentUser);

				ResultSet rs = preparedStatement.executeQuery();

				while (rs.next()) {
					String tName = rs.getString("tournamentName");
					int tAmount = rs.getInt("teamAmount");
					
					result.put(tName, tAmount);

				}
				rs.close();
				String test = result.toString();
				
				return ok(MainTournamentPage.render(test));
			} catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException ice) {
				return badRequest(ice.toString());
			} catch (NumberFormatException nfe) {
				return badRequest(nfe.toString());
			} catch (SQLException se) {
				// Handle sql errors
				return internalServerError(se.toString());
			} catch (Exception e) {
				// Handle errors for Class.forName
				return internalServerError(e.toString());
			} finally {
				// finally block used to close resources
				// try {
				// if (preparedStatement != null)
				// conn.close();
				// } catch (SQLException se) {
				// } //do nothing
				try {
					if (conn != null)
						conn.close();
				} catch (SQLException se) {
					return internalServerError(se.toString());
				} // end finally try
			} // end try
		}
	}

	public static Result addTournament() {

		Connection conn = null;
		PreparedStatement preparedStatement = null;
		JsonNode json = request().body().asJson();

		JsonNode jsonparent = json.findParent("tournamentInfo");
		String tournamentData = jsonparent.toString();
		String dataOnly = tournamentData.substring(tournamentData
				.lastIndexOf(":"));
		String tournamentName = json.findPath("name").textValue();
		String tournamentAmount = json.findPath("amount").textValue();
		String currentUser = session("connected");
		try {

			conn = DB.getConnection();

			int teamAmount = Integer.parseInt(tournamentAmount);
			String insertIntoDatabase = "INSERT INTO ETournament (admin, tournamentData, tournamentName, teamAmount) VALUES(?,?,?,?)";
			preparedStatement = conn.prepareStatement(insertIntoDatabase);

			preparedStatement.setString(1, currentUser);
			preparedStatement.setString(2, tournamentData);
			preparedStatement.setString(3, tournamentName);
			preparedStatement.setInt(4, teamAmount);

			preparedStatement.executeUpdate();
			return redirect(routes.Application.mainMethod());
		} catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException ice) {
			return badRequest(ice.toString());
		} catch (NumberFormatException nfe) {
			return badRequest(nfe.toString());
		} catch (SQLException se) {
			// Handle sql errors
			return internalServerError(se.toString());
		} catch (Exception e) {
			// Handle errors for Class.forName
			return internalServerError(e.toString());
		} finally {
			// finally block used to close resources
			// try {
			// if (preparedStatement != null)
			// conn.close();
			// } catch (SQLException se) {
			// } //do nothing
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				return internalServerError(se.toString());
			} // end finally try
		} // end try
	}

}
