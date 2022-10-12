import java.sql.*;

public class Database {

    /**
     * Connects to the todo_list.db SQLite database
     *
     * @return the Connection object
     */
    public Connection connect() {
        // SQLite connection URL
        String url = "jdbc:sqlite:C:/code/training/intro-to-java/Training-todo/src/main/sqlite/db/todo.db";
        Connection conn = null;
        // Try obtaining a connection unless an exception is encountered
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * Deletes an entry from the database.
     * @param id - Item ID of the entry to delete.
     * @return errorFlag - raised if ID is invalid.
     */
    public Boolean delete(int id) {

        Boolean errorFlag = false;
        String testExists = "SELECT * FROM todo_list WHERE item_id = ?";
        try (Connection connTest = this.connect();
             PreparedStatement pstmtTest = connTest.prepareStatement(testExists)){

            pstmtTest.setInt(1, id);
            ResultSet rs = pstmtTest.executeQuery();

            if(!rs.isBeforeFirst()){
                System.out.println("Item ID not found. Please try again with a valid Item ID."); //data does not exist
                errorFlag = true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        String sql = "DELETE FROM todo_list WHERE item_id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setInt(1, id);
            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return errorFlag;
    }

    public void selectAll(){
        String sql = "SELECT item_id, content, priority, category, created_date FROM todo_list";
        String columnPrintFormat = TerminalScanner.formatStringGenerator(5);

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
             System.out.printf(columnPrintFormat, "Item ID", "To-Do Content", "Priority",
                     "Category", "Date Created");
            // loop through the result set
            while (rs.next()) {
                System.out.printf(columnPrintFormat, rs.getInt("item_id"),
                        rs.getString("content"),
                        rs.getString("priority"),
                        rs.getString("category"),
                        rs.getString("created_date"));
//                System.out.println(rs.getInt("item_id") +  "\t" +
//                        rs.getString("content") + "\t" +
//                        rs.getString("created_date"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Integer findMaxIndex(){
        String sql = "SELECT max(item_id) FROM todo_list";
        Integer maxIndex = null;
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            if (rs.next()) {
                maxIndex = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return maxIndex;
    }

    public void clear() {
//        for (int i = 1; i <= this.findMaxIndex(); i++) {
//        String sql = "DELETE FROM todo_list WHERE 1=1";
        String sql = "DELETE FROM todo_list";
            try (Connection conn = this.connect();
                 Statement stmt  = conn.createStatement()){
                stmt.executeUpdate(sql);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

}
