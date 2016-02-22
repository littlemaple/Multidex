/**
 *
 */
package com.medzone.mcloud.cache.isynchro;

import java.util.List;

/**
 * @author Robert. 2014年9月10日
 */
public interface ICloudSynchronize<T> {

    /**
     * @param actionTag 行为标签，如ADD/UPDATE/DELETE
     * @return
     */
    List<T> getSource(Integer... actionTag);

    String getSourcePacked(int actionTag);

}
