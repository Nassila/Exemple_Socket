package main.clientTCP;

import java.util.Scanner;

import utilsTCP.SocketCommunicator;



public class Client {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);

		while (true){
			System.out.print("Introduire une operation : ");
            //pour puvoir saisir dans la console
			String operation =scanner.next().trim();
            
			//si le client saisi quitter, il quittera le scanner et n'aura plus la possibilité de communiquer avec le serveur 
			if (operation.equals("quitter")) {
				System.out.println("Bye Bye ...");
				break;
			}
            
			//separer la chaine operation en sous chaines selon les separateurs defini avec ma methode spilt 
			//et stocker chaque sous chaine dans le tableau parts
			String[] parts = operation.split("\\+|\\-|\\*|/");
			
            //comme on fait des operation mathematique, il faut verifier que le client a bien saisi deux element 
			//pour se faire il faut que la taille du tableau soit egale à 2
			if (parts.length !=2) {
				System.err.println("Erreur dans l'operation");
			}else{
				try{
					//envoyer la requete au serveur
					Object reponse= SocketCommunicator.envoyerRequete(operation);
					System.out.println("le resultat est : " + reponse);
				}
				catch(Exception e){
					System.err.println("Erreur dans les operands");
				}
			}
			//laisser trois lignes vides entre chaque message sur la console pour mieux voir le resultat
			System.out.println();
			System.out.println();
			System.out.println();
		}
		scanner.close();
	}

}
