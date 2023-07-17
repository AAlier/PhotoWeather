package com.photoweather.weather.database

import com.photoweather.weather.data.Gallery
import com.photoweather.weather.database.model.GalleryEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.unmockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Calendar

@ExperimentalCoroutinesApi
class GalleryLocalRepositoryImplTest {

    private lateinit var repository: GalleryLocalRepositoryImpl
    private lateinit var galleryDao: GalleryDao

    @Before
    fun setup() {
        galleryDao = mockk(relaxed = true)
        repository = GalleryLocalRepositoryImpl(galleryDao)
    }

    @After
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun `listenGallery should emit mapped Gallery objects`() = runTest {
        // Given
        val galleryEntities = listOf(
            GalleryEntity(id = 1, imagePath = "path1", createdAt = Calendar.getInstance()),
            GalleryEntity(id = 2, imagePath = "path2", createdAt = Calendar.getInstance())
        )
        val expectedGalleries = galleryEntities.map {
            Gallery(id = it.id, imagePath = it.imagePath, createdAt = it.createdAt)
        }
        coEvery { galleryDao.listen() } returns flowOf(galleryEntities)

        // When
        val flow = repository.listenGallery()

        // Then
        flow.collect { galleries ->
            galleries.forEachIndexed { index, gallery ->
                assert(gallery == expectedGalleries[index])
            }
        }

        coVerify { galleryDao.listen() }
    }

    @Test
    fun `save should insert a new GalleryEntity`() = runBlocking {
        // Given
        val imagePath = "path"
        val calendarMock = mockk<Calendar>(relaxed = true)
        mockkStatic(Calendar::class)
        every { Calendar.getInstance() } returns calendarMock

        // When
        repository.save(imagePath)

        // Then
        val expectedEntity = GalleryEntity(createdAt = calendarMock, imagePath = imagePath)
        coVerify { galleryDao.insert(expectedEntity) }
        unmockkStatic(Calendar::class)
    }

    @Test
    fun `delete should call delete function in GalleryDao`() = runBlocking {
        // Given
        val id = 1L

        // When
        repository.delete(id)

        // Then
        coVerify { galleryDao.delete(id) }
    }
}