package conexao.postgres;
import java.sql.*;


public class DAO {
	private Connection conexao;
	
	public DAO() {
		conexao = null;
	}
	
	public boolean conectar() {
		String driverName = "org.postgresql.Driver";
		String serverName = "localhost";
		String mydatabase = "02ConexaoPostgres";
		int porta = 5432; 
		String url = "jdbc:postgresql://" + serverName + ":" + porta +"/" + mydatabase;
		String username = "ti2cc";
		String password = "ti@cc";
		boolean status = false;
		
		try {
			Class.forName(driverName);
			conexao = DriverManager.getConnection(url, username, password);
			status = (conexao == null);
			System.out.println("Conexão efetuada com o postgres!");
		} catch (ClassNotFoundException e) { 
			System.err.println("Conexão NÃO efetuada com o postgres -- Driver não encontrado -- " + e.getMessage());
		} catch (SQLException e) {
			System.err.println("Conexão NÃO efetuada com o postgres -- " + e.getMessage());
		}
		
		return status;
	}
	
	public boolean close() {
        boolean status = false;
		
		try {
			conexao.close();
			status = true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return status;
	}
	
	public boolean inserirPessoa(Pessoa pessoa) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement();
			st.executeUpdate("INSERT INTO pessoa (id, nome, data_nasc, telefone) "
					       + "VALUES ("+pessoa.getId()+ ", '" + pessoa.getNome() + "', '"  
					       + pessoa.getData() + "', '" + pessoa.getTelefone() + "');");
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
	
	public boolean atualizarPessoa(Pessoa pessoa) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement();
			String sql = "UPDATE pessoa SET nome = '" + pessoa.getNome() + "', data_nasc = '"  
				       + pessoa.getData() + "', telefone = '" + pessoa.getTelefone() + "'"
					   + " WHERE id = " + pessoa.getId();
			st.executeUpdate(sql);
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
	
	
	public Pessoa getPessoa(int id) {
		boolean status = false;
		Pessoa pessoa = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM pessoa WHERE id="+id;
			ResultSet rs = st.executeQuery(sql);	
	        if(rs.next()){            
	        	 pessoa = new Pessoa(rs.getInt("id"), rs.getString("nome"), rs.getString("data_nasc"), 
	                				   rs.getString("telefone"));
	        }
	        st.close();
	        status = true;
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return pessoa;
	}
	
	public boolean excluirPessoa(int id) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement();
			st.executeUpdate("DELETE FROM pessoa WHERE id = " + id);
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
	
	public Pessoa[] getPessoas() {
		Pessoa[] pessoas = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery("SELECT * FROM pessoa");		
	         if(rs.next()){
	             rs.last();
	             pessoas = new Pessoa[rs.getRow()];
	             rs.beforeFirst();

	             for(int i = 0; rs.next(); i++) {
	                pessoas[i] = new Pessoa(rs.getInt("id"), rs.getString("nome").trim(), 
	                		                  rs.getString("data_nasc").trim(), rs.getString("telefone").trim());
	             }
	          }
	          st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return pessoas;
	}
	
	
}
