package com.reactorsolutions.holog.controllers

import com.reactorsolutions.holog.dto.RequestItemDTO
import com.reactorsolutions.holog.dto.ResponseItemDTO
import com.reactorsolutions.holog.exceptions.category.CategoryNotFoundException
import com.reactorsolutions.holog.exceptions.item.ItemNotFoundException
import com.reactorsolutions.holog.models.Item
import com.reactorsolutions.holog.services.api.ItemsServiceAPI
import com.reactorsolutions.holog.utils.mapper.item.ItemToResponse
import com.reactorsolutions.holog.utils.mapper.item.RequestToItem
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

class ItemsControllerTest {

    private val fromRequestToItem = RequestToItem()
    private val fromItemToResponse = ItemToResponse()

    @Test
    fun getAllItemsReturn200WhenThereAreTwoElements() {

        val serviceMock = mockk<ItemsServiceAPI>()
        every { serviceMock.getAllItems() } returns mutableListOf(
            Item(
                "Interestellar", "lorem ipsum", "Christopher Nolan",
                mutableSetOf(), 1
            ),
            Item("Avatar", "dolor upsim", "James Cameron", mutableSetOf(), 2)
        )

        val itemsController = ItemsController(
            serviceMock,
            this.fromRequestToItem, this.fromItemToResponse
        )
        val items = itemsController.getItems(1)
        assertEquals(HttpStatus.OK, items.statusCode)
        assertEquals(
            mutableListOf<ResponseItemDTO>(
                ResponseItemDTO(1, "Interestellar", "lorem ipsum", "Christopher Nolan"),
                ResponseItemDTO(2, "Avatar", "dolor upsim", "James Cameron")
            ), items.body
        )
    }

    @Test
    fun getAllItemsReturn200WhenThereIsOneElements() {
        val serviceMock = mockk<ItemsServiceAPI>()
        every { serviceMock.getAllItems() } returns mutableListOf(
            Item(
                "Interestellar", "lorem ipsum", "Christopher Nolan",
                mutableSetOf(), 1
            )
        )
        val itemsController = ItemsController(
            serviceMock,
            this.fromRequestToItem, this.fromItemToResponse
        )
        val items = itemsController.getItems(1)
        assertEquals(HttpStatus.OK, items.statusCode)
        assertEquals(
            mutableListOf<ResponseItemDTO>(
                ResponseItemDTO(1, "Interestellar", "lorem ipsum", "Christopher Nolan"),
            ),
            items.body
        )
    }

    @Test
    fun getAllItemsReturn200WhenThereAreNoElements() {
        val serviceMock = mockk<ItemsServiceAPI>()
        every { serviceMock.getAllItems() } returns mutableListOf()

        val itemsController = ItemsController(
            serviceMock,
            this.fromRequestToItem, this.fromItemToResponse
        )
        val items = itemsController.getItems(1)
        assertEquals(HttpStatus.OK, items.statusCode)
        assertEquals(mutableListOf<ResponseItemDTO>(), items.body)

    }

    //GET ITEM BY ID TESTS
    @Test
    fun getItemByIdReturn200WhenThereAreOneElement() {
        val serviceMock = mockk<ItemsServiceAPI>()
        every { serviceMock.getItemById(1) } returns Item(
            "Interestellar", "lorem ipsum", "Christopher Nolan",
            mutableSetOf(), 1
        )
        val itemsController = ItemsController(
            serviceMock,
            this.fromRequestToItem, this.fromItemToResponse
        )
        val items = itemsController.getItemById(1, 1)
        assertEquals(HttpStatus.OK, items.statusCode)
        assertEquals(ResponseItemDTO(1, "Interestellar", "lorem ipsum", "Christopher Nolan"), items.body)
    }

    @Test
    fun getItemByIdReturn404WhenThereAreNoElement() {
        val serviceMock = mockk<ItemsServiceAPI>()
        every { serviceMock.getItemById(1) } throws ItemNotFoundException("Item not found")
        val itemsController = ItemsController(
            serviceMock,
            this.fromRequestToItem, this.fromItemToResponse
        )
        assertThrows<ItemNotFoundException> { itemsController.getItemById(1, 1) }
    }

    //CREATE ITEM
    @Test
    fun createItemByIdReturn201WhenElementCreated() {

        val serviceMock = mockk<ItemsServiceAPI>()
        every {
            serviceMock.createItem(
                Item(
                    "Interestellar",
                    "lorem ipsum",
                    "Christopher Nolan"
                )
            )
        } returns Item("Interestellar", "lorem ipsum", "Christopher Nolan", mutableSetOf(), 1)
        val mockServletRequest = MockHttpServletRequest()
        RequestContextHolder.setRequestAttributes(ServletRequestAttributes(mockServletRequest))
        val location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(1)
            .toUri()

        val itemsController = ItemsController(
            serviceMock,
            this.fromRequestToItem, this.fromItemToResponse
        )
        val items = itemsController.insertItem(
            1, RequestItemDTO(
                "Interestellar",
                "lorem ipsum",
                "Christopher Nolan"
            )
        )
        assertEquals(HttpStatus.CREATED, items.statusCode)
        assertEquals("Item created", items.body)
    }


