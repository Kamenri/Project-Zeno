import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;

//import java.sql.DriverManager;
/*
CSCE 315
9-25-2019

java -cp ".;postgresql-42.2.8.jar" jdbcpostgreSQLGUI.java
*/

public class jdbcpostgreSQLGUI implements ActionListener{

  private static JTextField userText;
  private static JTextField passText;
  public static String username;
  public static String password;
  private static JLabel success;
  private static JButton Loginbutton;
  private static JButton TableButton;
  private static JButton logout;
  private static JButton clear;
  private static JButton Submit;
  private static JComboBox tableList;
  private static JComboBox limitnum;
  private static String[] temp;
  private static String msg;
  private static String lim;
  private static JPanel panel2;
  private static JFrame frame2;
  private static JList<String> list;
  private static JScrollPane jsp;
  private static JScrollPane jsp2;
  private static int[] SelectedVals;
  private static ArrayList<String> items;
  public static Connection conn;
  
  private static ArrayList<String> titleList = new ArrayList<String>();;
  private static ArrayList<String> bambienceAL;
  private static ArrayList<String> bcategoriesAL;
  private static ArrayList<String> bgeneralAL = new ArrayList<String>();
  private static ArrayList<String> bhoursAL;
  private static ArrayList<String> bmiscAL;
  private static ArrayList<String> bparkingAL;
  private static ArrayList<String> checkinAL;
  private static ArrayList<String> reivewsAL;
  private static ArrayList<String> tipsAL;
  private static ArrayList<String> usersAL;
  

  private static ArrayList<String> filters = new ArrayList<String>();
  private static JTable dataTable;
  private static DefaultTableModel tblModel;
  private static ArrayList<Object> x;
  private static ArrayList<String> header = new ArrayList<String>();
  private static ArrayList<String> LimName = new ArrayList<String>();

  
/*
  public final class dbSetup  {
    public static final String user = "pokemon4907";
    public static final String pswd = "127001686";
  }*/

