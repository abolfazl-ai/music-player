package com.example.compose.ui.theme

import android.content.Context
import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.DoNotInline
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color

class TonalPalette(

    val neutral100: Color = Color.White,
    val neutral99: Color = Color.White,
    val neutral95: Color = Color.White,
    val neutral90: Color = Color.White,
    val neutral80: Color = Color.White,
    val neutral70: Color = Color.White,
    val neutral60: Color = Color.White,
    val neutral50: Color = Color.White,
    val neutral40: Color = Color.White,
    val neutral30: Color = Color.White,
    val neutral20: Color = Color.White,
    val neutral10: Color = Color.White,
    val neutral0: Color = Color.White,

    val neutralVariant100: Color = Color.White,
    val neutralVariant99: Color = Color.White,
    val neutralVariant95: Color = Color.White,
    val neutralVariant90: Color = Color.White,
    val neutralVariant80: Color = Color.White,
    val neutralVariant70: Color = Color.White,
    val neutralVariant60: Color = Color.White,
    val neutralVariant50: Color = Color.White,
    val neutralVariant40: Color = Color.White,
    val neutralVariant30: Color = Color.White,
    val neutralVariant20: Color = Color.White,
    val neutralVariant10: Color = Color.White,
    val neutralVariant0: Color = Color.White,

    val primary100: Color = Color.White,
    val primary99: Color = Color.White,
    val primary95: Color = Color.White,
    val primary90: Color = Color.White,
    val primary80: Color = Color.White,
    val primary70: Color = Color.White,
    val primary60: Color = Color.White,
    val primary50: Color = Color.White,
    val primary40: Color = Color.White,
    val primary30: Color = Color.White,
    val primary20: Color = Color.White,
    val primary10: Color = Color.White,
    val primary0: Color = Color.White,

    val secondary100: Color = Color.White,
    val secondary99: Color = Color.White,
    val secondary95: Color = Color.White,
    val secondary90: Color = Color.White,
    val secondary80: Color = Color.White,
    val secondary70: Color = Color.White,
    val secondary60: Color = Color.White,
    val secondary50: Color = Color.White,
    val secondary40: Color = Color.White,
    val secondary30: Color = Color.White,
    val secondary20: Color = Color.White,
    val secondary10: Color = Color.White,
    val secondary0: Color = Color.White,

    val tertiary100: Color = Color.White,
    val tertiary99: Color = Color.White,
    val tertiary95: Color = Color.White,
    val tertiary90: Color = Color.White,
    val tertiary80: Color = Color.White,
    val tertiary70: Color = Color.White,
    val tertiary60: Color = Color.White,
    val tertiary50: Color = Color.White,
    val tertiary40: Color = Color.White,
    val tertiary30: Color = Color.White,
    val tertiary20: Color = Color.White,
    val tertiary10: Color = Color.White,
    val tertiary0: Color = Color.White,
)

