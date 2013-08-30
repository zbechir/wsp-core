package org.wsp.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.wsp.threads.MainThread;
import org.wsp.threads.TurboRefrech;

public class Main {

	private static final Logger log = Logger.getLogger(Main.class);

	static String hostInetAddressString;
	static String clientInetAddressString;
	static int clientPortInt;
	static ServerSocket serverSocket = null;
	static Socket clientSocket = null;
	static int clientSocketReceiveBufferSizeInt;
	static int clientSocketSendBufferSizeInt;
	static BufferedReader stdIn;
	static String fromUserString;
	static ObjectOutputStream out;
	static BufferedReader in;
	static int portInt;
	static String fromClient;
	static ApplicationContext context = new ClassPathXmlApplicationContext(
			"wsp-context.xml");
	static MainThread mainThread;

	public static void main(String[] args) {
		portInt = 2503;
		clientSocketReceiveBufferSizeInt = 2;
		clientSocketSendBufferSizeInt = 2;
		log.info("Server is intitiating. Please wait…");

		try {
			serverSocket = new ServerSocket(portInt);
			hostInetAddressString = serverSocket.getInetAddress().toString();
		} catch (IOException e) {
			log.error("Could not listen on port: " + portInt);
			log.error(e.getMessage(), e);
			System.exit(1);
		}
		log.info("Server initiated. Awaiting connection request…\n");

		while (true) {
			try {
				clientSocket = serverSocket.accept();
				clientInetAddressString = clientSocket.getInetAddress()
						.toString();
				clientSocket
						.setReceiveBufferSize(clientSocketReceiveBufferSizeInt);
				clientSocket.setSendBufferSize(clientSocketSendBufferSizeInt);
				log.info("Client connected with Inet Address "
						+ clientInetAddressString + " on port: "
						+ clientSocket.getPort() + "\n");
				out = new ObjectOutputStream(clientSocket.getOutputStream());
				// out = new PrintWriter(clientSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(
						clientSocket.getInputStream()));
				if ((fromClient = in.readLine()) != null) {

					if (fromClient.equalsIgnoreCase("quit")) {
						log.info("Closing the server...");
						closeServer();
					} else {
						log.info("Client: " + fromClient + "\n");
						out.writeObject(work(fromClient));
						out.flush();
						out.close();
						in.close();
						clientSocket.close();
					}
				}
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			} catch (InterruptedException e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	public static void closeServer() {
		// Close writers, readers and then sockets
		try {
			out.close();
			in.close();
			clientSocket.close();
			serverSocket.close();
			System.exit(0);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private static Object work(String command) throws InterruptedException {
		Object res = "KO";
		TurboRefrech turboRefrech = new TurboRefrech(context);
		if (command != null) {
			String[] commands = command.split(" ", 4);
			if (commands.length >= 1) {
				switch(commands[0].toUpperCase()){
				case "TURBOREFRESH" :
					Thread TrRefTh = new Thread(turboRefrech);
					TrRefTh.start();
					res = "OK";
					break;
				case "START" :
					res =start();
					break;
				case "STOP" :
					res =stop();
					break;
				case "STATUS" :
					res =status();
					break;
				case "RESTART" :
					res =stop();
					Thread.sleep(1000*25);
					if(res.equals("OK")){
						res=start();
					}else{
						Thread.sleep(1000*25);
						res=start();
					}
					break;
				default :
					
					break;
				}
			}

		}
		return res;
	}
	
	private static String start(){
		mainThread=new MainThread(context);
		Thread th=new Thread(mainThread);
		th.start();
		return "OK";		
	}
	
	private static String stop(){
		if(mainThread!=null){
			mainThread.setStop(true);
		}
		return "OK";		
	}
	
	private static String status(){
		String Res="Off";
		if(mainThread!=null){
			Res =mainThread.getStatus();
		}
		return Res;		
	}
	

}
