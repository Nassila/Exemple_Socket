package comSecurise.client;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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

public class Client extends Thread {

	public JFrame frame;

	private JTextField textField;
	private JPanel panelCenter;
	private JScrollPane scrollPane;
	private static JTextArea textArea;

	/**
	 * Launch the application.
	 */

	static Socket socket;
	static DataInputStream dataIn;
	static DataOutputStream dataOut;
	static PrivateKey privateKey;
	static PublicKey publicKey;
	static PublicKey pubKeyServeur;

	public static void main(String[] args) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(2048);
		KeyPair keypair = keyGen.genKeyPair();
		privateKey = keypair.getPrivate();
		publicKey = keypair.getPublic();

		byte[] key = publicKey.getEncoded();
		FileOutputStream keyfos = new FileOutputStream("cleClient");
		keyfos.write(key);
		keyfos.close();

		FileInputStream keyfis = new FileInputStream("cleServeur");
		byte[] encKey = new byte[keyfis.available()];
		keyfis.read(encKey);
		keyfis.close();
		EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		pubKeyServeur = keyFactory.generatePublic(pubKeySpec);

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client window = new Client();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		String message = "";
		try {
			socket = new Socket("127.0.0.1", 5000);

			dataOut = new DataOutputStream(socket.getOutputStream());
			dataIn = new DataInputStream(socket.getInputStream());

			String dechiffre = "";
			while (!dechiffre.equals("EXIT")) {
				message = dataIn.readUTF();

				// dechiffremnt
				dechiffre = outil.dechiffrement(privateKey, message);

				// verifier la singnature
				// sBoolean singnature = outil.verifierSignature(pubKeyServeur, message ,
				// dechiffre);
				// dataOut.writeUTF(singnature);

				textArea.setText(textArea.getText().trim() + "\n" + dechiffre);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the application.
	 */
	public Client() {

		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setTitle("Client ");
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
					frame.dispose();
				}
			}
		});
		panelsouth.add(textField);
		textField.setColumns(10);

		JButton btnEnvoyer = new JButton("envoyer");
		btnEnvoyer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String messageOut = "";
				try {
					messageOut = textField.getText().trim();

					// singnature
					// String singnature = outil.signature(privateKey, messageOut);
					// dataOut.writeUTF(singnature);

					// Chiffrement du message
					String chiffre = outil.chiffrement(pubKeyServeur, messageOut);

					dataOut.writeUTF(chiffre);

					textField.setText("");

				} catch (Exception e2) {
					e2.printStackTrace();
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
