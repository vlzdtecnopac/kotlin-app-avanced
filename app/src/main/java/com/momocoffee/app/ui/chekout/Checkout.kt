package com.momocoffee.app.ui.chekout

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.momocoffee.app.ui.components.Category
import com.momocoffee.app.ui.theme.BlueDark
import com.momocoffee.app.ui.theme.BlueLight
import com.momocoffee.app.ui.theme.redhatFamily
import com.momocoffee.app.R
import com.momocoffee.app.network.response.ItemShopping
import com.momocoffee.app.ui.chekout.components.OutlineTextField
import com.momocoffee.app.ui.chekout.section.ProductCartCheckout
import com.momocoffee.app.ui.chekout.section.ContentPropinas
import com.momocoffee.app.ui.chekout.section.ContentTypePayment
import com.momocoffee.app.ui.components.DashedDivider
import com.momocoffee.app.ui.components.headers.HeaderCategory
import com.momocoffee.app.ui.theme.BlueDarkTransparent
import com.momocoffee.app.ui.theme.OrangeDark
import com.momocoffee.app.viewmodel.BuildingViewModel
import com.momocoffee.app.viewmodel.CartViewModel
import com.momocoffee.app.viewmodel.CheckoutViewModel
import com.momocoffee.app.viewmodel.CuponesViewModel

import com.momocoffee.app.viewmodel.ShoppingViewModel
import com.spr.jetpack_loading.components.indicators.BallClipRotatePulseIndicator
import org.json.JSONArray
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


data class CoffeeCart(val name: String, val price: Float, val type: String?)

