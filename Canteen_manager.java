
package canteen_manager;

import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.*;
import com.opencsv.CSVWriter; 
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.GreekList;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.RomanList;
import com.itextpdf.text.Section;
import com.itextpdf.text.ZapfDingbatsList;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import javax.swing.JOptionPane;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
public class Canteen_manager {
    
    private static final DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    
    
    public DefaultTableModel searchEmployeeData(String card)
    {
        //ADD COLUMNS TO TABLE MODEL
        DefaultTableModel dm=new DefaultTableModel();
        dm.addColumn("CARD");
        dm.addColumn("NAME");
        dm.addColumn("SURNAME");
        dm.addColumn("DEPARTMENT");       
        
        //SQL STATEMENT
        String sql="SELECT * FROM employees where card='"+card+"'";
        
        try{
        
            //Connection con=DriverManager.getConnection(conString,username,password);
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/canteen_database","root","");
            // prepared statement
            Statement s=con.prepareStatement(sql);
            ResultSet rs=s.executeQuery(sql);
            
            //loop through getting all values
            while(rs.next())
            {
                //get values
                String card1 = rs.getString(1);
                String name = rs.getString(2);
                String surname = rs.getString(3);
                String department = rs.getString(4);
                
                dm.addRow(new String[]{card1,name,surname,department});
            }
            return dm;
            
            
        }catch(Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,ex.toString());
        }
        return null;
    }
 
    
    
    public Boolean updateEmployee(String card,String first_name, String surname, String department) throws ClassNotFoundException
    {
        String sql="UPDATE employees SET first_name='"+first_name+"',surname='"+surname+"',department='"+department+"' WHERE card='"+card+"'";
        
        try
        {
            //get connection
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/canteen_database","root","");
            
            //statement
            Statement s=con.prepareStatement(sql);
            //execute
            s.execute(sql);
            
            return true;
            
        }catch(SQLException ex){
            ex.printStackTrace();
            return false;
        }
    
    }
    
    public Boolean deleteEmployee(String card)
    {
        String sql="DELETE FROM employees WHERE card='"+card+"'";
        
        try
        {
            //get connection
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/canteen_database","root","");
            
            //statement
            Statement s=con.prepareStatement(sql);
            
            //execute
            s.execute(sql);
            
            return true;
        }catch(Exception ex)
        {
            ex.printStackTrace();
            return false;
            
            
        }
    }
    
     public DefaultTableModel getEmployeeData()
    {
        //ADD COLUMNS TO TABLE MODEL
        DefaultTableModel dm=new DefaultTableModel();
        dm.addColumn("CARD");
        dm.addColumn("NAME");
        dm.addColumn("SURNAME");
        dm.addColumn("DEPARTMENT");       
        
        //SQL STATEMENT
        String sql="SELECT * FROM employees";
        
        try{
        
            //Connection con=DriverManager.getConnection(conString,username,password);
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/canteen_database","root","");
            // prepared statement
            Statement s=con.prepareStatement(sql);
            ResultSet rs=s.executeQuery(sql);
            
            //loop through getting all values
            while(rs.next())
            {
                //get values
                String card = rs.getString(1);
                String name = rs.getString(2);
                String surname = rs.getString(3);
                String department = rs.getString(4);
                
                dm.addRow(new String[]{card,name,surname,department});
            }
            return dm;
            
            
        }catch(Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,ex.toString());
        }
        return null;
    }
    
        //insert into stock_in
      public Boolean addEmployee(String card,String first_name, String surname, String department)
    {
        //SQL STATEMENT
        
        
        try
        {
            
            //GET CONNECTION
            String sql= "INSERT INTO employees(card,first_name,surname,department) VALUES('"+card+"','"+first_name+"','"+surname+"','"+department+"')";
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/canteen_database","root","");
            
            //Prepaid statement
            Statement s=con.prepareStatement(sql);
            s.execute(sql);
            return true;
            
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return false;
    }
    

    
        public void generateOveralReport() throws IOException
        {
                 // Create a HashMap object called capitalCities
        //<key><object>
    
    HashMap<Integer, String> getCards = new HashMap<Integer, String>();
    HashMap<Integer, String> getFirstName = new HashMap<Integer, String>();
    HashMap<Integer, String> getSurname = new HashMap<Integer, String>();
    HashMap<Integer, String> getDepartment = new HashMap<Integer, String>();
    int num = 1;
    try
    {
            
            String sql = "SELECT * FROM employees";
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/canteen_database","root","");
            // prepared statement
            Statement s=con.prepareStatement(sql);
            ResultSet rs=s.executeQuery(sql);
            
            //loop through getting all values
            while(rs.next())
            {
                //get values
                String card = rs.getString(1);
                String first_name = rs.getString(2);
                String surname = rs.getString(3);
                String department = rs.getString(4);
                
                getCards.put(num , card );
                getFirstName.put(num, first_name );
                getSurname.put(num, surname );
                getDepartment.put(num, department );
                
                num++;
                
            }
            System.out.println("Done populating hash map....");
        
    }catch(Exception e)
    {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null,e.toString());
    }
    
    
//    int card_num = 1;
//    
//    System.out.println(getCards.get(card_num));
//    System.out.println(getCards.size());
//    System.out.println("Printing all cards using hash map....");
    
    // first create file object for file placed at location 
        // specified by filepath 
        String filePath = "C:\\reports\\OveralReport.csv"; 
        File file = new File(filePath);
        
        
        // create FileWriter object with file as parameter 
        FileWriter outputfile = new FileWriter(file); 
  
        // create CSVWriter object filewriter object as parameter 
        CSVWriter writer = new CSVWriter(outputfile); 
  
        // adding header to csv 
        String[] first_header = { "CARD","NAME","SURNAME","DEPARTMENT", "WESTERN DISHES", "TRADITIONAL DISHES"};
        writer.writeNext(first_header);
        
    
    for(int i = 1; i<=getCards.size(); i++)
    {
        //System.out.println("Position: "+i+" Card Number: "+getCards.get(i));
        
        try
        {
            //String sql = "SELECT COUNT(*) FROM reports2 where card='"+getCards.get(i)+"'";
            String sql = "SELECT COUNT(*) FROM reports2 where card='"+getCards.get(i)+"' and dish_type='WESTERN DISH' and authorisation='Access granted'";
            String sql1 = "SELECT COUNT(*) FROM reports2 where card='"+getCards.get(i)+"' and dish_type='TRADITIONAL DISH' and authorisation='Access granted'";
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/canteen_database","root","");
            // prepared statement
            Statement s=con.prepareStatement(sql);
            Statement s1=con.prepareStatement(sql1);
            
            ResultSet rs=s.executeQuery(sql);
            ResultSet rs1=s1.executeQuery(sql1);
            
            //loop through getting all values
            while(rs.next() && rs1.next())
            {
                // going to edit it to return name and other details staff
                int count = rs.getInt(1);
                int counttd = rs1.getInt(1);
                
                //System.out.println("Card: "+getCards.get(i)+" Western dish Count: "+count);
                //System.out.println("Writing to .csv file");
                
                String[] data = { getCards.get(i),getFirstName.get(i),getSurname.get(i),getDepartment.get(i), String.valueOf(count),String.valueOf(counttd) };
                writer.writeNext(data);
                
            }
            
            //writer.close(); 
        
        }catch(Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,e.toString());
        
        }
        
    }
    
        writer.close(); 
    
    
        }
    
    
         public void sendAttach(String to,String subject,String mymessage)
     {
        // Recipient's email ID needs to be mentioned.
        //to = "learnmoremukwena@outlook.com";
        // Sender's email ID needs to be mentioned
        ///old mutual
        //canteenmanagementoldmutual@gmail.com
        String from = "canteenmanagementoldmutual@gmail.com";
        final String username = "canteenmanagementoldmutual@gmail.com";//change accordingly
        final String password = "lt22111996";//change accordingly
        // Assuming you are sending email through relay.jangosmtp.net
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        // Get the Session object.
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));
            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            // Set Subject: header field
            message.setSubject(subject);
            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();
            // Now set the actual message
            messageBodyPart.setText(mymessage);
            // Create a multipar message
            Multipart multipart = new MimeMultipart();
            // Set text message part
            multipart.addBodyPart(messageBodyPart);
            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            String filename = "C:\\reports\\report_file.csv";
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);
            // Send the complete message parts
            message.setContent(multipart);
            // Send message
            Transport.send(message);
            System.out.println("Email Sent Successfully !!");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
            //JOptionPane.showMessageDialog(null,e.toString());
        }
                    

     }
    
       public DefaultTableModel getPricesData()
    {
        //ADD COLUMNS TO TABLE MODEL
        DefaultTableModel dm=new DefaultTableModel();
        dm.addColumn("Dish");
        dm.addColumn("Prices");
        
        //SQL STATEMENT
        String sql="SELECT * FROM dish_prices";
        
        try{
        
            //Connection con=DriverManager.getConnection(conString,username,password);
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/canteen_database","root","");
            // prepared statement
            Statement s=con.prepareStatement(sql);
            ResultSet rs=s.executeQuery(sql);
            
            //loop through getting all values
            while(rs.next())
            {
                //get values
                String dish = rs.getString(1);
                String price = rs.getString(2);
                
                dm.addRow(new String[]{dish,price});
            }
            return dm;
            
            
        }catch(Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,ex.toString());
        }
        return null;
    }
    public String getDate()
    {
        try
        {
            LocalDate localDate = LocalDate.now();
            //System.out.println(DateTimeFormatter.ofPattern("yyyMMdd").format(localDate));
            String datee = DateTimeFormatter.ofPattern("yyyMMdd").format(localDate);
            return datee;
        }catch(Exception ex)
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,ex.toString());
        }
        return null;
    }
    
     public String getCurrentDate()
    {
        try
        {
            Date date = new Date();
            return (sdf.format(date));
            
        }catch(Exception ex)
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,ex.toString());
        }
        return null;
    }
    
    public DefaultTableModel getReportsData()
    {
        //ADD COLUMNS TO TABLE MODEL
        DefaultTableModel dm=new DefaultTableModel();
        dm.addColumn("Time");
        dm.addColumn("Card");
        dm.addColumn("Dish");
        dm.addColumn("Authorisation");
        dm.addColumn("Name");
        dm.addColumn("Surname");
        dm.addColumn("Department");
        
        
        //SQL STATEMENT
        String sql="SELECT * FROM reports2";
        
        try{
        
            //Connection con=DriverManager.getConnection(conString,username,password);
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/canteen_database","root","");
            // prepared statement
            Statement s=con.prepareStatement(sql);
            ResultSet rs=s.executeQuery(sql);
            
            //loop through getting all values
            while(rs.next())
            {
                //get values
                String time = rs.getString(1);
                String dish = rs.getString(2);
                String authorisation = rs.getString(3);
                String card = rs.getString(4);
                String surname = rs.getString(5);
                String name = rs.getString(6);
                String department = rs.getString(7);
                
                dm.addRow(new String[]{time,card,dish,authorisation,name,surname,department});
            }
            return dm;
            
            
        }catch(Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,ex.toString());
        }
        return null;
    }
    
      public String findTotal(String name, String surname, String card)
     {
         String total = "Nan";
        
        try{
        
            //Connection con=DriverManager.getConnection(conString,username,password);
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/canteen_database","root","");
            // prepared statement
            String sql = "select count(*) from reports2 where first_name='"+name+"' and surname='"+surname+"' and card='"+card+"'";
            Statement s=con.prepareStatement(sql);
            ResultSet rs=s.executeQuery(sql);
            
            //loop through getting all values
            
            while(rs.next())
            {
                //get values
                total = rs.getString(1);
                
            }
            return total;
            
            
        }catch(Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,ex.toString());
        }
        return total;
         
         
     }
      
    public DefaultTableModel getSearchData(String newtime,String newcard,String newauthorisation,String newname,String newsurname,String newdepartment,String newdish)
    {
        //ADD COLUMNS TO TABLE MODEL
        DefaultTableModel dm=new DefaultTableModel();
        dm.addColumn("Time");
        dm.addColumn("Card");
        dm.addColumn("Department");
        dm.addColumn("Dish");
        dm.addColumn("Authorisation");
        dm.addColumn("Name");
        dm.addColumn("Surname");
        
        try{
             
               
                dm.addRow(new String[]{newtime,newcard,newdish,newauthorisation,newname,newsurname,newdepartment});
           
            return dm;
        }catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, e.toString());
        }
        return null;
    }
    
    public void addImage(String card,String authorisation,String name,String surname,String department)
    {
        Document document = new Document();
    try
    {
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:\\reports\\CanteenAddImage.pdf"));
        document.open();
        Paragraph title_sec1 = new Paragraph("Old Mutual Canteeen");
        Paragraph title_sec2 = new Paragraph("Mutual Gardens");
        Paragraph title_sec3 = new Paragraph("100 The Chase West");
        Paragraph title_sec4 = new Paragraph("Emerald Hill");
        Paragraph title_sec5 = new Paragraph("Harare");
        Paragraph title_sec6 = new Paragraph("");
        title_sec1.setAlignment(Element.ALIGN_RIGHT);
        document.add(title_sec1);
        title_sec2.setAlignment(Element.ALIGN_RIGHT);
        document.add(title_sec2);
        title_sec3.setAlignment(Element.ALIGN_RIGHT);
        document.add(title_sec3);
        title_sec4.setAlignment(Element.ALIGN_RIGHT);
        document.add(title_sec4);
        title_sec5.setAlignment(Element.ALIGN_RIGHT);
        document.add(title_sec5);
        title_sec6.setAlignment(Element.ALIGN_RIGHT);
        document.add(title_sec6);
        
        document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------"));
        
        Paragraph owner_name = new Paragraph("EMPLOYEE NAME: "+name+" "+surname);
        owner_name.setAlignment(Element.ALIGN_LEFT);
        document.add(owner_name);
        
        Paragraph owner_card = new Paragraph("CARD NUMBER: "+card);
        owner_card.setAlignment(Element.ALIGN_LEFT);
        document.add(owner_card);
        
        Paragraph owner_dept = new Paragraph("DEPARTMENT: "+department);
        owner_dept.setAlignment(Element.ALIGN_LEFT);
        document.add(owner_dept);
        
        Paragraph report_id = new Paragraph("REPORT NUMBER: "+new Canteen_manager().getDate()+name);
        owner_dept.setAlignment(Element.ALIGN_LEFT);
        document.add(report_id);
        //getCurrentDate()
        
        Paragraph dategenerated = new Paragraph("DATE GENERATED: "+new Canteen_manager().getCurrentDate());
        owner_dept.setAlignment(Element.ALIGN_LEFT);
        document.add(dategenerated);
        
        
        
        
        
            PdfPTable table = new PdfPTable(3); // 3 columns.
            table.setWidthPercentage(100); //Width 100%
            table.setSpacingBefore(10f); //Space before table
            table.setSpacingAfter(10f); //Space after table
 
        //Set Column widths
            float[] columnWidths = {1f, 1f, 1f};
            table.setWidths(columnWidths);
 
            PdfPCell cell1 = new PdfPCell(new Paragraph("DATE"));
            cell1.setBorderColor(BaseColor.BLUE);
            cell1.setPaddingLeft(10);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
 
            PdfPCell cell2 = new PdfPCell(new Paragraph("DISH"));
            cell2.setBorderColor(BaseColor.GREEN);
            cell2.setPaddingLeft(10);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
 
            PdfPCell cell3 = new PdfPCell(new Paragraph("COST"));
            cell3.setBorderColor(BaseColor.RED);
            cell3.setPaddingLeft(10);
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
            
            
            
            
 
        //To avoid having the cell border and the content overlap, if you are having thick cell borders
        //cell1.setUserBorderPadding(true);
        //cell2.setUserBorderPadding(true);
        //cell3.setUserBorderPadding(true);
 
            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);
            
//            String sql="SELECT dtime,dish FROM reports1";
            String sql = "SELECT time_date,dish_type FROM reports2 where card='"+card+"' and authorisation='Access granted' and first_name='"+name+"' and surname='"+surname+"' and depertment='"+department+"'";
        
        try{
        
            //Connection con=DriverManager.getConnection(conString,username,password);
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/canteen_database","root","");
            // prepared statement
            Statement s=con.prepareStatement(sql);
            ResultSet rs=s.executeQuery(sql);
            
            //loop through getting all values
            while(rs.next())
            {
                //get values
                String time = rs.getString(1);
                String dish = rs.getString(2);
                
                    PdfPCell cell10 = new PdfPCell(new Paragraph(time));
                    cell1.setBorderColor(BaseColor.BLUE);
                    cell1.setPaddingLeft(10);
                    cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

                    PdfPCell cell11 = new PdfPCell(new Paragraph(dish));
                    cell2.setBorderColor(BaseColor.GREEN);
                    cell2.setPaddingLeft(10);
                    cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    
                    if(dish.equals("WESTERN DISH"))
                    {
                        Double western_cost = new Canteen_manager().getWesternPrice(dish);
                        PdfPCell cell12 = new PdfPCell(new Paragraph(western_cost.toString()));
                        cell3.setBorderColor(BaseColor.RED);
                        cell3.setPaddingLeft(10);
                        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        
                        table.addCell(cell10);
                        table.addCell(cell11);
                        table.addCell(cell12);
                    }
                    else if(dish.equals("TRADITIONAL DISH"))
                    {
                        Double traditional_cost = new Canteen_manager().getWesternPrice(dish);
                        PdfPCell cell12 = new PdfPCell(new Paragraph(traditional_cost.toString()));
                        cell3.setBorderColor(BaseColor.RED);
                        cell3.setPaddingLeft(10);
                        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        
                        table.addCell(cell10);
                        table.addCell(cell11);
                        table.addCell(cell12);
                    }
                    else
                    {
                        PdfPCell cell12 = new PdfPCell(new Paragraph("NOT SPECIFIED"));
                        cell3.setBorderColor(BaseColor.RED);
                        cell3.setPaddingLeft(10);
                        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        
                        table.addCell(cell10);
                        table.addCell(cell11);
                        table.addCell(cell12);
                    }

//                    PdfPCell cell12 = new PdfPCell(new Paragraph("$10.00"));
//                    cell3.setBorderColor(BaseColor.RED);
//                    cell3.setPaddingLeft(10);
//                    cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
//                    cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                    
//                    table.addCell(cell10);
//                    table.addCell(cell11);
//                    table.addCell(cell12);
                
            }
            
        }catch(Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,ex.toString());
        }
            
            
            
            
            
            
            
            
            
        // looping for auto adding cells 
