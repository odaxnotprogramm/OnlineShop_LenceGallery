package org.web.shop.app.app.service.impl;

import org.springframework.stereotype.Service;
import org.web.shop.app.app.model.Supplier;
import org.web.shop.app.app.repository.SupplierRepository;
import org.web.shop.app.app.service.SupplierService;

import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public List<Supplier> findAll() {
        return supplierRepository.findAll();
    }
}
