package Day5;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public  class Libmanagement extends bookdetails{
	public static void authentication(PreparedStatement s) throws SQLException
	{
		 Scanner input = new Scanner(System.in);
		String name,pass;
		System.out.println("Employee name: "); 
		name=input.nextLine();
		System.out.println("Password: ");
		pass=input.nextLine();
		s.setString(1, name);
		s.setString(2, pass);
		 int row=s.executeUpdate();
	        if(row>0)
	        {
	            System.out.println("Employee Added");
	        }
	        s.close();
	}
	public static Boolean verifyuser(String user,String password,Statement state) throws SQLException
	{
		String query= "SELECT * FROM auth WHERE username='" + user + "' && password='" + password+ "' ";
        ResultSet rs = state.executeQuery(query);
        String databaseUsername ="";
        String  databasePassword="";
        while (rs.next()) {
            databaseUsername = rs.getString("username");
            databasePassword = rs.getString("password");
        }

        if (user.equals(databaseUsername) && password.equals(databasePassword)) {
            System.out.println("Successful Login!\n----");
            return true;
        } else {
            System.out.println("Incorrect Password\n----");
            return false;
        }
		
	}

	public static void main(String[] args) {
		 try {

	            String url = "jdbc:mysql://localhost:3306/employee";
	            String username = "root";
	            String password = "***********";
	            Connection connection = DriverManager.getConnection(url, username, password);
	            System.out.println("Connected to the database");
	            bookdetails bd=new bookdetails();
	            Scanner input = new Scanner(System.in);
	            int in;
	            System.out.println("Sign up or signin 1/0): ");
	            in=input.nextInt();
	            if(in==1) {
	            String sql="INSERT INTO auth (username, password) VALUES (?, ?)";
                PreparedStatement statement=connection.prepareStatement(sql);
	            authentication(statement);
	            statement.close();	            
	            }
	            else {
	            	 System.out.println("Name: ");
	                 String usernumber1;
	                 usernumber1=input.next();
	                 System.out.println("Password: ");
	                 String password1;
	                 password1=input.next();
	                 Statement state=connection.createStatement();
	                Boolean result=verifyuser(usernumber1,password1,state);
	                state.close();
	                if(result)
	                {
	                	bd.addbook();
	                }
	                else {
	                	System.out.println("Don't have access.");
	                }
	            }
	            
		 } catch(SQLException  e)
         {
             System.out.println("Oops, error");
             e.printStackTrace();
         }
		
	}

}

