import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import java.util.ArrayList;

//java -cp ".;postgresql-42.2.8.jar" jdbcpostgreSQLGUI.java
public class jdbcpostgreSQLGUI {
    // GUI Variables
    public JFrame mainFrame;
    public JPanel leftPanel;
    public JPanel rightPanel;
    public JTabbedPane filterPanel;
    public JTabbedPane displayPanel;
    public JTabbedPane problemPanel;
    public JComponent buttonPanel;
    public JScrollPane scrollPanel;
    public JTable displayTable;
    public static Connection conn;

    // filter button references select
    ArrayList<JCheckBox> generalDisplay = new ArrayList<JCheckBox>();
    ArrayList<JCheckBox> usersDisplay = new ArrayList<JCheckBox>();
    ArrayList<JCheckBox> reviewsDisplay = new ArrayList<JCheckBox>();
    ArrayList<JCheckBox> tipsDisplay = new ArrayList<JCheckBox>();
    // display button references where
    ArrayList<JCheckBox> ambienceFilter = new ArrayList<JCheckBox>();
    ArrayList<JCheckBox> categoriesFilter = new ArrayList<JCheckBox>();
    ArrayList<JTextField> generalFilter = new ArrayList<JTextField>();
    ArrayList<JCheckBox> parkingFilter = new ArrayList<JCheckBox>();
    ArrayList<Component> miscFilter = new ArrayList<Component>(); //why is this a component instead of a checkbox?
    ArrayList<JTextField> reviewsFilter = new ArrayList<JTextField>();
    ArrayList<JTextField> tipsFilter = new ArrayList<JTextField>();
    ArrayList<JTextField> usersFilter = new ArrayList<JTextField>();
    // problem buttons
    JTextField problem1_1;
    JTextField problem1_2;
    JTextField problem2;
    JTextField problem3;
    JTextField problem4;
    JTextField problem5_1;
    JTextField problem5_2;

    ArrayList<String> innerjoinAL = new ArrayList<String>();
    ArrayList<String> OnAL = new ArrayList<String>();

    ArrayList<String> header = new ArrayList<String>();
    ArrayList<Object> x;

    // filter tabs references

    // sql 
    public String sqlQuery;

    // table data
    public DefaultTableModel dtm;

    // misc
    public JComboBox limitButton;
    
    // temp
    public JButton tempButton;
    public JComponent tempComponent;

    //--Initialization--------------------------------------------------------------------------------------------------------------------------------------------------------

    public static void main(String args[]) {
        jdbcpostgreSQLGUI thing = new jdbcpostgreSQLGUI();
    }


    public jdbcpostgreSQLGUI() {
        initGUI();
    }

    public void initGUI() {
        mainFrame = new JFrame("Project ZENO - Octave");
        mainFrame.setSize(1000, 600);
        mainFrame.setLayout(new GridLayout(1, 2));
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //setting up the display panels
        leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(3, 1));
        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        //Top Display Panel for data display
        displayPanel = new JTabbedPane();
        displayPanel.addTab("General Display", makeGeneralDisplayPanel());  
        displayPanel.addTab("Reviews Display", makeReviewsDisplayPanel());
        displayPanel.addTab("Tips Display", makeTipsDisplayPanel());
        displayPanel.addTab("Users Display", makeUsersDisplayPanel());

        //Filter Display for Filtering the Data
        filterPanel = new JTabbedPane();
        filterPanel.addTab("General Filter", makeGeneralFilterPanel());  
        filterPanel.addTab("Ambience Filter", makeAmbienceFilterPanel()); 
        filterPanel.addTab("Categories Filter", makeCategoriesFilterPanel()); 
        filterPanel.addTab("Misc Filter", makeMiscFilterPanel()); 
        filterPanel.addTab("Parking Filter", makeParkingFilterPanel());
        filterPanel.addTab("Reviews Filter", makeReviewsFilterPanel()); 
        filterPanel.addTab("Tips Filter", makeTipsFilterPanel()); 
        filterPanel.addTab("Users Filter", makeUsersFilterPanel()); 
        
        //Scenrio Panel for Demo
        problemPanel = new JTabbedPane();
        problemPanel.addTab("Problem 1", makeProblem1Panel());  
        problemPanel.addTab("Problem 2", makeProblem2Panel());
        problemPanel.addTab("Problem 3", makeProblem3Panel());
        problemPanel.addTab("Problem 4", makeProblem4Panel());
        problemPanel.addTab("Problem 5", makeProblem5Panel());

        //Control Buttons
        buttonPanel = new JPanel();
        tempButton = new JButton("Apply Filters");
        tempButton.addActionListener(new filterActionListener());
        buttonPanel.add(tempButton);
        tempButton = new JButton("Clear Filters");
        tempButton.addActionListener(new filterActionListener());
        buttonPanel.add(tempButton);
        buttonPanel.add(new JLabel("Limit: "));
        limitButton = new JComboBox(new String[]{"10", "25", "50", "100"});
        buttonPanel.add(limitButton);

        //JTable setup
        displayTable = new JTable();
        scrollPanel = new JScrollPane(displayTable);
        displayTable.setFillsViewportHeight(true);

        leftPanel.add(displayPanel);
        leftPanel.add(filterPanel);
        leftPanel.add(problemPanel);
        rightPanel.add(buttonPanel, BorderLayout.NORTH);
        rightPanel.add(scrollPanel, BorderLayout.CENTER);

