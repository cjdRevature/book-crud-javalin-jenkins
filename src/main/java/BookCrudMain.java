import java.time.LocalDate;

import org.postgresql.util.PSQLException;

import exception.ApplicationException;
import io.javalin.Javalin;
import pojo.BookPojo;
import pojo.ErrorPojo;
import service.BookService;
import service.BookServiceImpl;

public class BookCrudMain {

	public static void main(String[] args) {
		
		BookService bookService = new BookServiceImpl();
		Javalin server = Javalin.create((config) -> config.enableCorsForAllOrigins()).start(7474);
		
		//pathParam - http://localhost:4040/api/books/5/Comedy
		//queryParam - http://localhost:4040/api/books/bookId=5&bookGenre=Comedy
		
		//http://localhost:4040/hello
		server.get("hello", (ctx) -> {
			// tell here what to do when the hello endpoint is requested for
			System.out.println("Hello endpoint is requested!!");
			ctx.result("Hello endpoint is requested!!");
		});

		// get endpoint to fetch all the books
		// http://localhost:4040/api/books
		server.get("/books" , (ctx) -> {
			ctx.json(bookService.getAllBooks());
			//System.out.println("get all books!");
			
			
		});
		
		// get endpoint to fetch one book
		// http://localhost:4040/api/books/5
		// 5 is a pathParam and they are given in a {}
		server.get("/books/{bid}", (ctx) -> {
			ctx.json(bookService.getABook(Integer.parseInt(ctx.pathParam("bid"))));
		});
		
		// delete endpoint to delete a book
		// http://localhost:4040/api/books/5
		server.delete("/books/{bid}", (ctx) -> {
			bookService.deleteBook(Integer.parseInt(ctx.pathParam("bid")));
		});
		
		// post endpoint to add a book
		// http://localhost:4040/api/books
		server.post("/books", (ctx) -> {
			BookPojo returnBookPojo = bookService.addBook(ctx.bodyAsClass(BookPojo.class));
			ctx.json(returnBookPojo);
		});
		
		// put endpoint to update a book
		// http://localhost:4040/api/books
		server.put("/books/{bid}", (ctx) -> {
			BookPojo returnBookPojo = bookService.updateBook(ctx.bodyAsClass(BookPojo.class));
			ctx.json(returnBookPojo);
		});
		
		server.exception(ApplicationException.class, (ae, ctx) -> {
			ErrorPojo error = new ErrorPojo();
			error.setErrorMessage(ae.getMessage());
			ctx.json(error).status(500);
		});
		
		
	}

}