package com.kafpin.jwtauth.ui.screens.ShippingInfoScreen.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.kafpin.jwtauth.models.shippings.ShippingOne


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShippingInfoCard(
    modifier: Modifier = Modifier,
    shipping: ShippingOne,
    onReload: () -> Unit = {},
    onAddCargo: (id: Int) -> Unit = {},
    onDelete: (id: Int) -> Unit = {}
) {


    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        )
        {
            // Информация о доставке
            ShippingInfo(shipping,
                onClickDelete = {
                    onDelete(shipping.id!!)
                },
                modifier = Modifier.pointerInput(Unit) {
                    detectVerticalDragGestures { change, dragAmount ->
                        if (dragAmount > 0) {
                            onReload()
                        }
                        change.consume()
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Direction(shipping = shipping)

            Spacer(modifier = Modifier.height(16.dp))

            CargoInfo(
                shipping = shipping,
                addCargoClick = { cargoId ->
                    onAddCargo(cargoId)
                }
            )

        }

    }

}