        mainFrame.add(leftPanel);
        mainFrame.add(rightPanel);
        mainFrame.setVisible(true);
    }

    //--Filter Panels--------------------------------------------------------------------------------------------------------------------------------------------------------

    protected JComponent makeAmbienceFilterPanel() {
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout(3, 3));
        JCheckBox filler;
        for(String strtemp : new String[]{"casual", "classy", "divey", "hipster", "intimate", "romantic", "touristy", "trendy", "upscale"}){
            filler = new JCheckBox(strtemp);
            ambienceFilter.add(filler);
            panel.add(filler);
        }
        return panel;
    }

    protected JComponent makeCategoriesFilterPanel() {
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout(6, 4));
        JCheckBox filler;
        for(String strtemp : new String[]{"activelife", "artsentertainment", "automotive", "beautyspas", "education", "eventplanningservices",
                                            "financialservices", "food", "healthmedical", "homeservices", "hotelstravel", "localflavor", "localservices",
                                            "massmedia", "nightlife", "pets", "professionalservices", "publicservices", "realestate", "religiousorganization",
                                            "restaurants", "shopping"}){
            filler = new JCheckBox(strtemp);
            categoriesFilter.add(filler);
            panel.add(filler);
        }
        return panel;
    }

    protected JComponent makeGeneralFilterPanel() {
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout(5, 4));
        JTextField filler;
        for(String strtemp : new String[]{"business_id", "name", "address", "city", "state", "postal_code", "stars", "operator", "review_count", "operator"}){
            filler = new JTextField("");
            generalFilter.add(filler);
            panel.add(new JLabel(strtemp));
            panel.add(filler);
        }
        return panel;
    }

    protected JComponent makeMiscFilterPanel() {
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout(3, 3));
        JCheckBox filler;
        for(String strtemp : new String[]{"bikeparking", "businessacceptscreditcards", "caters", "goodforkids", "hastv", "restaurantsreservations"}){
            filler = new JCheckBox(strtemp);
            miscFilter.add(filler);
            panel.add(filler);
        }
        JComboBox temp;
        temp = new JComboBox(new String[]{"alcohol", "none", "full_bar", "beer_and_wine"}); panel.add(temp); miscFilter.add(temp);
        temp = new JComboBox(new String[]{"restaurantspricerange", "1", "2", "3", "4"}); panel.add(temp); miscFilter.add(temp);
        temp = new JComboBox(new String[]{"wifi", "no", "paid", "free"}); panel.add(temp); miscFilter.add(temp);
        return panel;
    }

    protected JComponent makeParkingFilterPanel() {
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout(3, 2));
        JCheckBox filler;
        for(String strtemp : new String[]{"garage", "lot", "street", "valet", "validated"}){
            filler = new JCheckBox(strtemp);
            parkingFilter.add(filler);
            panel.add(filler);
        }
        return panel;
    }

    protected JComponent makeReviewsFilterPanel() {
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout(5, 4));
        JTextField filler;
        for(String strtemp : new String[]{"user_id", "business_id", "stars", "operator", "useful", "operator", "funny", "operator", "cool", "operator"}){
            filler = new JTextField("");
            reviewsFilter.add(filler);
            panel.add(new JLabel(strtemp));
            panel.add(filler);
        }
        return panel;
    }

    protected JComponent makeTipsFilterPanel() {
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout(2, 4));
        JTextField filler;
        for(String strtemp : new String[]{"user_id", "business_id", "review_count", "operator"}){
            filler = new JTextField("");
            tipsFilter.add(filler);
            panel.add(new JLabel(strtemp));
            panel.add(filler);
        }
        return panel;
    }

    protected JComponent makeUsersFilterPanel() {
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout(7, 4));
        JTextField filler;
        for(String strtemp : new String[]{"user_id", "name", "review_count", "operator", "average_stars", "operator", "fans", "operator", "useful", "operator", "funny", "operator", "cool", "operator"}){
            filler = new JTextField("");
            usersFilter.add(filler);
            panel.add(new JLabel(strtemp));
            panel.add(filler);
        }
        return panel;
    }

    protected JComponent makeGeneralDisplayPanel() {
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout(3, 4));
        JCheckBox filler;
        for(String strtemp : new String[]{"business_id", "name", "address", "city", "state", "postal_code", "latitude", "longitude", "stars", "review_count", "is_open"}){
            filler = new JCheckBox(strtemp);
            filler.addActionListener(new enableTabGeneral());
            generalDisplay.add(filler);
            panel.add(filler);
        }
        return panel;
    }

    protected JComponent makeUsersDisplayPanel() {
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout(3, 3));
        JCheckBox filler;
        for(String strtemp : new String[]{"user_id", "name", "review_count", "yelping_since", "useful", "funny", "cool", "fans", "average_stars"}){
            filler = new JCheckBox(strtemp);
            usersDisplay.add(filler);
            panel.add(filler);
        }
        return panel;
    }

    protected JComponent makeReviewsDisplayPanel() {
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout(3, 3));
        JCheckBox filler;
        for(String strtemp : new String[]{"review_id", "user_id", "business_id", "stars", "date", "useful", "funny", "cool"}){
            filler = new JCheckBox(strtemp);
            reviewsDisplay.add(filler);
            panel.add(filler);
        }
        return panel;
    }

    protected JComponent makeTipsDisplayPanel() {
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout(3, 2));
        JCheckBox filler;
        for(String strtemp : new String[]{"user_id", "business_id", "text", "date", "compliment_count"}){
            filler = new JCheckBox(strtemp);
            tipsDisplay.add(filler);
            panel.add(filler);
        }
       return panel;
    }


    protected JComponent makeProblem1Panel() {
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout(3, 2));
        JButton submit;
        panel.add(new JLabel("Restaurant 1"));
        problem1_1 = new JTextField(""); panel.add(problem1_1);
        panel.add(new JLabel("Restaurant 2"));
        problem1_2 = new JTextField(""); panel.add(problem1_2);
        submit = new JButton("Submit"); submit.addActionListener(new problem1Submit()); panel.add(submit);
        return panel;
    }

    protected JComponent makeProblem2Panel() {
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout(2, 2));
        JButton submit;
        panel.add(new JLabel("User Name"));
        problem2 = new JTextField(""); panel.add(problem2);
        submit = new JButton("Submit"); submit.addActionListener(new problem2Submit()); panel.add(submit);
        return panel;
    }

    protected JComponent makeProblem3Panel() {
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout(2, 2));
        JButton submit;
        panel.add(new JLabel("State"));
        problem3= new JTextField(""); panel.add(problem3);
        submit = new JButton("Submit"); submit.addActionListener(new problem3Submit()); panel.add(submit);
        return panel;
    }

    protected JComponent makeProblem4Panel() {
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout(2, 2));
        JButton submit;
        panel.add(new JLabel("City"));
        problem4 = new JTextField(""); panel.add(problem4);
        submit = new JButton("Submit"); submit.addActionListener(new problem4Submit()); panel.add(submit);
        return panel;
    }

    protected JComponent makeProblem5Panel() {
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout(3, 2));
        JButton submit;
        panel.add(new JLabel("Longitude"));
        problem5_1 = new JTextField(""); panel.add(problem5_1);
        panel.add(new JLabel("Latitude"));
        problem5_2 = new JTextField(""); panel.add(problem5_2);
        submit = new JButton("Submit"); submit.addActionListener(new problem5Submit()); panel.add(submit);
        return panel;
    }

    //--DB Calls and Display--------------------------------------------------------------------------------------------------------------------------------------------------------
    
    public ResultSet DBCall(String statement) throws SQLException{
        conn = DBConnect();
        ResultSet result = DBQuery(statement, conn);
        DBClose(conn);

        return result;
    }
    
    public static Connection DBConnect() {
        // connect to DB
        conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/db910_group8_project2",
                    "pokemon4907", "127001686");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error connecting to DB DBConnect()");
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return conn;
    }

    public static ResultSet DBQuery(String query, Connection conn) {
        // queries data
        ResultSet result = null;
        try {
            Statement stmt = conn.createStatement();
            String sqlStatement = query;
            result = stmt.executeQuery(sqlStatement);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error creating statement DBQuery()");
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return result;
    }

    public static void DBClose(Connection conn) {
        // close connection with db
        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error closing connection DBClose()");
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public void displayData(ResultSet data) throws SQLException{
        ArrayList<String> columnHeader = new ArrayList<String>();

        //get rids of any reptative elements and place it into the new Array List
        for(String element : header){
            if(!columnHeader.contains(element)){
                columnHeader.add(element);
            }
        }

        //columnHeader - Has removed any repetative elements for a clean visual display of the column headers
        dtm = new DefaultTableModel(columnHeader.toArray(), 0);
        displayTable.setModel(dtm);
        while(data.next()){
            x = new ArrayList<Object>();
            for(int i = 0; i< columnHeader.size(); i++)
            {
                x.add(data.getString(i+1));
            }

            dtm.addRow(x.toArray());
        }
    }

    //--Action Listeners--------------------------------------------------------------------------------------------------------------------------------------------------------    

    class filterActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Apply Filters")){
                sqlQuery = generateQuery();
                System.out.println(sqlQuery);
                ResultSet result = null;
                try{
                    result = DBCall(sqlQuery);
                }
                catch (Exception ex){
                    ex.printStackTrace();
                    System.err.println("1");
                    System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
                    System.exit(0);
                }
                try{
                    displayData(result);
                }
                catch (Exception ex){
                    ex.printStackTrace();
                    System.err.println("2");
                    System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
                    System.exit(0);
                }
                System.out.println("Apply Filters");
            }
            else if (e.getActionCommand().equals("Clear Filters")){
                for(JCheckBox cbtemp : generalDisplay){cbtemp.setSelected(false);}
                for(JCheckBox cbtemp : reviewsDisplay){cbtemp.setSelected(false);}
                for(JCheckBox cbtemp : tipsDisplay){cbtemp.setSelected(false);}
                for(JCheckBox cbtemp : usersDisplay){cbtemp.setSelected(false);}
                for(JCheckBox cbtemp : ambienceFilter){cbtemp.setSelected(false);}
                for(JCheckBox cbtemp : categoriesFilter){cbtemp.setSelected(false);}
                for(JCheckBox cbtemp : parkingFilter){cbtemp.setSelected(false);}

                for(Component c: miscFilter){
                    if (c instanceof JCheckBox){
                        JCheckBox temp = (JCheckBox) c;
                        temp.setSelected(false);
                    }
                    else {
                        JComboBox temp = (JComboBox) c;
                        temp.setSelectedIndex(0);
                    }
                }
                if(header != null){
                    header.clear();
                }
                if(innerjoinAL != null){
                    innerjoinAL.clear();
                }
                if(OnAL != null){
                    OnAL.clear();
                }

                System.out.println("Clear Filters");
            }
            else {
                System.out.println("Error filter action listener");
            }
            
        }
    }

    //ignore
    class enableTabGeneral implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Boolean flag = false;
            for(JCheckBox cbtmp: generalDisplay){
                if(cbtmp.isSelected() == true){
                    flag = true;
                    for(int i = 0; i < 5; i++){
                        filterPanel.setEnabledAt(i, true);
                        //for(JCheckBox cbtemp2: filterPanel.getComponentAt(i).getComponents()){ //getComponents returns an array of components
                        //    cbtemp2.setEnabled(true);
                        //}
                    }
                }
            }
            if(!flag){
                for(int i = 0; i < 5; i++){
                    filterPanel.setEnabledAt(i, false);
                    //for(JCheckBox cbtemp2: filterPanel.getComponentAt(i).getComponents()){
                    //    cbtemp2.setEnabled(false);
                    //}
                }
            }
        }
    }
    //---------------------------------------------------------------DEMO SCENERIOS-----------------------------------------------------------------------------------------------------------------------------------
    class problem1Submit implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String query = "WITH RECURSIVE search_path (path_ids, length, is_visited) AS"+
                            "( "+
                                "SELECT "+
                                    "ARRAY[from_id, to_id],"+
                                    "1,"+ 
                                    "from_id = to_id "+
                                "FROM "+
                                    "problem1 "+
                                
                                "UNION ALL "+
                                "SELECT "+
                                    "path_ids || d.to_id, "+
                                    "f.length + 1, "+
                                    "d.to_id = ANY(f.path_ids) "+
                                "FROM "+
                                    "problem1 d, "+
                                    "search_path f "+
                                "WHERE "+
                                "f.path_ids[array_length(path_ids, 1)] = d.from_id "+
                                "AND NOT f.is_visited "+
                            ") "+
                            "SELECT path_ids, length FROM search_path "+
                            "WHERE path_ids[1] = '" + problem1_1.getText() + "' "+
                            "AND path_ids[array_length(path_ids, 1)] = '" + problem1_2.getText() + "' "+
                            "LIMIT 1"+
                            ";";
            ResultSet result = null;
            System.out.println(query);
            
            List<String> listofBusinessID2 = new ArrayList<String>();
            int lengthOfChain;
            try {
                result = DBCall(query);
                System.out.println("hello");
                while(result.next()){
                    System.out.println(result.getString("path_ids") + " | " + result.getString("length"));
                    String [] listOfBusinessID = result.getString("path_ids").replace("{", "").replace("}", "").split(",");;
                    //listOfBusinessID = result.getString("path_ids").replaceAll("{", "").replaceAll("}", "").split(",");
                    lengthOfChain = Integer.parseInt(result.getString("length"));
                    listofBusinessID2 = Arrays.asList(listOfBusinessID);
                }
            }
            catch (Exception ex){
                System.err.println("problem 1.1");
                System.exit(0);
            }
            
            //listofBusinessID2 = Arrays.asList(listOfBusinessID);
            ArrayList<String> listOfUserID = new ArrayList<String>();
            for(int i = 0; i < listofBusinessID2.size() - 1; i++){
                query = "select user_id "+
                    "from reviews "+
                    "where business_id in ('" + listofBusinessID2.get(i) + "', '" + listofBusinessID2.get(i+1) + "') "+
                    "group by user_id "+
                    "having COUNT(*) > 1 LIMIT 1;";
                try {
                    result = DBCall(query);
                    System.out.println("hello2");
                    while(result.next()){
                        listOfUserID.add(result.getString("user_id"));
                    }
                }
                catch (Exception ex){
                    System.err.println("problem 1.2");
                    System.exit(0);
                }
            }

            String user_ids = "";
            for(int i = 0; i < listOfUserID.size(); i++){
                user_ids += "'" + listOfUserID.get(i) + "'" + ",";
            }
            
            query = "select name from users where user_id in (" + user_ids.substring(0, user_ids.length() - 1) + ")";
            dtm = new DefaultTableModel(new String[]{"name"}, 0);
            displayTable.setModel(dtm);
            try {
                result = DBCall(query);
                System.out.println("hello3");
                while(result.next()){
                    dtm.addRow(new Object[]{result.getString("name")});  
                }
            }
            catch (Exception ex){
                System.err.println("problem 1.3");
                System.exit(0);
            }

            System.out.println("Problem 1 submit button test");
        }
    }


    class problem2Submit implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String query = "select user_id from users where name = '" + problem2.getText() + "' limit 1;";
            ResultSet result = null;
            String user_id = "";
            System.out.println(query);
            try {
                result = DBCall(query);
                while(result.next()){
                    user_id = result.getString("user_id");
                }
            }
            catch (Exception ex){
                ex.printStackTrace();
                System.err.println("problem 3.0");
                System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
                System.exit(0);
            }

            query = "SELECT users.user_id, users.name, AVG(reviews.stars) AS AVGStars, COUNT(reviews.user_id) AS reviewCNT,"+
                            "AVG(reviews.useful) AS usefulAVG, AVG(reviews.funny) AS funnyAVG, AVG(reviews.cool) AS coolAVG "+
                            "FROM users INNER JOIN reviews ON users.user_id = reviews.user_id "+
                            "WHERE users.user_id = '" + user_id + "' "+
                            "GROUP BY users.user_id HAVING COUNT(reviews.user_id) >= 5 LIMIT " + limitButton.getSelectedItem() + ";";
            System.out.println(query);
            try {
                result = DBCall(query);
            }
            catch (Exception ex){
                ex.printStackTrace();
                System.err.println("problem 3.1");
                System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
                System.exit(0);
            }

            dtm = new DefaultTableModel(new String[]{"user_id","name","stars_avg","review_count","useful_avg","funny_avg","cool_avg"}, 0);
            displayTable.setModel(dtm);

            try{
                while(result.next()){
                    dtm.addRow(new Object[]{result.getString("user_id"),result.getString("name"),result.getString("avgstars"),result.getString("reviewcnt"),result.getString("usefulavg"),result.getString("funnyavg"),result.getString("coolavg")});  
                }
            }
            catch (Exception ex){
                ex.printStackTrace();
                System.err.println("problem 3.2");
                System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
                System.exit(0);
            }

            System.out.println("Problem 2 submit button test");
        }
    }

    class problem3Submit implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String query = "SELECT business_general.name, SQRT(SUM(POWER(business_general.longitude - avgSelect.avgLong,2) + POWER(business_general.latitude - avgSelect.avgLat,2)) / COUNT(business_general.name)) AS Spread FROM business_general "+
                            "INNER JOIN (SELECT COUNT(name) AS numLocation, name, AVG(longitude) AS avgLong, AVG(latitude) AS avgLat FROM "+
                            "business_general WHERE state = '" + problem3.getText() + "' GROUP BY name HAVING AVG(stars) >= 3.5 AND COUNT(name) > 1 ORDER BY name DESC) avgSelect ON "+
                            "business_general.name = avgSelect.name WHERE business_general.state = '" + problem3.getText() + "' GROUP BY business_general.name ORDER BY Spread DESC limit " + limitButton.getSelectedItem() + ";";
            ResultSet result = null;
            System.out.println(query);
            try {
                result = DBCall(query);
            }
            catch (Exception ex){
                ex.printStackTrace();
                System.err.println("problem 3.1");
                System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
                System.exit(0);
            }

            dtm = new DefaultTableModel(new String[]{"name","spread"}, 0);
            displayTable.setModel(dtm);

            try{
                while(result.next()){
                    dtm.addRow(new Object[]{result.getString("name"),result.getString("spread")});  
                }
            }
            catch (Exception ex){
                ex.printStackTrace();
                System.err.println("problem 3.2");
                System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
                System.exit(0);
            }

            System.out.println("Problem 3 submit button test");
        }
    }

    class problem4Submit implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String query = "select name, tips.* "+
                            "from "+
                            "("+
                                "select tips.business_id, count(*) as cnt "+
                                "from tips "+
                                "inner join "+
                                "("+
                                    "select business_id "+
                                    "from business_general "+
                                    "inner join "+
                                    "("+
                                        "select name, count(*) "+
                                        "from business_general "+
                                        "where city = '" + problem4.getText() + "' "+
                                        "group by name "+
                                        "having count(*) = 1 "+
                                    ") as temp "+
                                    "on business_general.name = temp.name "+
                                ") as temp2 "+
                                "on tips.business_id = temp2.business_id "+
                                "group by tips.business_id "+
                                "order by cnt desc "+
                                "limit 1 "+
                            ") as temp3 "+
                            "inner join tips "+
                            "on tips.business_id = temp3.business_id "+
                            "inner join business_general "+
                            "on business_general.business_id = tips.business_id "+
                            "limit " + limitButton.getSelectedItem() + ";";
            ResultSet result = null;
            System.out.println(query);
            try {
                result = DBCall(query);
            }
            catch (Exception ex){
                ex.printStackTrace();
                System.err.println("problem 4.1");
                System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
                System.exit(0);
            }

            dtm = new DefaultTableModel(new String[]{"name", "user_id","business_id","text","date", "compliment_count"}, 0);
            displayTable.setModel(dtm);

            try{
                while(result.next()){
                    dtm.addRow(new Object[]{result.getString("name"),result.getString("user_id"),result.getString("business_id"),result.getString("text"),result.getString("date"),result.getString("compliment_count")});  
                }
            }
            catch (Exception ex){
                ex.printStackTrace();
                System.err.println("problem 4.2");
                System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
                System.exit(0);
            }

            System.out.println("Problem 4 submit button test");
        }
    }

    class problem5Submit implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String query = "select business_id, name, state, longitude, latitude, acos((sin(latitude/57.29577951) * sin(" + problem5_2.getText() + "/57.29577951)) + (cos(latitude/57.29577951) * cos(" + problem5_2.getText() + "/57.29577951) * cos((" + problem5_1.getText() + "/57.29577951) - (longitude/57.29577951)))) as dist "+
                            "from business_general "+
                            "where stars >= 4.0 and review_count >= 100 "+
                            "order by dist limit " + limitButton.getSelectedItem() + ";";
            ResultSet result = null;
            System.out.println(query);
            try {
                result = DBCall(query);
            }
            catch (Exception ex){
                ex.printStackTrace();
                System.err.println("problem 5.1");
                System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
                System.exit(0);
            }

            dtm = new DefaultTableModel(new String[]{"business_id","name","state","longitude","latitude", "dist"}, 0);
            displayTable.setModel(dtm);

            try{
                while(result.next()){
                    dtm.addRow(new Object[]{result.getString("business_id"),result.getString("name"),result.getString("state"),result.getString("longitude"),result.getString("latitude"),result.getString("dist")});  
                }
            }
            catch (Exception ex){
                ex.printStackTrace();
                System.err.println("problem 5.2");
                System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
                System.exit(0);
            }

            System.out.println("Problem 5 submit button test");
        }
    }

    //--Generating SQL Logic--------------------------------------------------------------------------------------------------------------------------------------------------------
    
    //ArrayList<JCheckBox> generalDisplay = new ArrayList<JCheckBox>();
    //ArrayList<JCheckBox> usersDisplay = new ArrayList<JCheckBox>();
    //ArrayList<JCheckBox> reviewsDisplay = new ArrayList<JCheckBox>();
    //ArrayList<JCheckBox> tipsDisplay = new ArrayList<JCheckBox>();

    public String generateQuery(){  // TBD implement more complex logic for sql statements
        //String ret1 = "SELECT business_general.* FROM business_general ";
        String ret1 = "";  
        String ret2 = "";
        String ret3 = "";
        Boolean ambienceFlag = false;
        Boolean categoriesFlag = false;
        Boolean parkingFlag = false;
        Boolean miscFlag = false;

        int selectCount = 0;
        int innerjoinCount = 0;
        int wherecount = 0;
        Boolean genDispFlag = false;
        Boolean userDispFlag = false;
        Boolean revDispFlag = false;
        Boolean tipDispFlag = false;

        int generalcount = 0;
        int usercount = 0;
        int reviewcount = 0;
        int tipcount = 0;
        
        //"SELECT DispArrayList FROM business_general "
        ret1 += "SELECT ";
        //if only one table is selected 
        //note: create a way for innerjoin\
        //generalDissplay.size() > 0 || if some array list that contains 
        if(generalDisplay.size() > 0){ //if checkboxes of general has been added to general display then... for loop
            for(JCheckBox jcbtemp : generalDisplay){
                if(jcbtemp.isSelected() == true){                   //check boxes getting selected
                    if(selectCount < 1){                            //if there isn't anything after the word "SELCET"
                        genDispFlag = true;
                        ret1 += "business_general." + jcbtemp.getText();
                    }else{                                          //if there is already a filter after the word "SELECT"
                        genDispFlag = true;
                        ret1 += ", " + "business_general." + jcbtemp.getText();
                    }
                    selectCount++; //just for setting commas
                    generalcount++;
                    String headerS = jcbtemp.getText();
                    header.add(headerS); //new JTABLE Integration
                }            
            } 
            if(generalcount > 0){
                innerjoinAL.add("business_general");
            }
            //FROM
            for(int i = 0; i < innerjoinAL.size(); i++){
                if((innerjoinAL.get(i) == "business_general") && (innerjoinCount < 1)){
                    ret2 += " FROM business_general ";
                    innerjoinCount++;
                    OnAL.add(innerjoinAL.get(i)); 
                    innerjoinAL.remove(i); //innerjoinAL purpose is for select. OnAL is to document the FROM which table.
                }
                    
            }   
            
        }
        if(usersDisplay.size() > 0){                           //users
            for(JCheckBox jcbtemp : usersDisplay){      
                if(jcbtemp.isSelected() == true){
                    
                    if(selectCount < 1){
                        userDispFlag = true;
                        ret1 += "users." + jcbtemp.getText();
                    }else{
                        userDispFlag = true;
                        ret1 += ", " + "users." + jcbtemp.getText();
                    }
                    selectCount++;
                    usercount++;
                    String headerS = jcbtemp.getText();
                    header.add(headerS); //new JTABLE Integration
                    
                }
            }
            if(usercount > 0){
                innerjoinAL.add("users");
            }
            for(int i = 0; i < innerjoinAL.size(); i++){
                if((innerjoinAL.get(i) == "users") && (innerjoinCount < 1)){ //can only have one FROM 
                    ret2 += " FROM users ";
                    innerjoinCount++;
                    OnAL.add(innerjoinAL.get(i));
                    innerjoinAL.remove(i);
                }
            }                   
            
        }
        if(reviewsDisplay.size() > 0){                         //reviews
            for(JCheckBox jcbtemp : reviewsDisplay){
                if(jcbtemp.isSelected() == true){
                    
                    if(selectCount < 1){
                        revDispFlag = true;
                        ret1 += "reviews." + jcbtemp.getText();
                    }else{
                        revDispFlag = true;
                        ret1 += ", " + "reviews." + jcbtemp.getText();
                    }
                    selectCount++;
                    reviewcount++;
                    String headerS = jcbtemp.getText();
                    header.add(headerS); //new JTABLE Integration
                    
                }
            }
            if(reviewcount > 0){
                innerjoinAL.add("reviews");
            }
            for(int i = 0; i < innerjoinAL.size(); i++){
                if((innerjoinAL.get(i) == "reviews") && (innerjoinCount < 1)){
                    ret2 += " FROM reviews ";
                    innerjoinCount++;
                    OnAL.add(innerjoinAL.get(i));
                    innerjoinAL.remove(i);
                }
            }  
           
        }   
        if(tipsDisplay.size() > 0){                            //tips
            for(JCheckBox jcbtemp : tipsDisplay){
                if(jcbtemp.isSelected() == true){
                    
                    if(selectCount < 1){
                        tipDispFlag = true;
                        ret1 += "tips." + jcbtemp.getText();
                    }else{
                        tipDispFlag = true;
                        ret1 += ", " + "tips." + jcbtemp.getText();
                    }
                    selectCount++;
                    tipcount++;
                    String headerS = jcbtemp.getText();
                    header.add(headerS); //new JTABLE Integration
                    
                }
            }
            if(tipcount > 0){
                innerjoinAL.add("tips");
            }
            for(int i = 0; i < innerjoinAL.size(); i++){
                if((innerjoinAL.get(i) == "tips") && (innerjoinCount < 1)){
                    ret2 += " FROM tips ";
                    innerjoinCount++;
                    OnAL.add(innerjoinAL.get(i));
                    innerjoinAL.remove(i);
                }
            }
            
        }
                    
        //-------------------------------------------------------INNER JOIN----TOP PANEL-------------------------------------------------------------------------
        /*Inner Join Display Panels */
        for(int i = 0; i < innerjoinAL.size(); i++){
            if((innerjoinAL.get(i) == "business_general") && (innerjoinCount > 0)){
                ret2 += "INNER JOIN business_general ";
                innerjoinAL.remove(i);
                innerjoinCount++;
                //if(OnAL.get(i) == "users"){                                                 //What's key to link business_general and users?
                //    ret1+= "ON ";
                //}
                if(OnAL.get(0) == "reviews"){
                    ret2+= "ON business_general.business_id = reviews.business_id ";
                }
                else if(OnAL.get(0) == "tips"){
                    ret2+= "ON business_general.business_id = tips.business_id ";
                }

            }
            else if((innerjoinAL.get(i) == "users") && (innerjoinCount > 0)){
                ret2 += "INNER JOIN users ";
                innerjoinAL.remove(i);
                innerjoinCount++;
                //if(OnAL.get(i) == "business_general"){                                      //What's key to link business_general and users?
                //    ret1+= "ON ";
                //}
                if(OnAL.get(0) == "reviews"){
                    ret2+= "ON users.user_id = reviews.user_id ";
                }
                else if(OnAL.get(0) == "tips"){
                    ret2+= "ON users.user_id = tips.user_id ";
                }

            }
            else if((innerjoinAL.get(i) == "reviews") && (innerjoinCount > 0)){
                ret2 += "INNER JOIN reviews ";
                innerjoinAL.remove(i);
                innerjoinCount++;
                if(OnAL.get(0) == "business_general"){
                    ret2+= "ON reviews.business_id = business_general.business_id ";
                }
                else if(OnAL.get(0) == "users"){
                    ret2+= "ON reviews.user_id = users.user_id ";
                }
                else if(OnAL.get(0) == "tips"){
                    ret2+= "ON reviews.user_id = tips.user_id ";
                }

            }
            else if((innerjoinAL.get(i) == "tips") && (innerjoinCount > 0)){
                ret2 += "INNER JOIN tips ";
                innerjoinAL.remove(i);
                innerjoinCount++;
                if(OnAL.get(0) == "business_general"){
                    ret2+= "ON tips.business_id = business_general.business_id ";
                }
                else if(OnAL.get(0) == "users"){
                    ret2+= "ON tips.user_id = users.user_id ";
                }
                else if(OnAL.get(0) == "reviews"){
                    ret2+= "ON tips.user_id = reviews.user_id ";
                }

            }
        }

        //OnAL holds the FROM... repeat craeted strucutres for the misc
        //-------------------------------------------------------------------------------------------------------------------------------WHERE
        
        int whereinit = 0;
        for(JCheckBox jcbtemp : ambienceFilter){
            if(jcbtemp.isSelected() == true){
                whereinit++;
                if(whereinit == 1){
                    ret3 += "WHERE ( ";
                }
                if(wherecount < 1){
                    ambienceFlag = true;
                    ret3 += "business_ambience." + jcbtemp.getText() + " = '1' ";
                }else{
                    ambienceFlag = true;
                    ret3 += "OR " + "business_ambience." + jcbtemp.getText() + " = '1' ";
                }
                wherecount++;
                String headerS = jcbtemp.getText();
                header.add(headerS); //new JTABLE Integration
                
            }
        }
        for(JCheckBox jcbtemp : categoriesFilter){
            if(jcbtemp.isSelected() == true){
                whereinit++;
                if(whereinit == 1){
                    ret3 += "WHERE ( ";
                }
                if(wherecount < 1){
                    categoriesFlag = true;
                    ret3 += "business_categories." + jcbtemp.getText() + " = '1' ";
                }else{
                    categoriesFlag = true;
                    ret3 += "OR " + "business_categories." + jcbtemp.getText() + " = '1' ";
                }
                wherecount++;
                String headerS = jcbtemp.getText();
                header.add(headerS); //new JTABLE Integration
            }
        }
        for(JCheckBox jcbtemp : parkingFilter){
            if(jcbtemp.isSelected() == true){
                whereinit++;
                if(whereinit == 1){
                    ret3 += "WHERE ( ";
                }
                if(jcbtemp.isSelected() == true){
                    if(wherecount < 1){
                        parkingFlag = true;
                        ret3 += "business_parking." + jcbtemp.getText() + " = '1' ";
                    }else{
                        parkingFlag = true;
                        ret3 += "OR " + "business_parking." + jcbtemp.getText() + " = '1' ";
                    }
                    wherecount++;
                    String headerS = jcbtemp.getText();
                    header.add(headerS); //new JTABLE Integration
                }
                
            }
        }
        
        for(Component jcbtemp : miscFilter){ //need to check wit HUY on this one... confused tbh
            if(jcbtemp instanceof JCheckBox){
                JCheckBox temp = (JCheckBox) jcbtemp;
                if(temp.isSelected() == true){
                    whereinit++;
                    if(whereinit == 1){
                        ret3 += "WHERE ( ";
                    }
                    if(wherecount < 1){
                        miscFlag = true;
                        ret3 += "business_misc." + temp.getText() + " = '1' ";
                    }else{
                        miscFlag = true;
                        ret3 += "OR " + "business_misc." + temp.getText() + " = '1' ";
                    }
                    wherecount++;
                    String headerS = temp.getText();
                    header.add(headerS); //new JTABLE Integration
                    
                } 
            }else if (jcbtemp instanceof JComboBox){
                JComboBox temp2 = (JComboBox) jcbtemp;
                if(temp2.getItemAt(0) == "alcohol") {
                    if(temp2.getSelectedItem() == "alcohol"){ // if it equals the original default, its wrong. (shouldn't be set with the where)
                        ret3 += "";
                    } else{
                        whereinit++;
                        if(whereinit == 1){
                            ret3 += "WHERE ( ";
                        }                
                        if(wherecount < 1){
                            miscFlag = true;
                            ret3 += "business_misc.alcohol" + " = '" + temp2.getSelectedItem() + "' ";
                        }
                        else{
                            miscFlag = true;
                            ret3 += "OR " + "business_misc.alcohol" + " = '" + temp2.getSelectedItem() + "' ";
                        }
                        wherecount++;
                        String headerS = "alcohol";
                        header.add(headerS); //new JTABLE Integration
                    }
                }
                else if (temp2.getItemAt(0) == "restaurantspricerange") {
                    if(temp2.getSelectedItem() == "restaurantspricerange"){
                        ret3 += "";
                    } else {
                        whereinit++;
                        if(whereinit == 1){
                            ret3 += "WHERE ( ";
                        }
                        if(wherecount < 1){
                            miscFlag = true;
                            ret3 += "business_misc.restaurantspricerange" + " = '" + temp2.getSelectedItem() + "' ";
                        }
                        else{
                            miscFlag = true;
                            ret3 += "OR " + "business_misc.restaurantspricerange" + " = '" + temp2.getSelectedItem() + "' ";
                        }
                        wherecount++;
                        String headerS = "restaurantspricerange";
                        header.add(headerS); //new JTABLE Integration
                    }
                }
                else if (temp2.getItemAt(0) == "wifi") {
                    if(temp2.getSelectedItem() == "wifi"){
                        ret3 += "";
                    } else{
                        whereinit++;
                        if(whereinit == 1){
                            ret3 += "WHERE ( ";
                        }
                        if(wherecount < 1){
                            miscFlag = true;
                            ret3 += "business_misc.wifi" + " = '" + temp2.getSelectedItem() + "' ";
                        }
                        else{
                            miscFlag = true;
                            ret3 += "OR " + "business_misc.wifi" + " = '" + temp2.getSelectedItem() + "' ";
                        }
                        wherecount++;
                        String headerS = "wifi";
                        header.add(headerS); //new JTABLE Integration
                    }
                }
                
            }
        }
        
        //----------------------------------------------------------------------breaking point for the header = personal note *
        int jtipcount = 0;
        String Tipreviewcount = "";
        String operatorTip = "";
        boolean tipsFlag = false;
        boolean Tip_A1 = false;
        boolean Tip_A2 = false;
        for(JTextField jcbtemp : tipsFilter){
            jtipcount++; //counter for the arraylist
            if(jcbtemp.getText().equals("")){ //if equals empty string
                ret3 += "";
            }else{// if it doesn't equal to an empty string
                whereinit++;
                if(whereinit == 1){
                    ret3 += "WHERE ( ";
                }
                if(jtipcount == 1){ //user_id
                    if(wherecount < 1){
                        tipsFlag = true;
                        ret3 += "tips.user_id" + " = '" + jcbtemp.getText() + "' ";
                    }else{
                        tipsFlag = true;
                        ret3 += "OR " + "tips.user_id" + " = '" + jcbtemp.getText() + "' ";
                    }
                    String headerS = "user_id";
                    header.add(headerS); //new JTABLE Integration
                }
                else if (jtipcount == 2){//business_id
                    if(wherecount < 1){
                        tipsFlag = true;
                        ret3 += "tips.business_id" + " = '" + jcbtemp.getText() + "' ";
                    }else{
                        tipsFlag = true;
                        ret3 += "OR " + "tips.business_id" + " = '" + jcbtemp.getText() + "' ";
                    }
                    String headerS = "business_id";
                    header.add(headerS); //new JTABLE Integration
                }
                else if (jtipcount == 3){//review_count
                    Tipreviewcount += jcbtemp.getText();
                    Tip_A1 = true;
                }
                else if (jtipcount == 4){//operator
                    operatorTip += jcbtemp.getText();
                    Tip_A2 = true;
                }
                else if ( Tip_A1 == true && Tip_A2 == true){
                    if(wherecount < 1){
                        tipsFlag = true;
                        ret3 += "tips.review_count " + operatorTip + " " + Tipreviewcount + " ";
                    }else{
                        tipsFlag = true;
                        ret3 += "OR " + "tips.review_count " + operatorTip + " " + Tipreviewcount + " ";
                    }
                    String headerS = "review_count";
                    header.add(headerS); //new JTABLE Integration
                }
                wherecount++;

            }
        }
        
        int jreviewcount = 0;
        boolean reviewFlag = false;

        String Rstars = "";
        String RstarsOp = "";
        String Ruseful = "";
        String RusefulOp = "";
        String Rfunny = "";
        String RfunnyOp = "";
        String Rcool = "";
        String RcoolOp = "";

        boolean Tip_B1 = false; //stars
        boolean Tip_B2 = false; //stars operator
        boolean Tip_B3 = false; //useful
        boolean Tip_B4 = false; //useful operator
        boolean Tip_B5 = false; //funny 
        boolean Tip_B6 = false; //funny operator
        boolean Tip_B7 = false; //cool
        boolean Tip_B8 = false; //cool operator
        
        
        for(JTextField jcbtemp : reviewsFilter){
            jreviewcount++;
            if(jcbtemp.getText().equals("")){
                ret3 += "";
            }
            else{
                whereinit++;
                if(whereinit == 1){
                    ret3 += "WHERE ( ";
                }
                if(jreviewcount == 1){ //user_id
                    if(wherecount < 1){
                        reviewFlag = true;
                        ret3 += "reviews.user_id" + " = '" + jcbtemp.getText() + "' ";
                    }else{
                        reviewFlag = true;
                        ret3 += "OR " + "reviews.user_id" + " = '" + jcbtemp.getText() + "' ";
                    }
                    String headerS = "user_id";
                    header.add(headerS); //new JTABLE Integration
                }
                else if (jreviewcount == 2){//business_id
                    if(wherecount < 1){
                        reviewFlag = true;
                        ret3 += "reviews.business_id" + " = '" + jcbtemp.getText() + "' ";
                    }else{
                        reviewFlag = true;
                        ret3 += "OR " + "reviews.business_id" + " = '" + jcbtemp.getText() + "' ";
                    }
                    String headerS = "business_id";
                    header.add(headerS); //new JTABLE Integration
                }
                else if (jreviewcount == 3){//stars
                    Rstars += jcbtemp.getText();
                    Tip_B1 = true;
                }
                else if (jreviewcount == 4){//operator
                    RstarsOp += jcbtemp.getText();
                    Tip_B2 = true;
                }
                else if ( Tip_B1 == true && Tip_B2 == true){
                    if(wherecount < 1){
                        reviewFlag = true;
                        ret3 += "reviews.stars " + RstarsOp + " " + Rstars + " ";
                    }else{
                        reviewFlag = true;
                        ret3 += "OR " + "reviews.stars " + RstarsOp + " " + Rstars + " ";
                    }
                    String headerS = "stars";
                    header.add(headerS); //new JTABLE Integration
                }
                else if (jreviewcount == 5){//useful
                    Ruseful += jcbtemp.getText();
                    Tip_B3 = true;
                }
                else if (jreviewcount == 6){//operator
                    RusefulOp += jcbtemp.getText();
                    Tip_B4 = true;
                }
                else if ( Tip_B3 == true && Tip_B4 == true){
                    if(wherecount < 1){
                        reviewFlag = true;
                        ret3 += "reviews.useful " + RusefulOp + " " + Ruseful + " ";
                    }else{
                        reviewFlag = true;
                        ret3 += "OR " + "reviews.useful " + RusefulOp + " " + Ruseful + " ";
                    }
                    String headerS = "useful";
                    header.add(headerS); //new JTABLE Integration
                }
                else if (jreviewcount == 7){//funny
                    Rfunny += jcbtemp.getText();
                    Tip_B5 = true;
                }
                else if (jreviewcount == 8){//operator
                    RfunnyOp += jcbtemp.getText();
                    Tip_B6 = true;
                }
                else if ( Tip_B5 == true && Tip_B6 == true){
                    if(wherecount < 1){
                        reviewFlag = true;
                        ret3 += "reviews.funny " + RfunnyOp + " " + Rfunny + " ";
                    }else{
                        reviewFlag = true;
                        ret3 += "OR " + "reviews.funny " + RfunnyOp + " " + Rfunny + " ";
                    }
                    String headerS = "funny";
                    header.add(headerS); //new JTABLE Integration
                }
                else if (jreviewcount == 9){//cool
                    Rcool += jcbtemp.getText();
                    Tip_B7 = true;
                }
                else if (jreviewcount == 10){//operator
                    RcoolOp += jcbtemp.getText();
                    Tip_B8 = true;
                }
                else if ( Tip_B7 == true && Tip_B8 == true){
                    if(wherecount < 1){
                        reviewFlag = true;
                        ret3 += "reviews.cool " + RcoolOp + " " + Rcool + " ";
                    }else{
                        reviewFlag = true;
                        ret3 += "OR " + "reviews.cool " + RcoolOp + " " + Rcool + " ";
                    }
                    String headerS = "cool";
                    header.add(headerS); //new JTABLE Integration
                }
                wherecount++;
                
            }
        }

        int jusercount = 0;
        boolean userFlag = false;

        String Ureview = "";
        String UreviewOp = "";
        String Ustars = "";
        String UstarsOp = "";
        String Ufans = "";
        String UfansOp = "";
        String Uuseful = "";
        String UusefulOp = "";
        String Ufunny = "";
        String UfunnyOp = "";
        String Ucool = "";
        String UcoolOp = "";

        boolean Tip_C1 = false;  //reviews
        boolean Tip_C2 = false;  //reviews operator
        boolean Tip_C3 = false;  //stars
        boolean Tip_C4 = false;  //stars operator
        boolean Tip_C5 = false;  //fans 
        boolean Tip_C6 = false;  //fans operator
        boolean Tip_C7 = false;  //useful
        boolean Tip_C8 = false;  //useful operator
        boolean Tip_C9 = false;  //funny
        boolean Tip_C10 = false; //funny operator
        boolean Tip_C11 = false; //cool
        boolean Tip_C12 = false; //cool operator
        
        for(JTextField jcbtemp : usersFilter){
            jusercount++;
            if(jcbtemp.getText().equals("")){
                ret3 += "";
            }
            else{
                whereinit++;
                if(whereinit == 1){
                    ret3 += "WHERE ( ";
                }
                if(jusercount == 1){ //user_id
                    if(wherecount < 1){
                        userFlag = true;
                        ret3 += "users.user_id" + " = '" + jcbtemp.getText() + "' ";
                    }else{
                        userFlag = true;
                        ret3 += "OR " + "users.user_id" + " = '" + jcbtemp.getText() + "' ";
                    }
                    String headerS = "user_id";
                    header.add(headerS); //new JTABLE Integration
                }
                else if (jusercount == 2){//name
                    if(wherecount < 1){
                        userFlag = true;
                        ret3 += "users.name" + " = '" + jcbtemp.getText() + "' ";
                    }else{
                        userFlag = true;
                        ret3 += "OR " + "users.name" + " = '" + jcbtemp.getText() + "' ";
                    }
                    String headerS = "name";
                    header.add(headerS); //new JTABLE Integration
                }
                else if (jusercount == 3){//reivew_count
                    Ureview += jcbtemp.getText();
                    Tip_C1 = true;
                }
                else if (jusercount == 4){//operator
                    UreviewOp += jcbtemp.getText();
                    Tip_C2 = true;
                }
                else if ( Tip_C1 == true && Tip_C2 == true){
                    if(wherecount < 1){
                        userFlag = true;
                        ret3 += "users.reivew_count " + UreviewOp + " " + Ureview + " ";
                    }else{
                        userFlag = true;
                        ret3 += "OR " + "users.reivew_count " + UreviewOp + " " + Ureview + " ";
                    }
                    String headerS = "reivew_count";
                    header.add(headerS); //new JTABLE Integration
                }
                else if (jusercount == 5){//stars
                    Ustars += jcbtemp.getText();
                    Tip_C3 = true;
                }
                else if (jusercount == 6){//operator
                    UstarsOp += jcbtemp.getText();
                    Tip_C4 = true;
                }
                else if ( Tip_C3 == true && Tip_C4 == true){
                    if(wherecount < 1){
                        userFlag = true;
                        ret3 += "users.average_stars " + UstarsOp + " " + Ustars + " ";
                    }else{
                        userFlag = true;
                        ret3 += "OR " + "users.average_stars " + UstarsOp + " " + Ustars + " ";
                    }
                    String headerS = "average_stars";
                    header.add(headerS); //new JTABLE Integration
                }
                else if (jusercount == 7){//fans
                    Ufans += jcbtemp.getText();
                    Tip_C5 = true;
                }
                else if (jusercount == 8){//operator
                    UfansOp += jcbtemp.getText();
                    Tip_C6 = true;
                }
                else if ( Tip_C5 == true && Tip_C6 == true){
                    if(wherecount < 1){
                        userFlag = true;
                        ret3 += "users.fans " + UfansOp + " " + Ufans + " ";
                    }else{
                        userFlag = true;
                        ret3 += "OR " + "users.fans " + UfansOp + " " + Ufans + " ";
                    }
                    String headerS = "fans";
                    header.add(headerS); //new JTABLE Integration
                }
                else if (jusercount == 9){//useful
                    Uuseful += jcbtemp.getText();
                    Tip_C7 = true;
                }
                else if (jusercount == 10){//operator
                    UusefulOp += jcbtemp.getText();
                    Tip_C8 = true;
                }
                else if ( Tip_C7 == true && Tip_C8 == true){
                    if(wherecount < 1){
                        userFlag = true;
                        ret3 += "users.useful " + UusefulOp + " " + Uuseful + " ";
                    }else{
                        userFlag = true;
                        ret3 += "OR " + "users.useful " + UusefulOp + " " + Uuseful + " ";
                    }
                    String headerS = "useful";
                    header.add(headerS); //new JTABLE Integration
                }
                else if (jusercount == 11){//funny
                    Ufunny += jcbtemp.getText();
                    Tip_C9 = true;
                }
                else if (jusercount == 12){//operator
                    UfunnyOp += jcbtemp.getText();
                    Tip_C10 = true;
                }
                else if ( Tip_C9 == true && Tip_C10 == true){
                    if(wherecount < 1){
                        userFlag = true;
                        ret3 += "users.funny " + UfunnyOp + " " + Ufunny + " ";
                    }else{
                        userFlag = true;
                        ret3 += "OR " + "users.funny " + UfunnyOp + " " + Ufunny + " ";
                    }
                    String headerS = "funny";
                    header.add(headerS); //new JTABLE Integration
                }
                else if (jusercount == 13){//cool
                    Ucool += jcbtemp.getText();
                    Tip_C11 = true;
                }
                else if (jusercount == 14){//operator
                    UcoolOp += jcbtemp.getText();
                    Tip_C12 = true;
                }
                else if ( Tip_C11 == true && Tip_C12 == true){
                    if(wherecount < 1){
                        userFlag = true;
                        ret3 += "users.cool " + UcoolOp + " " + Ucool + " ";
                    }else{
                        userFlag = true;
                        ret3 += "OR " + "users.cool " + UcoolOp + " " + Ucool + " ";
                    }
                    String headerS = "cool";
                    header.add(headerS); //new JTABLE Integration
                }
                wherecount++;
                
            }
        }

        int jgeneralcount = 0;
        boolean generalFlag = false;

        String Gstars = "";
        String GstarsOp = "";
        String Greview = "";
        String GreviewOp = "";

        boolean Tip_D1 = false;  //reviews
        boolean Tip_D2 = false;  //reviews operator
        boolean Tip_D3 = false;  //stars
        boolean Tip_D4 = false;  //stars operator
        
        for(JTextField jcbtemp : generalFilter){
            jgeneralcount++; //counter for the arraylist
            if(jcbtemp.getText().equals("")){ //if equals empty string
                ret3 += "";
            }else{// if it doesn't equal to an empty string
                whereinit++;
                if(whereinit == 1){
                    ret3 += "WHERE ( ";
                }
                if(jgeneralcount == 1){ //business_id
                    if(wherecount < 1){
                        generalFlag = true;
                        ret3 += "business_general.business_id" + " = '" + jcbtemp.getText() + "' ";
                    }else{
                        generalFlag = true;
                        ret3 += "OR " + "business_general.business_id" + " = '" + jcbtemp.getText() + "' ";
                    }
                    String headerS = "business_id";
                    header.add(headerS); //new JTABLE Integration
                }
                else if (jgeneralcount == 2){//name
                    if(wherecount < 1){
                        generalFlag = true;
                        ret3 += "business_general.name" + " = '" + jcbtemp.getText() + "' ";
                    }else{
                        generalFlag = true;
                        ret3 += "OR " + "business_general.name" + " = '" + jcbtemp.getText() + "' ";
                    }
                    String headerS = "name";
                    header.add(headerS); //new JTABLE Integration
                }
                else if (jgeneralcount == 3){//address
                    if(wherecount < 1){
                        generalFlag = true;
                        ret3 += "business_general.address" + " = '" + jcbtemp.getText() + "' ";
                    }else{
                        generalFlag = true;
                        ret3 += "OR " + "business_general.address" + " = '" + jcbtemp.getText() + "' ";
                    }
                    String headerS = "address";
                    header.add(headerS); //new JTABLE Integration
                }
                else if (jgeneralcount == 4){//city
                    if(wherecount < 1){
                        generalFlag = true;
                        ret3 += "business_general.city" + " = '" + jcbtemp.getText() + "' ";
                    }else{
                        generalFlag = true;
                        ret3 += "OR " + "business_general.city" + " = '" + jcbtemp.getText() + "' ";
                    }
                    String headerS = "city";
                    header.add(headerS); //new JTABLE Integration
                }
                else if (jgeneralcount == 5){//state
                    if(wherecount < 1){
                        generalFlag = true;
                        ret3 += "business_general.state" + " = '" + jcbtemp.getText() + "' ";
                    }else{
                        generalFlag = true;
                        ret3 += "OR " + "business_general.state" + " = '" + jcbtemp.getText() + "' ";
                    }
                    String headerS = "state";
                    header.add(headerS); //new JTABLE Integration
                }
                else if (jgeneralcount == 6){//postal_code
                    if(wherecount < 1){
                        generalFlag = true;
                        ret3 += "business_general.postal_code" + " = '" + jcbtemp.getText() + "' ";
                    }else{
                        generalFlag = true;
                        ret3 += "OR " + "business_general.postal_code" + " = '" + jcbtemp.getText() + "' ";
                    }
                    String headerS = "postal_code";
                    header.add(headerS); //new JTABLE Integration
                }

                else if (jgeneralcount == 7){//stars
                    Gstars += jcbtemp.getText();
                    Tip_D1 = true;
                }
                else if (jgeneralcount == 8){//operator
                    GstarsOp += jcbtemp.getText();
                    Tip_D2 = true;
                }
                else if ( Tip_D1 == true && Tip_D2 == true){
                    if(wherecount < 1){
                        generalFlag = true;
                        ret3 += "business_general.stars " + GstarsOp + " " + Gstars + " ";
                    }else{
                        generalFlag = true;
                        ret3 += "OR " + "business_general.stars " + GstarsOp + " " + Gstars + " ";
                    }
                    String headerS = "stars";
                    header.add(headerS); //new JTABLE Integration
                }
                else if (jgeneralcount == 9){//review_count
                    Greview += jcbtemp.getText();
                    Tip_D3 = true;
                }
                else if (jgeneralcount == 10){//operator
                    GreviewOp += jcbtemp.getText();
                    Tip_D4 = true;
                }
                else if ( Tip_D3 == true && Tip_D4 == true){
                    if(wherecount < 1){
                        generalFlag = true;
                        ret3 += "business_general.review_count " + GreviewOp + " " + Greview + " ";
                    }else{
                        generalFlag = true;
                        ret3 += "OR " + "business_general.review_count " + GreviewOp + " " + Greview + " ";
                    }
                    String headerS = "review_count";
                    header.add(headerS); //new JTABLE Integration
                }
                wherecount++;

            }
        }
        
        // this is suppose to be last
        if(whereinit > 1){
            ret3 += ") ";
        }


        //----------------------------------------------------------------------------INNER JOIN FILTERS
                                                                                                        // for the second pannel
        //similar to the inner join above. use the onAL and do combinations based onthe onal.get(i)
        for(int i = 0; i < OnAL.size(); i++){
            if(OnAL.get(i) == "business_general"){
                if(ambienceFlag){
                    ret2 += "INNER JOIN business_ambience ON business_general.business_id = business_ambience.business_id ";
                }
                if(categoriesFlag){
                    ret2 += "INNER JOIN business_categories ON business_general.business_id = business_categories.business_id ";
                }
                if(parkingFlag){
                    ret2 += "INNER JOIN business_parking ON business_general.business_id = business_parking.business_id ";
                }
                if(miscFlag){
                    ret2 += "INNER JOIN business_misc ON business_general.business_id = business_misc.business_id ";
                };
                if(tipsFlag){
                    ret2 += "INNER JOIN tips ON business_general.business_id = tips.business_id ";
                };
                if(reviewFlag){
                    ret2 += "INNER JOIN reviews ON business_general.business_id = reviews.business_id ";
                }
            }
            /*else if(OnAL.get(i) == "users"){
                if(ambienceFlag){
                    ret1 += "INNER JOIN business_ambience ON users.business_id = business_ambience.business_id ";
                };
                if(categoriesFlag){
                    ret1 += "INNER JOIN business_categories ON users.business_id = business_categories.business_id ";
                };
                if(parkingFlag){
                    ret1 += "INNER JOIN business_parking ON users.business_id = business_parking.business_id ";
                };
            }*/
            else if(OnAL.get(i) == "reviews"){
                if(ambienceFlag){
                    ret2 += "INNER JOIN business_ambience ON reviews.business_id = business_ambience.business_id ";
                };
                if(categoriesFlag){
                    ret2 += "INNER JOIN business_categories ON reviews.business_id = business_categories.business_id ";
                };
                if(parkingFlag){
                    ret2 += "INNER JOIN business_parking ON reviews.business_id = business_parking.business_id ";
                };
                if(miscFlag){
                    ret2 += "INNER JOIN business_misc ON reviews.business_id = business_misc.business_id ";
                };
                if(tipsFlag){
                    ret2 += "INNER JOIN tips ON reviews.business_id = tips.business_id ";
                };
                if(userFlag){
                    ret2 += "INNER JOIN users ON reviews.user_id = users.user_id ";
                };
                if(generalFlag){
                    ret2 += "INNER JOIN business_general ON reviews.business_id = business_general.business_id ";
                };
            } 
            else if(OnAL.get(i) == "tips"){
                if(ambienceFlag){
                    ret2 += "INNER JOIN business_ambience ON tips.business_id = business_ambience.business_id ";
                };
                if(categoriesFlag){
                    ret2 += "INNER JOIN business_categories ON tips.business_id = business_categories.business_id ";
                };
                if(parkingFlag){
                    ret2 += "INNER JOIN business_parking ON tips.business_id = business_parking.business_id ";
                };
                if(miscFlag){
                    ret2 += "INNER JOIN business_misc ON tips.business_id = business_misc.business_id ";
                };
                if(reviewFlag){
                    ret2 += "INNER JOIN reviews ON tips.business_id = reviews.business_id ";
                };
                if(generalFlag){
                    ret2 += "INNER JOIN business_general ON tips.business_id = business_general.business_id ";
                };
            }
            
        }
        
        return ret1 + ret2 + ret3 + "LIMIT " + limitButton.getSelectedItem() + ";";
    }

}