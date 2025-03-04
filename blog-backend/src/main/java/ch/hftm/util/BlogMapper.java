package ch.hftm.util;

import ch.hftm.model.Blog;
import ch.hftm.model.BlogDTO;
import jakarta.enterprise.context.ApplicationScoped;

// @ApplicationScoped
public class BlogMapper {

    // Convert Blog entity to BlogDTO
    public static BlogDTO toDTO(Blog blog) {
        if (blog == null) {
            return null;
        }
        BlogDTO blogDTO = new BlogDTO();
        blogDTO.setId(blog.getId());
        blogDTO.setTitle(blog.getTitle());
        blogDTO.setContent(blog.getContent());
        blogDTO.setValidated(blog.getValidated());
        return blogDTO;
    }

    // Convert BlogDTO to Blog entity
    public static Blog toEntity(BlogDTO blogDTO) {
        if (blogDTO == null) {
            return null;
        }
        Blog blog = new Blog();
        blog.setTitle(blogDTO.getTitle());
        blog.setContent(blogDTO.getContent());
        blog.setValidated(blogDTO.getValidated());
        return blog;
    }
}