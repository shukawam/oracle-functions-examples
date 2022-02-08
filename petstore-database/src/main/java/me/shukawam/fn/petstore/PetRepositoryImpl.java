package me.shukawam.fn.petstore;

import com.oracle.bmc.auth.ResourcePrincipalAuthenticationDetailsProvider;
import com.oracle.bmc.auth.InstancePrincipalsAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.GetObjectRequest;
import com.oracle.bmc.objectstorage.requests.ListObjectsRequest;
import com.oracle.bmc.objectstorage.responses.GetObjectResponse;
import com.oracle.bmc.objectstorage.responses.ListObjectsResponse;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * A simple implementation of {@link PetRepository}.
 *
 * @author shukawam
 */
public class PetRepositoryImpl implements PetRepository {
    private final static Logger LOGGER = Logger.getLogger(PetRepositoryImpl.class.getName());
    private PoolDataSource poolDataSource;
    private final File walletDir = new File("/tmp", "wallet");
    private final String namespace = System.getenv().get("NAMESPACE");
    private final String bucketName = System.getenv().get("BUCKET_NAME");
    private final String dbUser = System.getenv().get("DB_USER");
    private final String dbPassword = System.getenv().get("DB_PASSWORD");
    private final String dbUrl = System.getenv().get("DB_URL");
    final static String CONN_FACTORY_CLASS_NAME = "oracle.jdbc.pool.OracleDataSource";

    public PetRepositoryImpl() {
        LOGGER.info("*** CONNECTION INFO ***");
        LOGGER.info("namespace: " + namespace);
        LOGGER.info("bucketName: " + bucketName);
        LOGGER.info("dbUser: " + dbUser);
        LOGGER.info("dbPassword: " + dbPassword);
        LOGGER.info("dbUrl: " + dbUrl);
        poolDataSource = PoolDataSourceFactory.getPoolDataSource();
        try {
            poolDataSource.setConnectionFactoryClassName(CONN_FACTORY_CLASS_NAME);
            poolDataSource.setURL(dbUrl);
            poolDataSource.setUser(dbUser);
            poolDataSource.setPassword(dbPassword);
            poolDataSource.setConnectionPoolName("UCP_POOL");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (needWalletDownload()) {
            LOGGER.info("Downloading wallet...");
            downloadWallet();
        }
    }

    @Override
    public List<Pet> getAllPets() {
        try (Connection conn = poolDataSource.getConnection()) {
            conn.setAutoCommit(false);
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id, name FROM PETS");
            List<Pet> pets = new ArrayList<>();
            while (resultSet.next()) {
                pets.add(new Pet(resultSet.getInt("id"), resultSet.getString("name")));
            }
            return pets;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Pet getPetById(int id) {
        try (Connection conn = poolDataSource.getConnection()) {
            conn.setAutoCommit(false);
            PreparedStatement statement = conn.prepareStatement("SELECT id, name FROM PETS WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Pet pet = new Pet(resultSet.getInt("id"), resultSet.getString("name"));
                return pet;
            } else {
                return new Pet(0, "not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Pet createPet(Pet pet) {
        try (Connection conn = poolDataSource.getConnection()) {
            conn.setAutoCommit(false);
            PreparedStatement statement = conn.prepareStatement("INSERT INTO PETS(id, name) VALUES (?, ?)");
            statement.setInt(1, pet.id);
            statement.setString(2, pet.name);
            int record = statement.executeUpdate();
            if (record != 1) {
                conn.rollback();
                throw new RuntimeException("Failed to create pet");
            }
            conn.commit();
            return pet;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private boolean needWalletDownload() {
        if (walletDir.exists()) {
            LOGGER.info("Wallet exists, don't download it again...");
            return false;
        } else {
            LOGGER.info("Wallet doesn't exist, let's download one...");
            walletDir.mkdirs();
            return true;
        }
    }

    private void downloadWallet() {
        // resource principal
        // final ResourcePrincipalAuthenticationDetailsProvider provider =
        // ResourcePrincipalAuthenticationDetailsProvider.builder().build();
        // instance principal
        final InstancePrincipalsAuthenticationDetailsProvider provider = InstancePrincipalsAuthenticationDetailsProvider
                .builder().build();
        ObjectStorage client = new ObjectStorageClient(provider);
        client.setRegion("AP_TOKYO_1");
        LOGGER.info("Retrieving a list of all objects in /" + namespace + "/" + bucketName + "...");
        ListObjectsRequest listObjectsRequest = ListObjectsRequest.builder()
                .namespaceName(namespace)
                .bucketName(bucketName)
                .build();
        ListObjectsResponse listObjectsResponse = client.listObjects(listObjectsRequest);
        LOGGER.info("List retrieved. Starting download of each object...");
        listObjectsResponse.getListObjects().getObjects().stream().forEach(objectSummary -> {
            System.out.println("Downloading wallet file: [" + objectSummary.getName() + "]");

            GetObjectRequest objectRequest = GetObjectRequest.builder()
                    .namespaceName(namespace)
                    .bucketName(bucketName)
                    .objectName(objectSummary.getName())
                    .build();
            GetObjectResponse objectResponse = client.getObject(objectRequest);

            try {
                File f = new File(walletDir + "/" + objectSummary.getName());
                FileUtils.copyToFile(objectResponse.getInputStream(), f);
                System.out.println("Stored wallet file: " + f.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