  //LOGIN GUI
  public void actionPerformed (ActionEvent evt){
    if (evt.getSource() == Loginbutton){
      String username = userText.getText();
      String password = passText.getText();
      //String password = passText.getText();
      
      if((username.equals("pokemon4907") && password.equals("127001686"))){
        success.setText("Authorization Confirmed. Connection Successful");
        jdbcpostgreSQLGUI.databaseC();
      } else{
      success.setText("Authorization Denied");
      } 
      //jdbcpostgreSQLGUI.databaseC();
      
    }
    //Dropdown Combobox for Table name
    else if(evt.getSource() == tableList){ //combobox
      JComboBox cb = (JComboBox)evt.getSource();
      msg = (String)cb.getSelectedItem();
      if(jsp != null){
        jsp.removeAll();
        panel2.remove(jsp);
      }        
      if(list != null)
        list.removeAll();
      //Select msg FROM 
      JOptionPane.showMessageDialog(null,msg);
      jdbcpostgreSQLGUI.ColumnDisplay(msg);
    }
    //Based on the Table, saves the selected Filters
    else if(evt.getSource() == TableButton){
      titleList.add(msg);
      JOptionPane.showMessageDialog(null,"Filters Applied");
      SelectedVals = list.getSelectedIndices(); //returns the index of selected values
      if(msg == "business_ambience"){
        bambienceAL = new ArrayList<String>();
        for(int i=0;i<SelectedVals.length;i++){
          filters.add(items.get(SelectedVals[i]));
        }
      }
      else if (msg == "business_categories") {
        bcategoriesAL = new ArrayList<String>();
        for(int i=0;i<SelectedVals.length;i++){
          filters.add(items.get(SelectedVals[i]));
        }      
      }
      else if (msg == "business_general") {
        
        for(int i=0;i<SelectedVals.length;i++){
          filters.add(items.get(SelectedVals[i]));
        }      
      }
      else if (msg == "business_hours") {
        bhoursAL = new ArrayList<String>();
        for(int i=0;i<SelectedVals.length;i++){
          filters.add(items.get(SelectedVals[i]));
        }      
      }
      else if (msg == "business_misc") {
        bmiscAL = new ArrayList<String>();
        for(int i=0;i<SelectedVals.length;i++){
          filters.add(items.get(SelectedVals[i]));
        }      
      }
      else if (msg == "business_parking") {
        bparkingAL = new ArrayList<String>();
        for(int i=0;i<SelectedVals.length;i++){
          filters.add(items.get(SelectedVals[i]));
        }      
      }
      else if (msg == "checkins") {
        checkinAL = new ArrayList<String>();
        for(int i=0;i<SelectedVals.length;i++){
          filters.add(items.get(SelectedVals[i]));
        }      
      }
      else if (msg == "reviews") {
        reivewsAL = new ArrayList<String>();
        for(int i=0;i<SelectedVals.length;i++){
          filters.add(items.get(SelectedVals[i]));
        }      
      }
      else if (msg == "tips") {
        tipsAL = new ArrayList<String>();
        for(int i=0;i<SelectedVals.length;i++){
          filters.add(items.get(SelectedVals[i]));
        }      
      }
      else if (msg == "users") {
        usersAL = new ArrayList<String>();
        for(int i=0;i<SelectedVals.length;i++){
          filters.add(items.get(SelectedVals[i]));
        }      
      }
      /*
      String sum = "";
      for(int i=0;i<SelectedVals.length;i++){
        sum = sum + items.get(SelectedVals[i]) + ", ";
      }
      JOptionPane.showMessageDialog(null, sum);*/
    }
    //closing the connection
    else if(evt.getSource() == logout){
      try {
        conn.close();
        JOptionPane.showMessageDialog(null,"Connection Terminated.");
      } catch(Exception e) {
        JOptionPane.showMessageDialog(null,"ERROR: Unable to terminate Connection.");
      }//end try catch
    }
    //clear ArrayList Filters
    else if(evt.getSource() == clear){
      JOptionPane.showMessageDialog(null,"Filters Cleared");
      if(bambienceAL != null)
        bambienceAL.clear();
      if(bcategoriesAL != null)
        bcategoriesAL.clear();
      if(bgeneralAL != null)
        bgeneralAL.clear();
      if(bhoursAL != null)
        bhoursAL.clear();
      if(bmiscAL != null)
        bmiscAL.clear();
      if(bparkingAL != null)
        bparkingAL.clear();
      if(checkinAL != null)
        checkinAL.clear();
      if(reivewsAL != null)
        reivewsAL.clear();
      if(tipsAL != null)
        tipsAL.clear();
      if(usersAL != null)
        usersAL.clear();
      if(titleList != null)
        titleList.clear();
      if(filters != null)
        filters.clear();
      if(x != null)
        x.clear();
      if(header != null)
        header.clear();
      if(LimName != null)
        LimName.clear();
    }
    else if(evt.getSource() == Submit){
      if(tblModel != null){
        tblModel.setRowCount(0);
        panel2.remove(dataTable);
      }        
      if(x != null)
        x.clear();
      
      SubmitT();        
    }
    //Combobox for limit
    else if(evt.getSource() == limitnum){
      JComboBox cb2 = (JComboBox)evt.getSource();
      lim = (String)cb2.getSelectedItem();

      limitDisp(lim);
    }
        
  }
  public static void limitDisp(String Lname){
    LimName.add(Lname);
    //JOptionPane.showMessageDialog(null, Lname);
  }

  //Submit Table
  public static void _SubmitT(){
    try{
      //Vector columnNames = new Vector();
      //Vector data = new Vector();

      //create a statement object
      Statement stmt = conn.createStatement();
      //create an SQL statement
      String sqlStatement = "SELECT name FROM users limit 10";
      //send statement to DBMS
      ResultSet result = stmt.executeQuery(sqlStatement);
      //DefaultTableModel tblModel = (DefaultTableModel)dataTable.getModel();
      //DefaultTableModel model = new DefaultTableModel(new String[]{"Class Name", "Home work", "Due Date"}, 0);
      DefaultTableModel tblModel = new DefaultTableModel(new String[]{"name"},0);

      //OUTPUT
      JOptionPane.showMessageDialog(null,"User names from the Database.");
      //System.out.println("______________________________________");
      while (result.next()) {
        
        String name = result.getString("name");

        //setting array for store data into jtable
        
        //add String arry data into jtable
        tblModel.addRow(new Object[]{name});
      }
      JOptionPane.showMessageDialog(null,"test from the Database.");

      dataTable = new JTable();
      dataTable.setModel(tblModel);
      
      //panel2.add(new JScrollPane(dataTable));

      jsp2 = new JScrollPane();
      jsp2.setBounds(400, 40, 400, 300);
      jsp2.setViewportView(dataTable);    

      panel2.add(jsp2);
      frame2.setVisible(true);

      //if(filters != null)
        filters.clear();
      //if(header != null)
        header.clear();
      
    }catch (Exception e){
      JOptionPane.showMessageDialog(null, "Error");
    }

  }
  
