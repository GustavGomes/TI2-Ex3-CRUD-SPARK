package conexao.postgres;

public class Pessoa {
	private int id;
	private String nome;
	private String data;
	private String telefone;
	
	public Pessoa() {
		this.setId(-1);
		this.setNome("");
		this.setData("");
		this.setTelefone("");
	}
	
	public Pessoa(int id, String nome, String data, String telefone) {
		this.setId(id);
		this.setNome(nome);
		this.setData(data);
		this.setTelefone(telefone);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	public String toString() {
		return "Pessoa [id = " + id + ", nome = " + nome + ", dataNasc = " + data + ", telefone = " + telefone + "]";
	}
	
}

