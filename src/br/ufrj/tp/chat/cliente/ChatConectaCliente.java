package br.ufrj.tp.chat.cliente;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

/*
 * 
 * 
 * ESSA É A CLASSE QUE DEVE SER CHAMADA NO INÍCIO!!!
 * 
 * 
 * 
 */


public class ChatConectaCliente extends JFrame implements ActionListener{

	public JTextField nome;
	public static JButton btnOK;
	public static JButton btnSair;
	
	public ChatConectaCliente(){
		super("TP Chat ::: Identificacao");
		
		/* Pessoal, se vocês puderem, adicionem mais duas caixas de texto aqui:
		 * uma para pegar o IP do servidor e outra para a porta...
		 * 
		 * Daí, na chamada lá embaixo, no método actionPerformed a gente pode
		 * passar eles como parâmetros para a Classe ChatClient
		 * */
		
		
		//Container serverPort = new JPanel();
		//serverPort.setLayout(new BorderLayout());
		//painel1.setLayout(new BoxLayout(painel1, BoxLayout.Y_AXIS));
		//serverPort.add(BorderLayout.NORTH,servidor);
		//serverPort.add(BorderLayout.NORTH,portaServidor);
		
		
		nome = new JTextField();
		nome.setText("");
		nome.requestFocus();
		
		btnOK = new JButton("Conectar");
		btnOK.addActionListener(this);
		btnSair = new JButton("Sair");
		btnSair.addActionListener(this);
		
		JPanel gridLogin = new JPanel();
		gridLogin.setLayout(new GridLayout(2,2, 10, 10));
		gridLogin.add(new JLabel("Nome:       "));
		gridLogin.add(nome);
		
		
		gridLogin.setMaximumSize(new Dimension(300,70));
		gridLogin.setPreferredSize(new Dimension(300,50));
		
		JPanel painel2 = new JPanel();
		painel2.setLayout(new BoxLayout(painel2, BoxLayout.Y_AXIS));
		painel2.add(Box.createVerticalGlue());
		painel2.add(gridLogin);
		painel2.add(Box.createVerticalGlue());
		
		JPanel painel3 = new JPanel();		
		JPanel botoes = new JPanel();
		painel3.setLayout(new BoxLayout(painel3, BoxLayout.Y_AXIS));
		painel3.add(new JSeparator());
		botoes.add(btnOK);
		botoes.add(btnSair);
		painel3.add(botoes);
		
		//this.add(painel1, BorderLayout.NORTH);
		this.add(painel2, BorderLayout.CENTER);
		this.add(painel3, BorderLayout.SOUTH);
		this.setSize(400,350);
		this.setLocation((java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize().width / 2)
				- (this.getWidth() / 2), (java.awt.Toolkit
				.getDefaultToolkit().getScreenSize().height / 2)
				- (this.getHeight() / 2));
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		new ChatConectaCliente();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		if(e.getSource() == ChatConectaCliente.btnSair){
			System.exit(0);
		}
		
		if(e.getSource() == ChatConectaCliente.btnOK){
			String clientId = this.nome.getText();
			// Aqui eu chamo a Classe que vai gerenciar o Cliente
			// Podem adicionar no contrutor dessa classe isso:
			// ChatCliente(clientId, servidor, porta)
			ChatCliente novoCliente = new ChatCliente(clientId);
			this.dispose();
		}
	}

}
