package com.wadpam.rnr.dao;

import com.wadpam.rnr.domain.DFeedback;

/**
 * Business Methods interface for entity DFeedback.
 * This interface is generated by mardao, but edited by developers.
 * It is not overwritten by the generator once it exists.
 *
 * Generated on 2012-12-26T15:11:12.247+0700.
 * @author mardao DAO generator (net.sf.mardao.plugin.ProcessDomainMojo)
 */
public interface DFeedbackDao extends GeneratedDFeedbackDao {

    /**
     * Delete all user feedback created before the provided timestamp.
     * @param timestamp timestamp
     * @return keys
     */
    public int deleteAllUpdatedBefore(Long timestamp);

    /**
     * Get all user feedback created after the provided timestamp.
     * @param timestamp timestamp
     * @return list of user feedback
     */
    public Iterable<DFeedback> queryUpdatedAfter(Long timestamp);

}
