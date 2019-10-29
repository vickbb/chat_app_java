package servidor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Servidor extends Thread {
	
	private static ArrayList<BufferedWriter>clientes;           
	private static ServerSocket serverSocket; 
	private String nome;
	private Socket socket;
	private InputStream inputStream;  
	private InputStreamReader inputStreamReader;  
	private BufferedReader bufferedReader;
	
	public Servidor(Socket socket){
	   this.socket = socket;
	   try {
	         inputStream  = socket.getInputStream();
	         inputStreamReader = new InputStreamReader(inputStream);
	         bufferedReader = new BufferedReader(inputStreamReader);
	   } catch (IOException e) {
	          e.printStackTrace();
	   }                          
	}

	public void run(){
        
	    try{                             
		    String mensagem;
		    OutputStream outputStream =  this.socket.getOutputStream();
		    Writer outputStreamWriter = new OutputStreamWriter(outputStream);
		    BufferedWriter buffferedWriter = new BufferedWriter(outputStreamWriter); 
		    clientes.add(buffferedWriter);
		    nome = mensagem = bufferedReader.readLine();
		               
		    while(!"Sair".equalsIgnoreCase(mensagem) && mensagem != null){           
		    	mensagem = bufferedReader.readLine();
			    enviarParaTodos(buffferedWriter, mensagem);
			    System.out.println(mensagem);
		    }
	    }catch (Exception e) {
	     e.printStackTrace();
	    }                       
	}
	
	public void enviarParaTodos(BufferedWriter bufferedWriterEntrada, String mensagem) throws  IOException {
	  BufferedWriter bufferedWriterSaida;
	    
	  for(BufferedWriter bufferedWriter : clientes){
	   bufferedWriterSaida = (BufferedWriter)bufferedWriter;
	   if(!(bufferedWriterEntrada == bufferedWriterSaida)){
	     bufferedWriter.write(nome + " -> " + mensagem+"\r\n");
	     bufferedWriter.flush(); 
	   }
	  }          
	}

	public static void main(String []args) {
	    
	  try{
	    //Cria os objetos necessário para instânciar o servidor
	    JLabel labelMensagem = new JLabel("Porta do Servidor:");
	    JTextField textoPorta = new JTextField("12345");
	    Object[] textos = {labelMensagem, textoPorta };  
	    JOptionPane.showMessageDialog(null, textos);
	    serverSocket = new ServerSocket(Integer.parseInt(textoPorta.getText()));
	    clientes = new ArrayList<BufferedWriter>();
	    JOptionPane.showMessageDialog(null,"Servidor ativo na porta: "+         
	    textoPorta.getText());
	    
	     while(true){
	       System.out.println("Aguardando conexão...");
	       Socket socket = serverSocket.accept();
	       System.out.println("Cliente conectado...");
	       Thread t = new Servidor(socket);
	        t.start();   
	    }
	                              
	  }catch (Exception e) {
	    
	    e.printStackTrace();
	  }                       
	}  
}
