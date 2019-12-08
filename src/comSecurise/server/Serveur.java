package comSecurise.server;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import comSecurise.outil;

public class Serveur extends Thread {

	public JFrame frame;
	private JTextField textField;
	private JPanel panelCenter;
	private JScrollPane scrollPane;
	private static JTextArea textArea;

	/**
	 * Launch the application.
	 * 
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws InvalidKeySpecException
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {

		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(2048);
		KeyPair keypair = keyGen.genKeyPair();
		privateKey = keypair.getPrivate();
		publicKey = keypair.getPublic();

		byte[] key = publicKey.getEncoded();
		FileOutputStream keyfos = new FileOutputStream("cleServeur");
		keyfos.write(key);
		keyfos.close();

		EventQueue.invokeLater(new Runnable() {

			public void run() {

				try {
					Serveur window = new Serveur();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		String message = "";
		try {
			serverSocket = new ServerSocket(5000);
			socket = serverSocket.accept();
			dataOut = new DataOutputStream(socket.getOutputStream());
			dataIn = new DataInputStream(socket.getInputStream());
			String out = "";
			while (!out.equals("EXIT")) {
				message = dataIn.readUTF();

				// dechiffremnt
				out = outil.dechiffrement(privateKey, message);
				textArea.setText(textArea.getText().trim() + "\n" + out);

				// String[] msgsplit = out.split("#-/-#");

				// Boolean bool = outil.verifierSignature(pubKeyA, msgsplit[1], msgsplit[0]);

				// if (bool) {
				// textArea.setText(textArea.getText().trim() + "\n" + "en confiance");
				// }

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Create the application.
	 * 
	 */

	static ServerSocket serverSocket;
	static Socket socket;
	static DataInputStream dataIn;
	static DataOutputStream dataOut;
	static PrivateKey privateKey;
	static PublicKey publicKey;
	static PublicKey pubKeyClient;

	public Serveur() {

		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setTitle("Serveur ");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panelsouth = new JPanel();
		frame.getContentPane().add(panelsouth, BorderLayout.SOUTH);

		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (textField.getText().equals("EXIT")) {
					try {
						dataOut.writeUTF("ClientServeur : Aurevoir");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
			}
		});
		panelsouth.add(textField);
		textField.setColumns(10);

		JButton btnEnvoyer = new JButton("envoyer");
		btnEnvoyer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					FileInputStream keyfis = new FileInputStream("cleClient");
					byte[] encKey = new byte[keyfis.available()];
					keyfis.read(encKey);
					keyfis.close();
					EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
					KeyFactory keyFactory = KeyFactory.getInstance("RSA");
					pubKeyClient = keyFactory.generatePublic(pubKeySpec);
				} catch (FileNotFoundException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NoSuchAlgorithmException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InvalidKeySpecException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				String messageOut = "";
				try {
					messageOut = textField.getText().trim();

					// Chiffrement du message
					String out = outil.chiffrement(pubKeyClient, messageOut);

					dataOut.writeUTF(out);

					textField.setText("");

				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		});
		panelsouth.add(btnEnvoyer);

		panelCenter = new JPanel();
		frame.getContentPane().add(panelCenter, BorderLayout.CENTER);

		textArea = new JTextArea();
		textArea.setRows(12);
		textArea.setColumns(35);
		textArea.setEditable(false);
		scrollPane = new JScrollPane(textArea);
		panelCenter.add(scrollPane);

	}

}
