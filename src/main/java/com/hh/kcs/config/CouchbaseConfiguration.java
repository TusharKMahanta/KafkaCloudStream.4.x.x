package com.hh.kcs.config;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.env.ClusterEnvironment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

import java.time.Duration;

@Configuration
@EnableCouchbaseRepositories
public class CouchbaseConfiguration extends AbstractCouchbaseConfiguration {
    @Value("${hh.couchbase.cluster.connectionString}")
    private String connectionString;
    @Value("${hh.couchbase.cluster.username}")
    private String username;
    @Value("${hh.couchbase.cluster.password}")
    private String password;
    @Value("${hh.couchbase.cluster.bucketName}")
    private String bucketName;
    @Override
    public String getConnectionString() {
        return connectionString;
    }

    @Override
    public String getUserName() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getBucketName() {
        return bucketName;
    }
    @Override
    public Cluster couchbaseCluster(ClusterEnvironment couchbaseClusterEnvironment) {
        Cluster cluster = super.couchbaseCluster(couchbaseClusterEnvironment);
        cluster.bucket(bucketName).waitUntilReady(Duration.ofSeconds(30));
        return cluster;
    }
}
