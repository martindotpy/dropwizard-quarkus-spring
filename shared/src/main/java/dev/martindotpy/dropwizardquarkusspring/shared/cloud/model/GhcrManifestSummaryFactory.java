package dev.martindotpy.dropwizardquarkusspring.shared.cloud.model;

import java.util.List;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class GhcrManifestSummaryFactory {
    public static ImageManifestSummary success(String imageName, GhcrManifestListResponse manifestResponse) {
        List<ImagePlatformDigest> platforms = mapPlatforms(manifestResponse);

        return new ImageManifestSummary(
                imageName,
                GhcrImageCatalog.imageReference(imageName),
                platforms);
    }

    private static List<ImagePlatformDigest> mapPlatforms(GhcrManifestListResponse response) {
        if (response.manifests() == null || response.manifests().isEmpty()) {
            return List.of();
        }

        return response.manifests().stream()
                .map(manifest -> {
                    GhcrPlatform platform = manifest.platform();

                    return new ImagePlatformDigest(
                            platform.architecture(),
                            manifest.digest(),
                            manifest.size() == null ? 0L : manifest.size());
                })
                .toList();
    }
}
