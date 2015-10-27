package br.ufrj.tp.chat.cliente;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatCliente extends JFrame {

	private static final int porta = 12345;
	private static final String servidor = "127.0.0.1";
	JTextField textoParaEnviar;
	Socket socket;
	PrintWriter escritor;
	Scanner leitor;
	String nome;
	JTextArea textoRecebido;
	
	// Classe interna que ficará escutando o Servidor
	private class EscutaServidor implements Runnable {

		@Override
		public void run() {
			
			try{
				/*
				 * Nessa parte o Cliente ficará aguardando novas mensagens do Servidor.
				 * Uso o método nextLine() do Scanner para verificar isso.
				 * Derpois de receber a mensagem do servidor, coloco a mensagem na caixa de texto.
				 * 
				 * Obs: No leitor eu defino, abaixo, no método configurarRede(), que leitor é apenas
				 * o getInputStream do Socket de comunicação com o Servidor.
				 */
				String texto;
				while((texto = leitor.nextLine()) != null){
					//if(texto.contains(s))
					textoRecebido.append(texto + "\n");
				}
				
			} catch (Exception e){}
		}
		
	}
	
	public ChatCliente(String nome){
		
		/*
		 * Só a interface gráfica...
		 * */
		
		super("Chat : " + nome);
		this.nome = nome;
		
		Font fonte = new Font("Serif", Font.PLAIN, 20);
		
		// Onde o Cliente vai digitar mensagens
		textoParaEnviar = new JTextField();
		textoParaEnviar.setFont(fonte);
		textoParaEnviar.addKeyListener(new EnviarEnter());
		
		// BOTAO ENVIO
		JButton botao = new JButton("Enviar");
		botao.setFont(fonte);
		botao.addActionListener(new EnviarListener());
		
		Container envio = new JPanel();
		envio.setLayout(new BorderLayout());
		envio.add(BorderLayout.CENTER, textoParaEnviar);
		envio.add(BorderLayout.EAST, botao);
		
		// AREA DO CHAT
		textoRecebido = new JTextArea();
		textoRecebido.setFont(fonte);
		textoRecebido.setEditable(false);
		JScrollPane scroll = new JScrollPane(textoRecebido);
		
		getContentPane().add(BorderLayout.CENTER,scroll);
		getContentPane().add(BorderLayout.SOUTH,envio);
		
		
		
		/*
		 * Opa...to chamando a classe de configuração de rede aqui!!!
		 * */
		configurarRede();
		
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500,500);
		setVisible(true);
		
	}

	/*
	 * Só pra pegar o Clique do Botão...não sei porque apertar o ENTER não funciona!!!
	 */
	private class EnviarListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) throws NullPointerException {
			escritor.println(nome + " falou: " + textoParaEnviar.getText()); // envia texto para o servidor
			escritor.flush(); // garante que a informacao foi encaminhada com sucesso
			textoParaEnviar.setText("");
			textoParaEnviar.requestFocus();
		}
		
	}
	
	
	/*
	 * Pra pegar o ENTER no Campo de Texto
	 * */
	private class EnviarEnter implements ActionListener, KeyListener {

		@Override
		public void keyPressed(KeyEvent arg0) {
			if(arg0.getKeyCode() == 10 && arg0.getSource() == textoParaEnviar){
				escritor.println(nome + " falou: " + textoParaEnviar.getText()); // envia texto para o servidor
				escritor.flush(); // garante que a informacao foi encaminhada com sucesso
				textoParaEnviar.setText("");
				textoParaEnviar.requestFocus();
			}
		}
		
		@Override
		public void keyTyped(KeyEvent arg0) {
			if(arg0.getKeyCode() == 10 && arg0.getSource() == textoParaEnviar){
				escritor.println(nome + " falou: " + textoParaEnviar.getText()); // envia texto para o servidor
				escritor.flush(); // garante que a informação foi encaminhada com sucesso
				textoParaEnviar.setText("");
				textoParaEnviar.requestFocus();		
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {}

		@Override
		public void actionPerformed(ActionEvent e) {}
		
	}
	
	
	/*
	 * Classe que Configura comunicação com o servidor
	 */
	private void configurarRede() {
		
		try {
			
			// Inicio o Socket e crio a conexão com o Servidor
			Socket socket = new Socket(this.servidor, this.porta);
			// Crio o escritor: que irá enviar as mensagens para o Servidor
			escritor =  new PrintWriter(socket.getOutputStream());
			// Crio o leitor: que irá receber as mensagens do Servidor
			leitor = new Scanner(socket.getInputStream());
			
			// Crio uma Thread para ficar lendo do servidor. (Tá lá em cima, no método run)
			new Thread(new EscutaServidor()).start();
			
			// Envia mensagem para o Servidor que o Cliente entrou na sala
			String msgNome = nome.toUpperCase();
			escritor.println("\n" + msgNome + " entrou na sala" + "\n");
			escritor.flush();
			
		} catch (IOException ioe){}
	}
	
	private void desconectar(){
		
		escritor.println("\n" + this.nome + "saiu da sala\n");
		escritor.flush();
		try {
			socket.close();
		} catch (IOException e) {}
	}

}
