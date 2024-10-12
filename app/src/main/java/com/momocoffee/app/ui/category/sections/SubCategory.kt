package com.momocoffee.app.ui.category.sections
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.momocoffee.app.ui.category.components.BtnOutlineCategory
import com.momocoffee.app.ui.theme.BlueDark
import com.momocoffee.app.ui.category.ListItem
import com.momocoffee.app.ui.components.headers.HeaderCategory
import com.momocoffee.app.viewmodel.CartViewModel
import org.json.JSONArray
import java.util.Locale


@Composable
fun SubCategory(navController: NavController,
                cartViewModel: CartViewModel,
                selectCategory: String,
                list: JSONArray,
                onCloseDialog: () -> Unit){

    val newList = mutableListOf<ListItem>()

    Dialog(
        onDismissRequest = onCloseDialog,
        DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier
                .padding(0.dp)
                .zIndex(88f),
            color = BlueDark
        ) {
            HeaderCategory(navController = navController, cartViewModel = cartViewModel, visibleCart = true)
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LazyVerticalGrid(
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.Center,
                    modifier = if (list.length() <= 2) { Modifier
                        .widthIn(0.dp, 550.dp) } else { Modifier.widthIn(0.dp, 800.dp)},
                    contentPadding = PaddingValues(vertical = 20.dp, horizontal = 20.dp),
                    columns = GridCells.Fixed(list.length()),
                    content = {
                        items(list.length()) { index ->
                            val jsonObject = list.getJSONObject(index)

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .padding(20.dp)
                            ) {
                                BtnOutlineCategory(
                                    icon = jsonObject.optString("image", ""),
                                    text = jsonObject.optString("label", "").replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase(
                                            Locale.ROOT
                                        ) else it.toString()
                                    },
                                    onclick = {
                                        navController.navigate("products/$selectCategory/${jsonObject.getString("label")}")
                                    }
                                )
                            }
                        }
                    })
            }
        }

    }
}