package com.momocoffee.app.ui.login.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.outlined.DisabledByDefault
import androidx.compose.material.icons.outlined.DisabledVisible
import androidx.compose.material.icons.outlined.DoNotDisturb
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.momocoffee.app.R

@Composable
fun PasswordOutTextField(
    textValue: String,
    onValueChange: (String) -> Unit,
) {

    var visibilityPassword by rememberSaveable { mutableStateOf(value = false) }
    val focusManager: FocusManager = LocalFocusManager.current

    OutlinedTextField(
        value = textValue,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp),
        label = { Text(text = "Password", color = Color.White) },
        placeholder = { Text(text = "password", color = Color.White) },
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.lock_icon),
                contentDescription = "Icon Clear",
                tint = Color.White,
                modifier = Modifier.size(26.dp)
            )
        },
        trailingIcon = {
            val (icon, iconColor) = if (visibilityPassword) {
                Pair(
                    Icons.Outlined.DisabledVisible,
                    Color.White.copy(0.7F)
                )
            } else {
                Pair(
                    Icons.Outlined.DoNotDisturb,
                    Color.White.copy(0.7f)
                )
            }

            IconButton(onClick = { visibilityPassword = !visibilityPassword }) {
                Icon(
                    icon,
                    contentDescription = "Visibility",
                    tint = iconColor,
                    modifier = Modifier.size(26.dp)
                )
            }
        },
        visualTransformation = if (visibilityPassword) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        shape = RoundedCornerShape(20),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.White,
            cursorColor = Color.White,
            textColor = Color.White,
            focusedLabelColor = Color.White,
            unfocusedBorderColor = Color.White,
            placeholderColor = Color.White
        )
    )
}