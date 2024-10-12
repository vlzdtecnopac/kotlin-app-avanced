package com.momocoffee.app.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.momocoffee.app.App
import com.momocoffee.app.ui.category.Category
import com.momocoffee.app.ui.chekout.Checkout
import com.momocoffee.app.ui.chekout.section.SuccessFullyPayment
import com.momocoffee.app.ui.client.Client
import com.momocoffee.app.ui.client.section.RegisterClient
import com.momocoffee.app.ui.components.AlertInvoiceState
import com.momocoffee.app.ui.components.SuccessPaymentModal
import com.momocoffee.app.ui.orderhere.OrderHere
import com.momocoffee.app.ui.login.Login
import com.momocoffee.app.ui.products.Products
import com.momocoffee.app.ui.products.section.Product
import com.momocoffee.app.ui.wellcome.WellCome
import com.momocoffee.app.ui.zettle.ZettlePayment
import com.momocoffee.app.viewmodel.CartViewModel

import com.momocoffee.app.viewmodel.LoginViewModel

@Composable
fun NavigationScreen(viewModelCart: CartViewModel, stateInvoice: String, resetState: () -> Unit) {

    val navController = rememberNavController()
    val sharedPreferences = App.instance.getSharedPreferences("momo_prefs", Context.MODE_PRIVATE)
    val token = sharedPreferences.getString("token", null) ?: ""
    val kiosko_id = sharedPreferences.getString("kioskoId", null) ?: ""

    NavHost(
        navController = navController,
        startDestination = Destination.getStartDestination(token, kiosko_id)
    ) {
        composable(route = Destination.Login.route) {
            Login(navController = navController)
        }

        composable(route = Destination.Wellcome.route) {
            WellCome(navController = navController)
        }

        composable(route = Destination.OrderHere.route) {
            AlertInvoiceState(
                navController = navController,
                stateInvoice,
                viewModelCart,
                resetState
            )
            OrderHere(navController = navController, viewModelCart)
        }

        composable(route = Destination.Client.route) {
            Client(navController = navController)
        }

        composable(
            route = Destination.Product.route,
            arguments = listOf(
                navArgument("category") { defaultValue = "" },
                navArgument("product_id") { defaultValue = "" })
        ) { backStackEntry ->
            val product_id = backStackEntry.arguments?.getString("product_id")
            val category = backStackEntry.arguments?.getString("category")
            Product(navController = navController, category, product_id, viewModelCart)
        }

        composable(
            route = Destination.ProductsCategoryAndSubProduct.route,
            arguments = listOf(
                navArgument("category") { defaultValue = "" },
                navArgument("subcategory") { defaultValue = "" })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category")
            val subcategory = backStackEntry.arguments?.getString("subcategory")
            Products(navController = navController, category, subcategory, viewModelCart)
        }

        composable(
            route = Destination.ProductsCategory.route,
            arguments = listOf(navArgument("category") { defaultValue = "" })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category")
            Products(navController = navController, category, viewModelCart = viewModelCart)
        }

        composable(route = Destination.Checkout.route) {
            Checkout(navController = navController, viewModelCart)
        }

        composable(route = Destination.Category.route) {
            Category(navController = navController, viewModelCart = viewModelCart)
        }

        composable(route = Destination.Zettle.route) {
            ZettlePayment(navController = navController)
        }

        composable(route = Destination.RegisterClient.route) {
            RegisterClient(navController = navController)
        }

        composable(route = Destination.SuccessPayment.route) {
            SuccessFullyPayment(navController = navController, cartViewModel = viewModelCart, resetState)
        }
    }
}