//            for (int i = 0; i < 5; i++) {
//                    
//                    PdfPCell cell10 = new PdfPCell(new Paragraph("12.23.23"));
//                    cell1.setBorderColor(BaseColor.BLUE);
//                    cell1.setPaddingLeft(10);
//                    cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
//                    cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
//
//                    PdfPCell cell11 = new PdfPCell(new Paragraph("WESTERN"));
//                    cell2.setBorderColor(BaseColor.GREEN);
//                    cell2.setPaddingLeft(10);
//                    cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
//                    cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
//
//                    PdfPCell cell12 = new PdfPCell(new Paragraph("$10.00"));
//                    cell3.setBorderColor(BaseColor.RED);
//                    cell3.setPaddingLeft(10);
//                    cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
//                    cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                    
//                    table.addCell(cell10);
//                    table.addCell(cell11);
//                    table.addCell(cell12);
//                
//                
//            }
                                                                                                                                                                                                                 
            String sql1="SELECT count(*) FROM reports2 where card='"+card+"' and authorisation='Access granted' and first_name='"+name+"' and surname='"+surname+"' and depertment='"+department+"' and dish_type='WESTERN DISH'";
            String sql2="SELECT count(*) FROM reports2 where card='"+card+"' and authorisation='Access granted' and first_name='"+name+"' and surname='"+surname+"' and depertment='"+department+"' and dish_type='TRADITIONAL DISH'";
        
        try{
            
            int count_western=0;
            int count_traditional=0;
        
            //Connection con=DriverManager.getConnection(conString,username,password);
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/canteen_database","root","");
            // prepared statement
            Statement s1=con.prepareStatement(sql1);
            Statement s2=con.prepareStatement(sql2);
            
            ResultSet rs1=s1.executeQuery(sql1);
            ResultSet rs2=s2.executeQuery(sql2);
            
            //loop through getting all values
            while(rs1.next())
            {
                //get values
                 count_western = rs1.getInt(1);
            }
            
            while(rs2.next())
            {
                //get values
                count_traditional = rs2.getInt(1);
            }
            String western="WESTERN DISH";
            String traditional="TRADITIONAL DISH";
            Double total=new Canteen_manager().getWesternPrice(western)*count_western + new Canteen_manager().getWesternPrice(traditional)*count_traditional;
            
            PdfPCell cell7 = new PdfPCell(new Paragraph("TOTAL COST"));
            cell1.setBorderColor(BaseColor.BLUE);
            cell1.setPaddingLeft(10);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
 
            PdfPCell cell8 = new PdfPCell(new Paragraph(""));
            cell2.setBorderColor(BaseColor.GREEN);
            cell2.setPaddingLeft(10);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
 
            PdfPCell cell9 = new PdfPCell(new Paragraph(total.toString()));
            cell3.setBorderColor(BaseColor.RED);
            cell3.setPaddingLeft(10);
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
            
            
             PdfPTable table1 = new PdfPTable(2); // 3 columns.
            table.setWidthPercentage(100); //Width 100%
            table.setSpacingBefore(10f); //Space before table
            table.setSpacingAfter(10f); //Space after table
 
        //Set Column widths
            float[] columnWidths1 = {1f, 1f};
            table1.setWidths(columnWidths1);
            
            PdfPCell cell10 = new PdfPCell(new Paragraph("NUMBER OF WESTERN DISHES"));
            cell1.setBorderColor(BaseColor.BLUE);
            cell1.setPaddingLeft(10);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
 
            String wst = Integer.toString(count_western);
            String trd = Integer.toString(count_traditional);
            
            PdfPCell cell11 = new PdfPCell(new Paragraph(wst));
            cell2.setBorderColor(BaseColor.GREEN);
            cell2.setPaddingLeft(10);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
 
            
            
            PdfPCell cell13 = new PdfPCell(new Paragraph("NUMBER OF TRADITIONAL DISHES"));
            cell1.setBorderColor(BaseColor.BLUE);
            cell1.setPaddingLeft(10);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

 
            PdfPCell cell15 = new PdfPCell(new Paragraph(trd));
            cell3.setBorderColor(BaseColor.RED);
            cell3.setPaddingLeft(10);
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
            
            table.addCell(cell7);
            table.addCell(cell8);
            table.addCell(cell9);
            table1.addCell(cell10);
            table1.addCell(cell11);
            table1.addCell(cell13);
            table1.addCell(cell15);
            
            
            document.add(table);
            document.add(table1);
            
        }catch(Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,ex.toString());
        }
        
        
        
        
        
        
        
        
        
