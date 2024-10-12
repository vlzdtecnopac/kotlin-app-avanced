package com.momocoffee.app.ui.chekout.components

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import com.momocoffee.app.ui.client.components.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
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
import com.momocoffee.app.network.dto.PedidoRequest
import com.momocoffee.app.network.dto.Producto
import com.momocoffee.app.network.dto.Sauce
import com.momocoffee.app.network.dto.Size
import com.momocoffee.app.network.dto.Sugar
import com.momocoffee.app.network.dto.Temperature
import com.momocoffee.app.network.dto.UpdateBilingRequest
import com.momocoffee.app.network.dto.productosToString
import com.momocoffee.app.ui.components.cart.parseItemModifiers
import com.momocoffee.app.ui.theme.BlueDark
import com.momocoffee.app.ui.theme.BlueDarkTransparent
import com.momocoffee.app.ui.theme.BlueLight
import com.momocoffee.app.ui.theme.OrangeDark
import com.momocoffee.app.ui.theme.redhatFamily
import com.momocoffee.app.ui.theme.stacionFamily
import com.momocoffee.app.viewmodel.BuildingViewModel
import com.momocoffee.app.viewmodel.CartViewModel
import com.momocoffee.app.viewmodel.ClientViewModel
import com.momocoffee.app.viewmodel.PedidoViewModel
import com.momocoffee.app.viewmodel.ShoppingViewModel
import com.momocoffee.app.viewmodel.TurnoViewModel
import com.spr.jetpack_loading.components.indicators.BallClipRotatePulseIndicator


data class ConfirmInput(
    val invite: String,
    val emailPayment: String,
    val shoppingID: String,
    val kioskoID: String,
    val valuePropina: String,
    val valueMontoDescuento: String,
    val valueSubTotal: String,
    val valueTotal: String,
    val productListString: String,
)

