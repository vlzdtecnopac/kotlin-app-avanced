package com.momocoffee.app.ui.components


import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.momocoffee.app.App
import com.momocoffee.app.R
import com.momocoffee.app.navigation.Destination
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
import com.momocoffee.app.ui.chekout.components.ConfirmEmailModal
import com.momocoffee.app.ui.components.cart.parseItemModifiers
import com.momocoffee.app.ui.theme.BlueDark
import com.momocoffee.app.ui.theme.BlueDarkTransparent
import com.momocoffee.app.ui.theme.BlueLight
import com.momocoffee.app.ui.theme.OrangeDark
import com.momocoffee.app.ui.theme.redhatFamily
import com.momocoffee.app.viewmodel.BuildingViewModel
import com.momocoffee.app.viewmodel.CartViewModel
import com.momocoffee.app.viewmodel.PedidoViewModel
import com.momocoffee.app.viewmodel.ShoppingViewModel
import com.spr.jetpack_loading.components.indicators.BallClipRotatePulseIndicator


@Composable
fun AlertInvoiceState(
    navController: NavController,
    stateInvoice: String,
    viewCartModel: CartViewModel,
    resetState: () -> Unit
) {

    when (stateInvoice) {
        "completed" -> SuccessPaymentModal(navController, viewCartModel = viewCartModel, resetState)
        "cancelled" -> ErrorPaymentModal(navController,  viewCartModel = viewCartModel, resetState, "canceled")
        "failed" -> ErrorPaymentModal(navController,  viewCartModel = viewCartModel, resetState, "failed")
        else -> {}
    }
}