class userdetails extends bookdetails{
   public static void getdetails(Connection con) throws SQLException
  {
  Scanner scan=new Scanner(System.in);
  System.out.println("Enter user name: ");
  String Name=scan.nextLine();
  System.out.println("Enter user Age: ");
  int Age= scan.nextInt();
  System.out.println("Enter user Phone number: ");
  String Ph_no=scan.next();
  System.out.println("Enter the title of the book");
  String title;
  title=scan.next();
  int limit=0;
  String sql="INSERT INTO userdetail (username, age, ph_no, booktitle, status ) VALUES (?, ?, ?, ? ,?)";
  PreparedStatement statement=con.prepareStatement(sql);
  int needbook = 1;
while (needbook == 1) {
    try {	
    	  statement.setString(1,Name);
    	  statement.setInt(2,Age);
    	  statement.setString(3,Ph_no);
        getbooks(limit,statement,con,title);
        limit++;
        System.out.println("Are you need another books(1/0): ");
        needbook = scan.nextInt();
    }
    catch (InputMismatchException e)
    {
        System.out.println("\n No books available"+e);
        needbook=0;
    }
}
statement.close();

}
   public static void getbooks(int j,PreparedStatement statement,Connection con,String title) throws SQLException
 {
     Scanner scan=new Scanner(System.in);
     Connection connection1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/book_details", "root", "*********");
     int temp_stock=0;   
     if(j<=5 )
     {
    	 
         String q="select Stock from book where Title='"+title+"'";
        Statement sq=connection1.createStatement();
         ResultSet rt=sq.executeQuery(q);
         while(rt.next()) {
        	  temp_stock=rt.getInt("stock");
        	  System.out.println(temp_stock);
         }
    	 if(temp_stock>0)
    	 {
    	 statement.setString(4,title);
    	 statement.setString(5, "Issued");
    	  int row=statement.executeUpdate();
    	  if(row>0)
    	  {
    		  temp_stock--;
    		  String qy="update book set Stock='"+temp_stock+"' where Title='"+title+"'";
    		  Statement st=connection1.createStatement();
    		  int rst=st.executeUpdate(qy);
    		  System.out.println("Stock updated"); 
    	      System.out.println("Book added");
    	  }
         }
    	 else{
    	 System.out.println("Out of stock.");
    	 }
     }
     else{
         System.out.println("User reached maximum limit");
     }
     connection1.close();
 }
   public static void dispalyuserdetail(Connection conn) throws SQLException
 {
	   String sql="select * from userdetail";
	   Statement statement=conn.createStatement();
	   ResultSet rs=statement.executeQuery(sql);
	   while(rs.next()) {
             System.out.println("\nName: "+rs.getString("username")+"\nAge: "+rs.getInt("age")+"\nPhone number: "+rs.getString("ph_no")+"\nBook: "+rs.getString("booktitle")+ "\nStatus: "+ rs.getString("status"));
	   }
 }
	public void userbook() throws SQLException
	{
		Scanner input=new Scanner(System.in);
		String url = "jdbc:mysql://localhost:3306/user";
        String username = "root";
        String password = "Taehyung@BTS7";
        Connection connection = DriverManager.getConnection(url, username, password);
        System.out.println("Connected to the database");       
        int cont=1;
      while(cont==1) {
          //System.out.println("Getting Books...");
          getdetails(connection);
          System.out.println("What another user login? (1/0)");
          cont=input.nextInt();
          System.out.print(cont);
        }
          System.out.println("User's are: ");
          dispalyuserdetail(connection);
            
          }
	
}
 
