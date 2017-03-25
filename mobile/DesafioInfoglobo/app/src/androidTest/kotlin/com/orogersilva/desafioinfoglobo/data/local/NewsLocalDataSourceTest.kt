package com.orogersilva.desafioinfoglobo.data.local

import android.content.ContentValues
import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.orogersilva.desafioinfoglobo.data.NewsDataSource
import com.orogersilva.desafioinfoglobo.data.local.contract.AuthorPersistenceContract
import com.orogersilva.desafioinfoglobo.data.local.contract.ImagePersistenceContract
import com.orogersilva.desafioinfoglobo.data.local.contract.NewsPersistenceContract
import com.orogersilva.desafioinfoglobo.data.local.contract.VideoPersistenceContract
import com.orogersilva.desafioinfoglobo.model.Image
import com.orogersilva.desafioinfoglobo.model.News
import com.orogersilva.desafioinfoglobo.model.Section
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Type
import java.nio.charset.Charset

/**
 * Created by orogersilva on 3/24/2017.
 */
@RunWith(AndroidJUnit4::class)
class NewsLocalDataSourceTest {

    // region SETUP / TEARDOWN CLASS METHODS

    companion object {

        private lateinit var context: Context
        private var newsLocalDataSource: NewsDataSource? = null

        @BeforeClass @JvmStatic fun setupClass() {

            context = InstrumentationRegistry.getTargetContext()

            newsLocalDataSource = NewsLocalDataSource.getInstance(DbHelper(context))
        }

        @AfterClass @JvmStatic fun teardownClass() {

            NewsLocalDataSource.destroyInstance()
            newsLocalDataSource = null
        }
    }

    // endregion

    // region TEST METHODS

    @Test fun getAllNews_whenThereAreNotNews_onDataNotAvailableWasSuccessful() {

        // ARRANGE

        val callback = object : NewsDataSource.LoadNewsCallback {

            override fun onNewsLoaded(news: List<News>) {

                // ASSERT

                fail()
            }

            override fun onDataNotAvailable() {

                // ASSERT

                return
            }
        }

        // ACT

        newsLocalDataSource?.getAllNews(callback)
    }

    @Test fun getNews_whenThereAreNews_onNewsLoadedWasSuccessful() {

        // ARRANGE

        val expectedNews = createTestData()

        val dbHelper = DbHelper(context)

        val db = dbHelper.writableDatabase

        expectedNews.forEach {

            val newsContentValues = ContentValues()

            with(newsContentValues) {

                put(NewsPersistenceContract.COLUMN_NAME_ID, it.id)
                put(NewsPersistenceContract.COLUMN_NAME_ADVERTISING_REPORT, if (it.advertisingReport) 1 else 0)
                put(NewsPersistenceContract.COLUMN_NAME_SUBTITLE, it.subtitle)
                put(NewsPersistenceContract.COLUMN_NAME_TEXT, it.text)
                put(NewsPersistenceContract.COLUMN_NAME_UPDATED_IN, it.updatedIn)
                put(NewsPersistenceContract.COLUMN_NAME_PUBLISHED_IN, it.publishedIn)
                put(NewsPersistenceContract.COLUMN_NAME_SECTION_NAME, it.section.name)
                put(NewsPersistenceContract.COLUMN_NAME_SECTION_URL, it.section.url)
                put(NewsPersistenceContract.COLUMN_NAME_TYPE, it.type)
                put(NewsPersistenceContract.COLUMN_NAME_TITLE, it.title)
                put(NewsPersistenceContract.COLUMN_NAME_URL, it.url)
                put(NewsPersistenceContract.COLUMN_NAME_ORIGINAL_URL, it.originalUrl)
            }

            db.insert(NewsPersistenceContract.NewsEntry.TABLE_NAME, null, newsContentValues)

            if (it.authors != null) {

                for (authorName in it.authors!!) {

                    val authorContentValues = ContentValues()

                    with(authorContentValues) {

                        put(AuthorPersistenceContract.COLUMN_NAME_NAME, authorName)
                        put(AuthorPersistenceContract.COLUMN_NAME_NEWS_ID, it.id)
                    }

                    db.insert(AuthorPersistenceContract.AuthorEntry.TABLE_NAME, null, authorContentValues)
                }
            }

            if (it.videos != null) {

                for (videoUrl in it.videos!!) {

                    val videoContentValues = ContentValues()

                    with(videoContentValues) {

                        put(VideoPersistenceContract.COLUMN_NAME_URL, videoUrl)
                        put(VideoPersistenceContract.COLUMN_NAME_NEWS_ID, it.id)
                    }

                    db.insert(VideoPersistenceContract.VideoEntry.TABLE_NAME, null, videoContentValues)
                }
            }

            if (it.images != null) {

                for (image in it.images!!) {

                    val imageContentValues = ContentValues()

                    with(imageContentValues) {

                        put(ImagePersistenceContract.COLUMN_NAME_AUTHOR, image.author)
                        put(ImagePersistenceContract.COLUMN_NAME_SOURCE, image.source)
                        put(ImagePersistenceContract.COLUMN_NAME_CAPTION, image.caption)
                        put(ImagePersistenceContract.COLUMN_NAME_URL, image.url)
                        put(ImagePersistenceContract.COLUMN_NAME_NEWS_ID, it.id)
                    }

                    db.insert(ImagePersistenceContract.ImageEntry.TABLE_NAME, null, imageContentValues)
                }
            }
        }

        db.close()

        val callback = object : NewsDataSource.LoadNewsCallback {

            override fun onNewsLoaded(news: List<News>) {

                // ASSERT

                expectedNews.forEach {
                    assertTrue(news.contains(it))
                }
            }

            override fun onDataNotAvailable() {

                // ASSERT

                fail()
            }
        }

        // ACT

        newsLocalDataSource?.getAllNews(callback)
    }

