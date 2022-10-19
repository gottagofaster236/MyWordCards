package com.lr_soft.mywordcards.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.lr_soft.mywordcards.R
import kotlinx.serialization.Serializable

/**
 * Enum of languages supported by Google Translate.
 */
@Serializable
@Immutable
enum class Language(
    @StringRes
    val nameResource: Int,
    val languageCode: String
) {
    AF(R.string.lang_af, "af"),
    GA(R.string.lang_ga, "ga"),
    SQ(R.string.lang_sq, "sq"),
    IT(R.string.lang_it, "it"),
    AR(R.string.lang_ar, "ar"),
    JA(R.string.lang_ja, "ja"),
    AZ(R.string.lang_az, "az"),
    KN(R.string.lang_kn, "kn"),
    EU(R.string.lang_eu, "eu"),
    KO(R.string.lang_ko, "ko"),
    BN(R.string.lang_bn, "bn"),
    LA(R.string.lang_la, "la"),
    BE(R.string.lang_be, "be"),
    LV(R.string.lang_lv, "lv"),
    BG(R.string.lang_bg, "bg"),
    LT(R.string.lang_lt, "lt"),
    CA(R.string.lang_ca, "ca"),
    MK(R.string.lang_mk, "mk"),
    ZH_CN(R.string.lang_zh_cn, "zh_cn"),
    MS(R.string.lang_ms, "ms"),
    ZH_TW(R.string.lang_zh_tw, "zh_tw"),
    MT(R.string.lang_mt, "mt"),
    HR(R.string.lang_hr, "hr"),
    NO(R.string.lang_no, "no"),
    CS(R.string.lang_cs, "cs"),
    FA(R.string.lang_fa, "fa"),
    DA(R.string.lang_da, "da"),
    PL(R.string.lang_pl, "pl"),
    NL(R.string.lang_nl, "nl"),
    PT(R.string.lang_pt, "pt"),
    EN(R.string.lang_en, "en"),
    RO(R.string.lang_ro, "ro"),
    EO(R.string.lang_eo, "eo"),
    RU(R.string.lang_ru, "ru"),
    ET(R.string.lang_et, "et"),
    SR(R.string.lang_sr, "sr"),
    TL(R.string.lang_tl, "tl"),
    SK(R.string.lang_sk, "sk"),
    FI(R.string.lang_fi, "fi"),
    SL(R.string.lang_sl, "sl"),
    FR(R.string.lang_fr, "fr"),
    ES(R.string.lang_es, "es"),
    GL(R.string.lang_gl, "gl"),
    SW(R.string.lang_sw, "sw"),
    KA(R.string.lang_ka, "ka"),
    SV(R.string.lang_sv, "sv"),
    DE(R.string.lang_de, "de"),
    TA(R.string.lang_ta, "ta"),
    EL(R.string.lang_el, "el"),
    TE(R.string.lang_te, "te"),
    GU(R.string.lang_gu, "gu"),
    TH(R.string.lang_th, "th"),
    HT(R.string.lang_ht, "ht"),
    TR(R.string.lang_tr, "tr"),
    IW(R.string.lang_iw, "iw"),
    UK(R.string.lang_uk, "uk"),
    HI(R.string.lang_hi, "hi"),
    UR(R.string.lang_ur, "ur"),
    HU(R.string.lang_hu, "hu"),
    VI(R.string.lang_vi, "vi"),
    IS(R.string.lang_is, "is"),
    CY(R.string.lang_cy, "cy"),
    ID(R.string.lang_id, "id"),
    YI(R.string.lang_yi, "yi"),
}
