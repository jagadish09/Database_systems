package dbmsproject;

import java.sql.*;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.File;

import javax.sql.*;
public class dbjdbctest {
	static String dbDriver1;
	static String url;
	static String username;
	static String password;
	static String s;
	
	
	 public static void setDriverName (String driver) { dbjdbctest.dbDriver1 = driver; }
	  public static void setConnectionUrl (String url) { dbjdbctest.url = url; }
	  public static void setDbUser (String user) { dbjdbctest.username = user; }
	  public static void setDbPassword (String password) { dbjdbctest.password = password; }
	
	public static void main(String args[]) throws Exception
	{
		setDriverName("oracle.jdbc.driver.OracleDriver");
		setConnectionUrl("jdbc:oracle:thin:@localhost:1521:orcl");
		setDbUser("hr");
		setDbPassword("orcl");
		
		boolean b;
		boolean c;


		File file = new File ("C:/Users/jbapanap/Downloads/Dbmsproject/Dbmsproject/Module2/dbms.txt");
		file.createNewFile();
		FileWriter filewriter = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(filewriter);
		String a3=new String("                                                                                                                                                                                                     ");
		String[] a2 = new String[200];
		String a1;
		Connection myconn=null;
		String a=null;
		ResultSet rs = null,rs1=null,rs2=null,rs3=null,rs4=null,rs5=null,rs6=null,rs7=null,rs8=null;
		PreparedStatement stmt1 = null,stmt2=null,stmt3=null,stmt4=null,stmt5=null,stmt6=null,stmt7=null,stmt8=null;
		try {
			 
			Class.forName(dbjdbctest.dbDriver1);
 
		} catch (ClassNotFoundException e) {
 
			System.out.println(e);
			return;
 
		}

		try
		{
			myconn=DriverManager.getConnection(url,username,password);
			DatabaseMetaData meta = myconn.getMetaData();
			System.out.println("JDBC driver version is " + meta.getDriverVersion());
			System.out.println("connection successful");
			System.out.println("UNIT      OUTCOME                   								  Required       		COSC");
			bw.write("UNIT      OUTCOME                   								  Required       		 COSC");
			bw.newLine();
			
			Statement stmt=myconn.createStatement();
			rs=stmt.executeQuery("SELECT KA.KACODE KACODE,   KA.KANAME KANAME FROM KA KA ORDER BY KA.KACODE");

			while(rs.next())
			{
				System.out.println(rs.getString("KACODE")+"-"+rs.getString("KANAME"));
				bw.write(rs.getString("KACODE")+"-"+rs.getString("KANAME"));
				bw.newLine();
	
				stmt1 = myconn.prepareStatement("select * from KU where ku.ka=? order by ku");
					stmt1.setString(1,rs.getString("KACODE"));	
					rs1 = stmt1.executeQuery();
				while(rs1.next()){
					
					stmt2 = myconn.prepareStatement("Select MINTIER1,mintier2,ku from KU where KUID=?");
					stmt2.setInt(1,rs1.getInt("KUID"));	
					rs2 = stmt2.executeQuery();
				
					rs2.next();
					
					stmt3 = myconn.prepareStatement("SELECT SUM(ckip.tier1hrs) tier1hrs,   SUM(ckip.tier2hrs) tier2hrs FROM   (SELECT MIN(ck.tier1hrs) tier1hrs,     MIN(ck.tier2hrs) tier2hrs   FROM nocoursesreq nc,     degreqs dr,     courses_kutaught ck   WHERE nc.options =dr.options   AND nc.DEGcode   =dr.degcode   AND dr.DEGcode   ='COSC'   AND ck.courseid  =dr.courseid   AND ck.kuid      =?   GROUP BY nc.options   ) ckip");
					stmt3.setInt(1,rs1.getInt("KUID"));	
					rs3 = stmt3.executeQuery();
					rs3.next();
					
				
				System.out.println((rs2.getString("KU")+a3).substring(0,98)+rs2.getFloat("mintier1")+"/"+rs2.getFloat("mintier2")+"			"+rs3.getFloat("tier1hrs")+"/"+rs3.getFloat("tier2hrs"));
				bw.write((rs2.getString("KU")+a3).substring(0,98)+rs2.getFloat("mintier1")+"/"+rs2.getFloat("mintier2")+"			"+rs3.getFloat("tier1hrs")+"/"+rs3.getFloat("tier2hrs"));
				bw.newLine();
				stmt4 = myconn.prepareStatement("select lo.lono from LO lo,KU ku where lo.kuid=ku.kuid and ku.kuid=? order by lo.lono");
					stmt4.setInt(1,rs1.getInt("KUID"));	
					rs4 = stmt4.executeQuery();
					
				while(rs4.next())
						{
							
					stmt5 = myconn.prepareStatement("SELECT MAX(llnum) llnum,lono lono FROM ( SELECT MIN(ll.num) llnum,ci.lono lono   FROM courses_kutaught ck,     courses_info ci,     degreqs dr,     LO lo,     LOLEVEL ll,     nocoursesreq nc   WHERE ck.COURSEID   =ci.COURSEID   AND ck.KUID         =ci.KUID   AND dr.COURSEID     =ck.COURSEID   AND ck.kuid         =?   AND nc.degcode      =dr.degcode   AND dr.degcode      ='COSC'   AND ci.lono         =?   AND lo.lono         =ci.lono   AND lo.kuid         =ck.kuid   AND ci.lolevel      =ll.lolevel   AND nc.options      =dr.options   GROUP BY nc.options,     ci.lono,     ck.kuid ) ll1 group by lono");
					stmt5.setInt(1,rs1.getInt("KUID"));	
					stmt5.setInt(2,rs4.getInt("lono"));
					rs5 = stmt5.executeQuery();
			
					b=rs5.next();
					
					stmt6 = myconn.prepareStatement("select LO from LO where LO.lono=? and LO.kuid=?");
	
					stmt6.setInt(2,rs1.getInt("KUID"));	
					stmt6.setInt(1,rs4.getInt("lono"));	
					rs6 = stmt6.executeQuery();
					
					
					rs6.next();
					
					stmt7 = myconn.prepareStatement("Select LOLEVEL FROM lo,ku WHERE LO.LONO=? and LO.KUID=?");
					stmt7.setInt(2,rs1.getInt("KUID"));	
					stmt7.setInt(1,rs4.getInt("lono"));	
					rs7 = stmt7.executeQuery();
					
					rs7.next();
					try
					{
					stmt8 = myconn.prepareStatement("select lolevel from LOLEVEL where LOLEVEL.num=?");
					stmt8.setInt(1,rs5.getInt("llnum"));	
					rs8 = stmt8.executeQuery();
					
					c=rs8.next();
					s=rs8.getString("LOLEVEL");
					rs8.close();
					stmt8.close();
					}
					catch(Exception e1)
					{
					c=false;
					if(rs8!=null)
						try{
							rs8.close();
							}catch(Exception e2)
								{
								System.out.println("rs8 closing error is-"+e2);
								System.exit(1);
								}
					
					if(stmt8!=null)
						try{
							stmt8.close();
							}catch(Exception e2)
								{
								System.out.println("stmt8 closing error is-"+e2);
								System.exit(1);
								}
					
					
					}
					
					a1=rs6.getString("LO");
					
					{
						int i=15;
					
					    int y=0;
					    int j=0;
						for(;y<a1.length();i++)
						{
							if(y+i>=a1.length())
							{
								a2[j]= new String(a1.substring(y,y+i));
								//System.out.println(a2[j]);
								break;
							}
						    if(i>10)
						   {
							if(a1.charAt(y+i)==' ')
							{
								a2[j]=a1.substring(y,y+i);
								//System.out.println(a2[j]);
								y=y+i;
								i=0;
								j++;
							}
						   }
						}
						for(int z=j+1;z<200;z++)
						{
							a2[z]="";
						}
					}
					if(b)
					{
					System.out.println(rs4.getInt("lono")+".	"+(a2[0]+a3).substring(0,90)+(rs7.getString("LOLEVEL")+"                                                                                       ").substring(0,20)+"		"+s);

					bw.write(rs4.getInt("lono")+".	"+(a2[0]+a3).substring(0,90)+(rs7.getString("LOLEVEL")+"                                                                                       ").substring(0,20)+"		"+s);
					bw.newLine();
					}
					
					else
					{
						System.out.println(rs4.getInt("lono")+".	"+(a2[0]+a3).substring(0,90)+(rs7.getString("LOLEVEL")+"                                                                                       ").substring(0,20)+"		"+"None");

						bw.write(rs4.getInt("lono")+".	"+(a2[0]+a3).substring(0,90)+(rs7.getString("LOLEVEL")+"                                                                                       ").substring(0,20)+"		"+"None");
						bw.newLine();
						
					
					}
					
					for(int i=1;a2[i]!="";i++)
					{
						System.out.println("	"+a2[i]);
						bw.write("	"+a2[i]);
						bw.newLine();
					}
					

					
					rs5.close();
					rs6.close();
					rs7.close();
					
					stmt5.close();
					stmt6.close();
					stmt7.close();
					
					}
				rs4.close();
				stmt4.close();
				



					

				rs2.close();
				rs3.close();

				
				stmt2.close();
				stmt3.close();
	
				}

				
				System.out.println();
				bw.newLine();
				rs1.close();
				stmt1.close();
				
			


		}
			rs.close();
			stmt.close();
			
			myconn.close();
			bw.close();
			}
		catch(Exception e){System.out.println(e);}
		finally
		{
		
			
			if(stmt1!=null)
				try{
					stmt1.close();
					}catch(Exception e)
						{
						System.out.println("stmt1 closing error is-"+e);
						System.exit(1);
						}
			
			if(stmt2!=null)
				try{
					stmt2.close();
					}catch(Exception e)
						{
						System.out.println("stmt2 closing error is-"+e);
						System.exit(1);
						}
			
			
			if(stmt3!=null)
				try{
					stmt3.close();
					}catch(Exception e)
						{
						System.out.println("stmt3 closing error is-"+e);
						System.exit(1);
						}
			
			
			if(stmt4!=null)
				try{
					stmt4.close();
					}catch(Exception e)
						{
						System.out.println("stmt4 closing error is-"+e);
						System.exit(1);
						}
			
			
			if(stmt5!=null)
				try{
					stmt5.close();
					}catch(Exception e)
						{
						System.out.println("stmt5 closing error is-"+e);
						System.exit(1);
						}
			
			
			if(stmt6!=null)
				try{
					stmt6.close();
					}catch(Exception e)
						{
						System.out.println("stmt6 closing error is-"+e);
						System.exit(1);
						}
			
			
			if(stmt7!=null)
				try{
					stmt7.close();
					}catch(Exception e)
						{
						System.out.println("stmt7 closing error is-"+e);
						System.exit(1);
						}
			
			
			if(stmt8!=null)
				try{
					stmt8.close();
					}catch(Exception e)
						{
						System.out.println("stmt8 closing error is-"+e);
						System.exit(1);
						}
			
			
			
			if(rs!=null)
				try{
					rs.close();
					}catch(Exception e)
						{
						System.out.println("rs closing error is-"+e);
						System.exit(1);
						}
			
			
			
			if(rs1!=null)
				try{
					rs1.close();
					}catch(Exception e)
						{
						System.out.println("rs1 closing error is-"+e);
						System.exit(1);
						}
			
			if(rs2!=null)
				try{
					rs2.close();
					}catch(Exception e)
						{
						System.out.println("rs2 closing error is-"+e);
						System.exit(1);
						}
			
			
			if(rs3!=null)
				try{
					rs3.close();
					}catch(Exception e)
						{
						System.out.println("rs3 closing error is-"+e);
						System.exit(1);
						}
			
			
			if(rs4!=null)
				try{
					rs4.close();
					}catch(Exception e)
						{
						System.out.println("rs4 closing error is-"+e);
						System.exit(1);
						}
			
			
			if(rs5!=null)
				try{
					rs5.close();
					}catch(Exception e)
						{
						System.out.println("rs5 closing error is-"+e);
						System.exit(1);
						}
			
			
			if(rs6!=null)
				try{
					rs6.close();
					}catch(Exception e)
						{
						System.out.println("rs6 closing error is-"+e);
						System.exit(1);
						}
			
			
			if(rs7!=null)
				try{
					rs7.close();
					}catch(Exception e)
						{
						System.out.println("rs7 closing error is-"+e);
						System.exit(1);
						}
			
			
			if(rs8!=null)
				try{
					rs8.close();
					}catch(Exception e)
						{
						System.out.println("rs8 closing error is-"+e);
						System.exit(1);
						}
			
			
			if(myconn!=null)
				try{
					myconn.close();
					}catch(Exception e)
						{
						System.out.println("connection closing error is-"+e);
						System.exit(1);
						}
			
			
			if(bw!=null)
				try{
					bw.close();
					}catch(Exception e)
						{
						System.out.println("bufferedwriter closing error is-"+e);
						System.exit(1);
						}
		}
		
		
	}

}