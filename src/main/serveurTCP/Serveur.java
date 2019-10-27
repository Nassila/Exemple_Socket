package main.serveurTCP;

import java.net.ServerSocket;
import java.net.Socket;

import utilsTCP.ConfigSocket;

public class Serveur {

	public static void main(String[] args) {
		try {
			@SuppressWarnings("resource")
			ServerSocket ss =new ServerSocket(ConfigSocket.NUM_PORT);

			while(true){

				System.out.println("Serveur Attend Requête ");
				//en attente de connexion
				Socket socket =ss.accept();//obtenir le canal de communication 

				System.out.println("Un canal creer par un client");
                
				//pour gérer les communications du serveur et du client par le serveur
				ThreadSocket ts=new ThreadSocket(socket);
				ts.start();


			}//fin de while
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

}
