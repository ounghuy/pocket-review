package com.wadpam.rnr.service;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.wadpam.rnr.dao.DProductDao;
import com.wadpam.rnr.domain.DProduct;
import com.wadpam.rnr.json.JProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Basic;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PersistenceManager {

    static final Logger LOG = LoggerFactory.getLogger(PersistenceManager.class);

    private DProductDao productDao;

    // Get a page of products using pagination
    // TODO: This method should be generated by Mardao and be supported by most methods
    protected String getProductPage(String cursor, int pageSize, Collection<DProduct> productPage) {

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query("DProduct");
        PreparedQuery preparedQuery = datastore.prepare(query);

        // Set fetch options
        FetchOptions fetchOptions = FetchOptions.Builder.withLimit(pageSize);
        if (null != cursor)
            fetchOptions.startCursor(Cursor.fromWebSafeString(cursor));

        // Run the query
        QueryResultList<Entity> entities = preparedQuery.asQueryResultList(fetchOptions);

        // Build the DProduct domain object from the entities
        for (Entity entity : entities) {
            DProduct dProduct = new DProduct();

            LOG.debug("Entity key name: " + entity.getKey().getName());
            dProduct.setProductId( entity.getKey().getName());
            dProduct.setRatingSum((Long)entity.getProperty("ratingSum"));
            dProduct.setRatingCount((Long)entity.getProperty("ratingCount"));
            dProduct.setLikeCount((Long)entity.getProperty("likeCount"));
            dProduct.setCommentCount((Long)entity.getProperty("commentCount"));
            dProduct.setLocation((GeoPt) entity.getProperty("location"));

            productPage.add(dProduct);
        }

        // Get a new cursor
        String newCursor = entities.getCursor().toWebSafeString();

        return newCursor;
    }

    // TODO: I think several of this methods should be able to be autogenerated my Mardao. Support a cached version of DB methods
    // Get product from the cache or the db
    DProduct getProductWithCache(String productId) {
        MemcacheService memcache = MemcacheServiceFactory.getMemcacheService(); // TODO: Do we need to set the namespace

        // Look for the product in the cache
        DProduct dProduct = (DProduct)memcache.get(productId);

        if (null == dProduct) {
            // Not found in the cache, read from the db
            dProduct = productDao.findByPrimaryKey(productId);

            // Update the cache if found
            if (null != dProduct)
                memcache.put(productId, dProduct);
        }

        return dProduct;
    }

    // Get products from the cache or the db
    Map<String, DProduct> getProductsWithCache(Collection<String> productIds) {
        MemcacheService memcache = MemcacheServiceFactory.getMemcacheService(); // TODO: Do we need to set the namespace

        if (null == productIds) return new HashMap<String, DProduct>();

        // Look for the products in the cache
        Map<String, DProduct> cachedProductsMap = (Map<String, DProduct>)(Object)memcache.getAll(productIds); // TODO: Is there a better way to cast?

        // Find all products id missing from the cache
        Collection<String> missingProductIds =  new ArrayList<String>(productIds);
        for (String key : cachedProductsMap.keySet())
            missingProductIds.remove(key);

        // Get the remaining products that was not in the cache from the db
        Map<String, DProduct> missingProductsMap = null;
        if (!productIds.isEmpty()) {
            missingProductsMap = productDao.findByPrimaryKeys(null, missingProductIds);

            // Update cache with missing products
            memcache.putAll(missingProductsMap);
        }

        // Combine the cached and missing products and return
        if (null != missingProductsMap)
            cachedProductsMap.putAll(missingProductsMap);

        return cachedProductsMap;
    }

    // Update the product and the cache if it already contains the product
    void storeProductWithCache(DProduct dProduct) {
        MemcacheService memcache = MemcacheServiceFactory.getMemcacheService(); // TODO: Do we need to set the namespace

        // Store in the db
        productDao.persist(dProduct);

        // Update the cache only if it exists in the cache
        // We do not like to populate the cache with rare products that is never requested
        if (memcache.contains(dProduct.getProductId())) {
            memcache.put(dProduct.getProductId(), dProduct);
        }
    }


    // Setters and Getters
    public DProductDao getProductDao() {
        return productDao;
    }

    public void setProductDao(DProductDao productDao) {
        this.productDao = productDao;
    }
}