@Composable
fun SuccessPaymentModal(
    navController: NavController,
    viewCartModel: CartViewModel,
    resetState: () -> Unit,
    pedidoViewModel: PedidoViewModel = viewModel(),
    shoppingViewModel: ShoppingViewModel = viewModel(),
    buildingViewModel: BuildingViewModel = viewModel()
) {
    val loading = buildingViewModel.loadingState.value
    var emailClient by remember { mutableStateOf("") }
    var optionsColumn by remember { mutableStateOf("") }
    var showModalConfirmEmail by remember { mutableStateOf(value = false) }
    val sharedPreferences = App.instance.getSharedPreferences("momo_prefs", Context.MODE_PRIVATE)
    val shoppingId = sharedPreferences.getString("shoppingId", null) ?: ""
    val clientId = sharedPreferences.getString("clientId", null) ?: ""
    val kioskoId = sharedPreferences.getString("kioskoId", null) ?: ""
    val bildingId = sharedPreferences.getString("bildingId", null) ?: ""
    val nameClient = sharedPreferences.getString("nameClient", null) ?: ""
    val cuponName = sharedPreferences.getString("cuponName", null) ?: ""
    val valueTotal = sharedPreferences.getString("valueTotal", null) ?: "0.0"


    val cart = viewCartModel.state

    LaunchedEffect(Unit) {
        Log.w("Result.BuildingViewModel", "Success Id: $bildingId")
        shoppingViewModel.getConfigShopping(shoppingId)
    }

    LaunchedEffect(shoppingViewModel.shoppingConfigState.value) {
        shoppingViewModel.shoppingConfigState.value?.let { result ->
            when {
                result.isSuccess -> {
                    val configResponse = result.getOrThrow()
                    optionsColumn = configResponse.typeColumn
                    buildingViewModel.updatePayment(
                        bildingId, UpdateBilingRequest(
                            name = nameClient,
                            shoppingID = shoppingId,
                            kioskoID = kioskoId,
                            state = "completed",
                            cupon = cuponName,
                            typePayment = "card",
                            toteatCheck = true,
                            status = "ready",
                            type = "takeaway",
                            channel = "pos",
                            vendorName = "Momo IZettle"
                        )
                    )
                }

                result.isFailure -> {
                    val exception = result.exceptionOrNull()
                    Log.e("Result.ShoppingModel", exception.toString())
                }

                else -> {}
            }
        }
    }


    LaunchedEffect( buildingViewModel.buildingUpdateResultState.value) {
        buildingViewModel.buildingUpdateResultState.value?.let {result ->
            when {
                result.isSuccess -> {
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
                                libTapa = Lid(
                                        itemsModifiersOptions["libTapa"]?.id ?: "",
                                        itemsListModifiersOptions["libTapa"]?.name ?: "",
                                        itemsListModifiersOptions["libTapa"]?.price?.toInt() ?: 0
                                    ),
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

                    if (optionsColumn.toInt() <= 1) {
                        optionsColumn = "8"
                    } else {
                        optionsColumn = "4"
                    }

                    val pedidoData = PedidoRequest(
                        client_id = clientId,
                        total = valueTotal,
                        name_client = nameClient,
                        shopping_id = shoppingId,
                        bildings_id = bildingId,
                        kiosko_id = kioskoId,
                        name_cupon = cuponName,
                        columns_pending = optionsColumn.toInt(),
                        product = productosToString(newProducts)
                    )
                    Log.e("Result.ShoppingModel", pedidoData.toString())
                    pedidoViewModel.create(pedidoData)
                }
                result.isFailure -> {
                    val exception = result.exceptionOrNull()
                    Log.e("Result.ShoppingModel", exception.toString())
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
                .widthIn(min = 460.dp, max = 830.dp)
                .heightIn(min = 410.dp, max = 420.dp)
                .zIndex(88f),
            color = BlueLight
        ) {
            if (showModalConfirmEmail) {
                ConfirmEmailModal(
                    title = stringResource(id = R.string.payment_success_received_processed),
                    subTitle = stringResource(id = R.string.please_enter_email_send_invoice),
                    onCancel = {
                        resetState()
                        val editor = sharedPreferences.edit()
                        editor.remove("clientId")
                        editor.remove("bildingId")
                        viewCartModel.clearAllCart()
                        showModalConfirmEmail = false
                        navController.navigate(Destination.OrderHere.route)
                    },
                    onSelect = { email ->
                        resetState()
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
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                        .background(BlueLight),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.clock_icon),
                        contentDescription = stringResource(id = R.string.momo_coffe),
                        modifier = Modifier.width(145.dp),
                        contentScale = ContentScale.Fit,
                    )
                    Text(
                        stringResource(id = R.string.payment_receive_success_order),
                        fontFamily = redhatFamily,
                        fontSize = 28.sp,
                        fontWeight = FontWeight(700),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        stringResource(id = R.string.profile_you_receive),
                        fontFamily = redhatFamily,
                        fontSize = 22.sp,
                        fontWeight = FontWeight(700),
                        textAlign = TextAlign.Center

                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier
                            .widthIn(0.dp, 400.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            modifier = Modifier
                                .padding(16.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .border(
                                    width = 0.6.dp,
                                    color = BlueDark,
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .weight(0.5f)
                                .height(60.dp),
                            onClick = {
                                resetState()
                            },
                            colors = ButtonDefaults.buttonColors(
                                disabledContentColor = Color.Transparent,
                                contentColor = Color.Transparent,
                                backgroundColor = Color.Transparent
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
                                stringResource(id = R.string.cancel),
                                color = BlueDark,
                                fontSize = 22.sp,
                                fontWeight = FontWeight(700)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                showModalConfirmEmail = true
                            },
                            modifier = Modifier
                                .weight(0.5f)
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
                                stringResource(id = R.string.txt_virtual),
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

@Composable
fun ErrorPaymentModal(
    navController: NavController,
    viewCartModel: CartViewModel,
    resetState: () -> Unit,
    state: String,
    buildingViewModel: BuildingViewModel = viewModel()
) {
    val sharedPreferences = App.instance.getSharedPreferences("momo_prefs", Context.MODE_PRIVATE)
    val shoppingId = sharedPreferences.getString("shoppingId", null) ?: ""
    val kioskoId = sharedPreferences.getString("kioskoId", null) ?: ""
    val bildingId = sharedPreferences.getString("bildingId", null) ?: ""

    LaunchedEffect(Unit) {
        Log.d("BuildingViewModel", bildingId)
        buildingViewModel.updatePayment(
            bildingId, UpdateBilingRequest(
                shoppingID = shoppingId,
                kioskoID = kioskoId,
                state = state,
                typePayment = "card",
                toteatCheck = true,
                status = "ready",
                type = "takeaway",
                channel = "pos",
                vendorName = "MOMO APP"
            )
        )
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
                .widthIn(min = 460.dp, max = 830.dp)
                .heightIn(min = 510.dp, max = 520.dp)
                .zIndex(88f),
            color = BlueLight
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
                    .background(BlueLight),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Image(
                    painter = painterResource(id = R.drawable.momo_coffe_mug),
                    contentDescription = stringResource(id = R.string.momo_coffe),
                    modifier = Modifier.width(150.dp),
                    contentScale = ContentScale.Crop,
                )
                Text(
                    stringResource(id = R.string.something_went_wrong),
                    fontFamily = redhatFamily,
                    fontSize = 28.sp,
                    fontWeight = FontWeight(700)
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    stringResource(id = R.string.text_try_again),
                    fontFamily = redhatFamily,
                    fontSize = 22.sp,
                    fontWeight = FontWeight(700)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    stringResource(id = R.string.yuo_payment_could_error),
                    fontFamily = redhatFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight(400),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    stringResource(id = R.string.there_problem_helping),
                    fontFamily = redhatFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight(400),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier
                        .widthIn(0.dp, 700.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier
                            .padding(16.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .border(
                                width = 0.6.dp,
                                color = BlueDark,
                                shape = RoundedCornerShape(14.dp)
                            )
                            .weight(0.5f)
                            .height(60.dp),
                        onClick = {
                            val editor = sharedPreferences.edit()
                            editor.remove("clientId")
                            editor.remove("bildingId")
                            viewCartModel.clearAllCart()
                            resetState()
                            navController.navigate(Destination.OrderHere.route)
                        },
                        colors = ButtonDefaults.buttonColors(
                            disabledContentColor = Color.Transparent,
                            contentColor = Color.Transparent,
                            backgroundColor = Color.Transparent
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
                            stringResource(id = R.string.cancel),
                            color = BlueDark,
                            fontSize = 22.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            resetState()
                            navController.navigate(Destination.Checkout.route)
                        },
                        modifier = Modifier
                            .weight(0.5f)
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
                            text = stringResource(id = R.string.try_again_payment),
                            fontSize = 22.sp,
                            color = Color.White,
                            fontFamily = redhatFamily,
                        )
                    }

                }
            }
        }

    }

}


