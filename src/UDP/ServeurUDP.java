package UDP;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ServeurUDP {

	public static void main(String args[]) throws Exception {

			DatagramSocket serverSocket = new DatagramSocket(9876);
			
			byte[] receiveData = new byte[1024];
			byte[] sendData = new byte[1024];
			
			while (true) {
			
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			
			//afficher l'adresse de la source et le port
			InetAddress clientAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();
			System.out.println("l'adresse de reception est : " + clientAddress.getHostName() + "et le port est : " + port);
			String sentence = new String(receivePacket.getData());
			System.out.println("le message envoyé par le client est : "+sentence);
			
			//modifier le code pour que le serveur envoi un message au client
			Scanner in = new Scanner(System.in);
			System.out.println("saissir le message à envoyer au client");
			String message = in.nextLine();
			sendData = message.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, port);
			serverSocket.send(sendPacket);
			
			
			
			}
			}
			}
