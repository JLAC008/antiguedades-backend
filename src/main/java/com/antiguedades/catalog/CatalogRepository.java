package com.antiguedades.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CatalogRepository extends JpaRepository<Catalog, UUID> {
}
