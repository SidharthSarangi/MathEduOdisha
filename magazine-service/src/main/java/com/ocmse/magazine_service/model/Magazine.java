package com.ocmse.magazine_service.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "magazines")
public class Magazine {
    @Id
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id ;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MagazineCategory category;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private UUID authorId;

    private String publisher;
    private String issn;
    private LocalDate publishedDate;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private String fileUrl;

    @Column(nullable = false)
    private String coverImageUrl;

    private String description;
    private Integer pageCount;

    @Column(nullable = false)
    private Boolean available = true;

    @Column(nullable = false)
    private String productType = "Magazine";

    public Magazine() {}

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public MagazineCategory getCategory() {
        return category;
    }

    public void setCategory(MagazineCategory category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public void setAuthorId(UUID authorId) {
        this.authorId = authorId;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }
}