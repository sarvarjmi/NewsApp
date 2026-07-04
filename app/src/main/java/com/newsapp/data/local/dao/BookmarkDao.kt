package com.newsapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.newsapp.data.local.entity.BookmarkEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the bookmarks table.
 * Defines the SQLite operations available for bookmarked articles.
 */
@Dao
interface BookmarkDao {

    /**
     * Inserts a new bookmark or replaces an existing one if the URL matches.
     * Use this for both initial saving and updating metadata.
     * 
     * @param bookmark The entity to be saved.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(bookmark: BookmarkEntity)

    /**
     * Removes a bookmark from the database.
     * 
     * @param bookmark The entity to be deleted.
     */
    @Delete
    suspend fun delete(bookmark: BookmarkEntity)

    /**
     * Explicitly updates an existing bookmark.
     * 
     * @param bookmark The entity with updated fields.
     */
    @Update
    suspend fun update(bookmark: BookmarkEntity)

    /**
     * Returns a Flow that emits the list of all bookmarks.
     * Sorted by 'bookmarkedAt' in descending order (newest first).
     * 
     * Room handles the threading and will automatically emit a new list 
     * whenever the table changes.
     */
    @Query("SELECT * FROM bookmarks ORDER BY bookmarkedAt DESC")
    fun getBookmarks(): Flow<List<BookmarkEntity>>

    /**
     * Retrieves a specific bookmark by its unique URL.
     * 
     * @param url The canonical URL of the article.
     * @return The [BookmarkEntity] if found, null otherwise.
     */
    @Query("SELECT * FROM bookmarks WHERE url = :url")
    suspend fun getBookmarkByUrl(url: String): BookmarkEntity?

    /**
     * Checks if a bookmark exists for a given URL.
     * Useful for toggling bookmark icons in the UI.
     * 
     * @param url The canonical URL to check.
     * @return true if the article is bookmarked, false otherwise.
     */
    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE url = :url)")
    suspend fun isBookmarked(url: String): Boolean
}