//            PdfPCell cell7 = new PdfPCell(new Paragraph("TOTAL COST"));
//            cell1.setBorderColor(BaseColor.BLUE);
//            cell1.setPaddingLeft(10);
//            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
//            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
// 
//            PdfPCell cell8 = new PdfPCell(new Paragraph(""));
//            cell2.setBorderColor(BaseColor.GREEN);
//            cell2.setPaddingLeft(10);
//            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
//            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
// 
//            PdfPCell cell9 = new PdfPCell(new Paragraph("$300.00"));
//            cell3.setBorderColor(BaseColor.RED);
//            cell3.setPaddingLeft(10);
//            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
//            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            
//            table.addCell(cell7);
//            table.addCell(cell8);
//            table.addCell(cell9);
//            
//            document.add(table);
 
    //Add Image "C:\reports\temp1.jpg"
        Image image1 = Image.getInstance("C:\\reports\\temp1.jpg");
        image1.scaleAbsolute(50, 50);
        image1.setAbsolutePosition(100f, 700f);
    //Fixed Positioning
    //    image1.setAbsolutePosition(100f, 550f);
    //Scale to new height and new width of image
    //    image1.scaleAbsolute(200, 200);
    //Add to document
        
        document.add(image1);
// 
//        String imageUrl = "http://www.eclipse.org/xtend/images/java8_logo.png";
//        Image image2 = Image.getInstance(new URL(imageUrl));
//        document.add(image2);
// 
        document.close();
        writer.close();
    } catch (Exception e)
    {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null,e.toString());
    }
    
    }
    
    public double getWesternPrice(String dish)
    {
         String sql="SELECT price FROM dish_prices where dish_name='"+dish+"'";
        
        try{
            
            double price = 0.0;
        
            //Connection con=DriverManager.getConnection(conString,username,password);
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/canteen_database","root","");
            // prepared statement
            Statement s=con.prepareStatement(sql);
            ResultSet rs=s.executeQuery(sql);
            
            //loop through getting all values
            while(rs.next())
            {
                //get values
                String getprice = rs.getString(1);
                price=Double.parseDouble(getprice);

            }
             
            return price;
            
            
        }catch(Exception ex){
            ex.printStackTrace();
            ex.toString();
        }
        return 0.0;
    }
    
    //update the prices
     public Boolean updatePrice(double price, String dish) throws ClassNotFoundException
    {
        String sql="UPDATE  dish_prices SET price='"+price+"' WHERE dish_name='"+dish+"'";
        
        try
        {
            //get connection
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/canteen_database","root","");
            
            //statement
            Statement s=con.prepareStatement(sql);
            //execute
            s.execute(sql);
            
            return true;
            
        }catch(SQLException ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,ex.toString());
            return false;
        }
    
    }
    
     public String countDish(String dish,String authorisation)
    {
         String sql="SELECT count(*) FROM reports2 where dish_type='"+dish+"' or authorisation='"+authorisation+"'";
        
        try{
            
            String count = null;
        
            //Connection con=DriverManager.getConnection(conString,username,password);
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/canteen_database","root","");
            // prepared statement
            Statement s=con.prepareStatement(sql);
            ResultSet rs=s.executeQuery(sql);
            
            //loop through getting all values
            while(rs.next())
            {
                //get values
                count = rs.getString(1);

            }
             
            return count;
            
            
        }catch(Exception ex){
            ex.printStackTrace();
            ex.toString();
        }
        return null;
    }
     
    public Boolean truncDb()
    {
     
        try {
             
             
             
              Class.forName("com.mysql.jdbc.Driver");
              Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/canteen_database","root","");
 
              Statement statement = connection.createStatement();

              statement.executeUpdate("TRUNCATE reports2");


              //System.out.println("Successfully truncated test_table");
              return true;

        } catch (Exception e) {

                  System.out.println("Could not truncate test_table " + e.getMessage());
                  JOptionPane.showMessageDialog(null,e.toString());
                  
                  return false;
        }
    }
    
    public void writeDataLineByLine(String filePath,String card,String name,String surname,String department) 
{ 
        // first create file object for file placed at location 
        // specified by filepath 
        //filePath = "C:\\reports\\"+name+""+surname+".csv";
        File file = new File(filePath); 
        try { 
        // create FileWriter object with file as parameter 
        FileWriter outputfile = new FileWriter(file); 
  
        // create CSVWriter object filewriter object as parameter 
        CSVWriter writer = new CSVWriter(outputfile); 
  
        // adding header to csv 
        String[] first_header = { "Old Mutual Canteen Report", "", "" };
        writer.writeNext(first_header);
        
        String[] name_header = { "NAME: " ,name, surname};
        writer.writeNext(name_header);
        
        String[] card_header = { "CARD NUMBER: ", card };
        writer.writeNext(card_header);
        
        String[] deprt_header = { "DEPARTMENT :",department};
        writer.writeNext(deprt_header);
        
        String[] report_header = { "REPORT NUMBER :",new Canteen_manager().getDate()+name};
        writer.writeNext(report_header);
        
        String[] date_header = { "DATE :",new Canteen_manager().getCurrentDate()};
        writer.writeNext(date_header);
         
        String[] data_header = { "DATE","DISH","COST"};
        writer.writeNext(data_header);
//        // add data to csv 
//        String[] data1 = { "Aman", "10", "620" }; 
//        writer.writeNext(data1); 
//        String[] data2 = { "Suraj", "10", "630" }; 
//        writer.writeNext(data2); 
        
         try{
             
             String sql = "SELECT time_date,dish_type FROM reports2 where card='"+card+"' and authorisation='Access granted' and first_name='"+name+"' and surname='"+surname+"' and depertment='"+department+"'";
            //Connection con=DriverManager.getConnection(conString,username,password);
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/canteen_database","root","");
            // prepared statement
            Statement s=con.prepareStatement(sql);
            ResultSet rs=s.executeQuery(sql);
            
            //loop through getting all values
            while(rs.next())
            {
                //get values
                String time = rs.getString(1);
                String dish = rs.getString(2);
                
                
                if(dish.equals("WESTERN DISH"))
                    {
                        Double western_cost = new Canteen_manager().getWesternPrice(dish);
                        //western_cost.toString();
                        
                        String[] data3 = {time , dish, western_cost.toString() }; 
                        writer.writeNext(data3);
                        
                    }
                    else if(dish.equals("TRADITIONAL DISH"))
                    {
                        Double traditional_cost = new Canteen_manager().getWesternPrice(dish);
                        traditional_cost.toString();
                        
                        String[] data3 = {time , dish, traditional_cost.toString() }; 
                        writer.writeNext(data3);
                
                    }
                    else
                    {
                        
                        String[] data3 = {time , dish, "NOT SPECIFIED" }; 
                        writer.writeNext(data3);
                        
                    }

                
            }
            
        }catch(Exception ex){
            ex.printStackTrace();
            
        }
         
         
            String sql1="SELECT count(*) FROM reports2 where card='"+card+"' and authorisation='Access granted' and first_name='"+name+"' and surname='"+surname+"' and depertment='"+department+"' and dish_type='WESTERN DISH'";
            String sql2="SELECT count(*) FROM reports2 where card='"+card+"' and authorisation='Access granted' and first_name='"+name+"' and surname='"+surname+"' and depertment='"+department+"' and dish_type='TRADITIONAL DISH'";
            
            try{
            
            int count_western=0;
            int count_traditional=0;
        
            //Connection con=DriverManager.getConnection(conString,username,password);
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/canteen_database","root","");
            // prepared statement
            Statement s1=con.prepareStatement(sql1);
            Statement s2=con.prepareStatement(sql2);
            
            ResultSet rs1=s1.executeQuery(sql1);
            ResultSet rs2=s2.executeQuery(sql2);
            
            //loop through getting all values
            while(rs1.next())
            {
                //get values
                 count_western = rs1.getInt(1);
            }
            
            while(rs2.next())
            {
                //get values
                count_traditional = rs2.getInt(1);
            }
            String western="WESTERN DISH";
            String traditional="TRADITIONAL DISH";
            
            Double total=new Canteen_manager().getWesternPrice(western)*count_western + new Canteen_manager().getWesternPrice(traditional)*count_traditional;
            
            
             String[] data4 = {"TOTAL COST","", total.toString() }; 
             writer.writeNext(data4);
             
            String wst = Integer.toString(count_western);
            String trd = Integer.toString(count_traditional);
            
             String[] data5 = {"NUMBER OF WESTERN DISHES",wst }; 
             writer.writeNext(data5);
             
             String[] data6 = {"NUMBER OF TRADITIONAL DISHES",trd }; 
             writer.writeNext(data6);  
            
        }catch(Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,ex.toString());
        }
  
        // closing writer connection 
        writer.close(); 
    } 
    catch (IOException e) { 
        // TODO Auto-generated catch block 
        e.printStackTrace(); 
    } 
} 
    
    

    public static void main(String[] args) {
        reports rp = new reports();
        rp.setTitle("Canteen Management System");
        rp.setVisible(true);
        
    }   

//    void writeDataLineByLine(String filePath, String card, String authorisation, String name, String surname, String department) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

   
}
