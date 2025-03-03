package ch.hftm.boundary;

import java.util.List;

import ch.hftm.model.Blog;
import ch.hftm.repository.BlogRepository;
import io.smallrye.mutiny.Multi;
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

    @POST
    public Blog createBlog(Blog blog) {
        blogRepository.persist(blog);
        return blog;
    }

    @GET
    public List<Blog> getAllBlogs() {
        return blogRepository.listAll();
    }

    @GET
    @Path("/pipeline")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> customPipeline() {
        return Uni.createFrom().item("Start")
                .onItem().transform(item -> item + " -> Step 1")
                .onItem().transform(item -> item + " -> Step 2")
                .onItem().transform(item -> item + " -> Done");
    }

    @GET
    @Path("/{id}")
    public Uni<Response> getBlogById(@PathParam("id") Long id) {
        return Uni.createFrom().item(() -> blogRepository.findById(id))
                .onItem().ifNotNull().transform(blog -> Response.ok(blog).build())
                .onItem().ifNull().continueWith(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Path("/search/title/{title}")
    public Multi<Blog> searchBlogsByTitle(@PathParam("title") String title) {
        return Multi.createFrom().iterable(blogRepository.find("title like ?1", "%" + title + "%").list());
    }

    @GET
    @Path("/search/content/{content}")
    public Multi<Blog> searchBlogsByContent(@PathParam("content") String content) {
        return Multi.createFrom().iterable(blogRepository.find("content like ?1", "%" + content + "%").list());
    }
}