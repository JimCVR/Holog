package com.reactorsolutions.holog.services.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.reactorsolutions.holog.exceptions.category.CategoryNotFoundException
import com.reactorsolutions.holog.exceptions.item.ItemNotFoundException
import com.reactorsolutions.holog.models.Item
import com.reactorsolutions.holog.repositories.CategoriesRepository
import com.reactorsolutions.holog.repositories.ItemsRepository
import com.reactorsolutions.holog.services.api.ItemsServiceAPI
import com.theokanning.openai.OpenAiApi
import com.theokanning.openai.completion.CompletionRequest
import com.theokanning.openai.completion.chat.ChatMessage
import com.theokanning.openai.service.OpenAiService
import com.theokanning.openai.service.OpenAiService.*
import okhttp3.OkHttpClient
import org.springframework.stereotype.Service
import retrofit2.Retrofit
import java.time.Duration

@Service
class ItemsServiceImpl(var itemsRepository: ItemsRepository, var categoriesRepository: CategoriesRepository) :
    ItemsServiceAPI {

    override fun getAllItems(): Set<Item> {
        return itemsRepository.findAllByOrderByIdAsc().toSet()
    }

    override fun getItemById(id: Long): Item {
         return itemsRepository.findById(id).orElseThrow { ItemNotFoundException("Item Not found") }
    }

    override fun getItemByCategory(categoryId: Long): Set<Item> {
        val category =
            categoriesRepository.findById(categoryId).orElseThrow { CategoryNotFoundException("Category not found") }
        return category.items!!
    }

    override fun createItem(categoryId: Long, item: Item): Item {
        categoriesRepository.findById(categoryId).ifPresentOrElse({
            item.category = it
        }, {
            throw CategoryNotFoundException("Category not found")
        })
        return itemsRepository.save(item)
    }

    override fun recommendation(): Set<Item> {
        val token = "token"
        val mapper: ObjectMapper = defaultObjectMapper()
        val client: OkHttpClient = defaultClient(token, Duration.ofMillis(60000))
            .newBuilder()
            .build()
        val retrofit: Retrofit = defaultRetrofit(client, mapper)
        val api = retrofit.create(OpenAiApi::class.java)
        val service = OpenAiService(api)

        val messages = mutableListOf<ChatMessage>()
        messages.add(ChatMessage("system", "you are a helpful assistant."))
        messages.add(
            ChatMessage(
                "user",
                "You are a recommendation assistant. The user will send you a category and a list of elements of this category and based on them, you should recommend it up to 3 new elements in this category. Your answer should be a json array with name, description and date as fields. User prompt: #Category: Movies from 90s, elements: Terminator 2, Titanic#\""
            )
        )
        val completionRequest = CompletionRequest.builder()
            .model("text-davinci-003")
            .prompt("You are a recommendation assistant. The user will send you a category and a list of elements of this category and based on them, you should recommend it up to 3 new elements in this category. Your answer should be a json array with name, description and date as fields. User prompt: #Category: Movies from 90s, elements: Terminator 2, Titanic#\"")
            .temperature(0.0)
            .maxTokens(200)
            .topP(1.0)
            .n(1)
            .build()
        val response = service.createCompletion(completionRequest)
        val items = response.choices.map { choice ->
            val item = Item("nombre")
            item.description = choice.text
            item
        }
        return items.toSet()
    }
    /*
    * curl https://api.openai.com/v1/chat/completions
  -H "Content-Type: application/json"
  -H "Authorization: Bearer "
  -d '{
     "model": "gpt-3.5-turbo",
     "messages": [{"role": "user", "content": "Say this is a test!"}],
     "temperature": 0.7
   }'*/

    override fun updateItem(categoryId: Long,id: Long, itemUpdated: Item): Boolean {
        itemsRepository.findById(id).orElseThrow { ItemNotFoundException("Item not found") }
        itemUpdated.category = categoriesRepository.findById(categoryId).orElseThrow { CategoryNotFoundException("Category not found") }
        itemUpdated.id = id
        var savedItem = itemsRepository.save(itemUpdated)
        return savedItem == itemUpdated
    }

    override fun deleteItem(id: Long): Item {
        val item: Item? = itemsRepository.findById(id).orElseThrow { ItemNotFoundException("Item not found") }
        itemsRepository.delete(item!!)
        return item!!
    }

}