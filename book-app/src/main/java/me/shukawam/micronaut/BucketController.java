package me.shukawam.micronaut;

import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.model.BucketSummary;
import com.oracle.bmc.objectstorage.requests.GetNamespaceRequest;
import com.oracle.bmc.objectstorage.requests.ListBucketsRequest;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.oraclecloud.core.TenancyIdProvider;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shukawam
 */
@Controller("os")
public class BucketController {
    private final TenancyIdProvider tenancyIdProvider;
    private final ObjectStorage objectStorage;

    public BucketController(TenancyIdProvider tenancyIdProvider, ObjectStorage objectStorage) {
        this.tenancyIdProvider = tenancyIdProvider;
        this.objectStorage = objectStorage;
    }

    @Get("/buckets{/compartmentId}")
    public List<String> listBuckets(@PathVariable @Nullable String compartmentId) {

        String compartmentOcid = compartmentId != null ? compartmentId : tenancyIdProvider.getTenancyId();

        GetNamespaceRequest getNamespaceRequest = GetNamespaceRequest.builder()
                .compartmentId(compartmentOcid).build();
        String namespace = objectStorage.getNamespace(getNamespaceRequest).getValue();

        ListBucketsRequest listBucketsRequest = ListBucketsRequest.builder()
                .namespaceName(namespace)
                .compartmentId(compartmentOcid)
                .build();

        return objectStorage.listBuckets(listBucketsRequest)
                .getItems()
                .stream()
                .map(BucketSummary::getName)
                .collect(Collectors.toList());
    }
}
