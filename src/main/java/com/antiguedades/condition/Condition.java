package com.antiguedades.condition;

import jakarta.persistence.*;

@Entity
@Table(name = "conditions")
public class Condition {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String label;

    public Condition() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
}
