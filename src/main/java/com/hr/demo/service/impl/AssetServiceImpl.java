package com.hr.demo.service.impl;

import com.hr.demo.domain.asset.AssetStatus;
import com.hr.demo.entity.AssetEntity;
import com.hr.demo.entity.CompanyEntity;
import com.hr.demo.entity.UserEntity;
import com.hr.demo.exceptions.BadRequestException;
import com.hr.demo.exceptions.ResourceNotFoundException;
import com.hr.demo.reaponse.AssetResponse;
import com.hr.demo.repository.AssetRepository;
import com.hr.demo.repository.CompanyRepository;
import com.hr.demo.repository.UserRepository;
import com.hr.demo.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AssetResponse> getAssets(Long companyId) {
        return assetRepository.findByCompany_IdOrderByCreatedAtDesc(companyId)
                .stream().map(AssetResponse::new).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AssetResponse getAsset(Long id) {
        return new AssetResponse(assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found")));
    }

    @Override
    public AssetResponse createAsset(Long companyId, String name, String assetTag,
                                     String serialNumber, String model, String brand,
                                     String type, String purchaseDate, String warrantyExpiry,
                                     String notes) {
        CompanyEntity company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        AssetEntity entity = AssetEntity.builder()
                .company(company)
                .name(name)
                .assetTag(assetTag)
                .serialNumber(serialNumber)
                .model(model)
                .brand(brand)
                .type(type)
                .purchaseDate(purchaseDate != null ? LocalDate.parse(purchaseDate) : null)
                .warrantyExpiry(warrantyExpiry != null ? LocalDate.parse(warrantyExpiry) : null)
                .status(AssetStatus.AVAILABLE)
                .notes(notes)
                .build();

        assetRepository.save(entity);
        return new AssetResponse(entity);
    }

    @Override
    public AssetResponse updateAsset(Long id, String name, String assetTag,
                                     String serialNumber, String model, String brand,
                                     String type, String purchaseDate, String warrantyExpiry,
                                     String status, String notes) {
        AssetEntity entity = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found"));

        if (name != null) entity.setName(name);
        if (assetTag != null) entity.setAssetTag(assetTag);
        if (serialNumber != null) entity.setSerialNumber(serialNumber);
        if (model != null) entity.setModel(model);
        if (brand != null) entity.setBrand(brand);
        if (type != null) entity.setType(type);
        if (purchaseDate != null) entity.setPurchaseDate(LocalDate.parse(purchaseDate));
        if (warrantyExpiry != null) entity.setWarrantyExpiry(LocalDate.parse(warrantyExpiry));
        if (status != null) entity.setStatus(AssetStatus.valueOf(status.toUpperCase()));
        if (notes != null) entity.setNotes(notes);

        assetRepository.save(entity);
        return new AssetResponse(entity);
    }

    @Override
    public void deleteAsset(Long id) {
        AssetEntity entity = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found"));
        assetRepository.delete(entity);
    }

    @Override
    public AssetResponse assignAsset(Long assetId, Long userId) {
        AssetEntity asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found"));

        if (asset.getStatus() == AssetStatus.ASSIGNED) {
            throw new BadRequestException("Asset is already assigned to someone");
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        asset.setAssignedTo(user);
        asset.setAssignedDate(LocalDate.now());
        asset.setStatus(AssetStatus.ASSIGNED);

        assetRepository.save(asset);
        return new AssetResponse(asset);
    }

    @Override
    public AssetResponse unassignAsset(Long assetId) {
        AssetEntity asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found"));

        if (asset.getStatus() != AssetStatus.ASSIGNED) {
            throw new BadRequestException("Asset is not currently assigned");
        }

        asset.setAssignedTo(null);
        asset.setReturnDate(LocalDate.now());
        asset.setStatus(AssetStatus.AVAILABLE);

        assetRepository.save(asset);
        return new AssetResponse(asset);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssetResponse> getMyAssets(Long userId) {
        return assetRepository.findByAssignedTo_IdOrderByCreatedAtDesc(userId)
                .stream().map(AssetResponse::new).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssetResponse> getUserAssets(Long userId) {
        return assetRepository.findByAssignedTo_IdOrderByCreatedAtDesc(userId)
                .stream().map(AssetResponse::new).toList();
    }
}
