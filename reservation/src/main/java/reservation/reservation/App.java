package reservation.reservation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App 
{
    public static void main( String[] args )throws Exception
    {
    	Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/airline_reservation","root","susmitha");
		Scanner input = new Scanner(System.in);
		System.out.println(ConsoleColors.GREEN_BOLD_BRIGHT+"                            AIRO AIRLINE RESERVATION"+ConsoleColors.RESET);
		System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT+"Welcome to the airline booking service."+ConsoleColors.RESET);
		System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT+"Are you a member? Y/N:"+ConsoleColors.RESET );
		
		String membership = input.next();
		if(membership.equalsIgnoreCase("Y")){
			System.out.println(ConsoleColors.PURPLE+"Please enter your username: "+ConsoleColors.RESET);
			String username = input.next();
			System.out.println(ConsoleColors.PURPLE+"Please enter your password: "+ConsoleColors.RESET);
			String password = input.next();
			
			ResultSet memberRs = getMemberIdByUsernameAndPassword(conn, username, password);
			if(memberRs.next()){
				int member = memberRs.getInt("memberID"); 
				List<Integer> FlightIdList = getFlightIdsByMemberId(member, conn);
				printFlightInfoByFlightId(conn, FlightIdList);
				
				String quit= "i";
				while(quit!="n"){
				System.out.println(ConsoleColors.CYAN_BRIGHT+"\n!!How can we help you?!!\n" + 
				"1:Check all flights information \n"+
				"2:Flight Booking \n"+
				"3:Add flight \n"+ 
				"4:Delete flight booking\n"+
				"5:Check all user information \n"+ 
				"6:Log out form booking \n"+"\nSelect your choice: "+ConsoleColors.RESET);
				System.out.println();
				
				int selection = input.nextInt();
				switch(selection){
				case 1:
					System.out.println(ConsoleColors.RED_BOLD_BRIGHT+"\n*****Collect all flight informtation are here:******"+ConsoleColors.RESET);
					System.out.println("101 chennai to bangalore 4:00");
					System.out.println("203 banglore to chennai 4:50");
					System.out.println("463 chennai to delhi 5:30");
					System.out.println("997 chennai to mumbai 7:00");
					System.out.println("865 chennai to bangalore 7:15");
					System.out.println("892 calicut to bangalore 10:30");
					System.out.println("301 hyderabd to chennai 13:00");
					
					
					break;
				case 2:
					System.out.println(ConsoleColors.PURPLE+"Enter the departure city: "+ConsoleColors.RESET);
					String departureCity = input.next();
					System.out.println(ConsoleColors.PURPLE+"Enter the destination city: "+ConsoleColors.RESET);
					String arrivalCity = input.next();
					
					System.out.println(ConsoleColors.PURPLE+"Enter a flight number to book: "+ConsoleColors.RESET);
					int flightId = input.nextInt();					
					//put into the order table in database
					
					System.out.println(ConsoleColors.RED_BOLD_BRIGHT+"**Your flight has booked successfully.**"+ConsoleColors.RESET);
					
					break;
				
				case 3:
					System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT+"Please enter the flight information for adding: \n"+ConsoleColors.RESET);
					//create a flight
					createFlight(input, conn);
					break;
				case 4:
					
					
					//call deleteFlight method
					deleteFlight(input, conn);
					
					//flight deleted
					break;
					
				case 5:					
					System.out.println(ConsoleColors.RED_BOLD_BRIGHT+"******All member's information:****** \n"+ConsoleColors.RESET);
					//call get all member information method
					printAllMemberInfo(conn);
					
					break;
				case 6:
					System.out.println(ConsoleColors.RED_BOLD_BRIGHT+"-----Logged out.----\n"+ConsoleColors.RESET);
					System.out.println(ConsoleColors.GREEN_BOLD_BRIGHT+ConsoleColors.GREEN_UNDERLINED+"*******THANK YOU FOR VISIT*******"+ConsoleColors.RESET);
				    System.exit(0);
				   
					break;
					
				default:
						System.out.println(ConsoleColors.BLACK_BOLD_BRIGHT+"Invalid. Enter an option from 1 to 6: "+ConsoleColors.RESET);
					
				}
				System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT+"\nBack to the main menu? press any key except 'n' "+ConsoleColors.RESET);
				quit = input.next().toLowerCase();
				}
			}else{
				
				PreparedStatement getMemberByUsernamePS = conn.prepareStatement(Queries.GET_MEMBER_BY_USERNAME);
				getMemberByUsernamePS.setString(1, username);
				ResultSet memberRsByUsername = getMemberByUsernamePS.executeQuery();
				if(memberRsByUsername.next()){
					
					String sq = memberRsByUsername.getString("securityQuestion");
					System.out.println(sq);
					String sqAnswer = input.next();
					int memberId = -1;
					if(memberRsByUsername.getString("securityAnswer").equalsIgnoreCase(sqAnswer)){
						System.out.println(ConsoleColors.RED_BOLD_BRIGHT+">>>>>>login sucess<<<<<<<<"+ConsoleColors.RESET);
						memberId = memberRsByUsername.getInt("memberID");
						List<Integer> FlightIdList = getFlightIdsByMemberId(memberId, conn);
						
						printFlightInfoByFlightId(conn, FlightIdList);
						
						String quit= "i";
						while(quit!="n"){
						System.out.println(ConsoleColors.CYAN_BRIGHT+"How can we help you?\n" + 
						"1:check all flights information \n"+
						"2:booking flight \n"+
						"3:add flight (Admin only) \n"+ 
						"4:delete flight (Admin only)\n"+
						"5:check all member information (Admin only)\n"+ 
						"6:log out \n"+"Enter a choice: "+ConsoleColors.RESET);
						
						int selection = input.nextInt();
						switch(selection){
						case 1:
							System.out.println(ConsoleColors.RED_BOLD_BRIGHT+"*******Collect all flight informtation are here:******* "+ConsoleColors.RESET);
							printAllFlightInfo(conn);
							
							break;
						case 2:
							System.out.println(ConsoleColors.PURPLE+"Enter the departure ciry: "+ConsoleColors.RESET);
							String departureCity = input.next();
							System.out.println(ConsoleColors.PURPLE+"Enter the destination city: "+ConsoleColors.RESET);
							String arrivalCity = input.next();
							
							System.out.println(ConsoleColors.PURPLE+"Enter a flight number to book: "+ConsoleColors.RESET);
							int flightId = input.nextInt();					
							
							System.out.println(ConsoleColors.RED_BOLD_BRIGHT+"*****Your flight has booked successfully.*****"+ConsoleColors.RESET);
							
							break;
						
						case 3:
							System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT+"Please enter the flight information for adding: \n"+ConsoleColors.RESET);
							createFlight(input, conn);
							break;
						case 4:
							deleteFlight(input, conn);
							break;
							
						case 5:					
							System.out.println(ConsoleColors.RED_BRIGHT+"*****All member's information:***** \n"+ConsoleColors.RESET);
							printAllMemberInfo(conn);
							
							break;
						case 6:
							System.out.println(ConsoleColors.RED_BOLD+"-----Logged out.------\n"+ConsoleColors.RESET);
							System.out.println(ConsoleColors.GREEN_BOLD_BRIGHT+ConsoleColors.GREEN_UNDERLINED+"*******THANK YOU FOR VISIT*******"+ConsoleColors.RESET);
						    System.exit(0);
						   
							break;
							
						default:
								System.out.println(ConsoleColors.BLACK_BOLD_BRIGHT+"Invalid input.Enter an option from 1 to 6: "+ConsoleColors.RESET);
							
						}
						System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT+"Back to the main menu? press any key except 'n' "+ConsoleColors.RESET);
						quit = input.next().toLowerCase();
	
						
						}		
					}else{
						System.out.println(ConsoleColors.RED_BOLD_BRIGHT+"~~~~~~~~~~~Login failed please register~~~~~~~~~~~~~"+ConsoleColors.RESET);
						memberId = createUser(input, conn);
					}
				}else{
					
					System.out.println(ConsoleColors.BLUE_BOLD+"Username and password are not found. Please register first."+ConsoleColors.RESET);
					createUser(input, conn);
					
					
				}
			}
		}else if(membership.equalsIgnoreCase("N")){
			createUser(input, conn);
		}

	}
	
	private static int createUser(Scanner input, Connection conn) throws SQLException{
		System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT+"##Please register your information below:## \n"+ConsoleColors.RESET);
		System.out.println("Enter your first name: ");
		String fname = input.next();
		System.out.println("Enter your last name: ");
		String lname = input.next();
		System.out.println("Enter your address: ");
		String address = input.next();
		System.out.println("Enter your zipcode: ");
		String zip = input.next();
		System.out.println("Enter your state: ");
		String state = input.next();
		System.out.println("Enter username: ");
		String username = input.next();
		System.out.println("Enter password: ");
		String password = input.next();
		System.out.println("Enter your email: ");
		String email = input.next();
		System.out.println("Enter your ssn: ");
		String ssn = input.next();						
		System.out.println("Enter security question: ");
		String securityQuestion = input.next();
		System.out.println("Enter security answer: ");
		String sqAnswer = input.next();
		
		
		PreparedStatement Member = conn.prepareStatement(Queries.REGISTER_USER_INFO);
		Member.setString(1, fname);
		Member.setString(2, lname);
		Member.setString(3, address);
		Member.setString(4, zip);
		Member.setString(5, state);
		Member.setString(6, username);
		Member.setString(7, password);
		Member.setString(8, email);
		Member.setString(9, ssn);
		Member.setString(10, securityQuestion);
		Member.setString(11, sqAnswer);
		Member.executeUpdate();
		System.out.println(ConsoleColors.MAGENTA_BOLD+"Recorded into Member table!"+ConsoleColors.RESET);
		
		
		int memberId = -1;
		ResultSet memberRs = getMemberIdByUsernameAndPassword(conn, username, password);
		if(memberRs.next()){
			memberId = memberRs.getInt("memberID"); 
		}
		
		return memberId;
	}
	
	
	private static ResultSet getMemberIdByUsernameAndPassword(Connection conn, String username, String password)  throws SQLException{
		PreparedStatement myStat = conn.prepareStatement(Queries.GET_USER_BY_USERNAME_AND_PASSWORD);
		myStat.setString(1, username);
		myStat.setString(2, password);
		ResultSet memberRs = myStat.executeQuery();
		return memberRs;
	}
	
	private static List<Integer> getFlightIdsByMemberId(int memberId, Connection conn) throws SQLException{
		List<Integer> flightIdList = new ArrayList<>();
		PreparedStatement myStat = conn.prepareStatement(Queries.GET_FLIGHT_BY_MemberID);
		myStat.setInt(1, memberId);
		ResultSet rs = myStat.executeQuery();
		while(rs.next()){
			flightIdList.add(rs.getInt("flightID"));
		}
		return flightIdList;
	}
	
	private static void printFlightInfoByFlightId(Connection conn, List<Integer> flightIds) throws SQLException{
		for(int id : flightIds){
			PreparedStatement myStat = conn.prepareStatement(Queries.GET_FLIGHT_BY_ID);
			myStat.setInt(1, id);
			ResultSet resultSet = myStat.executeQuery();
			while(resultSet.next()){
				System.out.println(ConsoleColors.GREEN+"*"+ resultSet.getString("flightID") + " , "
						+ resultSet.getString("flightCode")+ " , "
						+ resultSet.getString("departureCity")+" , "
						+ resultSet.getString("destination")+" , "
						+ resultSet.getString("departureTime")+" , "
						+ resultSet.getString("arrivalTime")+" , "
						+ resultSet.getString("seatCapacity")+ConsoleColors.RESET);
			}
		}
		System.out.println(ConsoleColors.BLACK+"*****************************************************************************"+ConsoleColors.RESET);
	}
	
	
	
	private static void printFlightByDepartureAndArrivalCity(Connection conn, String departureCity, String arrivalCity) throws SQLException{
		PreparedStatement myStat = conn.prepareStatement(Queries.GET_FLIGHT_BY_DEPARTURE_DESTINATION);
		myStat.setString(1, departureCity);
		myStat.setString(2, arrivalCity);
		ResultSet resultSet = myStat.executeQuery();
		while(resultSet.next()){
			System.out.println(ConsoleColors.GREEN+"*"+ resultSet.getString("flightID") + " , "
					+ resultSet.getString("flightCode")+ " , "
					+ resultSet.getString("departureCity")+" , "
					+ resultSet.getString("destination")+" , "
					+ resultSet.getString("departureTime")+" , "
					+ resultSet.getString("arrivalTime")+" , "
					+ resultSet.getString("seatCapacity")+ConsoleColors.RESET);
		}
	
	System.out.println(ConsoleColors.BLACK+"*****************************************************************************"+ConsoleColors.RESET);
		
	
	}
	
	
	
	
	private static void printAllFlightInfo(Connection conn) throws SQLException{
		PreparedStatement myStat = conn.prepareStatement(Queries.GET_ADD_FLIGHTS);
		ResultSet resultSet = myStat.executeQuery();
		while(resultSet.next()){
			System.out.println(ConsoleColors.GREEN+"*"+ resultSet.getString("flightID") + " , "
					+ resultSet.getString("flightCode")+ " , "
					+ resultSet.getString("departureCity")+" , "
					+ resultSet.getString("destination")+" , "
					+ resultSet.getString("departureTime")+" , "
					+ resultSet.getString("arrivalTime")+" , "
					+ resultSet.getString("seatCapacity")+ConsoleColors.RESET);
		}
	
	System.out.println(ConsoleColors.BLACK+"*****************************************************************************"+ConsoleColors.RESET);
		
	
	}
	
	
	private static void printAllMemberInfo(Connection conn) throws SQLException{
		PreparedStatement myStat = conn.prepareStatement(Queries.GET_MEMBERINFO);
		ResultSet resultSet = myStat.executeQuery();
		while(resultSet.next()){
			System.out.println(ConsoleColors.GREEN+"*"+ resultSet.getString("memberID") + " , "
					+ resultSet.getString("lname")+ " , "
					+ resultSet.getString("fname")+" , "
					+ resultSet.getString("address")+" , "
					+ resultSet.getString("zip")+" , "
					+ resultSet.getString("state")+" , "
					+ resultSet.getString("username")+" , "
					+ resultSet.getString("password")+" , "
					+ resultSet.getString("email")+" , "
					+ resultSet.getString("ssn")+" , "
					+ resultSet.getString("securityQuestion")+" , "
					+ resultSet.getString("securityAnswer")+ConsoleColors.RESET);
		}
	
	System.out.println(ConsoleColors.BLACK+"*****************************************************************************"+ConsoleColors.RESET);
		
	
	}
	
	
	

		
	private static void createFlight(Scanner input, Connection conn) throws SQLException{
		System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT+"##Please enter the flight information below:## \n"+ConsoleColors.RESET);
		System.out.println("Enter the flight code: ");
		String flightCode = input.next();
		System.out.println("Enter the destination: ");
		String destination = input.next();
		System.out.println("Enter the flight's seat capacity: ");
		String seatCapacity = input.next();
		System.out.println("Enter the departure city: ");
		String departureCity = input.next();
		System.out.println("Enter the departure time: ");
		String departureTime = input.next();
		System.out.print("Enter the arrival time: ");
		String arrivalTime = input.next();
		
		
		
		
		PreparedStatement Flight = conn.prepareStatement(Queries.ADD_FLIGHT);
		Flight.setString(1, flightCode);
		Flight.setString(2, destination);
		Flight.setString(3, seatCapacity);
		Flight.setString(4, departureCity);
		Flight.setString(5, departureTime);
		Flight.setString(6, arrivalTime);
		Flight.getResultSet();
		System.out.println(ConsoleColors.MAGENTA_BOLD+"Recorded into Flight table!"+ConsoleColors.RESET);
		
	}
	
	private static void deleteFlight(Scanner input, Connection conn) throws SQLException{
		System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT+"##Please enter the flight id numer for delete:## " +ConsoleColors.RESET);
		int deleteFlightId = input.nextInt();
		PreparedStatement deleteFlightPS = conn.prepareStatement(Queries.DELETE_FLIGHT);
		deleteFlightPS.setInt(1, deleteFlightId);
		deleteFlightPS.cancel();
		System.out.println(ConsoleColors.RED_BOLD+"******Flight deleted!*****"+ConsoleColors.RESET);
	}
	

     private static void printMemberProfByMemberId(Connection conn, List<Integer> memberId) throws SQLException{
    	 System.out.println(ConsoleColors.BLACK+"*****************************************************************************"+ConsoleColors.RESET);
    	 for(int id: memberId){
    	 PreparedStatement myStat = conn.prepareStatement(Queries.GET_MEMBERINFO_BY_MEMBERID);
    	 myStat.setInt(1,id);
    	 ResultSet resultSet = myStat.executeQuery();
    	 while(resultSet.next()){
				System.out.println(ConsoleColors.MAGENTA_BOLD+"*"+ resultSet.getString("memberID") + " , "
						+ resultSet.getString("lname")+ " , "
						+ resultSet.getString("fname")+" , "
						+ resultSet.getString("address")+" , "
						+ resultSet.getString("zip")+" , " 
						+ resultSet.getString("state")+" , "
						+ resultSet.getString("email")+" , "
						+ resultSet.getString("ssn")+" , "
						+ resultSet.getString("userneme")+" , "
						+ resultSet.getString("email")+ConsoleColors.RESET);
			
		}
    	 } 
     }
  }

