package com.hr.demo.reaponse;

import com.hr.demo.entity.AssetEntity;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class AssignedAssetResponse {

    private final Long id;
    private final String name;
    private final String assetTag;
    private final String serialNumber;
    private final String model;
    private final String brand;
    private final String type;
    private final String status;
    private final LocalDate assignedDate;
    private final LocalDate warrantyExpiry;

    public AssignedAssetResponse(AssetEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.assetTag = entity.getAssetTag();
        this.serialNumber = entity.getSerialNumber();
        this.model = entity.getModel();
        this.brand = entity.getBrand();
        this.type = entity.getType();
        this.status = entity.getStatus().name();
        this.assignedDate = entity.getAssignedDate();
        this.warrantyExpiry = entity.getWarrantyExpiry();
    }
}
