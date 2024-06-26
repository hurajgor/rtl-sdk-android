package com.copart.rtlaisdk.ui.vehicleDetails.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.copart.rtlaisdk.R
import com.copart.rtlaisdk.ui.theme.CopartBlue
import com.copart.rtlaisdk.ui.theme.GreyChateau
import com.copart.rtlaisdk.ui.theme.WhiteSmoke
import com.copart.rtlaisdk.ui.theme.labelBold16
import com.copart.rtlaisdk.ui.theme.labelNormal14
import com.copart.rtlaisdk.ui.theme.labelNormal16
import com.copart.rtlaisdk.utils.getWindowHeight
import com.copart.rtlaisdk.utils.toDp
import compose.icons.TablerIcons
import compose.icons.tablericons.Key

@Composable
fun VINDecode(onItemClick: (String) -> Unit) {

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteSmoke)
            .padding(16.dp)
            .verticalScroll(scrollState),
    ) {
        VINDecodeHeader()
        VINScan {}
        ORDivider()
        VINManualEntry {}
        ORDivider()
        ClaimEntry()
        DecodeButton {}
    }
}

@Composable
fun DecodeButton(onItemClick: (String) -> Unit) {
    var isValid = true // Replace with your validation logic
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { /* Handle button click */ },
            shape = ButtonDefaults.shape, // This uses the default rounded shape
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isValid) CopartBlue else GreyChateau,
                contentColor = Color.White
            ),
            enabled = isValid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    TablerIcons.Key,
                    contentDescription = "Send Icon",
                    modifier = Modifier.rotate(45f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.decode))
            }
        }
    }
}

@Composable
fun ClaimEntry() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.claim_number),
            style = labelBold16
        )
        TextField(
            value = "", // replace with a mutable state if you want to handle the text changes
            onValueChange = {}, // replace with a function to handle the text changes
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            placeholder = { Text(stringResource(R.string.claim_pound)) },
            textStyle = labelNormal14,
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}


@Composable
fun VINManualEntry(onItemClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.enter_vin_manually_and_decode_details),
            modifier = Modifier
                .fillMaxWidth(),
            style = labelNormal16,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center,
        )
        Text(
            text = stringResource(id = R.string.vin),
            style = labelBold16,
            modifier = Modifier.padding(top = 12.dp)
        )
        TextField(
            value = "", // replace with a mutable state if you want to handle the text changes
            onValueChange = {}, // replace with a function to handle the text changes
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            placeholder = { Text(stringResource(R.string.vin_placeholder)) },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.camera_drk),
                    contentDescription = "Camera Icon",
                    modifier = Modifier
                        .size(25.dp)
                        .padding(0.dp)
                )
            },
            textStyle = labelNormal14,
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
fun VINDecodeHeader() {
    Text(
        text = stringResource(R.string.vehicle_details),
        style = labelNormal16,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
fun ORDivider() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp, bottom = 30.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        )
        Text(text = stringResource(R.string.or))
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )
    }
}

@Composable
fun VINScan(onItemClick: (String) -> Unit) {

    val barcodeScanAnim by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.barcodescan)
    )

    Column(
        modifier = Modifier
            .padding(top = 2.dp)
    ) {
        Text(
            text = stringResource(R.string.tap_below_to_scan_the_vin_barcode_or_qr_code),
            modifier = Modifier
                .fillMaxWidth(),
            style = labelNormal16,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center,
        )
        LottieAnimation(
            composition = barcodeScanAnim,
            iterations = LottieConstants.IterateForever,
            speed = 0.85f,
            isPlaying = true,
            modifier = Modifier.height((getWindowHeight().toDp * 0.3f).dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun VINDecodePreview() {
    VINDecode {}
}