class bookdetails{
	//ADD BOOK DETAILS
	public void dataentry(Connection conn) throws SQLException
	{
	        Scanner scan=new Scanner(System.in);
	        System.out.println("Book Title:");
	        String Title=scan.nextLine();
	        System.out.println("Author:");
	        String Author=scan.nextLine();
	        System.out.println("Book Category:");
	        String Category=scan.nextLine();
	        System.out.println("Publication Date as YYYY/MM/DD: ");
	        String regDate=scan.nextLine();
	        System.out.println("Rack Number:");
	        String Rack_no=scan.nextLine();
	        System.out.println("Book Availability:");
	        int stock=scan.nextInt();
	        Integer id=0;
	        Boolean res=false;
	        while(!res) {
	            System.out.println("Unique Id :");
	            id = scan.nextInt();
	            String queryCheck = "SELECT * from book WHERE book_id = '" + id + "'";
	            PreparedStatement st = conn.prepareStatement(queryCheck);
	            ResultSet rs = st.executeQuery();
	            int unique_id = 0;
	            while(rs.next())
	            {
	            	unique_id= rs.getInt("book_id");
	            }
	                if (unique_id!=id)
	                {
	                    String query = "INSERT INTO book (book_id, Title, Author, Category, Rach_no,Date, Stock) VALUES (?,?,?,?,?,?,?)";
	                    st=conn.prepareStatement(query);
	                    st.setLong(1, id);
	            		st.setString(2, Title);
	            		st.setString(3, Author);
	            		st.setString(4, Category);
	            		st.setString(5, Rack_no);
	            		st.setString(6, regDate);
	            		st.setInt(7, stock);
	            		 int row=st.executeUpdate();
	            	        if(row>0)
	            	        {
	            	            System.out.println("Book Added");
	            	        }
	            	        st.close();
	            	        res=true;
	                 }
	                else
	                {
	                	 System.out.println("ID shoud be unique");
	                    res = false;
	                }
	        }

	    }
//SEARCH BOOK
	public static void searchbook(Connection conn) throws ParseException, SQLException {
      try (Scanner new_in = new Scanner(System.in)) {
		System.out.println("Searching...");
		  int choice=0;
		  while(choice!=5) {
		      System.out.println("\n1. Title\n2. Author\n3. Subject_Category\n4. Publication date\n5. End Session");
		      String queri="select * from book ";
		      Statement stt=conn.createStatement();
		      ResultSet rss=stt.executeQuery(queri);
		      System.out.println("\n\nEnter the option: ");
		      choice=new_in.nextInt();
		      if(choice==1)
		      {
		          System.out.println("Enter Title  of the Book: ");
		          String title =new_in.nextLine();
		          String query="select * from book where Title= '"+title+"'";
		          Statement st=conn.createStatement();
		          ResultSet rs=st.executeQuery(query);
		          while(rs.next())
		          {
		        	  String booktitle=rs.getString("Title");
		        	  String Author=rs.getString("Author");
		        	  String Category=rs.getString("Category");
		        	  String date=rs.getString("Date");
		        	  int stock=rs.getInt("stock");
		        	  String rack_no=rs.getString("Rach_no");
		        	  System.out.println("\nBook Name: "+booktitle+"\nAuthor: "+Author+"\nBook Category: "+Category+"\nPublication Date: "+date+"\nAvailable stock: "+stock+"\nRack Number: "+rack_no);
		          }
		      }
		      else if(choice==2)
		      {
		          System.out.println("Enter Author  of the Book: ");
		          String author=new_in.next();
		          String query="select * from book where Author= '"+author+"'";
		          Statement st=conn.createStatement();
		          ResultSet rs=st.executeQuery(query);
		          while(rs.next())
		          {
		        	  String booktitle=rs.getString("Title");
		        	  String bookAuthor=rs.getString("Author");
		        	  String Category=rs.getString("Category");
		        	  String date=rs.getString("Date");
		        	  int stock=rs.getInt("stock");
		        	  String rack_no=rs.getString("Rach_no");
		        	  System.out.println("\nBook Name: "+booktitle+"\nAuthor: "+bookAuthor+"\nBook Category: "+Category+"\nPublication Date: "+date+"\nAvailable stock: "+stock+"\nRack Number: "+rack_no);}
		      }
		      else if(choice==3)
		      {
		          System.out.println("Enter Category  of the Book: ");
		          String category=new_in.next();
		          String query="select * from book where Category= '"+category+"'";
		          Statement st=conn.createStatement();
		          ResultSet rs=st.executeQuery(query);
		          while(rs.next())
		          {
		        	  String booktitle=rs.getString("Title");
		        	  String Author=rs.getString("Author");
		        	  String bookcategory=rs.getString("Category");
		        	  String date=rs.getString("Date");
		        	  int stock=rs.getInt("stock");
		        	  String rack_no=rs.getString("Rach_no");
		        	  System.out.println("\nBook Name: "+booktitle+"\nAuthor: "+Author+"\nBook Category: "+bookcategory+"\nPublication Date: "+date+"\nAvailable stock: "+stock+"\nRack Number: "+rack_no);
		          }
		      }
		      else if(choice==4)
		      {
		          System.out.println("Enter Publication Date  of the Book as(YYYY/MM/DD): ");
		          String dates=new_in.next();
		          String query="select * from book where Date= '"+dates+"'";
		          Statement st=conn.createStatement();
		          ResultSet rs=st.executeQuery(query);
		          while(rs.next())
		          {
		        	  String booktitle=rs.getString("Title");
		        	  String Author=rs.getString("Author");
		        	  String bookcategory=rs.getString("Category");
		        	  String date=rs.getString("Date");
		        	  int stock=rs.getInt("stock");
		        	  String rack_no=rs.getString("Rach_no");
		        	  System.out.println("\nBook Name: "+booktitle+"\nAuthor: "+Author+"\nBook Category: "+bookcategory+"\nPublication Date: "+date+"\nAvailable stock: "+stock+"\nRack Number: "+rack_no);
		          }
		      }
		      else if(choice==5)
		      {
		          System.out.println("Searching session over");
		      }
		      else{
		          System.out.println("Entered Wrong option");
		      }
  }
	}
  }

