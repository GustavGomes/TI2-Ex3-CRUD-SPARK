package conexao.postgres;

import java.util.Scanner;
import java.io.File;
import spark.Request;
import spark.Response;

public class PessoaService {
	private DAO pessoaDAO = new DAO();
	private String form;
	private final int FORM_INSERT = 1;
	private final int FORM_DETAIL = 2;
	private final int FORM_UPDATE = 3;
	private final int FORM_ORDERBY_ID = 1;
	private final int FORM_ORDERBY_DESCRICAO = 2;
	private final int FORM_ORDERBY_PRECO = 3;
	
	
	public PessoaService() {
		makeForm();
	}

	
	public void makeForm() {
		makeForm(FORM_INSERT, new Pessoa(), FORM_ORDERBY_DESCRICAO);
	}

	
	public void makeForm(int orderBy) {
		makeForm(FORM_INSERT, new Pessoa(), orderBy);
	}

	
	public void makeForm(int tipo, Pessoa pessoa, int orderBy) {
		pessoaDAO.conectar();
		String nomeArquivo = "form.html";
		form = "";
		try{
			Scanner entrada = new Scanner(new File(nomeArquivo));
		    while(entrada.hasNext()){
		    	form += (entrada.nextLine() + "\n");
		    }
		    entrada.close();
		}  catch (Exception e) { System.out.println(e.getMessage()); }
		
		String umaPessoa = "";
		if(tipo != FORM_INSERT) {
			umaPessoa += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umaPessoa += "\t\t<tr>";
			umaPessoa += "\t\t\t<td align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;<a href=\"/pessoa/list/1\">Novo Pessoa</a></b></font></td>";
			umaPessoa += "\t\t</tr>";
			umaPessoa += "\t</table>";
			umaPessoa += "\t<br>";			
		}
		
		if(tipo == FORM_INSERT || tipo == FORM_UPDATE) {
			String action = "/pessoa/";
			String name, nome, buttonLabel;
			if (tipo == FORM_INSERT){
				action += "insert";
				name = "Inserir Pessoa";
				nome = "Ana, Pedro, ...";
				buttonLabel = "Inserir";
			} else {
				action += "update/" + pessoa.getId();
				name = "Atualizar Pessoa (ID " + pessoa.getId() + ")";
				nome = pessoa.getNome();
				buttonLabel = "Atualizar";
			}
			umaPessoa += "\t<form class=\"form--register\" action=\"" + action + "\" method=\"post\" id=\"form-add\">";
			umaPessoa += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umaPessoa += "\t\t<tr>";
			umaPessoa += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;" + name + "</b></font></td>";
			umaPessoa += "\t\t</tr>";
			umaPessoa += "\t\t<tr>";
			umaPessoa += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
			umaPessoa += "\t\t</tr>";
			umaPessoa += "\t\t<tr>";
			umaPessoa += "\t\t\t<td>&nbsp;Descrição: <input class=\"input--register\" type=\"text\" name=\"nome\" value=\""+ nome +"\"></td>";
			umaPessoa += "\t\t\t<td>Preco: <input class=\"input--register\" type=\"text\" name=\"data\" value=\""+ pessoa.getData() +"\"></td>";
			umaPessoa += "\t\t\t<td>Quantidade: <input class=\"input--register\" type=\"text\" name=\"telefone\" value=\""+ pessoa.getTelefone() +"\"></td>";
			umaPessoa += "\t\t</tr>";
			umaPessoa += "\t\t<tr>";
			umaPessoa += "\t\t\t<td align=\"center\"><input type=\"submit\" value=\""+ buttonLabel +"\" class=\"input--main__style input--button\"></td>";
			umaPessoa += "\t\t</tr>";
			umaPessoa += "\t</table>";
			umaPessoa += "\t</form>";		
		} else if (tipo == FORM_DETAIL){
			umaPessoa += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umaPessoa += "\t\t<tr>";
			umaPessoa += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Detalhar Pessoa (ID " + pessoa.getId() + ")</b></font></td>";
			umaPessoa += "\t\t</tr>";
			umaPessoa += "\t\t<tr>";
			umaPessoa += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
			umaPessoa += "\t\t</tr>";
			umaPessoa += "\t\t<tr>";
			umaPessoa += "\t\t\t<td>&nbsp;Nome: "+ pessoa.getNome() +"</td>";
			umaPessoa += "\t\t\t<td>Data: "+ pessoa.getData() +"</td>";
			umaPessoa += "\t\t\t<td>Telefone: "+ pessoa.getTelefone() +"</td>";
			umaPessoa += "\t\t</tr>";
			umaPessoa += "\t\t<tr>";
			umaPessoa += "\t\t\t<td>&nbsp;</td>";
			umaPessoa += "\t\t</tr>";
			umaPessoa += "\t</table>";		
		} else {
			System.out.println("ERRO! Tipo não identificado " + tipo);
		}
		form = form.replaceFirst("<UM-PRODUTO>", umaPessoa);
		
		String list = new String("<table width=\"80%\" align=\"center\" bgcolor=\"#f3f3f3\">");
		list += "\n<tr><td colspan=\"6\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Relação de Produtos</b></font></td></tr>\n" +
				"\n<tr><td colspan=\"6\">&nbsp;</td></tr>\n" +
    			"\n<tr>\n" + 
        		"\t<td><a href=\"/pessoa/list/" + FORM_ORDERBY_ID + "\"><b>ID</b></a></td>\n" +
        		"\t<td><a href=\"/pessoa/list/" + FORM_ORDERBY_DESCRICAO + "\"><b>Descrição</b></a></td>\n" +
        		"\t<td><a href=\"/pessoa/list/" + FORM_ORDERBY_PRECO + "\"><b>Preço</b></a></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Detalhar</b></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Atualizar</b></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Excluir</b></td>\n" +
        		"</tr>\n";
		
		Pessoa[] pessoas;
		pessoas = pessoaDAO.getPessoas();

		int i = 0;
		String bgcolor = "";
		for (Pessoa p : pessoas) {
			bgcolor = (i++ % 2 == 0) ? "#fff5dd" : "#dddddd";
			list += "\n<tr bgcolor=\""+ bgcolor +"\">\n" + 
            		  "\t<td>" + p.getId() + "</td>\n" +
            		  "\t<td>" + p.getNome() + "</td>\n" +
            		  "\t<td>" + p.getData() + "</td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"/pessoa/" + p.getId() + "\"><img src=\"/image/detail.png\" width=\"20\" height=\"20\"/></a></td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"/pessoa/update/" + p.getId() + "\"><img src=\"/image/update.png\" width=\"20\" height=\"20\"/></a></td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"javascript:confirmarDeleteProduto('" + p.getId() + "', '" + p.getNome() + "', '" + p.getData() + "');\"><img src=\"/image/delete.png\" width=\"20\" height=\"20\"/></a></td>\n" +
            		  "</tr>\n";
		}
		list += "</table>";		
		form = form.replaceFirst("<LISTAR-PRODUTO>", list);				
	}
	
	
	public Object insert(Request request, Response response) {
		String nome = request.queryParams("nome");
		String data = request.queryParams("data");
		String telefone = request.queryParams("telefone");
		
		String resp = "";
		
		Pessoa pessoa = new Pessoa(-1, nome, data, telefone);
		
		if(pessoaDAO.inserirPessoa(pessoa) == true) {
            resp = "Pessoa (" + nome + ") inserido!";
            response.status(201); // 201 Created
		} else {
			resp = "Pessoa (" + nome + ") não inserido!";
			response.status(404); // 404 Not found
		}
			
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}

	
	public Object get(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));		
		Pessoa pessoa = (Pessoa) pessoaDAO.getPessoa(id);
		
		if (pessoa != null) {
			response.status(200); // success
			makeForm(FORM_DETAIL, pessoa, FORM_ORDERBY_DESCRICAO);
        } else {
            response.status(404); // 404 Not found
            String resp = "Pessoa " + id + " não encontrado.";
    		makeForm();
    		form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
        }

		return form;
	}

	
	public Object getToUpdate(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));		
		Pessoa pessoa = (Pessoa) pessoaDAO.getPessoa(id);
		
		if (pessoa != null) {
			response.status(200); // success
			makeForm(FORM_UPDATE, pessoa, FORM_ORDERBY_DESCRICAO);
        } else {
            response.status(404); // 404 Not found
            String resp = "Pessoa " + id + " não encontrado.";
    		makeForm();
    		form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
        }

		return form;
	}
	
	
	public Object getAll(Request request, Response response) {
		int orderBy = Integer.parseInt(request.params(":orderby"));
		makeForm(orderBy);
	    response.header("Content-Type", "text/html");
	    response.header("Content-Encoding", "UTF-8");
		return form;
	}			
	
	public Object update(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
		Pessoa pessoa = pessoaDAO.getPessoa(id);
        String resp = "";       

        if (pessoa != null) {
        	pessoa.setNome(request.queryParams("nome"));
        	pessoa.setData(request.queryParams("preco"));
        	pessoa.setTelefone(request.queryParams("quantidade"));
        	pessoaDAO.atualizarPessoa(pessoa);
        	response.status(200); // success
            resp = "Pessoa (ID " + pessoa.getId() + ") atualizado!";
        } else {
            response.status(404); // 404 Not found
            resp = "Pessoa (ID \" + pessoa.getId() + \") não encontrado!";
        }
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}

	
	public Object delete(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Pessoa pessoa = pessoaDAO.getPessoa(id);
        String resp = "";       

        if (pessoa != null) {
            pessoaDAO.excluirPessoa(id);
            response.status(200); // success
            resp = "Pessoa (" + id + ") excluído!";
        } else {
            response.status(404); // 404 Not found
            resp = "Pessoa (" + id + ") não encontrado!";
        }
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}
}
