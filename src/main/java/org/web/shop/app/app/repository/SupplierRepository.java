package org.web.shop.app.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.web.shop.app.app.model.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    Supplier findSupplierByName(String value);
}
