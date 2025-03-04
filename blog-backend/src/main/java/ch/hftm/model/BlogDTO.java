package ch.hftm.model;

public class BlogDTO {

    private Long id;
    private String title;
    private String content;
    private boolean validated;

    // Constructors
    public BlogDTO() {}

    public BlogDTO(Long id, String title, String content, boolean validated) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.validated = validated;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean getValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }
}