    @Test fun savePubs_whenNewsIsNull_throwsNullPointerException() {

        // ACT

        try {

            newsLocalDataSource?.saveNews(null)

            // ASSERT

            fail()

        } catch (e: NullPointerException) {

            // ASSERT

            return
        }
    }

    @Test fun savePubs_whenThereAreNotNews_thenNoneNewsHasBeenInsertedOnDb() {

        // ARRANGE

        val news = mutableListOf<News>()

        // ACT

        newsLocalDataSource?.saveNews(news)

        // ASSERT

        val dbHelper = DbHelper(context)

        val db = dbHelper.writableDatabase

        val newsProjection = arrayOf(
                NewsPersistenceContract.COLUMN_NAME_ID,
                NewsPersistenceContract.COLUMN_NAME_ADVERTISING_REPORT,
                NewsPersistenceContract.COLUMN_NAME_SUBTITLE,
                NewsPersistenceContract.COLUMN_NAME_TEXT,
                NewsPersistenceContract.COLUMN_NAME_UPDATED_IN,
                NewsPersistenceContract.COLUMN_NAME_PUBLISHED_IN,
                NewsPersistenceContract.COLUMN_NAME_SECTION_NAME,
                NewsPersistenceContract.COLUMN_NAME_SECTION_URL,
                NewsPersistenceContract.COLUMN_NAME_TYPE,
                NewsPersistenceContract.COLUMN_NAME_TITLE,
                NewsPersistenceContract.COLUMN_NAME_URL,
                NewsPersistenceContract.COLUMN_NAME_ORIGINAL_URL
        )

        val authorProjection = arrayOf(
                AuthorPersistenceContract.COLUMN_NAME_NAME,
                AuthorPersistenceContract.COLUMN_NAME_NEWS_ID
        )

        val videoProjection = arrayOf(
                VideoPersistenceContract.COLUMN_NAME_URL,
                VideoPersistenceContract.COLUMN_NAME_NEWS_ID
        )

        val imageProjection = arrayOf(
                ImagePersistenceContract.COLUMN_NAME_AUTHOR,
                ImagePersistenceContract.COLUMN_NAME_SOURCE,
                ImagePersistenceContract.COLUMN_NAME_CAPTION,
                ImagePersistenceContract.COLUMN_NAME_URL,
                ImagePersistenceContract.COLUMN_NAME_NEWS_ID
        )

        val newsCursor = db.query(NewsPersistenceContract.NewsEntry.TABLE_NAME, newsProjection, null, null, null, null, null)
        val authorCursor = db.query(AuthorPersistenceContract.AuthorEntry.TABLE_NAME, authorProjection, null, null, null, null, null)
        val videoCursor = db.query(VideoPersistenceContract.VideoEntry.TABLE_NAME, videoProjection, null, null, null, null, null)
        val imageCursor = db.query(ImagePersistenceContract.ImageEntry.TABLE_NAME, imageProjection, null, null, null, null, null)

        val thereisNotNews =
                (newsCursor != null && newsCursor.count == 0) &&
                        (authorCursor != null && authorCursor.count == 0) &&
                        (videoCursor != null && videoCursor.count == 0) &&
                        (imageCursor != null && imageCursor.count == 0)

        newsCursor.close()
        authorCursor.close()
        videoCursor.close()
        imageCursor.close()

        db.close()

        assertTrue(thereisNotNews)
    }

