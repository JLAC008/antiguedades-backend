package com.antiguedades.catalog;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "antique_type", nullable = false)
    private String antiqueType;

    @Column(name = "subcategory_key", nullable = false)
    private String subcategoryKey;

    @Column(name = "subcategory_label", nullable = false)
    private String subcategoryLabel;

    @Column(name = "detail_key")
    private String detailKey;

    @Column(name = "detail_label")
    private String detailLabel;

    @Column(name = "detail_desc", columnDefinition = "TEXT")
    private String detailDesc;

    public Category() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAntiqueType() { return antiqueType; }
    public void setAntiqueType(String antiqueType) { this.antiqueType = antiqueType; }
    public String getSubcategoryKey() { return subcategoryKey; }
    public void setSubcategoryKey(String subcategoryKey) { this.subcategoryKey = subcategoryKey; }
    public String getSubcategoryLabel() { return subcategoryLabel; }
    public void setSubcategoryLabel(String subcategoryLabel) { this.subcategoryLabel = subcategoryLabel; }
    public String getDetailKey() { return detailKey; }
    public void setDetailKey(String detailKey) { this.detailKey = detailKey; }
    public String getDetailLabel() { return detailLabel; }
    public void setDetailLabel(String detailLabel) { this.detailLabel = detailLabel; }
    public String getDetailDesc() { return detailDesc; }
    public void setDetailDesc(String detailDesc) { this.detailDesc = detailDesc; }
}
