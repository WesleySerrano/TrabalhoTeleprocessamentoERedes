package br.ufrj.tp.chat.servidor;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class ChatServidor {

	// ArrayList com os canais de comunicacao dos clientes conectados
	ArrayList<PrintWriter> clientes = new ArrayList<>();
	//private static Map<String, PrintStream> MAP_CLIENTES;
	//private String nomeCliente;
	//PrintWriter canaisClientes = new PrintWriter();
	private int porta = 12345;
	
	public ChatServidor(){
		
		ServerSocket server;
		
		try{
			
			// Levanto ServerSocket na porta especificada. Fica escutando por novos Clientes
			server = new ServerSocket(this.porta);
			System.out.println("Servidor rodando na porta " + this.porta + "\n");
			
			while(true){
				// O SocketServer retorna um Socket para se comunicar com o cliente que fez a requisição
				Socket socket = server.accept();  // aceita conexao do Cliente
				
				// Gera uma Thread para escutar as mensagens que chegam do Cliente
				new Thread(new EscutaCliente(socket)).start();
				
				// Guarda, no ArrayList, o canal de comunicao com o Cliente
				// Dessa forma o Servidor conseguirá enviar mensagens para os Clientes
				PrintWriter canaisClientes = new PrintWriter(socket.getOutputStream());
				clientes.add(canaisClientes);
				
				//MAP_CLIENTES.put(this.nomeCliente, escritor);
				
			}
			
		} catch (IOException e){}
		
		
		
	}
	
	// Método que envia a mensagem para todos os Clientes conectados.
	// Como dito no comentário acima, tenho um ArrayList com os canais de comunicação
	// de cada Cliente. Basta fazer uso do Método println para que os Clientes recebam
	// as mensagens.
	private void encaminharParaTodos(String texto){
		for (PrintWriter p : clientes) {
			try{
				p.println(texto);
				p.flush();
			} catch (Exception e){}
		}
	}
	
	
	// Classe interna que ficará escutando um Cliente específico.
	// Quando a Thread é lançada, ela chama essa Classe, que é responsável por ficar escutando as mensagens
	// que chegam daquele Cliente específico
	private class EscutaCliente implements Runnable {
		Scanner leitor;
		
		// Cada Cliente do Chat tera o seu Scanner de leitura.
		// A classe Scanner ajuda a gerenciar as mensagens vindas do Cliente específico.
		// O construtor de EscutaCliente, cria a variavel leitor que guardará as mensagens
		// que estão entrando no servidor. O método getInputStream da classe Socket faz isso.
		public EscutaCliente(Socket socket){
			try {
				// leitor que pega as mensagens enviadas pela "Thread do Cliente".
				leitor = new Scanner(socket.getInputStream());
			} catch (IOException e) {}
			
		}

		// Esse método run só pega o que o leitor está lendo e encaminha para todos
		// os outros Clientes, através do método encaminharParaTodos.
		// O método encaminharParaTodos faz uso do getOutputStream.
		@Override
		public void run() {
			
			try {
				String texto;
				
				while((texto = leitor.nextLine()) != null){
					System.out.println(texto);
					encaminharParaTodos(texto);
				}
				
			} catch(Exception e){}
			
		}
		
	}
	
	
	// Inicia servidor
	public static void main(String[] args) {
		new ChatServidor();
	}

}
