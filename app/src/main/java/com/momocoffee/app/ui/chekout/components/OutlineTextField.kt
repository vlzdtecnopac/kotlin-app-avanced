package com.momocoffee.app.ui.chekout.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.momocoffee.app.R
import com.momocoffee.app.ui.theme.redhatFamily
import java.time.format.TextStyle


@Composable
fun OutlineTextField(
    label: String,
    placeholder: String,
    keyboardType: KeyboardType,
    textValue: String,
    icon: Int?,
    onValueChange: (String) -> Unit,
    onClickButton: () -> Unit,
    onDone: () -> Unit,
    borderColor: Color
){
    val focusManager = LocalFocusManager.current
    
    OutlinedTextField(
        value = textValue,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
        label = { Text(text = label, fontSize = 18.sp) },
        placeholder = { Text(text = placeholder, fontSize = 22.sp, fontFamily = redhatFamily, fontWeight = FontWeight(700)) },
        leadingIcon = {
            icon?.let { painterResource(it) }?.let {
                Icon(
                    modifier = Modifier.size(20.dp).padding(0.dp),
                    painter = it,
                    contentDescription = stringResource(id = R.string.momo_coffe),
                    tint = borderColor
                )
            }
        },

        trailingIcon = {
            IconButton(
                onClick = onClickButton
            ) {
                Icon(
                    modifier = Modifier.size(20.dp).padding(0.dp),
                    imageVector = Icons.Filled.Clear,
                    contentDescription = stringResource(id = R.string.momo_coffe),
                    tint = borderColor
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onDone()
                focusManager.clearFocus()
            }
        ),
        shape = RoundedCornerShape(20),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = borderColor,
            cursorColor = borderColor,
            textColor = borderColor,
            focusedLabelColor = borderColor,
            unfocusedBorderColor = borderColor,
            placeholderColor = borderColor,
            unfocusedLabelColor = borderColor

        ),
        textStyle = androidx.compose.ui.text.TextStyle(
            fontSize = 22.sp,
            fontFamily = redhatFamily,
            fontWeight = FontWeight(700)
        )
    )
}