package com.reactorsolutions.holog.controllers

import com.reactorsolutions.holog.dto.RequestItemDTO
import com.reactorsolutions.holog.dto.ResponseItemDTO
import com.reactorsolutions.holog.exceptions.item.ItemNotFoundException
import com.reactorsolutions.holog.models.Category
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


class ItemsControllerTest {

    private val fromRequestToItem = RequestToItem()
    private val fromItemToResponse = ItemToResponse()

    @Test
    fun getAllItemsReturn200WhenThereAreTwoElements() {

        val serviceMock = mockk<ItemsServiceAPI>()
        every { serviceMock.getAllItems() } returns mutableSetOf<Item>(
            Item(
                "Interestellar", "lorem ipsum","imageExample.png",
                4.7, null,"pending",false, category = Category("categoria1",2, mutableSetOf(),"o19249u42",1),123
            ),
            Item(
                "Ejemplo1", "lorem ipsum","imageExample.png",
                5.7, null,"pending",true, category = Category("categoria1",2, mutableSetOf(),"o19249u42",1),321
            )
        )

        val itemsController = ItemsController(
            serviceMock,
            this.fromRequestToItem, this.fromItemToResponse
        )
        val items = itemsController.getItems("o19249u42")
        assertEquals(HttpStatus.OK, items.statusCode)
        assertEquals(
            mutableListOf<ResponseItemDTO>(
                ResponseItemDTO(123, "Interestellar", "lorem ipsum", "imageExample.png",4.7,null,"pending",false,1),
                ResponseItemDTO(321, "Ejemplo1", "lorem ipsum", "imageExample.png",5.7,null,"pending",true,1)
            ), items.body
        )
    }

    @Test
    fun getAllItemsReturn200WhenThereIsOneElements() {
        val serviceMock = mockk<ItemsServiceAPI>()
        every { serviceMock.getAllItems() } returns mutableSetOf(
            Item(
                "Interestellar", "lorem ipsum","imageExample.png",
                4.7, null,"pending",false, category = Category("categoria1",2, mutableSetOf(),"o19249u42",1),123
            )
        )
        val itemsController = ItemsController(
            serviceMock,
            this.fromRequestToItem, this.fromItemToResponse
        )
        val items = itemsController.getItems("o19249u42")
        assertEquals(HttpStatus.OK, items.statusCode)
        assertEquals(
            mutableListOf<ResponseItemDTO>(
                ResponseItemDTO(123, "Interestellar", "lorem ipsum", "imageExample.png",4.7,null,"pending",false,1)
            ),
            items.body
        )
    }

    @Test
    fun getAllItemsReturn200WhenThereAreNoElements() {
        val serviceMock = mockk<ItemsServiceAPI>()
        every { serviceMock.getAllItems() } returns mutableSetOf()

        val itemsController = ItemsController(
            serviceMock,
            this.fromRequestToItem, this.fromItemToResponse
        )
        val items = itemsController.getItems("123")
        assertEquals(HttpStatus.OK, items.statusCode)
        assertEquals(mutableListOf<ResponseItemDTO>(), items.body)

    }

    //GET ITEM BY ID TESTS
    @Test
    fun getItemByIdReturn200WhenThereAreOneElement() {
        val serviceMock = mockk<ItemsServiceAPI>()
        every { serviceMock.getItemById(123) } returns Item(
            "Interestellar", "lorem ipsum","imageExample.png",
            4.7, null,"pending",false, category = Category("categoria1",2, mutableSetOf(),"o19249u42",1),123
        )
        val itemsController = ItemsController(
            serviceMock,
            this.fromRequestToItem, this.fromItemToResponse
        )
        val items = itemsController.getItemById("o19249u42", 123)
        assertEquals(HttpStatus.OK, items.statusCode)
        assertEquals(ResponseItemDTO(123, "Interestellar", "lorem ipsum", "imageExample.png",4.7,null,"pending",false,1), items.body)
    }

    @Test
    fun getItemByIdReturn404WhenThereAreNoElement() {
        val serviceMock = mockk<ItemsServiceAPI>()
        every { serviceMock.getItemById(1) } throws ItemNotFoundException("Item not found")
        val itemsController = ItemsController(
            serviceMock,
            this.fromRequestToItem, this.fromItemToResponse
        )
        assertThrows<ItemNotFoundException> { itemsController.getItemById("1345", 1) }
    }

