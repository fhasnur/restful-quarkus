package id.kawahedukasi.controller;

import id.kawahedukasi.service.ExportService;
import id.kawahedukasi.service.ItemService;
import net.sf.jasperreports.engine.JRException;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("/item")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemController {
    @Inject
    ItemService itemService;

    @Inject
    ExportService exportService;

    @GET
    public Response get() {
        return itemService.get();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long id){
        return itemService.get(id);
    }

    @GET
    @Path("/export")
    @Produces("application/pdf")
    public Response export() throws JRException {
        return exportService.exportItem();
    }

    @POST
    public Response post(Map<String, Object> request){
        return itemService.post(request);
    }

    @PUT
    @Path("/{id}")
    public Response put(@PathParam("id") Long id, Map<String, Object> request){
        return itemService.put(id, request);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id){
        return itemService.delete(id);
    }
}