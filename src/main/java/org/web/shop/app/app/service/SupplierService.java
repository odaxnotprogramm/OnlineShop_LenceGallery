package org.web.shop.app.app.service;

import org.springframework.stereotype.Service;
import org.web.shop.app.app.model.Supplier;

import java.util.List;

@Service
public interface SupplierService {
    List<Supplier> findAll();
}