    //CREATE ITEM
    @Test
    fun createItemReturn201WhenElementCreated() {

        val serviceMock = mockk<ItemsServiceAPI>()
        every {
            serviceMock.createItem(
                123,
                Item(
                    "Interestellar", "lorem ipsum","imageExample.png",
                    4.7, null,"pending",false, null, null
                )
            )

        } returns Item(
            "Interestellar", "lorem ipsum","imageExample.png",
            4.7, null,"pending",false, category = Category("cat",1, mutableSetOf(),"o19249u42",123), 1
        )
        val mockServletRequest = MockHttpServletRequest()
        RequestContextHolder.setRequestAttributes(ServletRequestAttributes(mockServletRequest))
        val itemsController = ItemsController(
            serviceMock,
            this.fromRequestToItem, this.fromItemToResponse
        )
        val items = itemsController.insertItem(
            "o19249u42", RequestItemDTO(
                "Interestellar",
                "lorem ipsum",
                "imageExample.png",
                4.7,
                null,
                "pending",
                false,
                123
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
                123,
                Item(
                    "Interestellar", "lorem ipsum","imageExample.png",
                    4.7, null,"pending",false, null,null
                )
            )
        } throws ItemNotFoundException("Item not found")
        val itemsController = ItemsController(
            serviceMock,
            this.fromRequestToItem, this.fromItemToResponse
        )
        assertThrows<ItemNotFoundException> {
            itemsController.insertItem(
                "1",
                RequestItemDTO(
                    "Interestellar",
                    "lorem ipsum",
                    "imageExample.png",
                    4.7,
                    null,
                    "pending",
                    false,
                    123
                )
            )
        }
    }

    @Test
    fun createItemReturn407WhenNameIsBlank() {
        val serviceMock = mockk<ItemsServiceAPI>()
        every {
            serviceMock.createItem(
                123,
                Item(
                    "", "lorem ipsum","imageExample.png",
                    4.7, null,"pending",false, category = Category("categoria1",2, mutableSetOf(),"o19249u42",123),null
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
            "1", RequestItemDTO(
                "",
                "lorem ipsum",
                "imageExample.png",
                4.7,
                null,
                "pending",
                false,
                123
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
                1,
                Item(
                    "nuevoNombre", "lorem ipsum","imageExample.png",
                    4.7, null,"pending",false, null, null
                )
            )
        } returns true
        val itemsController = ItemsController(
            serviceMock,
            this.fromRequestToItem, this.fromItemToResponse
        )
        val item = itemsController.updateItem(
            "1",
            1,
            RequestItemDTO(
                "nuevoNombre",
                "lorem ipsum",
                "imageExample.png",
                4.7,
                null,
                "pending",
                false,
                1
            )
        )
        assertEquals(HttpStatus.OK, item.statusCode)
        assertEquals("Item updated", item.body)
    }

    @Test
    fun updateItemByIdReturn407WhenNameIsBlank() {
        val serviceMock = mockk<ItemsServiceAPI>()
        every {
            serviceMock.updateItem(
                1,
                1,
                Item(
                    "", "lorem ipsum","imageExample.png",
                    4.7, null,"pending",false, category = Category("categoria1",2, mutableSetOf(),"o19249u42",123),1
                )
            )
        } returns true
        val itemsController = ItemsController(
            serviceMock,
            this.fromRequestToItem, this.fromItemToResponse
        )
        val item = itemsController.updateItem(
            "1",
            1,
            RequestItemDTO(
                "",
                "lorem ipsum",
                "imageExample.png",
                4.7,
                null,
                "pending",
                false,
                123
            )
        )
        assertEquals(HttpStatus.PRECONDITION_FAILED, item.statusCode)
        assertEquals("Item not modified", item.body)
    }

    @Test
    fun updateItemByIdReturn404WhenIdNotFound() {
        val serviceMock = mockk<ItemsServiceAPI>()
        every {
            serviceMock.updateItem(
                1,
                1,
                Item(
                    "Interestellar", "lorem ipsum","imageExample.png",
                    4.7, null,"pending",false,null,null
                )
            )
        } throws ItemNotFoundException("Item not found")
        val itemsController = ItemsController(
            serviceMock,
            this.fromRequestToItem, this.fromItemToResponse
        )
        assertThrows<ItemNotFoundException> {
            itemsController.updateItem(
                "1",
                1,
                RequestItemDTO(
                    "Interestellar",
                    "lorem ipsum",
                    "imageExample.png",
                    4.7,
                    null,
                    "pending",
                    false,
                    1
                )
            )
        }
    }

    //DELETE CATEGORY
    @Test
    fun deleteItemByIdReturn200WhenElementDeleted() {
        val serviceMock = mockk<ItemsServiceAPI>()
        every { serviceMock.deleteItem(1) } returns Item(
            "Interestellar", "lorem ipsum","imageExample.png",
            4.7, null,"pending",false, category = Category("categoria1",2, mutableSetOf(),"o19249u42",1),1
        )
        val itemsController = ItemsController(
            serviceMock,
            this.fromRequestToItem, this.fromItemToResponse
        )
        val items = itemsController.deleteItem("o19249u42", 1)
        assertEquals(HttpStatus.OK, items.statusCode)
        assertEquals(
            ResponseItemDTO(1, "Interestellar", "lorem ipsum", "imageExample.png",4.7,null,"pending",false,1), items.body
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
        assertThrows<ItemNotFoundException> { itemsController.deleteItem("1", 1) }
    }
}