package spring.product

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/products")
class ProductPort(val repo: ProductRepo) {

    @PostMapping
    fun post(@RequestBody productView: ProductView): ResponseEntity<ProductView> {

        val product =  Product(
            ArticleNo(productView.articleNo),
            ProductName(productView.name),
            EAN(productView.ean)
        )

        repo.save(product)

        return ResponseEntity.ok(ProductView.fromProduct(product))
    }

    @PutMapping("/{articleNo}")
    fun put(@PathVariable articleNo: String): ResponseEntity<ProductView> {
        val product = repo.findFirstByArticleNo(ArticleNo(articleNo))
        if (product == null) {
            return ResponseEntity.notFound().build()
        }

        // TODO update product

        return ResponseEntity.ok(ProductView.fromProduct(product))
    }

    @GetMapping("/{articleNo}")
    fun get(@PathVariable articleNo: String): ResponseEntity<ProductView> {
        val product = repo.findFirstByArticleNo(ArticleNo(articleNo))

        return if(product != null) {
            ResponseEntity.ok(ProductView.fromProduct(product))
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping
    fun getAll(): ResponseEntity<List<ProductView>> {
        val list = repo.findAll()
            .map { product -> ProductView.fromProduct(product) }

        return ResponseEntity.ok(list)
    }
}

data class ProductView(
    val articleNo: String,
    val name: String,
    val ean: String
) {
    companion object {
        fun fromProduct(product: Product): ProductView = ProductView(
            product.articleNo.value,
            product.name.name,
            product.ean.value
        )

        fun fromJson(json: String): ProductView {
            return jacksonObjectMapper().readValue(json, ProductView::class.java)
        }
    }
}