 public  void addbook() {
	try {
		Scanner scan=new Scanner(System.in);
		String url = "jdbc:mysql://localhost:3306/book_details";
        String username = "root";
        String password = "*********";
        Connection connection = DriverManager.getConnection(url, username, password);
        Connection connection1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/user", "root", "*********");
        //System.out.println("Connected to the database");
        userdetails ud=new userdetails();
        int option=0;
        while(option!=6)
        {
            System.out.println("1.Book Entry\n2.Search Books\n3.Get Books\n4.Book Return\n5.Book Reservation\n6.Display users\n7.End Session");
            option=scan.nextInt();
            if(option==1)
            {
                System.out.println("Entering Book Details");
                try {
                	bookdetails bd=new bookdetails();
                    bd.dataentry(connection);
                }
                catch (Exception e) {
                    System.out.println("Date mismatch "+e);
                }
            }           
            else if(option==2)
            {
                System.out.println("Searching book details");
                try {
                    searchbook(connection);
                }
                catch (Exception e)
                {
                    System.out.println("Date Mismatch "+e);
                }
            }
            else if(option==3)
            {
            	//userdetails ud=new userdetails();
            	ud.userbook();
            }
            else if(option==4)
            {
            	int temp_stock=0;
                System.out.println("Book Return");
                System.out.println("\nEnter the book to be return: ");
                String title=scan.next();
                System.out.println("\nEnter the name of the user: ");
                String name=scan.next();
                String q="select Stock from book where Title='"+title+"'";
                Statement sq=connection.createStatement();
                 ResultSet rt=sq.executeQuery(q);
                 while(rt.next()) {
               	  temp_stock=rt.getInt("stock");
               	  System.out.println(temp_stock);
                }
                 temp_stock+=1;
                 String qy="update book set Stock='"+temp_stock+"' where Title='"+title+"'";
                 String state="Returned";
                 String query="update userdetail set status='"+state+"' where username='"+name+"' and booktitle='"+title+"'";
       		     Statement st=connection.createStatement();
       		     Statement sst=connection1.createStatement();
       		     int rst=st.executeUpdate(qy);
       		     int rrt=sst.executeUpdate(query);
       		     System.out.println("Book Returned"); 
       		     
            }
            else if(option==5)
            {
                System.out.println("Book Reservation");
                String chain="y";
                while(chain.equals("y")) {
                System.out.println("Enter user name: ");
                String Name=scan.next();
                System.out.println("Enter user Age: ");
                int Age= scan.nextInt();
                System.out.println("Enter user Phone number: ");
                String Ph_no=scan.next();
                String title;
                System.out.println("Enter the title: ");
                title=scan.next();
                int limit=0;
                String sql="INSERT INTO userdetail (username, age, ph_no, booktitle, status ) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement statement=connection1.prepareStatement(sql);
                try {
                  statement.setString(1,Name);
              	  statement.setInt(2,Age);
              	  statement.setString(3,Ph_no);
                  ud.getbooks(limit,statement,connection1,title);
                  limit++;
                  String state="Reserved";
                  String query="update userdetail set status='"+state+"' where username='"+Name+"' and booktitle='"+title+"'";
        		  Statement sst=connection1.createStatement();
        		  int rrt=sst.executeUpdate(query);
        		  System.out.println("Book Reserved.");
                  System.out.println("Want to reserv more(y/n): ");
                  chain=scan.next();
                }
                catch(Exception e)
                {
                	System.out.println(e);
                }
                connection1.close();
                }
            }
            else if(option==6)
            {
                ud.dispalyuserdetail(connection1);
            }
            else if(option==7)
            {
                System.out.println("Session Ends");
                connection.close();
            }
            else if(option>6 || option<1)
            {
                System.out.println("Wrong Option Chose");
            }
        }
	}
	catch(SQLException e)
	{
		System.out.println(e);
	}
 }

}
