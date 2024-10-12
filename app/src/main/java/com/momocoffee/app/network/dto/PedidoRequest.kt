package com.momocoffee.app.network.dto


data class PedidoRequest(
    val client_id: String,
    val total: String,
    val name_client: String,
    val shopping_id: String,
    val kiosko_id: String,
    val bildings_id: String,
    val columns_pending: Int,
    val name_cupon: String,
    val product: String
)

data class Producto(
    val id: String,
    val name_product: String,
    val price: Int,
    val image: String,
    val extra: Extra,
    val quanty: Int,
    val subtotal: Int
)

data class Extra(
    val temperature: Temperature,
    val size: Size,
    val milk: Milk,
    val sugar: Sugar,
    val color: Colors,
    val endulzante: Endulzante,
    val extra: List<ExtraCoffee>,
    val libTapa: Lid,
    val sauce: List<Sauce>,
)

data class Size(val id: String, val name: String, val price: Int)
data class Milk(val id: String, val name: String, val price: Int)
data class Sugar(val id: String, val name: String, val price: Int)
data class Endulzante(val id: String, val name: String, val price: Int)
data class ExtraCoffee(val id: String, val name: String, val price: Int)
data class Lid(val id: String, val name: String, val price: Int)
data class Sauce(val id: String, val name: String, val price: Int)
data class Temperature(val id: String, val name: String, val price: Int)

data class Colors(val id: String, val name: String, val price: Int)


fun productoToString(producto: Producto): String {
    return """
        {
            "id": "${producto.id}",
            "name_product": "${producto.name_product}",
            "price": ${producto.price},
            "image": "${producto.image}",
            "extra": {
                "size": { 
                    "id": "${producto.extra.size.id}", 
                    "name": "${producto.extra.size.name}", 
                    "price": ${producto.extra.size.price} 
                },
                "milk": { 
                    "id": "${producto.extra.milk.id}", 
                    "name": "${producto.extra.milk.name}", 
                    "price": ${producto.extra.milk.price} 
                },
                "sugar": { 
                    "id": "${producto.extra.sugar.id}", 
                    "name": "${producto.extra.sugar.name}", 
                    "price": ${producto.extra.sugar.price} 
                },
                "endulzante": { 
                    "id": "${producto.extra.endulzante.id}", 
                    "name": "${producto.extra.endulzante.name}", 
                    "price": ${producto.extra.endulzante.price} 
                },
                "extra": ${
        producto.extra.extra.joinToString(", ", "[", "]") {
            """{ "id": "${it.id}", "name": "${it.name}", "price": ${it.price} }"""
        }
    }, 
    "libTapa": { 
                    "id": "${producto.extra.libTapa.id}", 
                    "name": "${producto.extra.libTapa.name}", 
                    "price": ${producto.extra.libTapa.price} 
                },
                "sauce": ${
        producto.extra.sauce.joinToString(", ", "[", "]") {
            """{ "id": "${it.id}", "name": "${it.name}", "price": ${it.price} }"""
        }
    },
                "temperature": { 
                    "id": "${producto.extra.temperature.id}", 
                    "name": "${producto.extra.temperature.name}", 
                    "price": ${producto.extra.temperature.price} 
                },
                "color": { 
                    "id": "${producto.extra.color.id}", 
                    "name": "${producto.extra.color.name}", 
                    "price": ${producto.extra.color.price} 
                }
            },
            "quanty": ${producto.quanty},
            "subtotal": ${producto.subtotal}
        }
    """.trimIndent()
}

fun productosToString(productos: List<Producto>): String {
    return productos.joinToString(separator = ", ", prefix = "[", postfix = "]") {
        productoToString(
            it
        )
    }
}