@Composable
fun Checkout(
    navController: NavHostController,
    cartViewModel: CartViewModel,
    shoppingViewModel: ShoppingViewModel = viewModel(),
    checkoutViewModel: CheckoutViewModel = viewModel(),
    cuponesViewModel: CuponesViewModel = viewModel(),
    buildingViewModel: BuildingViewModel = viewModel()
) {

    val context = LocalContext.current
    val loading = buildingViewModel.loadingState.value
    var optionsColumn by remember { mutableStateOf("") }
    var shoppingItems by remember { mutableStateOf<List<ItemShopping>>(emptyList()) }
    val sharedPreferences = context.getSharedPreferences("momo_prefs", Context.MODE_PRIVATE)
    val preference_shopping_id = sharedPreferences.getString("shoppingId", null) ?: ""
    val cuponName = sharedPreferences.getString("cuponName", null) ?: ""

    var textCuponCodeState by rememberSaveable { mutableStateOf(value = "") }
    val subTotalProduct = cartViewModel.stateTotalSub.value

    var propina by rememberSaveable { mutableStateOf(value = 2) }
    var typePropina by rememberSaveable { mutableStateOf(value = 0) }
    var valueTypeVente by rememberSaveable { mutableStateOf(value = "") }
    var montoDescuento by remember { mutableStateOf(0f) }
    var valueCupon by remember { mutableStateOf(value = 0f) }
    var valuePropinaPerson by remember { mutableStateOf(value = 0f) }
    var valuePropina by remember { mutableStateOf(value = 0f) }

    var valueTypeDiscount by remember { mutableStateOf(value = 0) }

    var isCuponValid by remember { mutableStateOf(false) }
    var contentTypePropinaState by remember { mutableStateOf(false) }
    var contentConfirmModalCupon by remember { mutableStateOf(false) }

    var tableList = remember { mutableStateListOf<CoffeeCart>() }
    val state = cartViewModel.state;

    val focusManager = LocalFocusManager.current

    val couponString = stringResource(id = R.string.coupon)
    val tipString = stringResource(id = R.string.tip)
    val couponValidMessage = stringResource(id = R.string.coupon_valid)
    val couponDeleteMessage = stringResource(id = R.string.delete_cupon)
    val couponNotValidStore = stringResource(id = R.string.cupon_not_validate_store)

    val symbols = DecimalFormatSymbols().apply {
        decimalSeparator = '.'
    }

    val decimalFormat = DecimalFormat("#.##", symbols)

    val subTotalProductMenosCupon = if(subTotalProduct <= montoDescuento){
        subTotalProduct
    } else{
        subTotalProduct - montoDescuento
    }

    fun generatePropina(){
        valuePropina = when (propina) {
            1 -> subTotalProductMenosCupon * 5 / 100
            2 -> subTotalProductMenosCupon * 10 / 100
            3 -> subTotalProductMenosCupon * 15 / 100
            4 -> {
                when (typePropina) {
                    1 -> subTotalProductMenosCupon * valuePropinaPerson / 100
                    2 -> valuePropinaPerson
                    else -> 0f
                }
            }

            else -> 0f
        }
    }

    fun initTable() {
        tableList.clear()
        tableList.add(CoffeeCart("Subtotal", subTotalProduct, null))
        tableList.add(CoffeeCart(tipString, valuePropina, null))
        tableList.add(CoffeeCart("Total", cartViewModel.valorTotal.value, "total"))
    }


    fun initTableCupon() {
        tableList.clear()
        cartViewModel.valorTotal.value = subTotalProduct - valueCupon + valuePropina
        tableList.add(CoffeeCart("Subtotal", subTotalProduct, null))
        tableList.add(CoffeeCart(tipString, valuePropina, null))
        tableList.add(CoffeeCart(couponString, valueCupon, "cupon"))
        tableList.add(CoffeeCart("Total", cartViewModel.valorTotal.value, null))
    }

    LaunchedEffect(Unit) {
        generatePropina()
        checkoutViewModel.convertAmount(cartViewModel.valorTotal.value)
        shoppingViewModel.getShopping(preference_shopping_id)
        shoppingViewModel.getConfigShopping(preference_shopping_id)
        cartViewModel.priceSubTotal()
        cartViewModel.countTotal()
        initTable()
    }

    LaunchedEffect(propina, typePropina, cartViewModel.state, valueCupon) {
        generatePropina()
    }


    LaunchedEffect(subTotalProduct, valuePropina, isCuponValid, cartViewModel.state) {
        checkoutViewModel.convertAmount(cartViewModel.valorTotal.value)

        if(cartViewModel.countCartState.value <= 1) {
            tableList[0] = CoffeeCart(
                "Subtotal (${cartViewModel.countCartState.value} producto)",
                subTotalProduct,
                null
            )
        }else{
            tableList[0] = CoffeeCart(
                "Subtotal (${cartViewModel.countCartState.value} productos)",
                subTotalProduct,
                null
            )
        }

        if (isCuponValid) {
            if(subTotalProduct <=  montoDescuento){
                cartViewModel.valorTotal.value =  valuePropina
            }else{
                cartViewModel.valorTotal.value =  (subTotalProduct - montoDescuento) + valuePropina
            }

            var valorFinal = if(  valuePropina >= 0 && cartViewModel.valorTotal.value < 0 && valueCupon > 0 ){
                valuePropina
            }else{
                cartViewModel.valorTotal.value
            }

            cartViewModel.valorTotal.value =  valorFinal


            tableList[1] = CoffeeCart(couponString, valueCupon, "cupon")
            tableList[2] = CoffeeCart(tipString, valuePropina, null)
            tableList[3] = CoffeeCart("Total", cartViewModel.valorTotal.value, "total")
        } else {
            cartViewModel.valorTotal.value =  listOf(subTotalProduct, valuePropina).sum()

            tableList[1] = CoffeeCart(tipString, valuePropina, null)
            tableList[2] = CoffeeCart("Total", cartViewModel.valorTotal.value, "total")
        }
    }


    LaunchedEffect(shoppingViewModel.shoppingResultState.value) {
        shoppingViewModel.shoppingResultState.value?.let { result ->
            when {
                result.isSuccess -> {
                    val shoppingResponse = result.getOrThrow()
                    shoppingItems = shoppingResponse.items
                }

                result.isFailure -> {
                    val exception = result.exceptionOrNull()
                    Log.d("Result.ShoppingModel", exception.toString())
                }

                else -> {
                    Log.d("Result.ShoppingModel", "Error")
                }
            }
        }
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

    LaunchedEffect(cuponesViewModel.cuponResultState.value) {
        cuponesViewModel.cuponResultState.value.let { result ->
            if (result != null) {
                when {
                    result.isSuccess -> {
                        val response = result.getOrThrow()
                        if (response.items.isNotEmpty()) {


                            val cuponItem = response.items.first()
                            val jsonArray = JSONArray(cuponItem.shopping)
                            var containValor = false

                            if(cuponItem.typeVigente == "1"){
                                if(cuponItem?.cuponConsume != null){
                                    if(cuponItem.cuponConsume >= cuponItem.countCupon){
                                        textCuponCodeState = ""
                                        Toast.makeText(context, R.string.cupones_not_available, Toast.LENGTH_SHORT)
                                            .show()
                                        return@let
                                    }
                                }
                            }

                            if(cuponItem.typeVigente == "2") {
                                val calendario: Calendar = Calendar.getInstance()
                                val formato = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                                val todayFormat = formato.format(calendario.time)
                                if (todayFormat >= cuponItem.vigencia) {
                                    textCuponCodeState = ""
                                    Toast.makeText(
                                        context,
                                        R.string.the_coupon_has_expired,
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    return@let
                                }
                            }

                            initTableCupon()

                            for (i in 0 until jsonArray.length()) {
                                if (jsonArray.getJSONObject(i)
                                        .getString("value") == preference_shopping_id
                                ) {
                                    containValor = true
                                    break
                                }
                            }

                            if (containValor) {
                                isCuponValid = true
                                valueTypeVente = cuponItem.typeVigente
                                valueTypeDiscount = cuponItem.typeDiscount.toInt()

                                valueCupon = cuponItem.discount.toFloat()


                                if (valueTypeDiscount == 1) {
                                    montoDescuento = subTotalProduct * cuponItem.discount.toInt() / 100
                                    Log.d("Result.CuponesViewModel", montoDescuento.toString())
                                } else {
                                    montoDescuento = valueCupon
                                    Log.d("Result.CuponesViewModel", montoDescuento.toString())
                                }
                                Toast.makeText(context, couponValidMessage, Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                focusManager.clearFocus()
                                val editor = sharedPreferences.edit()
                                editor.putString("cuponName",  "")
                                textCuponCodeState = ""
                                isCuponValid = false
                                valueCupon = 0f
                                Toast.makeText(context, couponNotValidStore, Toast.LENGTH_SHORT)
                                    .show()
                            }
                            initTableCupon()
                        } else {
                            focusManager.clearFocus()
                            val editor = sharedPreferences.edit()
                            editor.putString("cuponName",  "")
                            textCuponCodeState = ""
                            isCuponValid = false
                            valueCupon = 0f
                            initTable()
                            Toast.makeText(context, couponNotValidStore, Toast.LENGTH_SHORT).show()
                        }
                    }

                    result.isFailure -> {
                        val exception = result.exceptionOrNull()
                        Log.e("Result.CuponViewModel", exception.toString())
                        Toast.makeText(context, couponNotValidStore, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }



    Box {
        Column(
            modifier = Modifier.background(BlueLight),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BlueDark),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HeaderCategory(navController, cartViewModel, visibleCart = false)
                Category(navController, selectCategory = null, false, cartViewModel)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .widthIn(0.dp, 1100.dp)
                    .padding(10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(0.4f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(BlueDark)
                        .fillMaxHeight()
                        .padding(vertical = 5.dp, horizontal = 5.dp)
                ) {

                    if (!contentTypePropinaState) {
                        ContentPropinas(
                            navController = navController,
                            onSelectValue = {
                                valuePropinaPerson = it.toFloat()
                            },
                            onSelectPropina = {
                                propina = it
                            },
                            onTypePropina = { typePropina = it }
                        )
                    } else {

                            ContentTypePayment(
                                shoppingItems,
                                onCancel = {
                                    contentTypePropinaState = false
                                },
                                valueSubTotal = subTotalProduct,
                                valueCupon = valueCupon,
                                valuePropina = valuePropina,
                                valueMontoDescuento =  montoDescuento,
                                valueTotal = cartViewModel.valorTotal.value,
                                contentConfirmModalCupon = contentConfirmModalCupon,
                                cartViewModel = cartViewModel,
                                navController = navController,
                                onClose = {
                                    contentConfirmModalCupon = false
                                }
                            )

                    }
                }
                Spacer(modifier = Modifier.width(5.dp))
                Column(
                    modifier = Modifier
                        .weight(0.4f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(BlueDark)
                        .fillMaxHeight()
                        .padding(10.dp)
                ) {
                    LazyColumn(modifier = Modifier.fillMaxHeight()) {
                        items(
                            items = state.carts,
                            itemContent = { item -> ProductCartCheckout(item, cartViewModel) })
                    }

                }
                Spacer(modifier = Modifier.width(5.dp))
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .weight(0.4f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(BlueDark)
                        .fillMaxHeight()
                        .padding(4.dp),
                ) {

                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlineTextField(
                            label = stringResource(id = R.string.add_cupon),
                            placeholder = stringResource(id = R.string.add_cupon),
                            icon = R.drawable.procent_cupon_icon,
                            keyboardType = KeyboardType.Text,
                            textValue = textCuponCodeState,
                            onValueChange = {
                                textCuponCodeState = it.uppercase(Locale.getDefault())
                                val editor = sharedPreferences.edit()
                                editor.putString("cuponName",  textCuponCodeState)
                                editor.apply()
                                            },
                            onClickButton = {
                                val editor = sharedPreferences.edit()
                                editor.putString("cuponName",  "")
                                textCuponCodeState = ""
                                isCuponValid = false
                                valueCupon = 0f
                                initTable()
                                },
                            borderColor = Color.White,
                            onDone = {
                                focusManager.clearFocus()
                            }
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Button(
                            onClick = {
                                    focusManager.clearFocus()
                                    isCuponValid = false
                                    valueCupon = 0f
                                    initTable()
                                    cuponesViewModel.cuponResultState.value = null
                                    cuponesViewModel.cuponValidate(cuponName)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 5.dp),
                            shape = RoundedCornerShape(4.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = BlueLight,
                                disabledBackgroundColor = BlueLight,
                                disabledContentColor = BlueLight
                            ),
                            elevation = ButtonDefaults.elevation(0.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.txt_add),
                                fontSize = 16.sp,
                                color = BlueDark,
                                fontFamily = redhatFamily,
                            )
                        }
                    }

                    Column {
                        Column {
                            tableList.forEach { coffee ->
                                if (coffee.type == "total") {
                                    Spacer(modifier = Modifier.height(5.dp))
                                    DashedDivider()
                                    Spacer(modifier = Modifier.height(5.dp))
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            coffee.name,
                                            modifier = Modifier.weight(0.7f),
                                            fontFamily = redhatFamily,
                                            fontSize = 16.sp,
                                            color = Color.White
                                        )

                                        Text(
                                            "$ ${decimalFormat.format(coffee.price)}",
                                            modifier = Modifier.weight(0.3f),
                                            fontFamily = redhatFamily,
                                            fontSize = 18.sp,
                                            color = Color.White,
                                            textAlign = TextAlign.Right
                                        )

                                    }
                                } else {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        if (coffee.type == "cupon") {
                                            Text(
                                                coffee.name,
                                                modifier = Modifier.weight(0.5f),
                                                fontFamily = redhatFamily,
                                                fontSize = 12.sp,
                                                color = Color.White
                                            )
                                            Button(
                                                onClick = {
                                                    textCuponCodeState = ""
                                                    isCuponValid = false
                                                    valueCupon = 0f
                                                    initTable()
                                                    Toast.makeText(
                                                        context,
                                                        couponDeleteMessage,
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                },
                                                modifier = Modifier
                                                    .weight(0.4f)
                                                    .padding(horizontal = 2.dp),
                                                shape = RoundedCornerShape(50.dp),
                                                colors = ButtonDefaults.buttonColors(
                                                    backgroundColor = OrangeDark,
                                                    disabledBackgroundColor = OrangeDark,
                                                    disabledContentColor = OrangeDark
                                                ),
                                                elevation = ButtonDefaults.elevation(0.dp)
                                            ) {
                                                Text(
                                                    text = stringResource(id = R.string.delete),
                                                    fontSize = 14.sp,
                                                    color = Color.White,
                                                    fontFamily = redhatFamily,
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(10.dp))
                                            if (valueTypeDiscount == 1) {
                                                Text(
                                                    "${decimalFormat.format(coffee.price)} %",
                                                    modifier = Modifier.weight(0.2f),
                                                    fontFamily = redhatFamily,
                                                    fontSize = 16.sp,
                                                    color = Color.White,
                                                    textAlign = TextAlign.Right
                                                )
                                            } else {
                                                Text(
                                                    "$ -${decimalFormat.format(coffee.price)} ",
                                                    modifier = Modifier.weight(0.2f),
                                                    fontFamily = redhatFamily,
                                                    fontSize = 16.sp,
                                                    color = Color.White,
                                                    textAlign = TextAlign.Right
                                                )
                                            }

                                        } else {

                                            Text(
                                                coffee.name,
                                                modifier = Modifier.weight(0.85f),
                                                fontFamily = redhatFamily,
                                                fontSize = 12.sp,
                                                color = Color.White
                                            )
                                            Text(
                                                "$ ${decimalFormat.format(coffee.price)}",
                                                modifier = Modifier.weight(0.25f),
                                                fontFamily = redhatFamily,
                                                fontSize = 16.sp,
                                                color = Color.White,
                                                textAlign = TextAlign.Right
                                            )
                                        }
                                    }

                                }
                            }
                        }

                        Button(
                            onClick = {
                                if(isCuponValid && cartViewModel.valorTotal.value <= 0){
                                    contentTypePropinaState = true
                                    contentConfirmModalCupon = true
                                    return@Button
                                }
                                contentTypePropinaState = true
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 5.dp),
                            shape = RoundedCornerShape(4.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = OrangeDark,
                                disabledBackgroundColor = OrangeDark,
                                disabledContentColor = BlueLight
                            )
                        ) {
                            Text(
                                text = stringResource(id = R.string.payment),
                                fontSize = 20.sp,
                                color = Color.White,
                                fontFamily = redhatFamily,
                            )
                        }

                    }

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