  public static void SubmitT(){
    try{
      //Vector columnNames = new Vector();
      //Vector data = new Vector();
      //ArrayList<String> header = new ArrayList<String>();
      //create a statement object
      Statement stmt = conn.createStatement();
      //create an SQL statement
      //build the SQL statement
      String sqlStatement = "SELECT ";
      for(int i = 0; i<filters.size(); i++ )
      {
        sqlStatement = sqlStatement + filters.get(i);
        if(i != (filters.size() - 1))
        {
          sqlStatement = sqlStatement + ", ";
        }else{
          sqlStatement = sqlStatement + " ";
        }
        header.add(filters.get(i));
      }
      String lim10 = "limit ";
      String fromtxt = "FROM ";
      for(int i = 0; i<titleList.size(); i++ )
      {
        fromtxt = fromtxt + titleList.get(i);
        if(i != (titleList.size() - 1))
        {
          fromtxt = fromtxt + ", ";
        }else{
          fromtxt = fromtxt + " ";
        }
      }
      for(int i = 0; i < 1; i++){
        lim10 = lim10 + LimName.get(i);
      }
      sqlStatement = sqlStatement + fromtxt + lim10;

      JOptionPane.showMessageDialog(null,sqlStatement);
      //send statement to DBMS
      ResultSet result = stmt.executeQuery(sqlStatement);
      
      //DefaultTableModel tblModel = (DefaultTableModel)dataTable.getModel();
      //DefaultTableModel model = new DefaultTableModel(new String[]{"Class Name", "Home work", "Due Date"}, 0);
      //DefaultTableModel tblModel = new DefaultTableModel(new String[]{"1","2","3"},0);
      tblModel = new DefaultTableModel(header.toArray(),0);
      //OUTPUT
      //JOptionPane.showMessageDialog(null,"User names from the Database.");
      //System.out.println("______________________________________");
      while (result.next()) {
        x = new ArrayList<Object>();
        for(int i = 0; i< filters.size(); i++)
        {
          x.add(result.getString(i+1));
        }
        //String name = result.getString(1);

        //setting array for store data into jtable
        
        //add String arry data into jtable
        //tblModel.addRow(new Object[]{name});
        tblModel.addRow(x.toArray());
      }
      //JOptionPane.showMessageDialog(null,"test from the Database.");

      dataTable = new JTable();
      dataTable.setModel(tblModel);
      //dataTable.setEnabled(false);
      //panel2.add(new JScrollPane(dataTable));

      jsp2 = new JScrollPane();
      jsp2.setBounds(400, 40, 570, 500);
      jsp2.setViewportView(dataTable);    

      panel2.add(jsp2);
      frame2.setVisible(true);
      
      //if(filters != null)
      filters.clear();
      //if(header != null)
       header.clear();
      
    }catch (Exception e){
      JOptionPane.showMessageDialog(null, "Error");
    }

  }