    @Test
    fun createItemReturn404WhenNotCreated() {
        val serviceMock = mockk<ItemsServiceAPI>()
        every {
            serviceMock.createItem(
                Item(
                    "Interestellar",
                    "lorem ipsum",
                    "Christopher Nolan"
                )
            )
        } throws ItemNotFoundException("Item not found")
        val itemsController = ItemsController(
            serviceMock,
            this.fromRequestToItem, this.fromItemToResponse
        )
        assertThrows<ItemNotFoundException> {
            itemsController.insertItem(
                1,
                RequestItemDTO(
                    "Interestellar",
                    "lorem ipsum",
                    "Christopher Nolan"
                )
            )
        }
    }

    @Test
    fun createItemReturn407WhenNameIsBlank() {
        val serviceMock = mockk<ItemsServiceAPI>()
        every {
            serviceMock.createItem(
                Item(
                    "",
                    "lorem ipsum",
                    "Christopher Nolan"
                )
            )
        } returns Item(
            "",
            "lorem ipsum",
            "Christopher Nolan"
        )
        val itemsController = ItemsController(
            serviceMock,
            this.fromRequestToItem, this.fromItemToResponse
        )
        val item = itemsController.insertItem(
            1, RequestItemDTO(
                "",
                "lorem ipsum",
                "Christopher Nolan"
            )
        )
        assertEquals(HttpStatus.PRECONDITION_FAILED, item.statusCode)
        assertEquals("Item not created", item.body)
    }

    //UPDATE ITEM

    @Test
    fun updateItemByIdReturn200WhenElementModified() {
        val serviceMock = mockk<ItemsServiceAPI>()
        every {
            serviceMock.updateItem(
                1,
                Item(
                    "Interestellar",
                    "lorem ipsum",
                    "Christopher Nolan",
                    null
                )
            )
        } returns true
        val itemsController = ItemsController(
            serviceMock,
            this.fromRequestToItem, this.fromItemToResponse
        )
        val item = itemsController.updateItem(
            1, 1, RequestItemDTO(
                "Interestellar",
                "lorem ipsum",
                "Christopher Nolan"
            )
        )
        assertEquals(HttpStatus.OK, item.statusCode)
        assertEquals("Item updated", item.body)
    }

    @Test
    fun updateCategoryByIdReturn407WhenNameIsBlank() {
        val serviceMock = mockk<ItemsServiceAPI>()
        every {
            serviceMock.updateItem(
                1,
                Item(
                    "",
                    "lorem ipsum",
                    "Christopher Nolan", mutableSetOf(), 1
                )
            )
        } returns true
        val itemsController = ItemsController(
            serviceMock,
            this.fromRequestToItem, this.fromItemToResponse
        )
        val item = itemsController.updateItem(
            1, 1, RequestItemDTO(
                "",
                "lorem ipsum",
                "Christopher Nolan"
            )
        )
        assertEquals(HttpStatus.PRECONDITION_FAILED, item.statusCode)
        assertEquals("Item not modified", item.body)
    }

    @Test
    fun updateCategoryByIdReturn404WhenIdNotFound() {
        val serviceMock = mockk<ItemsServiceAPI>()
        every {
            serviceMock.updateItem(
                1,
                Item(
                    "Interestellar",
                    "lorem ipsum",
                    "Christopher Nolan"
                )
            )
        } throws CategoryNotFoundException("Item not found")
        val itemsController = ItemsController(
            serviceMock,
            this.fromRequestToItem, this.fromItemToResponse
        )
        assertThrows<CategoryNotFoundException> {
            itemsController.updateItem(
                1,
                1,
                RequestItemDTO(
                    "Interestellar",
                    "lorem ipsum",
                    "Christopher Nolan"
                )
            )
        }
    }

    //DELETE CATEGORY
    @Test
    fun deleteItemByIdReturn200WhenElementDeleted() {
        val serviceMock = mockk<ItemsServiceAPI>()
        every { serviceMock.deleteItem(1) } returns Item(
            "Interestellar",
            "lorem ipsum",
            "Christopher Nolan", mutableSetOf(), 1
        )
        val itemsController = ItemsController(
            serviceMock,
            this.fromRequestToItem, this.fromItemToResponse
        )
        val items = itemsController.deleteItem(1, 1)
        assertEquals(HttpStatus.OK, items.statusCode)
        assertEquals(
            ResponseItemDTO(
                1, "Interestellar", "lorem ipsum",
                "Christopher Nolan"
            ), items.body
        )
    }

    @Test
    fun deleteItemByIdReturn404WhenElementNotDeleted() {
        val serviceMock = mockk<ItemsServiceAPI>()
        every { serviceMock.deleteItem(1) } throws ItemNotFoundException("Item not found")
        val itemsController = ItemsController(
            serviceMock,
            this.fromRequestToItem, this.fromItemToResponse
        )
        assertThrows<ItemNotFoundException> { itemsController.deleteItem(1, 1) }
    }
}