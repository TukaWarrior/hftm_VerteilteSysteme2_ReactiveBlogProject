package ch.hftm.boundary;

import java.util.List;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import ch.hftm.model.BlogDTO;
import ch.hftm.repository.BlogRepository;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/blogs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BlogResource {

    @Inject
    BlogRepository blogRepository;

    @Inject
    @Channel("blogs-out") // Outgoing Kafka channel
    Emitter<BlogDTO> blogEmitter;
    
    @POST
    @WithTransaction
    public Uni<Response> createBlog(BlogDTO blogDTO) {
        return blogRepository.createBlog(blogDTO)
            .onItem().invoke(createdBlog -> blogEmitter.send(createdBlog))
            .onItem().transform(createdBlog -> Response.status(Response.Status.CREATED).entity(createdBlog).build());
    }

    @GET
    @WithSession
    public Uni<List<BlogDTO>> getAllBlogs() {
        return blogRepository.getAllBlogs();
    }

    @GET
    @Path("/{id}")
    @WithSession
    public Uni<Response> getBlogById(@PathParam("id") Long id) {
        return blogRepository.getBlogById(id)
                .onItem().ifNotNull().transform(blogDTO -> Response.ok(blogDTO).build())
                .onItem().ifNull().continueWith(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Path("/search/title/{title}")
    @WithSession
    public Uni<List<BlogDTO>> searchBlogsByTitle(@PathParam("title") String title) {
        return blogRepository.searchBlogByTitle(title);
    }

    @GET
    @Path("/search/content/{content}")
    @WithSession
    public Uni<List<BlogDTO>> searchBlogsByContent(@PathParam("content") String content) {
        return blogRepository.searchBlogsByContent(content);
    }

    @GET
    @Path("/pipeline")
    @Produces(MediaType.TEXT_PLAIN)
    @WithSession
    public Uni<String> customPipeline() {
        return Uni.createFrom().item("Start")
            .onItem().transform(item -> item + " -> Step 1")
            .onItem().transform(item -> item + " -> Step 2")
            .onItem().transform(item -> item + " -> Done");
    }
}