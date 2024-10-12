package com.momocoffee.app.ui.chekout.section

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.momocoffee.app.R
import com.momocoffee.app.navigation.Destination
import com.momocoffee.app.network.dto.BuildingRequest
import com.momocoffee.app.network.dto.ClientEmailInvoiceRequest
import com.momocoffee.app.network.dto.Colors
import com.momocoffee.app.network.dto.Endulzante
import com.momocoffee.app.network.dto.Extra
import com.momocoffee.app.network.dto.ExtraCoffee
import com.momocoffee.app.network.dto.Lid
import com.momocoffee.app.network.dto.Milk
import com.momocoffee.app.network.dto.Producto
import com.momocoffee.app.network.dto.Sauce
import com.momocoffee.app.network.dto.Size
import com.momocoffee.app.network.dto.Sugar
import com.momocoffee.app.network.dto.Temperature
import com.momocoffee.app.network.dto.UpdateBilingRequest
import com.momocoffee.app.network.dto.productosToString
import com.momocoffee.app.network.response.ItemShopping
import com.momocoffee.app.ui.chekout.components.ConfirmBonoModal
import com.momocoffee.app.ui.chekout.components.ConfirmEmailModal
import com.momocoffee.app.ui.chekout.components.ConfirmInput
import com.momocoffee.app.ui.chekout.components.ConfirmPayment
import com.momocoffee.app.ui.chekout.components.OutTextField
import com.momocoffee.app.ui.components.VerifyKiosko
import com.momocoffee.app.ui.components.cart.parseItemModifiers
import com.momocoffee.app.ui.theme.BlueDark
import com.momocoffee.app.ui.theme.BlueLight
import com.momocoffee.app.ui.theme.OrangeDark
import com.momocoffee.app.ui.theme.redhatFamily
import com.momocoffee.app.viewmodel.BuildingViewModel
import com.momocoffee.app.viewmodel.CartViewModel
import com.momocoffee.app.viewmodel.ClientViewModel
import io.socket.emitter.Emitter
import org.json.JSONObject

