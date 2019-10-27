package main.serveurTCP;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ThreadSocket extends Thread{

	private Socket socket=null;
	private static int count=0;


	public ThreadSocket(Socket socket){
		this.socket=socket;

	}

	public void run() {
		try {
			//pour lire les messages reçus par les clients 
			InputStream is = socket.getInputStream();
			InputStreamReader isr=new InputStreamReader(is);
			BufferedReader reader =new BufferedReader(isr);


			//pour envoyer les messages aux clients
			OutputStream os = (OutputStream) socket.getOutputStream();


			PrintWriter writer =new PrintWriter(os);

			//lire le message du client 
			String requete = reader.readLine().trim();
			System.out.println("la requete reçu est : "+requete);

			//separer la chaine operation en sous chaines selon les separateurs defini avec ma methode spilt 
			//et stocker chaque sous chaine dans le tableau parts
			String[] parts = requete.split("\\+|\\-|\\*|/");

			//convertir les deux elements du tableau en Float
			Float nb1 = Float.parseFloat(parts[0]);
			Float nb2 =  Float.parseFloat(parts[1]);

			Float reponse =0f;

			if (requete.contains("+")) {
				reponse = nb1 + nb2;
			}else if (requete.contains("-")) {
				reponse = nb1 - nb2;
			}else if (requete.contains("*")) {
				reponse = nb1 * nb2;
			}else if (requete.contains("/")) {
				reponse = nb1 / nb2;
			}




            //envoyer la reponse au client
			writer.println(reponse);
			writer.flush();//envoyer effectivement le msg

			reader.close();
			writer.close();
			socket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
