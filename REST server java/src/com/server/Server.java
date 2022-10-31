package com.server;

import com.user.*;

import java.io.IOException; 
import java.io.OutputStream;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server {
	private static int port = 4711; 											
	private final HttpServer server;
	
	private static final String EMPTY_BODY = "The body can't be empty";
	private static final String WRONG_KEY = "Wrong key, it must be {\"firstname\":\"value\",\"lastname\":\"value\"}";
	private static final String WRONG_VALUE = "Wrong value";
	private static final String EMPTY_FISTNAME = "The firstname can't be empty";
	private static final String FIRSTNAME_ALREADY_EXISTE = "The firstname already existe, it's must be an unique firstname!";
	private static final String EMPTY_LASTNAME = "The lastname can't be empty";
	private static final String USER_NOT_FOUND = "User not found ";

	/**
	 * Method to create the local server 
	 * @param port
	 * @throws IOException
	 */
    public Server(final int port) throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        this.server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());
        HttpContext context = server.createContext("/");
		context.setHandler(new Handler());
    }

    /**
     * Method to start the server
     * @throws IOException
     */
    public void start() throws IOException {
        this.server.start();
        System.out.println("Web-Server auf Port " + port + " gestartet.");
    }

    /**
     * Method to stop the server 
     */
    public void stop() throws IOException {
        this.server.stop(0);
        System.out.println("Web-Server gestoppt.");
    }

	
	class Handler implements HttpHandler {
		
		static final int OK = 200;
		static final int NOT_OK = 404;
		private UserList userList = new UserList();
		
		public void handle(HttpExchange exchange) throws IOException {
			String uri = exchange.getRequestURI().getPath();
			String body = new String(exchange.getRequestBody().readAllBytes());
			
			userList.clearUserTab();
			userList.getFromFile(); //Get anything from file before start and store in userList 
			try {
				spliter(exchange,uri,body);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		/**
		 * Method to send a message to the client (Web page or POTSMAN) 
		 * @param exchange
		 * @param message
		 * @throws IOException
		 */
		public void sendToUser(HttpExchange exchange, String message) throws Exception {
			try {
				exchange.getResponseHeaders().add("Content-Type", "application/json");
				exchange.sendResponseHeaders(OK, message.length());
				OutputStream outputStream = exchange.getResponseBody();
				outputStream.write(message.getBytes());
				outputStream.close();
			} catch (IllegalArgumentException a) {
				sendToUser(exchange, a.getMessage());
			}catch (Exception e) {
				sendToUser(exchange, e.getMessage());
			};
		}
	
		/**
		 * Method how get get the uri and excute different action 
		 * @param exchange
		 * @param uri
		 * @param body
		 * @throws Exception 
		 */
		public void spliter(HttpExchange exchange,String uri,String body) throws Exception {
			
			switch (uri) {
				case "/get":
					System.out.println("Method get");
					sendToUser(exchange,userList.toJSON());
					break;
				case "/get/someone":
					System.out.println("Method get/someone");
					sendToUser(exchange, getOneUser(exchange,body).toJSON());
					break;
				case "/add":
					System.out.println("Method add");
					addUserFromBodyToFile(exchange,body);
					sendToUser(exchange, "User correctly add to the file");
					break;
				case "/edit":
					System.out.println("Method edit");
					editOneUser(exchange,body);
					sendToUser(exchange, "User correctly edit");
					break;
				case "/delete":
					System.out.println("Method delete");
					deleteOneUser(exchange,body);
					sendToUser(exchange, "User correctly delete");
					break;
				default:
					System.out.println("error in the method selection");
					sendToUser(exchange, "error in the method selection, please select between \"/get, /get/someone, /add, /edit, /delete \"");
			}
		}
		
		/**
		 * Method how add an new user from an client entry into the database (here the "user.json" file) 
		 * @param exchange
		 * @param body
		 * @throws Exception 
		 */
		public void addUserFromBodyToFile(HttpExchange exchange,String body) throws Exception{
			check(exchange, body.isEmpty(), EMPTY_BODY);
			Object[] hash = userList.fromJSON(body);
			check(exchange, hash[1].toString() == "" || hash[1].toString().isEmpty(), EMPTY_FISTNAME);
			check(exchange, hash[3].toString() == "" || hash[3].toString().isEmpty(), EMPTY_LASTNAME);
			check(exchange, userList.getUserWithFirstname(hash[1].toString()) != null, FIRSTNAME_ALREADY_EXISTE);
			User user = new User(hash[1].toString(), hash[3].toString());
	        userList.addUser(user);
	        userList.addToFile();
		}
		
		/**
		 * Method how gives one user back, the user can be ask with the firstname, or with the lastname
		 * @param exchange
		 * @param body
		 * @return an user 
		 * @throws IOException
		 */
		public User getOneUser(HttpExchange exchange,String body) throws Exception {
			check(exchange, body == "" || body.isEmpty(), EMPTY_BODY);
			Object[] hash = userList.fromJSONWithOnePrams(body);
			check(exchange, hash[0].toString() == "" || hash[0].toString().isEmpty() || hash[0] == "firstname", WRONG_KEY);
			check(exchange, hash[1].toString() == "" || hash[1].toString().isEmpty(), WRONG_VALUE);
			check(exchange, userList.getUserWithFirstname(hash[1].toString()) == null, USER_NOT_FOUND);
			check(exchange, userList.getUserWithLastname(hash[1].toString()) == null,USER_NOT_FOUND);
			
			if(hash[0].toString().equals("firstname")) {
				return userList.getUserWithFirstname(hash[1].toString());
			}else if(hash[0].toString().equals("lastname")) {
				return userList.getUserWithLastname(hash[1].toString());
			}
			return null;
		}
		
		/**
		 * Method how find an user and change some info, the user can be ask with the firstname, or with the lastname and the fistname, or the last name can be change
		 * @param exchange
		 * @param body
		 */
		public void editOneUser(HttpExchange exchange,String body) throws Exception {
			check(exchange, body == "" || body.isEmpty(), EMPTY_BODY);
			Object[] hash = userList.fromJSON(body);
			check(exchange, hash[0].toString() == "" || hash[0].toString().isEmpty() || hash[2].toString() == "" || hash[2].toString().isEmpty(), WRONG_KEY);
			check(exchange, hash[1].toString() == "" || hash[1].toString().isEmpty() || hash[3].toString() == "" || hash[3].toString().isEmpty(), WRONG_VALUE);
			check(exchange, userList.getUserWithFirstname(hash[1].toString()) == null,USER_NOT_FOUND);
			check(exchange, userList.getUserWithLastname(hash[1].toString()) == null,USER_NOT_FOUND);
			userList.editUser(hash[0].toString(), hash[1].toString(), hash[3].toString());
		}
		
		/**
		 * Method how find an user an delete him, the user can be ask with the firstname, or with the lastname.
		 * @param exchange
		 * @param body
		 */
		public void deleteOneUser(HttpExchange exchange,String body) throws Exception {
			check(exchange, body == "", EMPTY_BODY);
			Object[] hash = userList.fromJSONWithOnePrams(body);
			
			check(exchange, hash[0].toString() == "" || hash[0].toString().isEmpty(), WRONG_KEY);
			check(exchange, hash[1].toString() == "" || hash[1].toString().isEmpty(), WRONG_VALUE);
			check(exchange, userList.getUserWithFirstname(hash[1].toString()) == null,USER_NOT_FOUND);
			
			userList.deleteUser(hash[0].toString(), hash[1].toString());
		}
		
		/**
		 * Method check and send back un messag error
		 * @param exchange
		 * @param check	
		 * @param message
		 * @throws Exception 
		 */
		public void check(HttpExchange exchange, boolean check, String message) throws Exception {
			 if (check){
	           	try {
					sendToUser(exchange, message);
					throw new IllegalArgumentException(message);
				} catch (IOException e) {
					e.printStackTrace();
				};
		    }
		}
		
	}
	
	
}