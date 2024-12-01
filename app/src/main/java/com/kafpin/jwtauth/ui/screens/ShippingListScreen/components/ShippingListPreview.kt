package com.kafpin.jwtauth.ui.screens.ShippingListScreen.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kafpin.jwtauth.models.shippings.ShippingList

// Composable для отображения списка
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShippingListPreview(
    shippingList: ShippingList,
    modifier: Modifier = Modifier,
    onSelectShipping: (id: String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .then(modifier)
    ) {
        items(shippingList.items) { shipping ->
            ShippingItem(shipping, onClickMoreInfo = {
                onSelectShipping(shipping.id.toString())
            })
        }
    }
}