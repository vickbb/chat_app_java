package cliente;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import javax.swing.*;                    

	
public class Cliente extends JFrame implements ActionListener, KeyListener {

  private static final long serialVersionUID = 1L;
  private JTextArea texto;
  private JTextField mensagem;
  private JButton botaoEnviar;
  private JButton botaoSair;
  private JLabel labelHistorico;
  private JLabel labelMensagem;
  private JPanel panelContent;
  private Socket socket;
  private OutputStream outputStream;
  private Writer ouw; 
  private BufferedWriter bufferedWriter;
  private JTextField IP;
  private JTextField porto;
  private JTextField nome;

  public Cliente() throws IOException{                  
    JLabel lblMessage = new JLabel("Verificar!");
    IP = new JTextField("127.0.0.1");
    porto = new JTextField("12345");
    nome = new JTextField("Cliente");                
    Object[] texts = {lblMessage, IP, porto, nome };  
    JOptionPane.showMessageDialog(null, texts);              
     panelContent = new JPanel();
     texto              = new JTextArea(10,20);
     texto.setEditable(false);
     texto.setBackground(new Color(240,240,240));
     mensagem                       = new JTextField(20);
     labelHistorico     = new JLabel("Histórico");
     labelMensagem        = new JLabel("Mensagem");
     botaoEnviar                     = new JButton("Enviar");
     botaoEnviar.setToolTipText("Enviar Mensagem");
     botaoSair           = new JButton("Sair");
     botaoSair.setToolTipText("Sair do Chat");
     botaoEnviar.addActionListener(this);
     botaoSair.addActionListener(this);
     botaoEnviar.addKeyListener(this);
     mensagem.addKeyListener(this);
     JScrollPane scroll = new JScrollPane(texto);
     texto.setLineWrap(true);  
     panelContent.add(labelHistorico);
     panelContent.add(scroll);
     panelContent.add(labelMensagem);
     panelContent.add(mensagem);
     panelContent.add(botaoSair);
     panelContent.add(botaoEnviar);
     panelContent.setBackground(Color.LIGHT_GRAY);                                 
     texto.setBorder(BorderFactory.createEtchedBorder(Color.BLUE,Color.BLUE));
     mensagem.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));                    
     setTitle(nome.getText());
     setContentPane(panelContent);
     setLocationRelativeTo(null);
     setResizable(false);
     setSize(250,300);
     setVisible(true);
     setDefaultCloseOperation(EXIT_ON_CLOSE);
  }

  public void conectar() throws IOException{
                            
    socket = new Socket(IP.getText(),Integer.parseInt(porto.getText()));
    outputStream = socket.getOutputStream();
    ouw = new OutputStreamWriter(outputStream);
    bufferedWriter = new BufferedWriter(ouw);
    bufferedWriter.write(nome.getText()+"\r\n");
    bufferedWriter.flush();
  }
  
  public void enviarMensagem(String msg) throws IOException{
                           
    if(msg.equals("Sair")){
      bufferedWriter.write("Desconectado \r\n");
      texto.append("Desconectado \r\n");
    }else{
      bufferedWriter.write(msg+"\r\n");
      texto.append( nome.getText() + " diz -> " +         mensagem.getText()+"\r\n");
    }
     bufferedWriter.flush();
     mensagem.setText("");        
  }

  public void escutar() throws IOException{
                           
   InputStream in = socket.getInputStream();
   InputStreamReader inr = new InputStreamReader(in);
   BufferedReader bfr = new BufferedReader(inr);
   String msg = "";
                           
    while(!"Sair".equalsIgnoreCase(msg))
                                      
       if(bfr.ready()){
         msg = bfr.readLine();
       if(msg.equals("Sair"))
         texto.append("Servidor caiu! \r\n");
        else
         texto.append(msg+"\r\n");         
        }
  }

  public void sair() throws IOException{
                          
   enviarMensagem("Sair");
   bufferedWriter.close();
   ouw.close();
   outputStream.close();
   socket.close();
  }

  public void actionPerformed(ActionEvent e) {
            
    try {
      if(e.getActionCommand().equals(botaoEnviar.getActionCommand()))
          enviarMensagem(mensagem.getText());
      else
          if(e.getActionCommand().equals(botaoSair.getActionCommand()))
          sair();
      } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
      }                       
  }

  public void keyPressed(KeyEvent e) {
                  
      if(e.getKeyCode() == KeyEvent.VK_ENTER){
        try {
            enviarMensagem(mensagem.getText());
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }                                                          
    }                       
  }
    
  @Override
  public void keyReleased(KeyEvent arg0) {
    // TODO Auto-generated method stub               
  }
      
  @Override
  public void keyTyped(KeyEvent arg0) {
    // TODO Auto-generated method stub               
  }

  public static void main(String []args) throws IOException{
               
   Cliente app = new Cliente();
   app.conectar();
   app.escutar();
  }
}