    @Test fun savePubs_whenThereAreNews_thenNewsHasBeenInsertedOnDb() {

        // ARRANGE

        val expectedNews = createTestData()

        // ACT

        newsLocalDataSource?.saveNews(expectedNews)

        // ASSERT

        val dbHelper = DbHelper(context)

        val db = dbHelper?.readableDatabase

        var projection = arrayOf(
                NewsPersistenceContract.COLUMN_NAME_ID,
                NewsPersistenceContract.COLUMN_NAME_ADVERTISING_REPORT,
                NewsPersistenceContract.COLUMN_NAME_SUBTITLE,
                NewsPersistenceContract.COLUMN_NAME_TEXT,
                NewsPersistenceContract.COLUMN_NAME_UPDATED_IN,
                NewsPersistenceContract.COLUMN_NAME_PUBLISHED_IN,
                NewsPersistenceContract.COLUMN_NAME_SECTION_NAME,
                NewsPersistenceContract.COLUMN_NAME_SECTION_URL,
                NewsPersistenceContract.COLUMN_NAME_TYPE,
                NewsPersistenceContract.COLUMN_NAME_TITLE,
                NewsPersistenceContract.COLUMN_NAME_URL,
                NewsPersistenceContract.COLUMN_NAME_ORIGINAL_URL
        )

        var c = db?.query(NewsPersistenceContract.TABLE_NAME, projection, null, null, null, null, null)

        val news = mutableListOf<News>()

        if (c != null && c.count > 0) {

            while (c.moveToNext()) {

                val id = c.getLong(c.getColumnIndex(NewsPersistenceContract.COLUMN_NAME_ID))
                val advertisingReport = c.getInt(c.getColumnIndex(NewsPersistenceContract.COLUMN_NAME_ADVERTISING_REPORT)) == 1
                val subtitle = c.getString(c.getColumnIndex(NewsPersistenceContract.COLUMN_NAME_SUBTITLE))
                val text = c.getString(c.getColumnIndex(NewsPersistenceContract.COLUMN_NAME_TEXT))
                val updatedIn = c.getString(c.getColumnIndex(NewsPersistenceContract.COLUMN_NAME_UPDATED_IN))
                val publishedIn = c.getString(c.getColumnIndex(NewsPersistenceContract.COLUMN_NAME_PUBLISHED_IN))
                val sectionName = c.getString(c.getColumnIndex(NewsPersistenceContract.COLUMN_NAME_SECTION_NAME))
                val sectionUrl = c.getString(c.getColumnIndex(NewsPersistenceContract.COLUMN_NAME_SECTION_URL))
                val type = c.getString(c.getColumnIndex(NewsPersistenceContract.COLUMN_NAME_TYPE))
                val title = c.getString(c.getColumnIndex(NewsPersistenceContract.COLUMN_NAME_TITLE))
                val url = c.getString(c.getColumnIndex(NewsPersistenceContract.COLUMN_NAME_URL))
                val originalUrl = c.getString(c.getColumnIndex(NewsPersistenceContract.COLUMN_NAME_ORIGINAL_URL))

                news.add(
                        News(
                                advertisingReport = advertisingReport,
                                subtitle = subtitle,
                                text = text,
                                updatedIn = updatedIn,
                                id = id,
                                publishedIn = publishedIn,
                                section = Section(sectionName, sectionUrl),
                                type = type,
                                title = title,
                                url = url,
                                originalUrl = originalUrl
                        )
                )
            }
        }

        c?.close()

        news.forEach {

            // 1. Querying author's names.

            c = db?.rawQuery(
                    "SELECT a.author_name " +
                            "FROM author as a " +
                            "WHERE a.author_news_id = ?",
                    arrayOf<String>(it.id.toString())
            )

            val authors = mutableListOf<String>()

            if (c != null && c!!.count > 0) {

                while (c!!.moveToNext()) {

                    val authorName = c?.getString(c!!.getColumnIndex(AuthorPersistenceContract.COLUMN_NAME_NAME))

                    authors.add(authorName!!)
                }
            }

            c?.close()

            // 2. Querying videos' URL's.

            c = db?.rawQuery(
                    "SELECT v.video_url " +
                            "FROM video as v " +
                            "WHERE v.video_news_id = ?",
                    arrayOf<String>(it.id.toString()
                    )
            )

            val videos = mutableListOf<String>()

            if (c != null && c!!.count > 0) {

                while (c!!.moveToNext()) {

                    val video = c?.getString(c!!.getColumnIndex(VideoPersistenceContract.COLUMN_NAME_URL))

                    videos.add(video!!)
                }
            }

            c?.close()

            // 3. Querying images.

            c = db?.rawQuery(
                    "SELECT i.image_author, i.image_source, i.image_caption, i.image_url " +
                            "FROM image as i " +
                            "WHERE i.image_news_id = ?",
                    arrayOf<String>(it.id.toString())
            )

            val images = mutableListOf<Image>()

            if (c != null && c!!.count > 0) {

                while (c!!.moveToNext()) {

                    val author = c?.getString(c!!.getColumnIndex(ImagePersistenceContract.COLUMN_NAME_AUTHOR))
                    val source = c?.getString(c!!.getColumnIndex(ImagePersistenceContract.COLUMN_NAME_SOURCE))
                    val caption = c?.getString(c!!.getColumnIndex(ImagePersistenceContract.COLUMN_NAME_CAPTION))
                    val url = c?.getString(c!!.getColumnIndex(ImagePersistenceContract.COLUMN_NAME_URL))

                    images.add(Image(author!!, source!!, caption!!, url!!))
                }
            }

            c?.close()

            it.authors = if (authors.isEmpty()) null else authors
            it.videos = if (videos.isEmpty()) null else videos
            it.images = if (images.isEmpty()) null else images
        }

        db?.close()

        expectedNews.forEach {
            assertTrue(news.contains(it))
        }
    }

    // endregion

    // region TEARDOWN METHODS

    @After fun teardown() {

        newsLocalDataSource?.deleteAllNews()
    }

    // endregion

    // region UTILITY METHODS

    private fun loadJsonFromAsset(fileName: String): String? {

        var jsonStr: String?
        var inputStream: InputStream? = null

        try {

            inputStream = javaClass.classLoader.getResourceAsStream(fileName)

            val size = inputStream.available()
            val buffer = ByteArray(size)

            inputStream.read(buffer)

            jsonStr = String(buffer, Charset.forName("UTF-8"))

        } catch (ex: IOException) {

            ex.printStackTrace()

            return null

        } finally {

            inputStream?.close()
        }

        return jsonStr
    }

    private fun createTestData(): List<News> {

        val listType = object : TypeToken<List<News>>(){}.type

        val news = Gson().fromJson<List<News>>(loadJsonFromAsset("headlines.json"), listType)

        return news
    }

    // endregion
}