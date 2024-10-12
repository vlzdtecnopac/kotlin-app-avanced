package com.momocoffee.app.ui.client.components

import android.content.Context
import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.momocoffee.app.R
import com.momocoffee.app.viewmodel.ClientViewModel
import kotlinx.coroutines.delay

private const val DEBOUNCE_TIME = 500L

@Composable
fun PhoneOutlineTextField(
    clientViewModel: ClientViewModel = viewModel(),
    label: String,
    placeholder: String,
    keyboardType: KeyboardType,
    icon: Int,
    selected: MutableState<String>,
){
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var phone by remember { mutableStateOf("") }

    LaunchedEffect(phone) {
        delay(DEBOUNCE_TIME)
        isValidPhone(context, clientViewModel, phone)
    }

    OutlinedTextField(
        value = phone,
        onValueChange = {
            phone = it
            selected.value = it
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp),
        label = { Text(text = label) },
        placeholder = { Text(text = placeholder) },
        leadingIcon = {
            Icon(
                painter = painterResource(icon),
                contentDescription = stringResource(id = R.string.momo_coffe),
                tint = Color.White,
                modifier = Modifier.size(26.dp)
            )
        },
        trailingIcon = {
            IconButton(
                onClick = {phone = ""}
            ) {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = stringResource(id = R.string.momo_coffe),
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {
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
            placeholderColor = Color.White,
            unfocusedLabelColor = Color.White

        )
    )

}

private suspend fun isValidPhone(
    context: Context,
    clientViewModel: ClientViewModel,
    phone: String
) {
    clientViewModel.getClient("", phone)
    delay(2000)
    clientViewModel.clientResultCheckEmailState.value?.let { result ->
        when {
            result.isSuccess -> {
                val response = result.getOrThrow()
                if (response.items.isNotEmpty()) {
                    Toast.makeText(
                        context,
                        R.string.warning_phone_exist,
                        Toast.LENGTH_LONG
                    )
                        .show()
                }else{

                }
            }

            result.isFailure -> {
                val exception = result.exceptionOrNull()
                Log.e("Result.ClientViewModel", exception.toString())
            }

            else -> {
            }
        }

    }
}