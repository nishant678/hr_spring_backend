package com.hr.demo.hr.service;

import com.hr.demo.reaponse.AssetResponse;

import java.util.List;

public interface AssetService {

    List<AssetResponse> getAssets(Long companyId);

    AssetResponse getAsset(Long id);

    AssetResponse createAsset(Long companyId, String name, String assetTag, String serialNumber,
                              String model, String brand, String type, String purchaseDate,
                              String warrantyExpiry, String notes);

    AssetResponse updateAsset(Long id, String name, String assetTag, String serialNumber,
                              String model, String brand, String type, String purchaseDate,
                              String warrantyExpiry, String status, String notes);

    void deleteAsset(Long id);

    AssetResponse assignAsset(Long assetId, Long userId);

    AssetResponse unassignAsset(Long assetId);

    List<AssetResponse> getMyAssets(Long userId);

    List<AssetResponse> getUserAssets(Long userId);
}
