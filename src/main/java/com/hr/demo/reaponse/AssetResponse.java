package com.hr.demo.reaponse;

import com.hr.demo.entity.AssetEntity;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class AssetResponse {

    private final Long id;
    private final String name;
    private final String assetTag;
    private final String serialNumber;
    private final String model;
    private final String brand;
    private final String type;
    private final LocalDate purchaseDate;
    private final LocalDate warrantyExpiry;
    private final String status;
    private final Long assignedToId;
    private final String assignedToName;
    private final String assignedToEmployeeId;
    private final LocalDate assignedDate;
    private final LocalDate returnDate;
    private final String notes;

    public AssetResponse(AssetEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.assetTag = entity.getAssetTag();
        this.serialNumber = entity.getSerialNumber();
        this.model = entity.getModel();
        this.brand = entity.getBrand();
        this.type = entity.getType();
        this.purchaseDate = entity.getPurchaseDate();
        this.warrantyExpiry = entity.getWarrantyExpiry();
        this.status = entity.getStatus().name();
        if (entity.getAssignedTo() != null) {
            this.assignedToId = entity.getAssignedTo().getId();
            String fn = entity.getAssignedTo().getFirstName() != null ? entity.getAssignedTo().getFirstName() : "";
            String ln = entity.getAssignedTo().getLastName() != null ? entity.getAssignedTo().getLastName() : "";
            this.assignedToName = (fn + " " + ln).trim();
            this.assignedToEmployeeId = entity.getAssignedTo().getEmployeeId();
        } else {
            this.assignedToId = null;
            this.assignedToName = null;
            this.assignedToEmployeeId = null;
        }
        this.assignedDate = entity.getAssignedDate();
        this.returnDate = entity.getReturnDate();
        this.notes = entity.getNotes();
    }
}
