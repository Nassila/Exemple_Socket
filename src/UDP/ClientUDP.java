package UDP;

import java.net.*;
import java.util.Scanner;

public class ClientUDP {

public static void main(String args[]) throws Exception {
	
	Scanner inFromUser = new Scanner(System.in);

	DatagramSocket clientSocket = new DatagramSocket();
	InetAddress IPAddress = InetAddress.getByName("localhost");

	//lit une chaine de caracteres au clavier
	byte[] sendData = new byte[1024];
	System.out.println("saisir un message à envoyer au serveur");
	String sentence = inFromUser.nextLine();
	sendData = sentence.getBytes();
	//envoi de la chaine de caracteres au serveur
	DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
	clientSocket.send(sendPacket);
	
	//pour recevoir le message du serveur
	byte[] receiveData = new byte[1024];
	DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	clientSocket.receive(receivePacket);
	String message = new String(receivePacket.getData());
	System.out.println("le message envoyé par le serveur est : "+message);
}
}