  //JList Column Display
  public static void ColumnDisplay(String Cname){ //returns columns in an Array
    items = new ArrayList<String>();
    if(Cname == "business_ambience"){
      temp = new String[]{"business_id", "touristy", "hipster", "romantic", "intimate", "trendy", "upscale", "classy", "casual", "divey"};
    }else if(Cname == "business_categories"){
      temp = new String[]{"business_id", "activelife", "artsentertainment", "automotive", "beautyspas", "education", "eventplanningservices", "financialservices", "food", "healthmedical", "homeservices", "hotelstravel", "localflavor", "localservice", "massmedia", "nightlife", "pets", "professionalservices", "publicservicesgovernment", "realestate", "religiousorganizations", "restaurants", "shopping"};
    }else if(Cname == "business_general"){
      temp = new String[]{"business_id", "name", "address", "city", "state", "postal_code", "stars", "review_count", "is_open"};
    }else if(Cname == "business_hours"){
      temp = new String[]{"business_id", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"};
    }else if(Cname == "business_misc"){
      temp = new String[]{"business_id", "businessacceptscreditcards", "goodforkids", "restaurantspricerange", "bikeparking", "wifi", "hastv", "alcohol", "restaurantsreservations", "caters"};
    }else if(Cname == "business_parking"){
      temp = new String[]{"business_id", "garage", "street", "validated", "lot", "valet"};
    }else if(Cname == "checkins"){
      temp = new String[]{"business_id", "0to3", "3to6","6to9","9to12","12to15","15to18","18to21","21to24", "totalvisits", "mostpopulartime"};
    }else if(Cname == "reviews"){
      temp = new String[]{"review_id", "user_id", "business_id", "stars", "date", "useful", "funny", "cool"};
    }else if(Cname == "tips"){
      temp = new String[]{"user_id", "business_id", "date", "compliment_count"};
    }else if(Cname == "users"){
      temp = new String[]{"user_id", "name", "review_count", "yelping_since", "useful", "funny", "cool", "fans", "average_stars"};
    }

    for(int i = 0; i<temp.length; i++) {
      items.add(temp[i]);
    }
    list = new JList(items.toArray());

    list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
   
    jsp = new JScrollPane();
    jsp.setBounds(80, 72, 165, 200);
    jsp.setViewportView(list);    

    panel2.add(jsp);
    frame2.setVisible(true);
    
  }

  
  //Main Function
  public static void main(String args[]) {
    JPanel panel = new JPanel();
    JFrame frame = new JFrame();
    frame.setSize(350, 200);
    frame.setTitle("Login - CSCE 315 Section 910");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(panel);

    panel.setLayout(null);
    
    JLabel pathlabel = new JLabel("db910_group8_p2");
    pathlabel.setBounds(10,10, 160, 25);
    panel.add(pathlabel);

    JLabel userlabel = new JLabel("Username:");
    userlabel.setBounds(10,40, 80, 25);
    panel.add(userlabel);

    userText = new JTextField();
    userText.setBounds(80,40,165,25);
    panel.add(userText);

    JLabel passlabel = new JLabel("Password:");
    passlabel.setBounds(10,70, 80, 25);
    panel.add(passlabel);

    passText = new JTextField();
    passText.setBounds(80,70,165,25);
    panel.add(passText);

    Loginbutton = new JButton("Login");
    Loginbutton.setBounds(20,100,80,25);
    Loginbutton.addActionListener(new jdbcpostgreSQLGUI());
    panel.add(Loginbutton);

    success = new JLabel("");
    success.setBounds(10,130,300,25);
    panel.add(success);

    frame.setVisible(true);
  }
  
  //Database Connection
  public static void databaseC()
  {
    conn = null;

      try {
          Class.forName("org.postgresql.Driver");
          conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/db910_group8_project2",
          jdbcpostgreSQLGUI.userText.getText(), jdbcpostgreSQLGUI.passText.getText());
          //"pokemon4907", "127001686");
      } 
      catch (Exception e) {
          e.printStackTrace();
          System.err.println(e.getClass().getName()+": "+e.getMessage());
          System.exit(0);
      }//end try catch
      
      JOptionPane.showMessageDialog(null,"Database Successfully Connected");

      //frame for tablee GUI
      
      String[] tables = new String[]{"business_ambience", "business_categories", "business_general", "business_hours", "business_misc", "business_parking", "checkins", "reviews", "tips", "users"};
            
      panel2 = new JPanel();
      frame2 = new JFrame();
      frame2.setSize(1000, 600);
      frame2.setTitle("Project ZENO - OCTAVE");
      frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame2.add(panel2);

      panel2.setLayout(null);

      String userNLabel = "Current User: " + userText.getText();
      JLabel userN = new JLabel(userNLabel);
      userN.setBounds(10, 5, 165, 25);
      panel2.add(userN);
        
      JLabel tablelabel = new JLabel("Table:");
      tablelabel.setBounds(10,40, 80, 25);
      panel2.add(tablelabel);

      tableList = new JComboBox(tables);
      tableList.setBounds(80,40,165,25);
      tableList.setSelectedIndex(0);
      tableList.addActionListener(new jdbcpostgreSQLGUI()); //adds an action listener when choice is selected.
      panel2.add(tableList);

      JLabel columnlabel = new JLabel("Column:");
      columnlabel.setBounds(10,70, 80, 25);
      panel2.add(columnlabel);

      JLabel Limit = new JLabel("Row Count: ");
      Limit.setBounds(10,280,80,25);
      panel2.add(Limit);

      String[] limitN = new String[]{"10", "20", "30", "50", "100"};
      limitnum = new JComboBox(limitN);
      limitnum.setBounds(80, 280, 80, 25);
      limitnum.setSelectedIndex(0);
      limitnum.addActionListener(new jdbcpostgreSQLGUI());
      panel2.add(limitnum);
      

      TableButton = new JButton("Apply Filters");
      TableButton.setBounds(250,80,120,25);
      TableButton.addActionListener(new jdbcpostgreSQLGUI());
      panel2.add(TableButton);
      
      clear = new JButton("Clear Filters");
      clear.setBounds(250,120,120,25);
      clear.addActionListener(new jdbcpostgreSQLGUI());
      panel2.add(clear);
      
      logout = new JButton("Disconnect");
      logout.setBounds(10, 520, 120, 25);
      logout.addActionListener(new jdbcpostgreSQLGUI());
      panel2.add(logout);

      Submit = new JButton("Submit");
      Submit.setBounds(150, 520, 120, 25);
      Submit.addActionListener(new jdbcpostgreSQLGUI());
      panel2.add(Submit);
      
      frame2.setVisible(true);
      panel2.setVisible(true);


     /*
     //String[] tables = new String[]{"business_ambience", "business_categories", "business_general", "business_hours", "business_misc", "business_parking", "checkins", "reviews", "tips", "users"};
     String Utable = (String) JOptionPane.showInputDialog(null, "Pick your table: ", "Table", JOptionPane.PLAIN_MESSAGE, null, tables, tables[0]);
     
     String[] baT = new String[]{"business_id", "touristy", "hipster", "romantic", "intimate", "trendy", "upscale", "classy", "casual", "divey"}; //business_ambience
     String[] bcT = new String[]{"business_id", "activelife", "artsentertainment", "automotive", "beautyspas", "education", "eventplanningservices", "financialservices", "food", "healthmedical", "homeservices", "hotelstravel", "localflavor", "localservice", "massmedia", "nightlife", "pets", "professionalservices", "publicservicesgovernment", "realestate", "religiousorganizations", "restaurants", "shopping"}; //business categories
     String[] bgT = new String[]{"business_id", "name", "address", "city", "state", "postal_code", "stars", "review_count", "is_open"}; //business general
     String[] bhT = new String[]{"business_id", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"}; //business hours
     String[] bmT = new String[]{"business_id", "businessacceptscreditcards", "goodforkids", "restaurantspricerange", "bikeparking", "wifi", "hastv", "alcohol", "restaurantsreservations", "caters"}; //business misc
     String[] bpT = new String[]{"business_id", "garage", "street", "validated", "lot", "valet"}; //business parking
     String[] chT = new String[]{"business_id", "0to3", "3to6","6to9","9to12","12to15","15to18","18to21","21to24", "totalvisits", "mostpopulartime"}; //business checkins
     String[] reT = new String[]{"review_id", "user_id", "business_id", "stars", "date", "useful", "funny", "cool"}; //reviews
     String[] tipsT = new String[]{"user_id", "business_id", "date", "compliment_count"}; //tips 
     String[] usersT = new String[]{"user_id", "name", "review_count", "yelping_since", "useful", "funny", "cool", "fans", "average_stars"}; //users
     
     String ouputU = "";
       if (Utable == "business_ambience") {
       String userinput = (String) JOptionPane.showInputDialog(null, "Pick your Column: ", "Business Ambience", JOptionPane.PLAIN_MESSAGE, null, baT, baT[0]);
       }
       if (Utable == "business_categories") {
       String userinput = (String) JOptionPane.showInputDialog(null, "Pick your Column: ", "Business Categories", JOptionPane.PLAIN_MESSAGE, null, bcT, bcT[0]);
       }
       if (Utable == "business_general") {
       String userinput = (String) JOptionPane.showInputDialog(null, "Pick your Column: ", "Business General", JOptionPane.PLAIN_MESSAGE, null, bgT, bgT[0]);
       }
       if (Utable == "business_hours") {
       String userinput = (String) JOptionPane.showInputDialog(null, "Pick your Column: ", "Business Hours", JOptionPane.PLAIN_MESSAGE, null, bhT, bhT[0]);
       }
       if (Utable == "business_misc") {
       String userinput = (String) JOptionPane.showInputDialog(null, "Pick your Column: ", "Business Misc", JOptionPane.PLAIN_MESSAGE, null, bmT, bmT[0]);
       }
       if (Utable == "business_parking") {
       String userinput = (String) JOptionPane.showInputDialog(null, "Pick your Column: ", "Business Parking", JOptionPane.PLAIN_MESSAGE, null, bpT, bpT[0]);
       }
       if (Utable == "checkins") {
       String userinput = (String) JOptionPane.showInputDialog(null, "Pick your Column: ", "Checkins", JOptionPane.PLAIN_MESSAGE, null, chT, chT[0]);
       }
       if (Utable == "reviews") {
       String userinput = (String) JOptionPane.showInputDialog(null, "Pick your Column: ", "Reviews", JOptionPane.PLAIN_MESSAGE, null, reT, reT[0]);
       }
       if (Utable == "tips") {
       String userinput = (String) JOptionPane.showInputDialog(null, "Pick your Column: ", "Tips", JOptionPane.PLAIN_MESSAGE, null, tipsT, tipsT[0]);
       }
       if (Utable ==  "users"){
       String userinput = (String) JOptionPane.showInputDialog(null, "Pick your Column: ", "Users - 1", JOptionPane.PLAIN_MESSAGE, null, usersT, usersT[0]);
       //String userinput2 = (String) JOptionPane.showInputDialog(null, "Pick your Column: ", "Users - 2 ", JOptionPane.PLAIN_MESSAGE, null, usersT, usersT[0]);
       if(userinput == "name"){
         try{
           //create a statement object
           Statement stmt = conn.createStatement();
           //create an SQL statement
           String sqlStatement = "SELECT name FROM users limit 10";
           //send statement to DBMS
           ResultSet result = stmt.executeQuery(sqlStatement);

           //OUTPUT
           JOptionPane.showMessageDialog(null,"User names from the Database.");
           //System.out.println("______________________________________");
           while (result.next()) {
             //System.out.println(result.getString("cus_lname"));
             ouputU += result.getString("name")+"\n";
           }
       }catch (Exception e){
         JOptionPane.showMessageDialog(null,"Error accessing Database.");
       }
       } if(userinput == "user_id"){
       try{
         //create a statement object
         Statement stmt = conn.createStatement();
         //create an SQL statement
         String sqlStatement = "SELECT user_id FROM users limit 10";
         //send statement to DBMS
         ResultSet result = stmt.executeQuery(sqlStatement);

         //OUTPUT
         JOptionPane.showMessageDialog(null,"User ID from the Database.");
         //System.out.println("______________________________________");
         while (result.next()) {
           //System.out.println(result.getString("cus_lname"));
           ouputU += result.getString("user_id")+"\n";
         }
     }catch (Exception e){
       JOptionPane.showMessageDialog(null,"Error accessing Database.");
     }
       }
       if(userinput == "review_count"){}
       if(userinput == "yelping_since"){}
       if(userinput == "useful"){}
       if(userinput == "funny"){}
       if(userinput == "cool"){}
       if(userinput == "fans"){}
       if(userinput == "average_stars"){}      

     }
     
     
   // String cus_lname = "Logging Out... \n"; // Customer Last Name
     
   JOptionPane.showMessageDialog(null,ouputU);

   */
   
   



  }// end public function databaseC
    
}//end Class
