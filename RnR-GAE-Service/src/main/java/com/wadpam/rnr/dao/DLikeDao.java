package com.wadpam.rnr.dao;

import com.wadpam.rnr.domain.DLike;

import java.util.Collection;
import java.util.List;

/**
 * Business Methods interface for entity DLike.
 * This interface is generated by mardao, but edited by developers.
 * It is not overwritten by the generator once it exists.
 *
 * Generated on 2012-08-05T20:54:54.772+0700.
 * @author mardao DAO generator (net.sf.mardao.plugin.ProcessDomainMojo)
 */
public interface DLikeDao extends GeneratedDLikeDao {

    /**
     * Find likes done by a specific user and a specific product.
     * @param productId the product
     * @param username the unique user name or id
     * @return a like
     */
    public DLike findByProductIdUsername(String productId, String username);


    /**
     * Find likes done by a specific user and a specific product.
     * @param productId the product
     * @param username the unique user name or id
     * @return a key
     */
    Long findKeyByProductIdUsername(String productId, String username);

    /**
     * Find likes done by user on a list of products
     *
     * @param productIds the produce ids
     * @param username the user name
     * @return list of matching likes
     */
    Iterable<DLike> findByProductIdsUsername(Collection<String> productIds, String username);

    /**
     * Find like keys done by user on a list of products
     *
     * @param productIds the produce ids
     * @param username the user name
     * @return list of matching like keys
     */
    Iterable<Long> findKeysByProductIdsUsername(Collection<String> productIds, String username);
}
