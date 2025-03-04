package ch.hftm.repository;

import java.util.List;
import java.util.stream.Collectors;

import ch.hftm.model.Blog;
import ch.hftm.model.BlogDTO;
import ch.hftm.util.BlogMapper;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BlogRepository implements PanacheRepository<Blog> {

    @WithTransaction
    public Uni<BlogDTO> createBlog(BlogDTO blogDTO) {
        Blog blog = BlogMapper.toEntity(blogDTO);
        return this.persistAndFlush(blog)
            .replaceWith(BlogMapper.toDTO(blog));
    }

    @WithSession
    public Uni<List<BlogDTO>> getAllBlogs() {
        return this.listAll()
            .onItem().transform(blogs -> blogs.stream()
            .map(BlogMapper::toDTO)
            .collect(Collectors.toList()));
    }

    @WithSession
    public Uni<BlogDTO> getBlogById(Long id) {
        return this.findById(id)
            .onItem().ifNotNull().transform(BlogMapper::toDTO);
    }

    @WithSession
    public Uni<List<BlogDTO>> searchBlogByTitle(String title) {
        return this.find("title like ?1", "%" + title + "%")
            .list() // Returns Uni<List<Blog>>
            .onItem().transform(blogs -> blogs.stream()
            .map(BlogMapper::toDTO)
            .collect(Collectors.toList())); // Convert List<Blog> to List<BlogDTO>
    }

    @WithSession
    public Uni<List<BlogDTO>> searchBlogsByContent(String content) {
        return this.find("content like ?1", "%" + content + "%")
            .list() // Returns Uni<List<Blog>>
            .onItem().transform(blogs -> blogs.stream()
            .map(BlogMapper::toDTO)
            .collect(Collectors.toList())); // Convert List<Blog> to List<BlogDTO>
    }
}
