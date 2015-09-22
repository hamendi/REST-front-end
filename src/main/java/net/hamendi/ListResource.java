package net.hamendi;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Root resource (exposed at "list" path)
 *
 * Put is meant to map to a specific uri and be idempotent, it will not be
 * therefore used, instead I will use the POST HTTP method.
 *
 * //http:localhost:8080/restful-list-service/api/list
 *
 * @author hamendi
 */
@Singleton
@Path("list")
public class ListResource {

    private volatile LockLessLinkedListTailLIFO<Object> list;

    @GET //instantiate the list, or CRUD's 'Create'
    @Produces(MediaType.APPLICATION_JSON)
    @Path("create") //http://localhost:8080/restful-list-service/api/list/create
    public Response createList() {
        if (this.list == null) {
            this.list = new LockLessLinkedListTailLIFO<>();
        }
        //HTTP Status 200 - OK
        return Response.ok(true, MediaType.APPLICATION_JSON_TYPE).build();
    }

    @GET //pop an element off the tail of the list, or CRUD's 'Read'
    @Produces(MediaType.APPLICATION_JSON)
    @Path("read") //http://localhost:8080/restful-list-service/api/list/read
    public Response readElement() {
        if (this.list == null) {
            //HTTP Status 404 - Not Found
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        //HTTP Status 200 - OK
        return Response.ok(this.list.pop(), MediaType.APPLICATION_JSON_TYPE).build();
    }

    @POST //push an element to the end of the list, or CRUD's 'Update'
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("update") //http://localhost:8080/restful-list-service/api/list/update/
    public Response updateList(Object dto) {
        if (this.list == null || dto == null) {
            //HTTP Status 400 - Bad Request
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        //HTTP Status 200 - OK
        return Response.ok(this.list.push(dto), MediaType.APPLICATION_JSON_TYPE).build();
    }

    @POST //insert an element after another element in the list, or another variation of CRUD's 'Update'
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("insert") //http://localhost:8080/restful-list-service/api/list/insert/
    public Response updateListInsert(List<Object> dto) {
        if (this.list == null || dto == null || dto.size() != 2) {
            //HTTP Status 400 - Bad Request
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        //HTTP Status 200 - OK
        return Response.ok(list.insertAfter(dto.get(0), dto.get(1)), MediaType.APPLICATION_JSON_TYPE).build();
    }

    @DELETE //delete the list, or CRUD's 'Delete'
    @Produces(MediaType.APPLICATION_JSON)
    @Path("delete") //http://localhost:8080/restful-list-service/api/list/delete
    public Response deleteList() {
        if (this.list != null) {
            this.list = null;
        }
        //HTTP Status 200 - OK
        return Response.ok(true, MediaType.APPLICATION_JSON_TYPE).build();
    }

	@GET
    @Produces(MediaType.TEXT_HTML)
    public String intro() {
        String intro =
                "<html>\n" +
                "    <body>\n" +
                "        <h2>RESTful API for the Lock Free Tail LIFO Singly Linked List</h2>\n" +
                "        <br/>\n" +
                "        <p>1. <a href=\"list/create\"><b>Create</b> a list</a></p>\n" +
                "        <p>2. <a href=\"list/read\"><b>Read</b> an element off the list (pop)</a></p>\n" +
                "        <p>3. <a href=\"list/update/<element>\"><b>Update</b> the list with an element (push)</a></p>\n" +
                "        <p>4. <a href=\"list/insert/<element>/after/<after>\"><b>Update</b> the list with an element after another (insertAfter)</a></p>\n" +
                "        <p>4. <a href=\"list/delete\"><b>Delete</b> the list</a></p>\n" +
                "        <br/>\n" +
                "        <h2>Mohammed Hamendi</h2>\n" +
                "    </body>\n" +
                "</html>";
        return intro;
    }
}
