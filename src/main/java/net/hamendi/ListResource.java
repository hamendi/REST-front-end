package net.hamendi;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Root resource (exposed at "list" path)
 *
 * Put is meant to map to a specific uri and be idempotent, it will not be
 * therefore used, instead I will use the POST HTTP method!
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
        this.list = new LockLessLinkedListTailLIFO<>();
        return Response.ok().entity(true).build();
    }

    @GET //pop an element off the tail of the list, or CRUD's 'Read'
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("read") //http://localhost:8080/restful-list-service/api/list/read
    public Response readElement() {
        if(this.list == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().entity(this.list.pop()).build();
    }

    @POST //push an element to the end of the list, or CRUD's 'Update'
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("update/{element}") //http://localhost:8080/restful-list-service/api/list/update/<element>
    public BooleanResponse updateList(@PathParam("element") Object element) { /////cant be object in PathParam, either or
//        if (element == null) {
//            return Response.status(Response.Status.BAD_REQUEST).build();
//        }
        BooleanResponse response = new BooleanResponse();
        response.setResult(this.list.push(element));
        return response;
    }
//
//    @POST //insert an element after another element in the list, or another variation of CRUD's 'Update'
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    @Path("insert/{element}/after/{after}") //http://localhost:8080/restful-list-service/api/list/insert/<element>/after/<after>
//    public Response updateListInsert(@PathParam("element") Object element, @PathParam("after") Object after) {
//        if (element == null || after == null) {
//            return Response.status(Response.Status.BAD_REQUEST).build();
//        }
//        return Response.ok().entity(list.insertAfter(element, after)).build();
//    }

    @DELETE //delete the list, or CRUD's 'Delete'
    @Produces(MediaType.APPLICATION_JSON)
    @Path("delete") //http://localhost:8080/restful-list-service/api/list/delete
    public Response deleteList() {
        this.list = null;
        if(this.list == null) {
            return Response.ok().entity(true).build();
        }
        return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
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
