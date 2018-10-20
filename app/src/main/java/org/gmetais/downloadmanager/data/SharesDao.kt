package org.gmetais.downloadmanager.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SharesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) fun insertShares(vararg shares: SharedFile)
    @Insert(onConflict = OnConflictStrategy.REPLACE) fun insertShares(shares: List<SharedFile>)

    @Update fun updateShares(vararg shares: SharedFile)

    @Query("SELECT * FROM shares order by name asc") fun getShares() : LiveData<List<SharedFile>>

    @Delete fun deleteShares(vararg shares: SharedFile)
}