package com.momocoffee.app

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import com.momocoffee.app.navigation.NavigationScreen
import com.momocoffee.app.network.database.CartDataBase
import com.momocoffee.app.ui.theme.MomoCoffeClientTheme
import com.momocoffee.app.viewmodel.CartViewModel
import com.momocoffee.app.viewmodel.LoginViewModel
import com.momocoffee.app.viewmodel.RegionInternational
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.momocoffee.app.ui.components.AlertInvoiceState
import com.momocoffee.app.ui.theme.BlueDark
import com.momocoffee.app.ui.theme.redhatFamily

import java.util.Locale

class MainActivity : ComponentActivity() {

    private var stateInvoice by mutableStateOf("init")

    override fun attachBaseContext(newBase: Context?) {
        val localeToSwitchTo = Locale("es")
        val localeUpdatedContext =
            newBase?.let { RegionInternational.updateLocale(it, localeToSwitchTo) }
        super.attachBaseContext(localeUpdatedContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

        val sharedPreferences = App.instance.getSharedPreferences("momo_prefs", Context.MODE_PRIVATE)
        val intent = intent
        if (intent.hasExtra("zettleStatus")) {
            val zettleStatus: String? = intent.getStringExtra("zettleStatus")
            val zettleBildingId: String? = intent.getStringExtra("zettleBildingId")
            val zettleMountTotal: Float? = intent.getFloatExtra("zettleMountTotal", 0f)
            sharedPreferences.edit().putString("valueTotal",  zettleMountTotal.toString()).apply()
            sharedPreferences.edit().putString("bildingId",  zettleBildingId).apply()
            Log.i("Result.BuildingViewModel", "ZettleBildingId: $zettleBildingId")

            stateInvoice = when (zettleStatus) {
                "completed" -> "completed"
                "cancelled" -> "cancelled"
                "failed" -> "failed"
                else -> "init"
            }
        } else {
            Log.d("RESULT.ZettlePaymentMomo", "Not output data in ZettlePaymentMomo")
        }

        if (isTablet()) {
            setContent {
                MomoCoffeClientTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val database =
                            Room.databaseBuilder(
                                applicationContext,
                                CartDataBase::class.java,
                                "momo_db"
                            )
                                .build()

                        val viewModelDb by viewModels<CartViewModel>(factoryProducer = {
                            object : ViewModelProvider.Factory {
                                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                    return CartViewModel(database.dao) as T
                                }
                            }
                        })

                        LaunchedEffect(Unit) {
                            // Set up and establish socket connection
                            SocketHandler.setSocket()
                            SocketHandler.establishConnection()
                        }

                        NavigationScreen(viewModelCart = viewModelDb, stateInvoice, ::resetState)
                    }
                }
            }
        } else {
            setContent {
                MomoCoffeClientTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.background(BlueDark)
                        ){
                            Image(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = stringResource(id = R.string.momo_coffe),
                                modifier = Modifier.width(180.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Image(
                                painter = painterResource(id = R.drawable.tablet_icon),
                                contentDescription = stringResource(id = R.string.momo_coffe),
                                modifier = Modifier.width(140.dp),
                                contentScale = ContentScale.Crop,
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Box(
                                modifier = Modifier.width(320.dp).wrapContentSize(Alignment.Center)
                            ){
                                Text(
                                    stringResource(id = R.string.app_compatible_android),
                                    color = Color.White,
                                    fontSize = 22.sp,
                                    fontFamily = redhatFamily,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center

                                )
                            }
                        }

                    }
                }
            }
        }
    }

    private fun isTablet(): Boolean {
        return resources.configuration.smallestScreenWidthDp >= 600 // For tablets with at least 600dp width
    }

    override fun onBackPressed() {
        // No hacer nada al presionar el botón de "volver atrás"
        // Esto bloquea el comportamiento predeterminado
    }

    private fun resetState() {
        stateInvoice = "init"
    }

}


