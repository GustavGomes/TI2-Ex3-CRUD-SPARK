package conexao.postgres;

import static spark.Spark.*;


public class Program {
private static PessoaService pessoaService = new PessoaService();
	
    public static void main(String[] args) {
        port(6789);
        
        staticFiles.location("/public");
        
        post("/pessoa/insert", (request, response) -> pessoaService.insert(request, response));

        get("/pessoa/:id", (request, response) -> pessoaService.get(request, response));
        
        get("/pessoa/list/:orderby", (request, response) -> pessoaService.getAll(request, response));

        get("/pessoa/update/:id", (request, response) -> pessoaService.getToUpdate(request, response));
        
        post("/pessoa/update/:id", (request, response) -> pessoaService.update(request, response));
           
        get("/pessoa/delete/:id", (request, response) -> pessoaService.delete(request, response));

             
    }
}
