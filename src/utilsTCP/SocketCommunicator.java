package utilsTCP;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public final class SocketCommunicator {

	private SocketCommunicator(){

	}
	public static Object envoyerRequete(Object requete){
		Object reponse = null;

		try {

			//creer le canal de communication ...
			Socket socket=new Socket(ConfigSocket.ADDR_IP, ConfigSocket.NUM_PORT);

			
			
			//envoyer la requête au serveur
			OutputStream  os = (OutputStream) socket.getOutputStream();
			PrintWriter writer = new PrintWriter(os);

			writer.println(requete);
			//forcer l'envoi
			writer.flush();
			
			//lire la réponse du serveur
			InputStream  is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader reader = new BufferedReader(isr);
			reponse = reader.readLine();




			writer.close();
			reader.close();
			socket.close();   

		} catch (Exception e) {
			e.printStackTrace();
		}

		return reponse;
	}
}