@RequiresApi(Build.VERSION_CODES.S)
fun dynamicTonalPalette(context: Context): TonalPalette = TonalPalette(
    // The neutral tonal range from the generated dynamic color palette.
    neutral100 = ColorResourceHelper.getColor(context, android.R.color.system_neutral1_0),
    neutral99 = ColorResourceHelper.getColor(context, android.R.color.system_neutral1_10),
    neutral95 = ColorResourceHelper.getColor(context, android.R.color.system_neutral1_50),
    neutral90 = ColorResourceHelper.getColor(context, android.R.color.system_neutral1_100),
    neutral80 = ColorResourceHelper.getColor(context, android.R.color.system_neutral1_200),
    neutral70 = ColorResourceHelper.getColor(context, android.R.color.system_neutral1_300),
    neutral60 = ColorResourceHelper.getColor(context, android.R.color.system_neutral1_400),
    neutral50 = ColorResourceHelper.getColor(context, android.R.color.system_neutral1_500),
    neutral40 = ColorResourceHelper.getColor(context, android.R.color.system_neutral1_600),
    neutral30 = ColorResourceHelper.getColor(context, android.R.color.system_neutral1_700),
    neutral20 = ColorResourceHelper.getColor(context, android.R.color.system_neutral1_800),
    neutral10 = ColorResourceHelper.getColor(context, android.R.color.system_neutral1_900),
    neutral0 = ColorResourceHelper.getColor(context, android.R.color.system_neutral1_1000),

    // The neutral variant tonal range, sometimes called "neutral 2",  from the
    // generated dynamic color palette.
    neutralVariant100 = ColorResourceHelper.getColor(context, android.R.color.system_neutral2_0),
    neutralVariant99 = ColorResourceHelper.getColor(context, android.R.color.system_neutral2_10),
    neutralVariant95 = ColorResourceHelper.getColor(context, android.R.color.system_neutral2_50),
    neutralVariant90 = ColorResourceHelper.getColor(context, android.R.color.system_neutral2_100),
    neutralVariant80 = ColorResourceHelper.getColor(context, android.R.color.system_neutral2_200),
    neutralVariant70 = ColorResourceHelper.getColor(context, android.R.color.system_neutral2_300),
    neutralVariant60 = ColorResourceHelper.getColor(context, android.R.color.system_neutral2_400),
    neutralVariant50 = ColorResourceHelper.getColor(context, android.R.color.system_neutral2_500),
    neutralVariant40 = ColorResourceHelper.getColor(context, android.R.color.system_neutral2_600),
    neutralVariant30 = ColorResourceHelper.getColor(context, android.R.color.system_neutral2_700),
    neutralVariant20 = ColorResourceHelper.getColor(context, android.R.color.system_neutral2_800),
    neutralVariant10 = ColorResourceHelper.getColor(context, android.R.color.system_neutral2_900),
    neutralVariant0 = ColorResourceHelper.getColor(context, android.R.color.system_neutral2_1000),

    // The primary tonal range from the generated dynamic color palette.
    primary100 = ColorResourceHelper.getColor(context, android.R.color.system_accent1_0),
    primary99 = ColorResourceHelper.getColor(context, android.R.color.system_accent1_10),
    primary95 = ColorResourceHelper.getColor(context, android.R.color.system_accent1_50),
    primary90 = ColorResourceHelper.getColor(context, android.R.color.system_accent1_100),
    primary80 = ColorResourceHelper.getColor(context, android.R.color.system_accent1_200),
    primary70 = ColorResourceHelper.getColor(context, android.R.color.system_accent1_300),
    primary60 = ColorResourceHelper.getColor(context, android.R.color.system_accent1_400),
    primary50 = ColorResourceHelper.getColor(context, android.R.color.system_accent1_500),
    primary40 = ColorResourceHelper.getColor(context, android.R.color.system_accent1_600),
    primary30 = ColorResourceHelper.getColor(context, android.R.color.system_accent1_700),
    primary20 = ColorResourceHelper.getColor(context, android.R.color.system_accent1_800),
    primary10 = ColorResourceHelper.getColor(context, android.R.color.system_accent1_900),
    primary0 = ColorResourceHelper.getColor(context, android.R.color.system_accent1_1000),

    // The secondary tonal range from the generated dynamic color palette.
    secondary100 = ColorResourceHelper.getColor(context, android.R.color.system_accent2_0),
    secondary99 = ColorResourceHelper.getColor(context, android.R.color.system_accent2_10),
    secondary95 = ColorResourceHelper.getColor(context, android.R.color.system_accent2_50),
    secondary90 = ColorResourceHelper.getColor(context, android.R.color.system_accent2_100),
    secondary80 = ColorResourceHelper.getColor(context, android.R.color.system_accent2_200),
    secondary70 = ColorResourceHelper.getColor(context, android.R.color.system_accent2_300),
    secondary60 = ColorResourceHelper.getColor(context, android.R.color.system_accent2_400),
    secondary50 = ColorResourceHelper.getColor(context, android.R.color.system_accent2_500),
    secondary40 = ColorResourceHelper.getColor(context, android.R.color.system_accent2_600),
    secondary30 = ColorResourceHelper.getColor(context, android.R.color.system_accent2_700),
    secondary20 = ColorResourceHelper.getColor(context, android.R.color.system_accent2_800),
    secondary10 = ColorResourceHelper.getColor(context, android.R.color.system_accent2_900),
    secondary0 = ColorResourceHelper.getColor(context, android.R.color.system_accent2_1000),

    // The tertiary tonal range from the generated dynamic color palette.
    tertiary100 = ColorResourceHelper.getColor(context, android.R.color.system_accent3_0),
    tertiary99 = ColorResourceHelper.getColor(context, android.R.color.system_accent3_10),
    tertiary95 = ColorResourceHelper.getColor(context, android.R.color.system_accent3_50),
    tertiary90 = ColorResourceHelper.getColor(context, android.R.color.system_accent3_100),
    tertiary80 = ColorResourceHelper.getColor(context, android.R.color.system_accent3_200),
    tertiary70 = ColorResourceHelper.getColor(context, android.R.color.system_accent3_300),
    tertiary60 = ColorResourceHelper.getColor(context, android.R.color.system_accent3_400),
    tertiary50 = ColorResourceHelper.getColor(context, android.R.color.system_accent3_500),
    tertiary40 = ColorResourceHelper.getColor(context, android.R.color.system_accent3_600),
    tertiary30 = ColorResourceHelper.getColor(context, android.R.color.system_accent3_700),
    tertiary20 = ColorResourceHelper.getColor(context, android.R.color.system_accent3_800),
    tertiary10 = ColorResourceHelper.getColor(context, android.R.color.system_accent3_900),
    tertiary0 = ColorResourceHelper.getColor(context, android.R.color.system_accent3_1000),
)

@RequiresApi(23)
private object ColorResourceHelper {
    @DoNotInline
    fun getColor(context: Context, @ColorRes id: Int): Color {
        return Color(context.resources.getColor(id, context.theme))
    }
}