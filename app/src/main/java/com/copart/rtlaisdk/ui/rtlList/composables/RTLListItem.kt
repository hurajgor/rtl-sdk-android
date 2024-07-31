package com.copart.rtlaisdk.ui.rtlList.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.copart.rtlaisdk.R
import com.copart.rtlaisdk.data.model.RTLListItem
import com.copart.rtlaisdk.data.model.buildRTLListItemPreview
import com.copart.rtlaisdk.ui.theme.CopartBlue
import com.copart.rtlaisdk.ui.theme.Error1
import com.copart.rtlaisdk.ui.theme.Success1
import com.copart.rtlaisdk.ui.theme.labelBold16
import com.copart.rtlaisdk.ui.theme.labelBold20
import com.copart.rtlaisdk.ui.theme.labelNormal14
import com.copart.rtlaisdk.utils.DateUtils
import com.copart.rtlaisdk.utils.RTLStatusConstants
import com.copart.rtlaisdk.utils.buildAnnotatedStringWithBoldDynamicValue

@Composable
fun RTLListItem(rtlData: RTLListItem, onItemClick: (RTLListItem) -> Unit) {
    val status = when (rtlData.status) {
        RTLStatusConstants.AUTO_COMPLETED -> RTLStatusConstants.COMPLETED
        else -> rtlData.status
    }

    val statusBgColor = when (status) {
        RTLStatusConstants.PENDING -> Error1
        RTLStatusConstants.COMPLETED -> Success1
        else -> CopartBlue
    }

    Column(modifier = Modifier.clickable {
        onItemClick(rtlData)
    }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Image on the left
            AsyncImage(
                model = rtlData.imageFD,
                contentDescription = null,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            // Text components on the right
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "${rtlData.year} ${rtlData.make} ${rtlData.model}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = labelBold20
                )
                if (!rtlData.vinNumber.isNullOrEmpty()) {
                    Text(
                        text = buildAnnotatedStringWithBoldDynamicValue(
                            stringResource(R.string.vin) + ": ",
                            rtlData.vinNumber.toString()
                        ),
                        style = labelNormal14
                    )
                } else {
                    Text(
                        text = buildAnnotatedStringWithBoldDynamicValue(
                            stringResource(R.string.claimPound) + ": ",
                            rtlData.claimNumber.toString()
                        ),
                        style = labelNormal14
                    )
                }
                Text(
                    text = buildAnnotatedStringWithBoldDynamicValue(
                        stringResource(R.string.seller) + ": ",
                        rtlData.sellerCode.toString()
                    ),
                    style = labelNormal14
                )
                Text(
                    text = buildAnnotatedStringWithBoldDynamicValue(
                        stringResource(R.string.generated) + ": ",
                        DateUtils.isoToFriendlyDateTimeWithZone(rtlData.created.toString()),
                    ),
                    style = labelNormal14
                )
            }
        }
        // Status Text
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(statusBgColor)
        ) {
            Text(
                text = "$status",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .wrapContentHeight(Alignment.CenterVertically),
                textAlign = TextAlign.Center,
                color = Color.White,
                style = labelBold16
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RTLListItemPreview() {
    RTLListItem(rtlData = buildRTLListItemPreview()) {}
}
