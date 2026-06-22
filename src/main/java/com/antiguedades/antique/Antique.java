package com.antiguedades.antique;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "antiques")
public class Antique {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "catalog_id")
    private UUID catalogId;

    @Column(nullable = false)
    private String name;

    @Column(name = "allow_duplicate_name", nullable = false)
    private boolean allowDuplicateName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AntiqueType type;

    @Column(nullable = false)
    private String subcategory;

    @Column(nullable = false)
    private String detail;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private String element;

    private String title;
    private String author;
    private String editor;
    private String imprenta;
    private String edition;
    private String signature;
    private String theme;
    private String century;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price = BigDecimal.ZERO;

    @Column(name = "year_era")
    private String yearEra;

    @Column(nullable = false)
    private String condition;

    private String material;
    private String dimensions;

    @Column(name = "paper_type")
    private String paperType;

    @Column(name = "paper_format")
    private String paperFormat;

    @Column(name = "paper_weight")
    private Integer paperWeight;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "antique_images", joinColumns = @JoinColumn(name = "antique_id"))
    @Column(name = "image_url")
    private List<String> images = new ArrayList<>();

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Antique() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getCatalogId() { return catalogId; }
    public void setCatalogId(UUID catalogId) { this.catalogId = catalogId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public boolean isAllowDuplicateName() { return allowDuplicateName; }
    public void setAllowDuplicateName(boolean allowDuplicateName) { this.allowDuplicateName = allowDuplicateName; }
    public AntiqueType getType() { return type; }
    public void setType(AntiqueType type) { this.type = type; }
    public String getSubcategory() { return subcategory; }
    public void setSubcategory(String subcategory) { this.subcategory = subcategory; }
    public String getDetail() { return detail; }
    public void setDetail(String detail) { this.detail = detail; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getElement() { return element; }
    public void setElement(String element) { this.element = element; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getEditor() { return editor; }
    public void setEditor(String editor) { this.editor = editor; }
    public String getImprenta() { return imprenta; }
    public void setImprenta(String imprenta) { this.imprenta = imprenta; }
    public String getEdition() { return edition; }
    public void setEdition(String edition) { this.edition = edition; }
    public String getSignature() { return signature; }
    public void setSignature(String signature) { this.signature = signature; }
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
    public String getCentury() { return century; }
    public void setCentury(String century) { this.century = century; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getYearEra() { return yearEra; }
    public void setYearEra(String yearEra) { this.yearEra = yearEra; }
    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }
    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }
    public String getDimensions() { return dimensions; }
    public void setDimensions(String dimensions) { this.dimensions = dimensions; }
    public String getPaperType() { return paperType; }
    public void setPaperType(String paperType) { this.paperType = paperType; }
    public String getPaperFormat() { return paperFormat; }
    public void setPaperFormat(String paperFormat) { this.paperFormat = paperFormat; }
    public Integer getPaperWeight() { return paperWeight; }
    public void setPaperWeight(Integer paperWeight) { this.paperWeight = paperWeight; }
    public List<String> getImages() { return images; }
    public void setImages(List<String> images) { this.images = images; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
