package spring.product

import org.assertj.core.api.Assertions
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner
import spring.App
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [App::class], webEnvironment = RANDOM_PORT)
class ProductPortTest {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @Test
    fun `we can create a new product`() {
        // given - a new product
        val articleNo = "12345678"
        val product = ProductView(articleNo, "Bier", articleNo)

        // when - post on the resource
        val result = testRestTemplate.postForEntity("/products", product, String::class.java)

        // then - http ok and the new product as response
        assertNotNull(result)
        assertEquals(HttpStatus.OK, result?.statusCode)
        assertProductResponse(result.body ?: "", articleNo)
    }

    @Test
    fun `rejects product with empty name`() {

    }

    @Test
    fun `rejects product with empty article number`() {

    }

    @Test
    fun `rejects product without EAN`() {

    }

    @Test
    fun `we can get an existing product`() {
        // given - an existing product
        val articleNo = "000001"
        anExistingProduct("000001")

        // when - a get on the articleNo is made
        val result = testRestTemplate.getForEntity("/products/$articleNo", String::class.java)

        // then - http ok and the product response
        assertNotNull(result)
        assertEquals(HttpStatus.OK, result?.statusCode)
        assertProductResponse(result?.body ?: "", articleNo)
    }

    @Test
    fun `cannot create product with same article number twice`() {
        anExistingProduct("42")

        val result = testRestTemplate.getForEntity("/products/42", String::class.java)

        Assertions.assertThat(result.statusCode.is2xxSuccessful)
                .`as`("Expected second product creation request to be rejected.")
                .isFalse()
    }

    @Test
    fun `can update existing product`() {
        anExistingProduct("42")

        val updatedProduct = ProductView("42", "Wein", "12345")
        testRestTemplate.exchange("/products/42", HttpMethod.PUT, HttpEntity(updatedProduct), String::class.java)

        val productFromApi = getProduct("42")
        Assertions.assertThat(productFromApi).isNotNull
        Assertions.assertThat(productFromApi!!.name)
                .isNotNull()
                .isEqualTo("Wein")
    }

    @Test
    fun `error when trying to update a product that does not exist`() {

    }

    private fun assertProductResponse(responseBody: String, articleNo: String) {
        Assertions.assertThat(responseBody).contains("\"articleNo\":\"$articleNo\"");
    }

    private fun anExistingProduct(articleNo: String) {
        val product = ProductView(articleNo, "Bier", "12345678")
        testRestTemplate.postForEntity("/products", product, String::class.java)
    }

    private fun getProduct(articleNo: String): ProductView? {
        val result = testRestTemplate.getForEntity("/products/$articleNo", String::class.java)
        assertNotNull(result)

        if (result.statusCode.value() == 404) {
            return null
        }
        val body = result.body
        assertNotNull(body)

        return ProductView.fromJson(body!!)
    }
}