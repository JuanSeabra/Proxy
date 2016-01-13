import java.io.*;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
	
	public int porta;
	public String arquivo; //buffer para ler o arquivo
	public boolean firewall;
	
	public Servidor(int porta, boolean firewall) {
		this.porta = porta;
		this.firewall = firewall;
	}
	
	public Servidor(int porta, String arquivo, boolean firewall) {
		this.porta = porta;
		this.arquivo = arquivo;
		this.firewall = firewall;
	}
	
	public List<String> getLinhasRequisicao(BufferedReader in) {
		List<String> linhas = new ArrayList<>();
		String inputLine;
		
		try {
			while ((inputLine = in.readLine()) != null) { 
				if (inputLine.equals("")) break;
	           	linhas.add(inputLine);	              
	           	System.out.println(inputLine);
	        }
		}
		catch (IOException e) {
			return null;
		}		
		
		return linhas;
	}
	
	public void enviarRequisicao(PrintWriter writer, List<String> linhas) {
		for (int i = 0; i < linhas.size(); i++) {
			writer.println(linhas.get(i));						
		}					
		writer.println("");
		writer.flush();
	}
	
	public void acessoProibido(InputStream resposta, PrintWriter out) {		    		
		out.write("HTTP/1.0 403 Forbidden\r\n");
        out.write("Connection: close\r\n");
        out.write("\r\n");			
		out.flush();
	}

	public void executaServidor() throws IOException {		
		
		System.out.println("Executando");
		System.out.println("Aguardando conexões...");	
		ServerSocket serverSocket = null;

		try  {
			//Cria um socket para a porta passada por parametro
			serverSocket = new ServerSocket(this.porta);        	

			while(true) {
				//Aguarda ate que um cliente se conecte e estabeleca conexao na porta
				Socket clientSocket = serverSocket.accept();  
				
				//Streams de destino e fonte referentes ao cliente
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);                   
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				
				//Aqui serao armazenadas a linha da requisicao enviada pelo servidor				
				List<String> linhas = new ArrayList<>();
				//Este vetor armazena os campos da primeira linha da requisicao
				//Os campos sao, em ordem: metodo, URL e versao HTTP
				String[] campos; 
				String endereco = new String();
				
				try {
					System.out.println("Requisição recebida:\n");
					linhas = this.getLinhasRequisicao(in);				
					campos = linhas.get(0).split(" ");
					//verifica se a URL contem o protocolo HTTP
					//senao, adiciona-o a ela
					if (!campos[1].contains("http")) {
						campos[1] = "http://" + campos[1];
					}	
					
					endereco = campos[1];
				}
				//caso ocorra uma excecao, fecha a conexao com o cliente
				catch (ArrayIndexOutOfBoundsException e) {					
					clientSocket.close();
				}
				catch (NullPointerException e) {
					clientSocket.close();
				}										
				
				//cria um objeto URL a partir da String obtida no vetor anterior 
				URL url = new URL(endereco);  
				
				//A seguir, a conexao do proxy com o host desejado eh feita
				try {              	
					//socket que representa a conexao do proxy com um server, na porta padrao (80)
					//a funcao url.getHost() retorna o host obtido a partir da URL anterior
					Socket socket = new Socket(url.getHost(), 80);
					
					//Stream responsavel por preparar a requisicao HTTP feita ao proxy e envia-la ao server
					PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));	
					
					this.enviarRequisicao(writer, linhas);					
					
					//Aqui, a resposta do server sera repassada ao cliente do proxy
					
					//Streams auxiliares para a formatacao da resposta
					DataOutputStream resp = new DataOutputStream(clientSocket.getOutputStream());					
					InputStream resposta = socket.getInputStream();
					ByteArrayOutputStream saida = new ByteArrayOutputStream();			
					byte[] buffer = new byte[16384];
					int n;							
					
					while(-1 != (n = resposta.read(buffer))) {
						saida.write(buffer, 0, n);							
					}						
					
					//vetor de bytes contendo a resposta 
					byte[] dados = saida.toByteArray();			
					
					System.out.println("\nResposta:\n");
					
					//se o arquivo foi passado por parametro, eh ativada a verificacao de paginas
					//senao, repassa a resposta normalmente
					if (firewall) {
						byte[] data = dados.clone();
						String conteudoResp = new String(data, "UTF-8");
						conteudoResp = conteudoResp.toLowerCase();
						
						VerificaStrings verificador = new VerificaStrings(conteudoResp, this.arquivo);				
						
						//validar conteudo
						if (verificador.paginaContemBadWord()) {
							System.out.println("Este endereço possui conteúdo proibido.");
							this.acessoProibido(resposta, out);
						}
						else {
							System.out.println("Solicitação concluída.");
							resp.write(dados);
						}		
					}
					else {
						System.out.println("Solicitação concluída.");
						resp.write(dados);						
					}
					
					//Encerra os streams
					saida.close();
					resposta.close();
					resp.close();
					out.close();
					in.close();
					
					//Encerra os sockets
					socket.close();
					clientSocket.close();        
				}
				catch (MalformedURLException e) {
					System.out.println("URL mal formatada!");
				}     
			}
		} catch (IOException e) {        	
			System.out.println("Exceção ao tentar ouvir a porta "
					+ this.porta);
			
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {
				System.out.println("Não foi possível fechar o socket");
			}
			catch (NullPointerException e2) {
				System.out.println("Não foi possível fechar o socket");
			}

		}

	}

}
