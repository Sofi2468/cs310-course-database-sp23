package edu.jsu.mcis.cs310.coursedb.dao;
import com.github.cliftonlabs.json_simple.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class SectionDAO {
    
    // INSERT YOUR CODE HERE
    // SQL query to retrieve sections from the database
    String query = "SELECT * FROM section WHERE termid = ? AND subjectid = ? AND num = ? ORDER BY crn";
    private final DAOFactory daoFactory;
    
    SectionDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    public String find(int termid, String subjectid, String num){
        
        JsonArray result = new JsonArray();
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        
        try {
            
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0)) {
                
                // INSERT YOUR CODE HERE
                // Prepare the SQL statement and set parameters
                ps = conn.prepareStatement(query);
                ps.setInt(1, termid);
                ps.setString(2, subjectid);
                ps.setString(3, num);
                
                boolean hasresults = ps.execute();
                
                if (hasresults) {

                    rs = ps.getResultSet();

                    // Loop through the result set and add each row as a JSON object to the sections array
                    while (rs.next()) {
                        
                        
                        JsonObject section = new JsonObject();
                        rsmd= rs.getMetaData();
                        for (int i=1; i<rsmd.getColumnCount()+1; i++)
                        {
                            String col_label= rsmd.getColumnLabel(i);
                            String col_value= rs.getString(col_label);
                            
                            section.put(col_label, col_value);
                            
                       }
                       
                       result.add(section);
                    }

                    
                }
                
            }
      
        }
        
        catch (Exception e) { e.printStackTrace(); }
        
        finally {
            
            if (rs != null) { try { rs.close(); } catch (Exception e) { e.printStackTrace(); } }
            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        return Jsoner.serialize(result);
        
    }
    
}