@Composable
fun ConfirmBonoModal(
    data: ConfirmInput,
    onClose: () -> Unit,
    viewCartModel: CartViewModel,
    navController: NavController,
    pedidoViewModel: PedidoViewModel = viewModel(),
    shoppingViewModel: ShoppingViewModel = viewModel(),
    buildingViewModel: BuildingViewModel = viewModel(),
    turnoViewModel: TurnoViewModel = viewModel()
) {
    val context = LocalContext.current
    val loading = buildingViewModel.loadingState.value
    var showModalConfirmEmail by remember { mutableStateOf(value = false) }
    var emailClient by remember { mutableStateOf("") }
    val sharedPreferences = context.getSharedPreferences("momo_prefs", Context.MODE_PRIVATE)
    var optionsColumn by remember { mutableStateOf("") }
    val nameClient = sharedPreferences.getString("nameClient", null) ?: ""
    var fullName by remember { mutableStateOf(value = nameClient) }
    val bildingId = sharedPreferences.getString("bildingId", null) ?: ""
    val clientId = sharedPreferences.getString("clientId", null) ?: ""
    val shoppingId = sharedPreferences.getString("shoppingId", null) ?: ""
    val kioskoId = sharedPreferences.getString("kioskoId", null) ?: ""
    val cuponName = sharedPreferences.getString("cuponName", null) ?: ""

    val cart = viewCartModel.state

    LaunchedEffect(Unit) {
        shoppingViewModel.getConfigShopping(shoppingId)
    }

    LaunchedEffect(shoppingViewModel.shoppingConfigState.value) {
        shoppingViewModel.shoppingConfigState.value?.let { result ->
            when {
                result.isSuccess -> {
                    val configResponse = result.getOrThrow()
                    optionsColumn = configResponse.typeColumn
                }
                result.isFailure -> {
                    val exception = result.exceptionOrNull()
                    Log.e("Result.ShoppingModel", exception.toString())
                }
            }
        }
    }

    LaunchedEffect(buildingViewModel.buildingResultState.value) {

        buildingViewModel.buildingResultState.value?.let { result ->
            when {
                result.isSuccess -> {
                    val bildingResponse = result.getOrThrow()
                    val newProducts = cart.carts.mapIndexed { index, item ->
                        val itemsModifiersOptions = parseItemModifiers(item.modifiersOptions)
                        val itemsListModifiersOptions = parseItemModifiers(item.modifiersList)
                        Producto(
                            id = item.id.toString(),
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
                                        itemsModifiersOptions["extra"]?.id ?: "",
                                        itemsListModifiersOptions["extra"]?.name ?: "",
                                        itemsListModifiersOptions["extra"]?.price?.toInt() ?: 0
                                    )
                                ),
                                libTapa =
                                    Lid(
                                        itemsModifiersOptions["libTapa"]?.id ?: "",
                                        itemsModifiersOptions["libTapa"]?.name ?: "",
                                        itemsModifiersOptions["libTapa"]?.price?.toInt() ?: 0
                                    )
                                ,
                                sauce = listOf(
                                    Sauce(
                                        itemsModifiersOptions["sauce"]?.id ?: "",
                                        itemsModifiersOptions["sauce"]?.name ?: "",
                                        itemsModifiersOptions["sauce"]?.price?.toInt() ?: 0
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


                    if (optionsColumn <= "1") {
                        optionsColumn = "8"
                    } else {
                        optionsColumn = "4"
                    }

                    val pedidoData = PedidoRequest(
                        client_id = clientId,
                        total = data.valueSubTotal,
                        name_client = fullName,
                        shopping_id = shoppingId,
                        bildings_id = bildingResponse.data.first().bildingID,
                        kiosko_id = kioskoId,
                        columns_pending = optionsColumn.toInt(),
                        name_cupon = cuponName,
                        product = productosToString(newProducts)
                    )
                    turnoViewModel.getTurnByBilding(bildingResponse.data.first().bildingID, "completed")
                    pedidoViewModel.create(pedidoData)
                }

                result.isFailure -> {
                    val exception = result.exceptionOrNull()
                    Log.e("Result.BuildingViewModel", exception.toString())
                }
            }
        }
    }

    LaunchedEffect(pedidoViewModel.pedidoResultState.value){
        pedidoViewModel.pedidoResultState.value?.let{ result ->
            when {
                result.isSuccess -> {
                    showModalConfirmEmail = true
                    buildingViewModel.updatePayment(
                        bildingId, UpdateBilingRequest(
                            name = fullName,
                            shoppingID = shoppingId,
                            kioskoID = kioskoId,
                            state = "completed",
                            cupon = cuponName,
                            typePayment = "effecty",
                            toteatCheck = true,
                            status = "ready",
                            type = "takeaway",
                            channel = "pos",
                            vendorName = "Bono MOMO"
                        )
                    )
                }
                result.isFailure -> {
                    showModalConfirmEmail = false
                }
            }
        }
    }

    LaunchedEffect(buildingViewModel.emailResultState.value) {
        buildingViewModel.emailResultState.value?.let { result ->
            when {
                result.isSuccess -> {
                    val emailResponse = result.getOrThrow()
                    buildingViewModel.updatePayment(
                        bildingId, UpdateBilingRequest(
                            emailPayment = emailClient,
                        )
                    )
                    Log.d("Result.EmailViewModel", emailResponse.message)
                    navController.navigate(Destination.OrderHere.route)
                }

                result.isFailure -> {
                    showModalConfirmEmail = false
                    val exception = result.exceptionOrNull()
                    Log.e("Result.ShoppingModel", exception.toString())
                    navController.navigate(Destination.OrderHere.route)
                }

                else -> {}
            }
        }
    }

    Dialog(
        onDismissRequest = {},
        DialogProperties(
            usePlatformDefaultWidth = true
        )
    ) {
        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .padding(0.dp)
                .widthIn(min = 480.dp, max = 580.dp)
                .height(560.dp)
                .zIndex(88f),
            color = BlueDark
        ) {
            if (showModalConfirmEmail) {
                ConfirmEmailModal(
                    title = stringResource(id = R.string.payment_success_received_processed),
                    subTitle = stringResource(id = R.string.please_enter_email_send_invoice),
                    onCancel = {
                        val editor = sharedPreferences.edit()
                        editor.remove("clientId")
                        editor.remove("bildingId")
                        viewCartModel.clearAllCart()
                        showModalConfirmEmail = false
                        navController.navigate(Destination.OrderHere.route)
                    },
                    onSelect = { email ->
                        emailClient = email
                        buildingViewModel.sendClientEmailInvoice(
                            ClientEmailInvoiceRequest(
                                from = "Nuevo Recibo - MOMO Coffee <davidvalenzuela@tecnopac.com.co>",
                                to = email,
                                subject = "Tienes Un Nuevo Pedido",
                                bilding_id = bildingId
                            )
                        )
                    })
            }
            Box {
                Column(
                    modifier = Modifier.padding(10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.back_icon),
                            contentDescription = stringResource(id = R.string.momo_coffe),
                            modifier = Modifier
                                .width(50.dp)
                                .padding(10.dp)
                                .clickable {
                                    onClose()
                                },
                            contentScale = ContentScale.Fit,
                        )
                    }
                    Image(
                        painter = painterResource(id = R.drawable.bono_icon),
                        contentDescription = stringResource(id = R.string.momo_coffe),
                        modifier = Modifier.width(120.dp),
                        contentScale = ContentScale.FillWidth,
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Column(
                        modifier = Modifier.widthIn(min = 220.dp, max = 490.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Pagas con cupon",
                            fontFamily = redhatFamily,
                            fontSize = 28.sp,
                            fontWeight = FontWeight(400),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            stringResource(id = R.string.cupon_limit_continue_payment),
                            fontFamily = redhatFamily,
                            fontSize = 18.sp,
                            fontWeight = FontWeight(400),
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Total a pagar.",
                            fontFamily = redhatFamily,
                            fontSize = 18.sp,
                            fontWeight = FontWeight(400),
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(
                            "$0",
                            fontFamily = stacionFamily,
                            fontSize = 48.sp,
                            fontWeight = FontWeight(400),
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                        OutlineTextField(
                            label = stringResource(id = R.string.first_name),
                            placeholder = stringResource(id = R.string.first_name),
                            icon = R.drawable.user,
                            keyboardType = KeyboardType.Text,
                            textValue = fullName,
                            onValueChange = { fullName = it },
                            onClickButton = { fullName = "" },
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                if(fullName.isNotBlank()){

                                    buildingViewModel.payment(
                                        invoice = BuildingRequest(
                                            name = fullName,
                                            emailPayment = data.emailPayment,
                                            shoppingID = data.shoppingID,
                                            kioskoID = data.kioskoID,
                                            typePayment = "effecty",
                                            propina = data.valuePropina,
                                            mountReceive = data.valueTotal,
                                            mountDiscount = data.valueMontoDescuento,
                                            cupon = cuponName,
                                            iva = "",
                                            subtotal = data.valueSubTotal,
                                            total = data.valueTotal,
                                            state = "pending",
                                            product = data.productListString
                                        )
                                    )
                                }else{
                                    Toast.makeText(
                                        context,
                                        R.string.enter_full_name_order,
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }

                            },
                            modifier = Modifier
                                .width(220.dp)
                                .height(60.dp)
                                .padding(horizontal = 5.dp)
                                .border(
                                    width = 0.6.dp,
                                    color = OrangeDark,
                                    shape = RoundedCornerShape(14.dp)
                                ),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = OrangeDark,
                                disabledBackgroundColor = OrangeDark,
                                disabledContentColor = OrangeDark
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
                                stringResource(id = R.string.payment),
                                fontSize = 22.sp,
                                color = Color.White,
                                fontFamily = redhatFamily,
                                fontWeight = FontWeight(700)
                            )
                        }
                    }
                }
                if (loading) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(BlueDarkTransparent)
                    ) {
                        BallClipRotatePulseIndicator()
                    }
                }
            }

        }
    }

}