@Composable
fun ContentTypePayment(
    shoppingItems: List<ItemShopping>,
    onCancel: () -> Unit,
    valueSubTotal: Float,
    valueCupon: Float,
    valuePropina: Float,
    valueMontoDescuento: Float,
    valueTotal: Float,
    navController: NavController,
    cartViewModel: CartViewModel,
    contentConfirmModalCupon: Boolean,
    onClose: () -> Unit,
    clientViewModel: ClientViewModel = viewModel(),
    buildingViewModel: BuildingViewModel = viewModel()
) {
    val context = LocalContext.current
    var scrollState =  rememberScrollState()
    val sharedPreferences = context.getSharedPreferences("momo_prefs", Context.MODE_PRIVATE)
    val client_id = sharedPreferences.getString("clientId", null) ?: ""
    val shopping_id = sharedPreferences.getString("shoppingId", null) ?: ""
    val kiosko_id = sharedPreferences.getString("kioskoId", null) ?: ""
    val cuponName = sharedPreferences.getString("cuponName", null) ?: ""
    var showModalConfirmPayment by remember { mutableStateOf(value = false) }
    var showModalConfirmEmail by remember { mutableStateOf(value = false) }
    var invite by remember { mutableStateOf(value = "") }
    var email by remember { mutableStateOf(value = "") }
    var bilding_id by remember { mutableStateOf(value = "") }
    var productListString by remember { mutableStateOf(value = "") }
    var validTypePayment by remember { mutableStateOf(value = 0) }
    var typePaymentState by remember { mutableStateOf(value = "effecty") }
    val enterNameInvited = stringResource(id = R.string.enter_name_invitado)
    val notFoundProducts = stringResource(id = R.string.validate_products)

    VerifyKiosko(navController)

    LaunchedEffect(key1 = true) {
        SocketHandler.getSocket().on("building_finish_socket_app", Emitter.Listener { args ->
            val data = args[0] as JSONObject
            if (data.getString("shopping_id") == shopping_id && data.getString("kiosko_id") == kiosko_id) {
                showModalConfirmPayment = false
                showModalConfirmEmail = true
            }
        })
    }

    LaunchedEffect(Unit) {
        if (client_id.isNotBlank()) {
            clientViewModel.getClient("", "", client_id)
        }
    }

    LaunchedEffect(cartViewModel.state) {
        val newProducts = cartViewModel.state.carts.mapIndexed { index, item ->
            val itemsModifiersOptions = parseItemModifiers(item.modifiersOptions)
            val itemsListModifiersOptions = parseItemModifiers(item.modifiersList)

            Producto(
                id = item.productId,
                name_product = item.titleProduct,
                price = item.priceProduct.toInt(),
                image = item.imageProduct,
                extra = Extra(
                    size = Size(
                        itemsModifiersOptions["size"]?.id ?: "",
                        itemsModifiersOptions["size"]?.name ?: "",
                        itemsModifiersOptions["size"]?.price?.toInt() ?: 0
                    ),
                    milk = Milk(
                        itemsModifiersOptions["milk"]?.id ?: "",
                        itemsModifiersOptions["milk"]?.name ?: "",
                        itemsModifiersOptions["milk"]?.price?.toInt() ?: 0
                    ),
                    sugar = Sugar(
                        itemsModifiersOptions["sugar"]?.id ?: "",
                        itemsModifiersOptions["sugar"]?.name ?: "",
                        itemsModifiersOptions["sugar"]?.price?.toInt() ?: 0
                    ),
                    endulzante = Endulzante(
                        itemsModifiersOptions["endulzante"]?.id ?: "",
                        itemsModifiersOptions["endulzante"]?.name ?: "",
                        itemsModifiersOptions["endulzante"]?.price?.toInt() ?: 0
                    ),
                    extra = listOf(
                        ExtraCoffee(
                            itemsListModifiersOptions["extra"]?.id ?: "",
                            itemsListModifiersOptions["extra"]?.name ?: "",
                            itemsListModifiersOptions["extra"]?.price?.toInt() ?: 0
                        )
                    ),
                    libTapa = Lid(
                        itemsModifiersOptions["libTapa"]?.id ?: "",
                        itemsModifiersOptions["libTapa"]?.name ?: "",
                        itemsModifiersOptions["libTapa"]?.price?.toInt() ?: 0
                        ),
                    sauce = listOf(
                        Sauce(
                            itemsListModifiersOptions["sauce"]?.id ?: "",
                            itemsListModifiersOptions["sauce"]?.name ?: "",
                            itemsListModifiersOptions["sauce"]?.price?.toInt() ?: 0
                        )
                    ),
                    temperature = Temperature(
                        itemsModifiersOptions["temp"]?.id ?: "",
                        itemsModifiersOptions["temp"]?.name ?: "",
                        itemsModifiersOptions["temp"]?.price?.toInt() ?: 0
                    ),
                    color = Colors(
                        itemsModifiersOptions["color"]?.id ?: "",
                        itemsModifiersOptions["color"]?.name ?: "",
                        itemsModifiersOptions["color"]?.price?.toInt() ?: 0
                    ),
                ),
                quanty = item.countProduct,
                subtotal = item.priceProductMod.toInt()
            )
        }

        productListString = productosToString(newProducts)
    }


    LaunchedEffect(clientViewModel.clientResultCheckEmailState.value) {
        clientViewModel.clientResultCheckEmailState.value?.let { result ->
            when {
                result.isSuccess -> {
                    val userResponse = result.getOrThrow()
                    if (userResponse.items.isNotEmpty()) {
                        invite =
                            "${userResponse.items[0].firstName} ${userResponse.items[0].lastName}"
                        email = userResponse.items[0].email
                    }
                }

                result.isFailure -> {
                    val exception = result.exceptionOrNull()
                    Log.e("Result.ClientViewModel", exception.toString())
                }
            }
        }
    }

    LaunchedEffect(buildingViewModel.buildingResultState.value) {
        buildingViewModel.buildingResultState.value?.let { result ->
            when {
                result.isSuccess -> {
                    showModalConfirmEmail = false
                    val userResponse = result.getOrThrow()
                    bilding_id = userResponse.data.first().bildingID
                    sharedPreferences.edit().putString("bildingId", bilding_id).apply()

                    if (typePaymentState == "card") {
                        try {
                            val intent = Intent()
                            intent.component = ComponentName(
                                "com.momocoffe.izettlemomo",
                                "com.momocoffe.izettlemomo.MainActivity"
                            )
                            intent.putExtra("zettleSubTotal", valueSubTotal)
                            intent.putExtra("zettleMountCupon", valueCupon)
                            intent.putExtra("zettleMountPropina", valuePropina)
                            intent.putExtra("zettleMountTotal", valueTotal)
                            intent.putExtra("zettleAuthorName", invite)
                            intent.putExtra("zettleBildingId", bilding_id)
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Log.e("TAG", "Error al abrir la aplicación", e)
                            Toast.makeText(
                                context,
                                R.string.zettle_info_install,
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                    }

                }

                result.isFailure -> {
                    val exception = result.exceptionOrNull()
                    Log.e("Result.BuildingViewModel", exception.toString())
                }
            }
        }
    }

    LaunchedEffect(buildingViewModel.emailResultState.value) {
        buildingViewModel.emailResultState.value?.let { result ->
            when {
                result.isSuccess -> {
                    cartViewModel.clearAllCart()
                    navController.navigate(Destination.OrderHere.route)
                }

                result.isFailure -> {
                    showModalConfirmEmail = false
                    val exception = result.exceptionOrNull()
                    Log.e("Result.ShoppingModel", exception.toString())
                }

                else -> {}
            }
        }
    }

    if (contentConfirmModalCupon) {
        ConfirmBonoModal(
            ConfirmInput(
                invite = invite,
                emailPayment = email,
                shoppingID = shopping_id,
                kioskoID = kiosko_id,
                valuePropina = valuePropina.toString(),
                valueMontoDescuento = valueMontoDescuento.toString(),
                valueSubTotal = valueSubTotal.toString(),
                valueTotal = valueTotal.toString(),
                productListString = productListString
            ),
            onClose = {
                onClose()
            },
            viewCartModel = cartViewModel,
            navController = navController
        )
    }

    if (showModalConfirmPayment) {
        ConfirmPayment(title = stringResource(id = R.string.please_barista_go),
            subTitle = stringResource(
                id = R.string.completed_transaccion
            ),
            onSelect = { showModalConfirmEmail = it })
    }

    if (showModalConfirmEmail) {

        ConfirmEmailModal(
            title = stringResource(id = R.string.payment_success_received_processed),
            subTitle = stringResource(id = R.string.please_enter_email_send_invoice),
            onCancel = {
                showModalConfirmEmail = false
                cartViewModel.clearAllCart()
                navController.navigate(Destination.OrderHere.route)
            },
            onSelect = { email ->
                buildingViewModel.updatePayment(
                    bilding_id, UpdateBilingRequest(
                        emailPayment = email
                    )
                )
                buildingViewModel.sendClientEmailInvoice(
                    ClientEmailInvoiceRequest(
                        from = "Nuevo Recibo - MOMO Coffee <davidvalenzuela@tecnopac.com.co>",
                        to = email,
                        subject = "Tienes Un Nuevo Pedido",
                        bilding_id = bilding_id
                    )
                )
            })
    }


    shoppingItems.let {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (client_id.isEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    stringResource(id = R.string.write_name),
                    color = Color.White,
                    fontSize = 22.sp,
                    fontFamily = redhatFamily,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(5.dp))
                OutTextField(
                    textValue = invite,
                    onValueChange = {
                        invite = it
                        val editor = sharedPreferences.edit()
                        editor.putString("nameClient", it)
                        editor.apply()
                    },
                    onClickButton = { invite = "" },
                    keyboardType = KeyboardType.Text,
                    icon = painterResource(R.drawable.user)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                stringResource(id = R.string.select_yuo_payment),
                color = Color.White,
                fontSize = 22.sp,
                fontFamily = redhatFamily,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)){
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    if (it.first().card) {
                        Button(
                            onClick = {
                                if (invite.isEmpty()) {
                                    Toast.makeText(
                                        context,
                                        enterNameInvited,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    if (valueTotal == 0f || valueTotal.isNaN() || valueTotal <= 0) {

                                        Toast.makeText(
                                            context,
                                            notFoundProducts,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        return@Button
                                    }

                                    if (contentConfirmModalCupon) {
                                        return@Button
                                    }

                                    typePaymentState = "card"

                                    buildingViewModel.payment(
                                        invoice = BuildingRequest(
                                            name = invite,
                                            emailPayment = "",
                                            shoppingID = shopping_id,
                                            kioskoID = kiosko_id,
                                            typePayment = typePaymentState,
                                            propina = valuePropina.toString(),
                                            mountReceive = "",
                                            mountDiscount = valueMontoDescuento.toString(),
                                            cupon = cuponName,
                                            iva = "",
                                            subtotal = valueSubTotal.toString(),
                                            total = valueTotal.toString(),
                                            state = "pending",
                                            product = productListString,
                                        )
                                    )

                                }
                            },
                            modifier = Modifier
                                .width(420.dp)
                                .height(100.dp)
                                .padding(horizontal = 5.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = if (validTypePayment == 1) OrangeDark else BlueLight,
                                disabledBackgroundColor = if (validTypePayment == 1) OrangeDark else BlueLight,
                                disabledContentColor = if (validTypePayment == 1) OrangeDark else BlueLight
                            )
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    painterResource(id = R.drawable.credicard_icon),
                                    contentDescription = stringResource(id = R.string.momo_coffe),
                                    tint = if (validTypePayment == 1) Color.White else BlueDark,
                                    modifier = Modifier.size(width = 43.dp, height = 43.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = stringResource(id = R.string.credicard),
                                    fontSize = 18.sp,
                                    color = if (validTypePayment == 1) Color.White else BlueDark,
                                    fontFamily = redhatFamily,
                                    fontWeight = FontWeight(700)
                                )
                            }

                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    if (it.first().effecty) {
                        Button(
                            onClick = {
                                if (invite.isEmpty()) {
                                    Toast.makeText(
                                        context,
                                        enterNameInvited,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    if (valueTotal == 0f || valueTotal.isNaN() || valueTotal <= 0) {
                                        Toast.makeText(
                                            context,
                                            notFoundProducts,
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        return@Button
                                    }

                                    if (contentConfirmModalCupon) {
                                        return@Button
                                    }

                                    typePaymentState = "effecty"

                                    buildingViewModel.payment(
                                        invoice = BuildingRequest(
                                            name = invite,
                                            emailPayment = email,
                                            shoppingID = shopping_id,
                                            kioskoID = kiosko_id,
                                            typePayment = typePaymentState,
                                            propina = valuePropina.toString(),
                                            mountReceive = "",
                                            mountDiscount = valueMontoDescuento.toString(),
                                            cupon = cuponName,
                                            iva = "",
                                            subtotal = valueSubTotal.toString(),
                                            total = valueTotal.toString(),
                                            state = "pending",
                                            product = productListString,
                                        )
                                    )

                                    showModalConfirmPayment = true

                                }
                            },
                            modifier = Modifier
                                .width(420.dp)
                                .height(100.dp)
                                .padding(horizontal = 5.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = if (validTypePayment == 2) OrangeDark else BlueLight,
                                disabledBackgroundColor = if (validTypePayment == 2) OrangeDark else BlueLight,
                                disabledContentColor = if (validTypePayment == 2) OrangeDark else BlueLight
                            )
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    painterResource(id = R.drawable.effecty_icon),
                                    contentDescription = stringResource(id = R.string.momo_coffe),
                                    tint = if (validTypePayment == 2) Color.White else BlueDark,
                                    modifier = Modifier.size(width = 35.dp, height = 35.dp)
                                )
                                Spacer(modifier = Modifier.width(15.dp))
                                Text(
                                    text = stringResource(id = R.string.efecty),
                                    fontSize = 18.sp,
                                    color = if (validTypePayment == 2) Color.White else BlueDark,
                                    fontFamily = redhatFamily,
                                    fontWeight = FontWeight(700)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = onCancel,
                        modifier = Modifier
                            .width(420.dp)
                            .height(60.dp)
                            .padding(horizontal = 5.dp)
                            .border(
                                width = 0.6.dp,
                                color = Color.White,
                                shape = RoundedCornerShape(14.dp)
                            ),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Transparent,
                            disabledBackgroundColor = Color.Transparent,
                            disabledContentColor = Color.Transparent,
                        ),
                        elevation = ButtonDefaults.elevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp,
                            disabledElevation = 0.dp,
                            hoveredElevation = 0.dp,
                            focusedElevation = 0.dp
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.cancel),
                            fontSize = 18.sp,
                            color = Color.White,
                            fontFamily = redhatFamily,
                            fontWeight = FontWeight(700)
                        )
                    }
                }
            }
        }
    }
}


