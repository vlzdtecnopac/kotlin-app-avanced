package com.momocoffee.app.ui.chekout.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.momocoffee.app.ui.theme.BlueDark
import com.momocoffee.app.ui.theme.BlueLight
import com.momocoffee.app.ui.theme.OrangeDark
import com.momocoffee.app.ui.theme.redhatFamily
import com.momocoffee.app.R

@Composable
fun PropinaModal(
    title: String,
    onCancel: () -> Unit,
    onSelect: (Int) -> Unit
) {
    var textState by remember { mutableStateOf(value = "") }
    val focusManager = LocalFocusManager.current


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
                .heightIn(min = 540.dp, max = 560.dp)
                .zIndex(88f),
            color = BlueLight
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(10.dp)
                    .background(BlueLight),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Image(
                    painter = painterResource(id = R.drawable.momo_coffe_mug),
                    contentDescription = stringResource(id = R.string.momo_coffe),
                    modifier = Modifier.width(160.dp),
                    contentScale = ContentScale.Crop,
                )

                Text(
                    title,
                    fontFamily = redhatFamily,
                    fontSize = 28.sp,
                    fontWeight = FontWeight(700)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    modifier = Modifier
                        .widthIn(0.dp, 700.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlineTextField(
                        label = stringResource(id = R.string.writing_here),
                        placeholder = stringResource(id = R.string.writing_here),
                        icon = null,
                        keyboardType = KeyboardType.Number,
                        textValue = textState,
                        onValueChange = { textState = it.filter { char -> char.isDigit() } },
                        onClickButton = { textState = "" },
                        borderColor = BlueDark,
                        onDone = {
                            focusManager.clearFocus()
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
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
                            onClick = onCancel,
                            colors = ButtonDefaults.buttonColors(
                                disabledContentColor = BlueLight,
                                contentColor =  BlueLight,
                                backgroundColor =  BlueLight
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
                                onSelect(textState.toInt())
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
                                text = stringResource(id = R.string.add_propina),
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


}